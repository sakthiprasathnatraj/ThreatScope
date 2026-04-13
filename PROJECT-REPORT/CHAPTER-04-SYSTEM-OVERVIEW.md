
# CHAPTER 4 – SYSTEM OVERVIEW

## 4.1 System Architecture Overview

ThreatScope is built on a **9-Layer Backend Architecture** that strictly separates concerns across the packet capture, analysis, detection, risk scoring, explanation, state management, and output stages. This layered approach ensures that each component is independently testable, maintainable, and extensible.

---

### 4.1.1 High-Level Architecture Diagram

```
╔══════════════════════════════════════════════════════════════════╗
║                    THREATSCOPE v1.0 ARCHITECTURE                  ║
╠══════════════════════════════════════════════════════════════════╣
║                                                                    ║
║  ┌─────────────────────────────────────────────────────────────┐  ║
║  │  LAYER 1: CAPTURE LAYER                                     │  ║
║  │  PacketSniffer.java  +  NetworkInterfaceScanner.java         │  ║
║  │  → Uses Pcap4J + Npcap to capture live IPv4 packets         │  ║
║  │  → Filters: Non-IPv4, Zero-length, Local/Private IPs        │  ║
║  └─────────────────────┬───────────────────────────────────────┘  ║
║                         │ Raw Packet (IPv4)                        ║
║  ┌─────────────────────▼───────────────────────────────────────┐  ║
║  │  LAYER 2: DECODE LAYER                                      │  ║
║  │  PacketDecoder.java                                          │  ║
║  │  → Extracts: Source IP, Dest IP, Dest Port, Protocol        │  ║
║  └─────────────────────┬───────────────────────────────────────┘  ║
║                         │ Decoded Fields                           ║
║  ┌─────────────────────▼───────────────────────────────────────┐  ║
║  │  LAYER 3: CLASSIFY LAYER                                    │  ║
║  │  TrafficClassifier.java  +  IPReputationDatabase.java        │  ║
║  │  → Classifies source IP:                                    │  ║
║  │    TRUSTED / BENIGN_NOISE / SUSPICIOUS / CONFIRMED_THREAT   │  ║
║  └─────────────────────┬───────────────────────────────────────┘  ║
║                         │ (srcIp, dstPort, protocol, class)        ║
║  ┌─────────────────────▼───────────────────────────────────────┐  ║
║  │  LAYER 4: DETECT LAYER                                      │  ║
║  │  EventAggregator.java  (Port Scan / Brute Force)            │  ║
║  │  DDoSDetector.java     (SYN Flood / UDP Flood / ICMP Flood) │  ║
║  │  PatternDetector.java  (Null/XMAS/FIN Scan, Backdoors)      │  ║
║  │  → Sliding time-window aggregation                          │  ║
║  │  → Alert deduplication (60-sec cooldown)                    │  ║
║  └─────────────────────┬───────────────────────────────────────┘  ║
║                         │ SecurityEvent                            ║
║  ┌─────────────────────▼───────────────────────────────────────┐  ║
║  │  LAYER 5: RISK LAYER                                        │  ║
║  │  RiskEngine.java  +  ConfidenceEvaluator.java               │  ║
║  │  → Risk = BaseRisk + (Evidence × Multiplier)                │  ║
║  │  → Gradual escalation (max +20 per event)                   │  ║
║  │  → Classification-based risk capping                        │  ║
║  │  → Confidence: LOW / MEDIUM / HIGH                          │  ║
║  └─────────────────────┬───────────────────────────────────────┘  ║
║                         │ (risk, confidence)                       ║
║  ┌─────────────────────▼───────────────────────────────────────┐  ║
║  │  LAYER 6: EXPLAIN LAYER                                     │  ║
║  │  ExplanationEngine.java                                     │  ║
║  │  → Generates human-readable explanation per threat type     │  ║
║  │  → Recommended action (block, monitor, investigate)         │  ║
║  └─────────────────────┬───────────────────────────────────────┘  ║
║                         │ Explanation text                         ║
║  ┌─────────────────────▼───────────────────────────────────────┐  ║
║  │  LAYER 7: STATE LAYER                                       │  ║
║  │  SystemStateManager.java                                    │  ║
║  │  → State Machine: SAFE → OBSERVE → WARNING → CRITICAL       │  ║
║  │  → Anti-flapping: 30-sec downward transition cooldown       │  ║
║  └─────────────────────┬───────────────────────────────────────┘  ║
║                         │ SystemState                              ║
║  ┌─────────────────────▼───────────────────────────────────────┐  ║
║  │  LAYER 8: OUTPUT LAYER                                      │  ║
║  │  OutputGateway.java  (SINGLE OUTPUT POINT)                  │  ║
║  │  → Prints security observation to console                   │  ║
║  │  → Publishes events to JavaFX UI via BackendBridge          │  ║
║  └─────────────────────┬───────────────────────────────────────┘  ║
║                         │                                          ║
║  ┌─────────────────────▼───────────────────────────────────────┐  ║
║  │  LAYER 9: MODEL LAYER (Data Structures)                     │  ║
║  │  SecurityEvent.java, ThreatType.java, TrafficClass.java,    │  ║
║  │  ConfidenceLevel.java, SystemState.java                     │  ║
║  └─────────────────────────────────────────────────────────────┘  ║
╚══════════════════════════════════════════════════════════════════╝
```

---

### 4.1.2 Project Folder Structure

```
ThreatScope/
├── src/main/java/com/threatscope/
│   ├── Main.java                          ← Entry point
│   ├── core/
│   │   ├── capture/
│   │   │   ├── PacketSniffer.java         ← Live packet capture (Pcap4J)
│   │   │   ├── NetworkInterfaceScanner.java ← Lists available NICs
│   │   │   ├── InterfaceSelector.java     ← Selects best interface
│   │   │   └── SnifferRunner.java         ← Runnable thread wrapper
│   │   ├── decode/
│   │   │   └── PacketDecoder.java         ← Extracts IP, port, protocol
│   │   ├── classify/
│   │   │   ├── TrafficClassifier.java     ← IP classification logic
│   │   │   └── IPReputationDatabase.java  ← Known IPs database
│   │   ├── detect/
│   │   │   ├── EventAggregator.java       ← Port scan / brute-force detection
│   │   │   ├── DDoSDetector.java          ← DDoS attack detection
│   │   │   ├── PatternDetector.java       ← TCP flag anomaly detection
│   │   │   └── TrafficStats.java          ← Traffic statistics
│   │   ├── risk/
│   │   │   ├── RiskEngine.java            ← Risk score calculation
│   │   │   ├── ConfidenceEvaluator.java   ← Confidence level assessment
│   │   │   ├── SystemStateEngine.java     ← State machine
│   │   │   ├── SystemStateManager.java    ← State management facade
│   │   │   ├── TrafficClassifier.java     ← (Risk-layer classifier)
│   │   │   └── IPReputationDatabase.java  ← IP reputation data
│   │   ├── explain/
│   │   │   └── ExplanationEngine.java     ← Human-readable explanations
│   │   ├── output/
│   │   │   └── OutputGateway.java         ← Single output point
│   │   ├── correlate/
│   │   │   └── IncidentCorrelator.java    ← Event correlation
│   │   └── model/
│   │       ├── SecurityEvent.java         ← Core event data structure
│   │       ├── ThreatType.java            ← Threat type enumeration
│   │       ├── TrafficClass.java          ← Traffic classification enum
│   │       └── ConfidenceLevel.java       ← Confidence level enum
│   └── ui/
│       ├── MainApp.java                   ← JavaFX Application entry
│       ├── DashboardView.java             ← Main dashboard FXML view
│       └── DashboardController.java       ← Dashboard controller
├── src/main/resources/
│   └── logback.xml                        ← Logging configuration
├── pom.xml                                ← Maven build configuration
└── threatscope.properties                 ← Runtime configuration
```

---

### 4.1.3 Data Flow Diagram

The following describes the complete data flow through ThreatScope from raw packet to alert output:

```
 NETWORK INTERFACE
        │
        ▼
 [1] PacketSniffer
     - Capture raw packet via Pcap4J
     - Filter: IPv4 only, length > 0
     - Filter: Exclude local/private IPs
        │
        ▼
 [2] PacketDecoder
     - Extract: srcIp, dstIp, dstPort, protocol (TCP/UDP/ICMP)
        │
        ├──────────────────────────────────┐
        ▼                                  ▼
 [3] TrafficClassifier              [3] PatternDetector
     - Classify srcIp                - Check TCP flags
     - TRUSTED / BENIGN /            - Detect Null/XMAS/FIN scan
       SUSPICIOUS / THREAT           - Detect backdoor ports
        │                                  │
        ▼                                  ▼
 [4] EventAggregator             [4] DDoSDetector
     - Sliding window tracking       - Packet rate monitoring
     - Port count per srcIp          - SYN/UDP/ICMP flood detection
     - Threshold: 10 ports / 10s     - Threshold: packets/sec
        │                                  │
        └─────────────┬────────────────────┘
                       ▼
               SecurityEvent Created
               {srcIp, threatType, description,
                evidenceCount, classification, duration}
                       │
                       ▼
 [5] RiskEngine
     - Calculate RawRisk = BaseRisk + (Evidence × Multiplier)
     - Cap increase to max +20/event (gradual escalation)
     - Apply ClassificationMaxRisk cap
     - Calculate ConfidenceLevel
                       │
                       ▼
 [6] ExplanationEngine
     - Generate human-readable threat explanation
     - Generate recommended action
                       │
                       ▼
 [7] SystemStateManager
     - Evaluate: SAFE / OBSERVE / WARNING / CRITICAL
     - Apply anti-flapping cooldown for downward transitions
     - Publish state change if transition occurs
                       │
                       ▼
 [8] OutputGateway (SINGLE OUTPUT POINT)
     - Print security observation to console
     - Publish to JavaFX UI dashboard
```

---

### 4.1.4 Technology Stack Summary

| Layer | Technology | Role |
|-------|-----------|------|
| **Packet Capture** | Pcap4J 1.8.2 + Npcap | Live raw packet capture on Windows |
| **Language** | Java 21 | Core implementation language |
| **Build Tool** | Apache Maven 3.8+ | Dependency management, build automation |
| **GUI Framework** | JavaFX 21 | Desktop dashboard UI |
| **Logging** | Logback 1.2.13 + SLF4J | Application logging (pcap4j noise suppressed) |
| **Concurrency** | Java ConcurrentHashMap | Thread-safe sliding window tracking |
| **IDE** | IntelliJ IDEA 2023+ | Development environment |
| **OS** | Windows 10/11 | Target platform |

---

### 4.1.5 SecurityEvent – Core Data Model

The `SecurityEvent` class is the central data structure passed between all layers:

```java
public class SecurityEvent {
    private final String sourceIp;         // Source IP of the threat
    private final ThreatType threatType;   // PORT_SCAN, BRUTE_FORCE, DDOS_ATTACK, etc.
    private final String description;     // Human-readable description
    private final int evidenceCount;      // e.g., number of unique ports scanned
    private final long timestamp;         // Event occurrence time (ms)
    private final TrafficClass classification; // TRUSTED/BENIGN/SUSPICIOUS/CONFIRMED_THREAT
    private final long duration;          // Duration of observed activity (ms)
}
```

### Threat Types Supported

| ThreatType | Description |
|-----------|-------------|
| `PORT_SCAN` | Multiple unique destination ports accessed within time window |
| `BRUTE_FORCE` | Repeated connection attempts to same service |
| `DDOS_ATTACK` | High packet rate from single source |
| `SYN_FLOOD` | High rate of SYN packets (connection flooding) |
| `UDP_FLOOD` | High rate of UDP packets |
| `ICMP_FLOOD` | High rate of ICMP (ping) packets |
| `BACKDOOR_ATTEMPT` | Connection to known backdoor/malware port |
| `SUSPICIOUS_PATTERN` | Anomalous TCP flag combinations (Null/XMAS/FIN scan) |

---

*End of Chapter 4*
