
# CHAPTER 2 – SYSTEM ANALYSIS

## 2.1 Study of Existing Systems

Before designing ThreatScope, a thorough analysis of existing Intrusion Detection Systems (IDS) and network monitoring tools was conducted. The objective of this study was to understand the strengths and weaknesses of current solutions so that ThreatScope could be designed to address their shortcomings.

---

### 2.1.1 SNORT – Open Source IDS

**Overview:**
SNORT is one of the most widely deployed open-source network intrusion detection systems. It performs real-time traffic analysis and packet logging on IP networks. It can perform protocol analysis, content searching and matching, and is used to detect a variety of attacks.

**Features:**
- Rule-based packet inspection
- Over 50,000 community rules available
- Runs on Linux and Windows
- Supports packet logging and network sniffing modes

**Limitations:**
- Requires significant manual configuration and rule management
- Generated alerts are cryptic and not user-friendly
- No built-in explanation engine – analysts must manually interpret alerts
- Generates massive alert volumes (alert fatigue)
- Does not classify traffic by reputation or source behaviour
- Difficult to deploy for non-expert users

---

### 2.1.2 Suricata – High-Performance IDS/IPS

**Overview:**
Suricata is a high-performance network IDS, IPS, and Network Security Monitoring (NSM) engine. It uses multi-threading for high-speed network analysis.

**Features:**
- Multi-threaded packet processing
- Supports YAML and JSON output
- Protocol identification
- File extraction capability
- Lua scripting support

**Limitations:**
- Complex configuration – requires expert-level knowledge
- No GUI included by default
- Alert output is raw and requires integration with SIEM tools (Elasticsearch, Kibana)
- No explain-before-alert capability
- High resource usage on low-end hardware

---

### 2.1.3 Wireshark – Packet Analyser

**Overview:**
Wireshark is the world's foremost network protocol analyser. It captures and interactively browses traffic running on a computer network.

**Features:**
- Deep packet inspection
- Protocol-level analysis
- Colour-coded traffic display
- Export to PCAP files

**Limitations:**
- Not an IDS – does not detect threats automatically
- Requires human analyst to interpret captured packets
- No automated alerting or risk scoring
- Cannot run in background monitoring mode
- Not suitable for long-term continuous monitoring

---

### 2.1.4 Zeek (formerly Bro) – Network Analysis Framework

**Overview:**
Zeek is a powerful framework for network analysis. It focuses on high-performance network traffic analysis and generates detailed logs of activity on a network.

**Features:**
- Extensible scripting language
- Deep network protocol analysis
- Can detect anomalies and generate alerts
- Used by major enterprises and research institutions

**Limitations:**
- Primarily Linux-based
- Steep learning curve – requires Zeek scripting knowledge
- No user-friendly dashboard included
- Alert output requires integration with external tools
- Performance overhead on high-traffic networks

---

### 2.1.5 Comparison Table – Existing Systems vs ThreatScope

| Feature | SNORT | Suricata | Wireshark | Zeek | **ThreatScope** |
|---------|-------|----------|-----------|------|-----------------|
| Real-Time Detection | ✅ | ✅ | ❌ | ✅ | ✅ |
| User-Friendly Alerts | ❌ | ❌ | ❌ | ❌ | ✅ |
| Explainability Engine | ❌ | ❌ | ❌ | ❌ | ✅ |
| Risk Scoring (0–100) | ❌ | ❌ | ❌ | ❌ | ✅ |
| Traffic Classification | ❌ | Partial | ❌ | Partial | ✅ |
| False Positive Reduction | Partial | Partial | N/A | Partial | ✅ |
| Anti-Flapping State Machine | ❌ | ❌ | ❌ | ❌ | ✅ |
| GUI Dashboard | ❌ | ❌ | ✅ | ❌ | ✅ |
| Windows Support | ✅ | ✅ | ✅ | Limited | ✅ |
| Open Source / Free | ✅ | ✅ | ✅ | ✅ | ✅ |
| Ease of Use | Low | Low | Medium | Low | **High** |
| Beginner Friendly | ❌ | ❌ | Partial | ❌ | ✅ |

---

## 2.2 Problem Analysis and Proposed Solution

Based on the study of existing systems, the following specific problems were identified that ThreatScope is designed to solve:

---

### 2.2.1 Problem 1: Alert Fatigue

**Observed Problem:**
Traditional IDS systems generate thousands of alerts per day, the majority of which are false positives. Analysts become desensitised to alerts, increasing the risk of missing genuine threats.

**Root Cause:**
- Per-packet analysis triggers alerts for every suspicious packet, not behavioural patterns
- No traffic reputation/classification – CDN traffic, cloud services generate false alerts
- No cooldown or deduplication mechanisms

**ThreatScope Solution:**
```
Sliding Time-Window Aggregation:
- Track unique destination ports per source IP over a 10-second window
- Only trigger alert when threshold (10 unique ports) is crossed within window
- 60-second cooldown per source IP prevents duplicate alerts

Traffic Classification:
- Classify each source IP as TRUSTED / BENIGN_NOISE / SUSPICIOUS / CONFIRMED_THREAT
- Apply risk cap per class:
  TRUSTED        → max risk = 20  (can never trigger WARNING)
  BENIGN_NOISE   → max risk = 40  (can never trigger CRITICAL)
  SUSPICIOUS     → max risk = 75
  CONFIRMED_THREAT → max risk = 100
```

---

### 2.2.2 Problem 2: Lack of Explainability

**Observed Problem:**
Security tools report event type and severity but do not explain why the event is suspicious or what action should be taken. This is particularly challenging for non-expert users.

**ThreatScope Solution:**
Every security observation includes:
- **What happened:** Human-readable description of the detected pattern
- **Why it matters:** Explanation of the risk and potential impact
- **Risk Score:** Numeric score (0–100) indicating severity
- **Confidence Level:** LOW / MEDIUM / HIGH based on evidence quality
- **Recommended Action:** Plain-language guidance (e.g., "Block this IP at firewall level")

---

### 2.2.3 Problem 3: State Flapping

**Observed Problem:**
Rapid oscillation between security states (e.g., WARNING → SAFE → WARNING within seconds) creates confusion and reduces trust in the monitoring system.

**ThreatScope Solution:**
```
Anti-Flapping Mechanism:
- Upward transitions (SAFE → WARNING): Allowed immediately
- Downward transitions (WARNING → SAFE): Require 30-second cooldown
- State change only printed on actual transition (not repeatedly)
```

---

### 2.2.4 Problem 4: Sudden Risk Jumps

**Observed Problem:**
Some IDS tools can jump directly from a normal state to CRITICAL based on a single high-severity event, causing panic and distrust.

**ThreatScope Solution:**
```
Gradual Risk Escalation:
- MAX_RISK_INCREASE_PER_EVENT = 20 (capped)
- Risk increases gradually over multiple events
- Risk decays over time if no new events (RISK_DECAY_AMOUNT = 10 per minute)
- CRITICAL state requires both Risk ≥ 80 AND Confidence == HIGH
```

---

### 2.2.5 Summary of Proposed Solution

ThreatScope proposes a **9-layer backend architecture** that addresses each identified problem:

```
┌─────────────────────────────────────────────────────────────┐
│  LAYER 1: CAPTURE      → PacketSniffer (Pcap4J + Npcap)    │
│  LAYER 2: DECODE       → PacketDecoder (IP, TCP, UDP)       │
│  LAYER 3: CLASSIFY     → TrafficClassifier + IPReputation   │
│  LAYER 4: DETECT       → EventAggregator + PatternDetector  │
│                           DDoSDetector                       │
│  LAYER 5: RISK         → RiskEngine + ConfidenceEvaluator   │
│  LAYER 6: EXPLAIN      → ExplanationEngine                  │
│  LAYER 7: STATE        → SystemStateManager                 │
│  LAYER 8: OUTPUT       → OutputGateway (single point)       │
│  LAYER 9: MODEL        → SecurityEvent, ThreatType,         │
│                           TrafficClass, ConfidenceLevel       │
└─────────────────────────────────────────────────────────────┘
```

Each layer has a single, well-defined responsibility — embodying the **Single Responsibility Principle** and ensuring the system is maintainable, testable, and extensible.

---

*End of Chapter 2*
