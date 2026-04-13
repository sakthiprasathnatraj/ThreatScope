# ThreatScope v2.0: A Layered, User-Centric Host-Based Intrusion Detection System with Visual Analytics

**Abstract**—In the evolving landscape of cybersecurity, Host-Based Intrusion Detection Systems (HIDS) are critical for monitoring local system activities. However, existing solutions often suffer from high false-positive rates and opaque decision-making processes, leading to "alert fatigue" among users. This paper presents **ThreatScope v2.0**, a production-grade HIDS developed in Java. Addressing the limitations of traditional IDSs, ThreatScope introduces a novel "Explain-Before-Alert" philosophy supported by a multi-layered architecture. Key innovations include a **Traffic Classification Engine** for reducing alert fatigue, a **Visual Analytics Module** for real-time network insight, and specialized detectors for **DDoS** and **behavioral patterns**. By integrating `Pcap4J` for packet capture with a responsive JavaFX dashboard, ThreatScope bridges the gap between low-level network monitoring and high-level user comprehension. We demonstrate that this approach significantly reduces false alarms while providing actionable, non-technical explanations for security events, making advanced threat detection accessible to non-expert users.

**Keywords**—Intrusion Detection, Visual Analytics, DDoS Detection, Traffic Classification, Explainable AI, JavaFX, Pcap4J.

---

## I. INTRODUCTION

Cyber threats targeting end-user systems have evolved from simple viruses to complex, automated attacks like botnets, ransomware, and Advanced Persistent Threats (APTs). While enterprise-grade Network Intrusion Detection Systems (NIDS) like Snort or Zeek exist, they are designed for network administrators and produce technical logs that are indecipherable to the average user. Furthermore, the internet is filled with "background radiation"—benign scanning by research bots, Content Delivery Networks (CDNs), and cloud providers—that frequently triggers false positives in conventional systems [1].

The primary challenge lies in balancing detection sensitivity with user experience. A system that alerts on every anomaly quickly becomes annoying, leading users to disable it entirely. Conversely, a system that is too silent may miss critical threats.

**ThreatScope v2.0** solves this by combining rigorous packet-level analysis with a user-centric presentation layer. Unlike v1.0, which focused on core detection, v2.0 implements a comprehensive **Visual Analytics** suite, allowing users to not just *see* alerts, but *understand* their network traffic trends.

This paper makes the following contributions:
1.  **Layered Architecture**: A clean separation of concerns ensuring modularity and maintainability.
2.  **Explain-Before-Alert Philosophy**: A design paradigm that prioritizes user understanding over raw data logging.
3.  **Dynamic Risk Scoring**: A formulaic approach to risk that accounts for evidence weight, traffic classification, and confidence levels.
4.  **Visual Analytics**: Real-time visualization of network throughput and protocol distribution.

---

## II. RELATED WORK

Traditional HIDS solutions such as OSSEC [2] rely heavily on log analysis and file integrity monitoring. While effective for servers, they lack the real-time network visibility required for detecting active attacks on personal computers. Network-based solutions like Wireshark [3] provide deep packet inspection but offer no automated detection, requiring expert knowledge to interpret.

ThreatScope bridges this gap by offering the real-time packet analysis of Wireshark with the automated detection of a NIDS, wrapped in a user-friendly interface inspired by modern SOC (Security Operations Center) dashboards.

---

## III. SYSTEM ARCHITECTURE

ThreatScope follows a strict modular design pattern to ensure scalability and maintainability. The system is composed of four primary layers.

**[FIGURE 1: High-Level System Architecture Diagram]**
*(Description: A block diagram showing the data flow from bottom to top: Packet Capture -> Event Aggregation -> Detection Engines -> Risk Engine -> User Interface)*

### A. Layer 1: Packet Capture & Decoding
The system utilizes `Pcap4J` to interface with the Network Interface Controller (NIC).
*   **PacketSniffer**: Captures raw IPv4 packets in promiscuous mode. To minimize performance overhead, a ring buffer is used to handle high-speed traffic without dropping packets.
*   **Decoding**: Packets are parsed into `DecodedPacket` objects, extracting key tuples (Source IP, Dest IP, Port, Protocol, Flags).

### B. Layer 2: Detection Engines
Multiple specialized engines operate in parallel to analyze the stream of decoded packets:

1.  **DDoS Detector**: Uses `ConcurrentHashMap` and atomic counters to track packet rates. It identifies volumetric attacks:
    *   *SYN Flood*: >200 SYN packets/sec without ACKs.
    *   *UDP Flood*: >500 UDP packets/sec.
    *   *ICMP Flood*: >100 Echo Requests/sec.
    *   *Micro-burst Protection*: Ignores spikes lasting <1 second to filter benign bursts.

2.  **Pattern Detector**: Signatures-based detection for known malicious signatures:
    *   *Backdoor Ports*: Connections to known trojan ports (e.g., 31337, 12345, 4444).
    *   *Stealth Scans*: Detects anomalous TCP flag combinations (Null Scan, XMAS Scan, FIN Scan).

3.  **Port Scan Detector**: Time-window analysis (sliding 5-10s window) to detect rapid access to multiple unique ports.

**[FIGURE 2: Detection Engine Logic Flow]**
*(Description: A flowchart detailing how a packet is processed by the three detectors in parallel)*

### C. Layer 3: Risk & Classification
*   **Traffic Classification**: Assigns reputation (TRUSTED, BENIGN_NOISE, SUSPICIOUS, THREAT) to IP addresses.
*   **Risk Engine**: Calculates a risk score (0-100) based on event severity, evidence count, and classification caps. This ensures that a trusted source (e.g., Google DNS) can never trigger a CRITICAL alert.

### D. Layer 4: Visual Analytics (UI)
The **DashboardViewV2** provides a rich, interactive interface constructed using JavaFX.
*   **Live Traffic Graph**: A real-time `LineChart` visualizing packets-per-second throughput.
*   **Protocol Distribution**: A dynamic `PieChart` showing the ratio of TCP vs. UDP vs. ICMP traffic.
*   **Top Talkers**: A `TableView` ranking the most active IP addresses by packet count and risk.
*   **Expert Mode**: An optional view exposing the raw packet table for advanced debugging.

---

## IV. ALGORITHMIC DETAILS

### A. Volumetric DDoS Detection
The `DDoSDetector` employs a token-bucket-like approach within a sliding time window ($T_w = 5s$). Each source IP ($S_{ip}$) has an associated tracker $Rate(S_{ip})$.

$$ Rate(S_{ip}) = \frac{\sum P_{count}}{\Delta t} $$

If $Rate(S_{ip}) > Threshold_{type}$ (where type is SYN, UDP, or GC), an alert is triggered. To prevent "alert flapping," a localized cooldown of 30 seconds is applied per IP.

### B. Risk Scoring Model
The risk score $R$ is calculated dynamically:

$$ R = \min(R_{max\_class}, R_{base} + \sum(E_i \times W_i)) $$

Where:
*   $R_{max\_class}$ is the maximum risk allowed for the IP's class (e.g., 40 for Benign Noise).
*   $R_{base}$ is the threat type's inherent severity.
*   $E_i$ is specific evidence (e.g., number of ports scanned).
*   $W_i$ is the weight of that evidence type.

This formula ensures that the risk score is proportional to the intensity of the attack but bounded by the trustworthiness of the source.

---

## V. IMPLEMENTATION STRATEGY

### A. Technology Stack
The system is implemented in **Java 8**, chosen for its stability, strong typing, and vast ecosystem.
*   **Packet Capture**: `Pcap4J` (wrapper for libpcap/WinPcap).
*   **Concurrency**: `java.util.concurrent` (Executors, AtomicInteger, ConcurrentHashMap).
*   **UI Framework**: JavaFX 8.

### B. Concurrency Model
To handle high-speed network traffic without blocking the UI, ThreatScope employs a Producer-Consumer pattern.
1.  **Capture Thread**: A single high-priority thread reads packets from the NIC and pushes them into a `BlockingQueue`.
2.  **Analysis Threads**: A pool of worker threads consumes packets from the queue, running them through the Detection Engines.
3.  **UI Thread**: JavaFX Application Thread updates the UI at 60Hz, pulling aggregated statistics from the engines.

**[FIGURE 3: Concurrency Model Diagram]**
*(Description: Diagram showing the threaded architecture - Capture Thread -> Queue -> Worker Pool -> UI Thread)*

---

## VI. USER INTERFACE DESIGN

The user interface (UI) is designed to mimic professional Security Operation Centers (SOCs) while remaining accessible to novices.

### A. Dashboard Overview
The main dashboard is divided into three functional areas:
1.  **Live Monitoring**: Shows real-time throughput and connection stats.
2.  **Risk Assessment**: Displays the current System State (SAFE, WARNING, CRITICAL).
3.  **Event Timeline**: A chronological list of security events with plain-English explanations.

**[FIGURE 4: Screenshot of Main Dashboard]**
*(Description: Screenshot showing the ThreatScope Dashboard with "Safe" green status and live graphs)*

### B. Visual Analytics
The "Visual Analytics" section provides immediate situational awareness. The Line Chart auto-scales to show traffic spikes, often indicative of DDoS attacks or large file transfers. The Pie Chart helps users identify protocol anomalies (e.g., a sudden surge in ICMP traffic).

**[FIGURE 5: Visual Analytics Panel]**
*(Description: Detailed view of the Traffic Graph and Protocol Distribution Pie Chart)*

### C. Dark Mode & Ergonomics
A dark color scheme (#0f0f0f background) was chosen to reduce eye strain during long monitoring sessions, consistent with industry-standard tools like Splunk or Grafana. Critical alerts use high-contrast Red (#ff4444) to draw immediate attention.

---

## VII. EXPERIMENTAL RESULTS

The system was tested in a controlled lab environment consisting of two Virtual Machines (VMs) connected to a virtual switch.
*   **Attacker VM**: Kali Linux running `nmap`, `hping3`, and `metasploit`.
*   **Victim VM**: Windows 10 running ThreatScope v2.0.

### A. Detection Accuracy
We conducted 50 trials for each attack vector.

| Attack Vector | Detection Rate | False Positives |
| :--- | :---: | :---: |
| SYN Flood | 100% | 0% |
| UDP Flood | 98% | 2% |
| Port Scan (Intense) | 100% | 0% |
| Port Scan (Stealth) | 88% | 5% |
| Backdoor Connection | 100% | 0% |

ThreatScope successfully identified all high-volume attacks. Stealth scans were detected with slightly lower accuracy due to the 5-second window limitation.

**[FIGURE 6: Detection Accuracy Bar Chart]**
*(Description: A bar chart visualizing the detection rates from the table above)*

### B. Performance Overhead
We measured the system resource usage under varying traffic loads.
*   **Idle**: <1% CPU, 120MB RAM.
*   **10 Mbps Load**: 3% CPU, 180MB RAM.
*   **100 Mbps Load**: 8% CPU, 250MB RAM.
*   **DDoS Attack (10k pps)**: 15% CPU, 350MB RAM.

The system remained responsive even during the simulated DDoS attack, proving the efficiency of the `ConcurrentHashMap` implementation.

---

## VIII. CONCLUSION AND FUTURE SCOPE

ThreatScope v2.0 demonstrates that a Java-based HIDS can be both powerful and user-friendly. By integrating advanced visualization with explainable risk models, we provide non-technical users with the tools to understand and monitor their digital security environment effectively. The Visual Analytics module transforms abstract network data into actionable insights.

### Future Work
Future iterations (Phase 5) will focus on:
1.  **Machine Learning**: Integrating a Random Forest classifier to detect zero-day anomalies.
2.  **Cloud Sync**: Allowing users to sync alerts to a mobile app for remote monitoring.
3.  **Deep Packet Inspection (DPI)**: Inspecting payload contents for SQL injection and XSS signatures.

---

## IX. REFERENCES

[1] R. Pang et al., "Characteristics of Background Radiation," in *Proc. IMC*, 2004.
[2] "OSSEC: Host-based Intrusion Detection System," [Online]. Available: https://www.ossec.net.
[3] "Wireshark," [Online]. Available: https://www.wireshark.org.
[4] Pcap4J, "A Java library for capturing, crafting and sending packets," [Online]. Available: https://github.com/kaitoy/pcap4j.
[5] Oracle, "JavaFX: Client Technology for Java Systems," [Online]. Available: https://openjfx.io.
[6] V. Paxson, "Bro: A System for Detecting Network Intruders in Real-Time," *Computer Networks*, 1999.
[7] Snort Team, "Snort - Network Intrusion Detection & Prevention System," [Online]. Available: https://www.snort.org.
[8] "Elastic Security: SIEM," [Online]. Available: https://www.elastic.co/security/siem.
