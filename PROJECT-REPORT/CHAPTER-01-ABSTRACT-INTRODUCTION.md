
# THREATSCOPE: A USER-CENTRIC REAL-TIME NETWORK THREAT DETECTION AND EXPLANATION PLATFORM

### Department of Computer Science and Engineering
### Academic Year: 2025–2026

---

> **Submitted By:** [Your Name]
> **Register Number:** [Your Register Number]
> **Institution:** [Your College Name]
> **Guide:** [Your Guide Name], [Designation]

---

---

# ABSTRACT

In today's hyper-connected digital landscape, network security threats are increasing in both frequency and sophistication. Traditional Intrusion Detection Systems (IDS) are often complex, resource-intensive, and produce large volumes of alerts—many of which are false positives—leading to what is commonly referred to as "alert fatigue." Security analysts spend enormous amounts of time sifting through noise rather than responding to genuine threats.

**ThreatScope** is a user-centric, real-time network threat detection and explanation platform designed to address these limitations. Built using **Java 21**, **Pcap4J** for live packet capture, and a layered backend architecture, ThreatScope captures, decodes, classifies, and analyses network traffic to detect suspicious patterns including Port Scans, Brute Force attempts, DDoS attacks (SYN Flood, UDP Flood, ICMP Flood), Backdoor Access attempts, and suspicious TCP flag combinations (Null Scan, XMAS Scan, Stealth FIN Scan).

The system's key innovation lies in its **"Explain-Before-Alert" philosophy**—every security observation is accompanied by a human-readable explanation, a risk score (0–100), a confidence level (LOW / MEDIUM / HIGH), and a recommended action. The system uses a **Sliding Time-Window Aggregation** mechanism to prevent per-packet false positives, and a **Classification-Based Risk Capping** engine that prevents trusted or benign traffic sources (e.g., CDN providers) from triggering critical-level alerts.

The system state machine (SAFE → OBSERVE → WARNING → CRITICAL) includes an **anti-flapping mechanism** that prevents rapid oscillation between states by enforcing a 30-second cooldown on downward transitions. This ensures that security personnel receive meaningful, actionable alerts rather than a flood of noise.

ThreatScope is implemented as a **JavaFX desktop application** with an integrated backend monitoring engine. It compiles and runs on Windows using the Maven build tool and Npcap for low-level packet capture. The system successfully detects realistic threat scenarios, produces clean structured output, and has been validated as production-quality, demo-ready, and academically sound.

**Keywords:** Intrusion Detection System, Network Security, Packet Analysis, Pcap4J, Java, Real-Time Monitoring, Risk Scoring, Threat Classification, Alert Fatigue, Sliding Window Detection.

---

---

# CHAPTER 1 – INTRODUCTION

## 1.1 Background and Motivation

The rapid expansion of internet-connected systems has fundamentally transformed how organisations operate. From small businesses to large enterprises, virtually every organisation relies on network infrastructure to conduct daily operations. This increasing reliance on digital networks has simultaneously increased the attack surface available to malicious actors.

According to industry reports, the number of reported cybersecurity incidents grows by approximately 15% year-on-year. Threats such as port scanning, brute-force login attempts, Distributed Denial of Service (DDoS) attacks, and malware communications are now commonplace. Organisations that lack the tools and expertise to detect and respond to these threats in real time are at significant risk of data breaches, service disruptions, and reputational damage.

Traditional security solutions—such as commercial firewalls, SNORT-based IDS, and SIEM platforms—are powerful but carry significant drawbacks for smaller organisations and academic environments:

- **High Cost:** Enterprise IDS/SIEM solutions can cost thousands of dollars per year.
- **Complexity:** Configuration and tuning require specialised security expertise.
- **Alert Fatigue:** These systems generate enormous volumes of alerts, many of which are false positives. Analysts spend more time dismissing alerts than responding to real threats.
- **Lack of Explainability:** Most IDS tools report *what* was detected but not *why* it is considered a threat or *what* should be done about it.

These challenges motivate the development of **ThreatScope** — a lightweight, explainable, and user-centric intrusion detection platform designed to provide meaningful threat visibility without overwhelming the user.

---

## 1.2 Problem Statement

The primary problems addressed by this project are:

1. **Alert Fatigue:** Existing IDS generate too many alerts. Many are false positives resulting from scanning legitimate CDN traffic, local network broadcasts, or NIC offloading artifacts. Security teams stop trusting their own systems.

2. **Lack of Explainability:** Most security tools report a threat type and severity without explaining the reasoning behind the alert. Non-expert users cannot understand or act appropriately on these alerts.

3. **Per-Packet Analysis Limitations:** Naïve IDS implementations trigger alerts on individual packets rather than behavioural patterns, leading to massive numbers of meaningless alerts.

4. **Rigid State Machines:** Many IDS state machines oscillate rapidly between states (state flapping), creating confusion and instability in the monitoring process.

5. **Inaccessibility:** High-cost and high-complexity tools are inaccessible to small organisations, academic researchers, and individual security practitioners.

**ThreatScope** addresses all five of these problems through:
- **Sliding time-window aggregation** (prevents per-packet alert spam)
- **Traffic classification with risk capping** (prevents false CRITICAL alerts from benign sources)
- **Explain-before-alert engine** (human-readable explanations for every observation)
- **Anti-flapping state machine** (stable, meaningful state transitions)
- **Open-source, Java-based implementation** (accessible and extensible)

---

### Key Objectives of ThreatScope

| # | Objective | Description |
|---|-----------|-------------|
| 1 | Real-Time Capture | Capture live network packets using Pcap4J |
| 2 | Multi-Threat Detection | Detect Port Scan, Brute Force, DDoS, Backdoor, TCP Anomalies |
| 3 | Explainable Alerts | Provide human-readable explanation for every security event |
| 4 | Risk Scoring | Calculate 0–100 risk score with gradual escalation |
| 5 | Traffic Classification | Classify source IPs as TRUSTED / BENIGN / SUSPICIOUS / CONFIRMED_THREAT |
| 6 | State Management | Manage SAFE → OBSERVE → WARNING → CRITICAL states with anti-flapping |
| 7 | False Positive Reduction | Multi-layer filtering to eliminate noise |
| 8 | Desktop Interface | JavaFX GUI for real-time dashboard visualisation |

---

### 1.3 Scope of the Project

The scope of ThreatScope v1.0 includes:

- **Network Layer Monitoring:** IPv4 traffic analysis at the packet level using Pcap4J and Npcap.
- **Detection Capabilities:** Port Scan, Brute Force, SYN Flood, UDP Flood, ICMP Flood, Backdoor Port Access, Null Scan, XMAS Scan, Stealth FIN Scan.
- **Risk Engine:** Evidence-based risk scoring with classification-based capping.
- **Explainability Engine:** Human-readable explanations with recommended actions.
- **Desktop Application:** JavaFX-based GUI with real-time monitoring dashboard.
- **Platform:** Windows OS (Windows 10/11) with Npcap driver.
- **Build System:** Apache Maven with Java 21.

**Out of Scope (v1.0):**
- Deep packet inspection (payload analysis)
- Machine learning-based detection
- Cloud deployment
- Linux/macOS support (planned for v2.0)

---

### 1.4 Organisation of the Report

This report is organised as follows:

| Chapter | Title | Description |
|---------|-------|-------------|
| 2 | System Analysis | Existing systems study, problem analysis |
| 3 | System Requirements | Hardware, software, functional requirements |
| 4 | System Overview | Architecture, data flow, component overview |
| 5 | System Design | Detailed design, class diagrams, algorithms |
| 6 | System Testing & Implementation | Test cases, results, implementation screenshots |
| 7 | Conclusion | Summary of achievements |
| 8 | Future Enhancement | Planned improvements |
| 9 | References | Academic and technical references |
| 10 | Appendix | Source code listings |

---

*End of Chapter 1*
