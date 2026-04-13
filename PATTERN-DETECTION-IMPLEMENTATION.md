# Pattern Detection Module Implementation

## 🎯 Overview
We have successfully implemented a new **Pattern Detection** module for ThreatScope. This module goes beyond simple flood detection to identify subtle, malicious network signatures and anomaly patterns.

## 🛡️ New Detection Capabilities

### 1. Backdoor Port Detection (`BACKDOOR_ATTEMPT`)
Detects connection attempts to ports known to be used by Trojans, RATs (Remote Access Trojans), and malware.

**Monitored Ports:**
- `31337` (BackOrifice)
- `12345` (NetBus)
- `6667` (IRC / Botnets)
- `2323` (Mirai IoT Botnet)
- `4444` (Metasploit Default)
- `5555` (ADB / Android Malware)
- `27374` (Sub7)

### 2. Suspicious TCP Flag Analysis (`SUSPICIOUS_PATTERN`)
Analyzes TCP packet headers for illegal or suspicious flag combinations often used in stealth scanning (reconnaissance) to evade firewalls.

**Detected Patterns:**
- **Null Scan**: No flags set (0x00). Bypasses naive filters.
- **XMAS Scan**: FIN, URG, and PSH flags set. "Lights up" the packet like a Christmas tree.
- **Stealth FIN Scan**: FIN set without ACK. Used to map listening ports without completing a handshake.

---

## 🔧 Technical Implementation

### Components
1.  **`PatternDetector.java`**: Core logic class.
    - Inspects every connection-initiation packet.
    - Uses `Packet` and `TcpPacket` from Pcap4J.
    - Implements an **Alert Cooldown** (default 5s) to prevent log spam for the same source/pattern.

2.  **`PacketSniffer.java` Integration**:
    - Inserted as **Step 4.2** in the packet processing pipeline.
    - Runs *after* DDoS detection but *before* event aggregation.

3.  **`ThreatType` & `SecurityEvent` Updates**:
    - Added `BACKDOOR_ATTEMPT` and `SUSPICIOUS_PATTERN` enums.
    - Added factory methods for easy event creation.

4.  **`ExplanationEngine.java` Updates**:
    - Added rich, educational explanations for the new threats.
    - Explains *why* a Null Scan is dangerous (OS fingerprinting) and *what* a Backdoor port implies (malware infection).

### Verification (Demo Mode)
We updated the **Test Event Generator** in the UI to cycle through all 8 supported threat types, including the new ones.

**How to Test:**
1.  Launch ThreatScope.
2.  Enable **Expert Mode**.
3.  Click **Generate Test Events** repeatedly.
4.  Observe the new **Backdoor** and **Suspicious Pattern** events appearing with full explanations.

---

## 🚀 Next Steps
- **Refine Signatures**: Add more backdoor ports as needed.
- **Stateful Analysis**: Track sequence numbers to detect more complex anomalies (e.g., TCP Hijacking attempts).
