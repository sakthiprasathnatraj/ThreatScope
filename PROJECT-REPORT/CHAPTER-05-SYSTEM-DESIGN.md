
# CHAPTER 5 – SYSTEM DESIGN

## 5.1 Detailed System Design

This chapter presents the detailed design of ThreatScope, including component-level design, algorithm descriptions, key class implementations, and decision logic.

---

### 5.1.1 Component Design

#### Component 1: PacketSniffer (Capture Layer)

**Responsibility:** Capture live packets from the selected network interface and route valid packets to the decode and detection layers.

**Algorithm:**
```
1. Enumerate all available network interfaces via Pcap4J
2. Select interface by index (from configuration)
3. Open interface in promiscuous mode (snapLen=65536, timeout=10ms)
4. For each captured packet:
   a. Check if packet is IPv4 → skip if not
   b. Check if packet length > 0 → skip if NIC offloading artifact
   c. Extract srcIp from IPv4 header
   d. Check if srcIp is local/private → skip if true
   e. Route packet to: EventAggregator, DDoSDetector, PatternDetector
5. On error: log and continue (do not crash)
```

**Local IP Filtering Logic:**
```java
private static boolean isLocalIp(String ip) {
    return ip.startsWith("127.")    // Loopback
        || ip.startsWith("192.168.") // Private LAN
        || ip.startsWith("10.")      // Private LAN
        || ip.startsWith("172.16.")  // Private LAN range
        || ip.startsWith("172.17.")
        || ip.startsWith("172.31.");
}
```

---

#### Component 2: TrafficClassifier (Classify Layer)

**Responsibility:** Classify each source IP into one of four traffic classes to enable risk capping and confidence adjustment.

**Classification Decision Tree:**
```
Input: (srcIp, threatType, evidenceCount, duration)

Step 1: Check IPReputationDatabase
  → If blacklisted → CONFIRMED_THREAT
  → If whitelisted → TRUSTED

Step 2: Check IP ranges
  → If known CDN range (Akamai, Cloudflare, Fastly) → BENIGN_NOISE
  → If known cloud range (AWS, Azure, Google) → BENIGN_NOISE

Step 3: Behavioural Analysis
  → If evidenceCount > 50 OR very short duration → CONFIRMED_THREAT
  → If evidenceCount > 20 → SUSPICIOUS
  → Default → SUSPICIOUS

Output: TrafficClass { TRUSTED, BENIGN_NOISE, SUSPICIOUS, CONFIRMED_THREAT }
```

**Risk Caps per Classification:**
```java
public enum TrafficClass {
    TRUSTED(20),          // max risk = 20
    BENIGN_NOISE(40),     // max risk = 40
    SUSPICIOUS(75),       // max risk = 75
    CONFIRMED_THREAT(100); // max risk = 100

    private final int maxRisk;
}
```

---

#### Component 3: EventAggregator (Detect Layer)

**Responsibility:** Aggregate per-packet observations into behavioural patterns using a sliding time window. Triggers SecurityEvent only when threshold is crossed.

**Sliding Window Algorithm:**
```
Data Structures:
  portMap       : ConcurrentHashMap<String srcIp, Set<Integer> ports>
  windowStartTime: ConcurrentHashMap<String srcIp, Long startTime>
  lastAlertTime : ConcurrentHashMap<String srcIp, Long alertTime>

Constants:
  TIME_WINDOW_MS     = 10,000ms  (10 seconds)
  PORT_SCAN_THRESHOLD = 10       (unique ports)
  ALERT_COOLDOWN_MS  = 60,000ms  (1 minute)

recordPacket(srcIp, dstPort, protocol):
  now = currentTimeMillis()

  IF windowStartTime[srcIp] exists:
    IF now - windowStartTime[srcIp] > TIME_WINDOW_MS:
      RESET window for srcIp   ← Window expired, start fresh
  ELSE:
    windowStartTime[srcIp] = now   ← Start new window

  portMap[srcIp].add(dstPort)
  uniquePorts = portMap[srcIp].size()

  IF uniquePorts >= PORT_SCAN_THRESHOLD:
    IF isInCooldown(srcIp, now): RETURN  ← Suppress duplicate alert
    
    classification = TrafficClassifier.classify(srcIp, ...)
    event = SecurityEvent.portScan(srcIp, uniquePorts, classification, duration)
    RiskEngine.processEvent(event)
    lastAlertTime[srcIp] = now
    RESET window for srcIp
```

---

#### Component 4: DDoSDetector (Detect Layer)

**Responsibility:** Detect volumetric DDoS attacks by monitoring packet rates per source IP.

**Algorithm:**
```
For each packet (srcIp, protocol, flags):
  Increment packetCount[srcIp]
  Increment synCount[srcIp] (if TCP SYN)
  Increment udpCount[srcIp] (if UDP)
  Increment icmpCount[srcIp] (if ICMP)

  IF (now - windowStart[srcIp]) >= RATE_WINDOW (1 second):
    rate = packetCount[srcIp] / windowDuration

    IF rate >= DDOS_THRESHOLD (1000 pps):
      Trigger DDOS_ATTACK event

    IF synCount rate >= SYN_FLOOD_THRESHOLD (500 pps):
      Trigger SYN_FLOOD event

    IF udpCount rate >= UDP_FLOOD_THRESHOLD (800 pps):
      Trigger UDP_FLOOD event

    IF icmpCount rate >= ICMP_FLOOD_THRESHOLD (500 pps):
      Trigger ICMP_FLOOD event

    RESET counters for srcIp
```

---

#### Component 5: PatternDetector (Detect Layer)

**Responsibility:** Detect suspicious TCP flag combinations and known backdoor port accesses.

**Backdoor Port List:**
```java
private static final Set<Integer> BACKDOOR_PORTS = new HashSet<>(Arrays.asList(
    31337, // Back Orifice
    12345, // NetBus
    6667,  // IRC Botnet (C&C)
    2323,  // Mirai Botnet Telnet
    4444,  // Metasploit default
    5555,  // Android ADB
    27374  // Sub7
));
```

**TCP Flag Pattern Checks:**
```
Null Scan:        !SYN && !ACK && !FIN && !RST && !PSH && !URG
XMAS Scan:        FIN && URG && PSH
Stealth FIN Scan: FIN && !ACK && !SYN && !RST
```

---

#### Component 6: RiskEngine (Risk Layer)

**Responsibility:** Calculate risk score with gradual escalation and classification-based capping.

**Risk Formula:**
```
RawRisk = BaseRisk + (EvidenceCount × EVIDENCE_MULTIPLIER)
RiskIncrease = min(RawRisk - PreviousRisk, MAX_RISK_INCREASE_PER_EVENT)
RiskIncrease = max(RiskIncrease, 0)  ← Never negative
NewRisk = PreviousRisk + RiskIncrease
FinalRisk = min(NewRisk, ClassificationMaxRisk)
FinalRisk = min(FinalRisk, 100)  ← Absolute cap
```

**Base Risk Values:**
```
PORT_SCAN   → BaseRisk = 30
BRUTE_FORCE → BaseRisk = 40
DDoS (all)  → BaseRisk = 50
BACKDOOR    → BaseRisk = 60
PATTERN     → BaseRisk = 35
```

**Constants:**
```java
private static final int EVIDENCE_MULTIPLIER = 3;
private static final int MAX_RISK_INCREASE_PER_EVENT = 20;
private static final int RISK_DECAY_AMOUNT = 10;
private static final long RISK_DECAY_INTERVAL_MS = 60_000; // 1 minute
```

**Confidence Level Calculation:**
```
TRUSTED classification:
  → Always LOW (trusted source, not a real threat)

BENIGN_NOISE classification:
  → MEDIUM if (risk >= 30 AND eventCount >= 2)
  → LOW otherwise

SUSPICIOUS classification:
  → HIGH   if (risk >= 60 AND eventCount >= 3 AND duration >= 5000ms)
  → MEDIUM if (risk >= 40 OR eventCount >= 2)
  → LOW otherwise

CONFIRMED_THREAT classification:
  → HIGH   if (risk >= 50 AND eventCount >= 2)
  → MEDIUM if (risk >= 30)
  → LOW otherwise
```

---

#### Component 7: SystemStateEngine (State Layer)

**Responsibility:** Manage the system's security state machine with anti-flapping protection.

**State Transition Rules:**
```
State Determination:
  risk >= 80 AND confidence == HIGH → CRITICAL
  risk >= 60                        → WARNING
  risk >= 40                        → OBSERVE
  risk < 40                         → SAFE

Transition Rules:
  Upward Transition (e.g., SAFE → WARNING):
    → Allowed immediately (fast response to threats)

  Downward Transition (e.g., WARNING → SAFE):
    → Only allowed if (now - lastStateChange) >= 30,000ms
    → Otherwise: stay in current state (prevents flapping)

State Change Output:
  [STATE CHANGE]
  SYSTEM STATE: SAFE → WARNING
```

**State Machine Diagram:**
```
         ┌──────┐
         │ SAFE │  risk < 40
         └──┬───┘
    risk≥40 │ ↑ risk<40 (after cooldown)
         ┌──▼─────┐
         │ OBSERVE │  risk ≥ 40
         └──┬──────┘
    risk≥60 │ ↑ risk<60 (after cooldown)
         ┌──▼─────┐
         │ WARNING │  risk ≥ 60
         └──┬──────┘
     risk≥80│ ↑ risk<80 (after cooldown)
    + HIGH  │   OR conf!=HIGH
    conf    │
         ┌──▼──────┐
         │ CRITICAL │  risk ≥ 80 + confidence HIGH
         └──────────┘
```

---

#### Component 8: OutputGateway (Output Layer)

**Responsibility:** Act as the single output point for all security observations. Formats and routes output to both the console and the JavaFX UI.

**Console Output Format:**
```
========================================
[SECURITY OBSERVATION]
Time         : 2026-02-23 09:30:15
Source IP    : 203.0.113.42
Threat Type  : PORT_SCAN
Description  : Multiple destination ports accessed (14) within time window
Classification: SUSPICIOUS
Risk Score   : 72/100
Confidence   : HIGH
System State : WARNING

Explanation:
  This IP has accessed 14 unique ports within 10 seconds.
  This is a strong indicator of automated port scanning activity,
  commonly used by attackers to map open services.

Recommended Action:
  Investigate this IP immediately. Consider blocking at firewall level.
  Check if this IP appears in threat intelligence feeds.
========================================
```

---

### 5.1.2 UML Class Diagram (Key Classes)

```
┌─────────────────────────────────┐
│         SecurityEvent           │
├─────────────────────────────────┤
│ - sourceIp: String              │
│ - threatType: ThreatType        │
│ - description: String           │
│ - evidenceCount: int            │
│ - timestamp: long               │
│ - classification: TrafficClass  │
│ - duration: long                │
├─────────────────────────────────┤
│ + portScan(): SecurityEvent     │
│ + bruteForce(): SecurityEvent   │
│ + ddosAttack(): SecurityEvent   │
│ + getSourceIp(): String         │
│ + getThreatType(): ThreatType   │
│ + getEvidenceCount(): int       │
└──────────────┬──────────────────┘
               │ uses
    ┌──────────▼──────────┐
    │     RiskEngine      │
    ├─────────────────────┤
    │ + processEvent()    │
    │ - calculateRawRisk()│
    │ - calculateConf()   │
    │ + performRiskDecay()│
    └──────────┬──────────┘
               │ calls
    ┌──────────▼──────────────┐
    │   SystemStateManager    │
    ├─────────────────────────┤
    │ + evaluateState()       │
    │ + getCurrentState()     │
    │ - performStateChange()  │
    └─────────────────────────┘
```

---

### 5.1.3 Sequence Diagram – Port Scan Detection

```
PacketSniffer  EventAggregator  TrafficClassifier  RiskEngine  OutputGateway
     │               │                  │               │            │
     │ recordPacket() │                  │               │            │
     │──────────────▶│                  │               │            │
     │               │ classify()       │               │            │
     │               │─────────────────▶│               │            │
     │               │ TrafficClass     │               │            │
     │               │◀─────────────────│               │            │
     │               │ processEvent()   │               │            │
     │               │─────────────────────────────────▶│            │
     │               │                  │  printOutput() │            │
     │               │                  │               │────────────▶│
     │               │                  │               │  [output]  │
```

---

*End of Chapter 5*
