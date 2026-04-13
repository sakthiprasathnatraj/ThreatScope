
# CHAPTER 3 – SYSTEM REQUIREMENTS

## 3.1 Requirements Specification

This chapter documents the complete requirements for ThreatScope — hardware requirements, software requirements, and functional/non-functional requirements gathered through the system analysis phase.

---

### 3.1.1 Hardware Requirements

The following hardware configuration is the **minimum recommended** for running ThreatScope effectively:

| Component | Minimum Requirement | Recommended |
|-----------|-------------------|-------------|
| **Processor** | Intel Core i3 (2.0 GHz) | Intel Core i5/i7 (3.0 GHz+) |
| **RAM** | 4 GB | 8 GB or more |
| **Storage** | 500 MB free disk space | 2 GB or more |
| **Network Interface** | Any Ethernet or Wi-Fi adapter | Gigabit Ethernet (for high-traffic environments) |
| **Display** | 1280 × 720 resolution | 1920 × 1080 or higher |
| **Operating System** | Windows 10 (64-bit) | Windows 10 / Windows 11 (64-bit) |

**Notes:**
- Administrator (root) privileges are required to access raw network sockets for packet capture.
- The network interface must be active and connected to a network for live packet monitoring.
- For high-traffic network environments (enterprise networks), 8+ GB RAM and a fast CPU are recommended to keep up with packet rates.

---

### 3.1.2 Software Requirements

#### Development Environment

| Software | Version | Purpose |
|----------|---------|---------|
| **Java JDK** | Java 21 (LTS) | Core programming language and runtime |
| **Apache Maven** | 3.8+ | Build automation and dependency management |
| **IntelliJ IDEA** | 2023.x or later | Integrated Development Environment (IDE) |
| **Git** | 2.x | Version control |

#### Runtime Dependencies

| Library | Version | Purpose |
|---------|---------|---------|
| **Pcap4J Core** | 1.8.2 | Java wrapper for libpcap/Npcap – raw packet capture |
| **Pcap4J Packet Factory** | 1.8.2 | Static packet factory for protocol decoding |
| **JavaFX Controls** | 21 | GUI components (buttons, tables, charts) |
| **JavaFX Graphics** | 21 | Rendering engine for JavaFX |
| **JavaFX Base** | 21 | Core JavaFX bindings and properties |
| **SLF4J API** | 1.7.36 | Logging abstraction layer |
| **Logback Classic** | 1.2.13 | Logging implementation |

#### System-Level Dependencies

| Software | Version | Purpose |
|----------|---------|---------|
| **Npcap** | 1.70+ | Low-level Windows packet capture driver (WinPcap replacement) |
| **Windows Packet Filter** | Bundled with Npcap | Kernel-level packet capture filter |

> **Note:** Npcap must be installed with "WinPcap API-compatible Mode" enabled for Pcap4J compatibility.

---

### 3.1.3 Maven Project Configuration

The project's `pom.xml` defines the build configuration:

```xml
<project>
  <groupId>com.threatscope</groupId>
  <artifactId>threatscope</artifactId>
  <version>1.0-SNAPSHOT</version>

  <properties>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <maven.compiler.release>21</maven.compiler.release>
  </properties>

  <dependencies>
    <!-- Pcap4J for live packet capture -->
    <dependency>
      <groupId>org.pcap4j</groupId>
      <artifactId>pcap4j-core</artifactId>
      <version>1.8.2</version>
    </dependency>

    <!-- JavaFX for GUI -->
    <dependency>
      <groupId>org.openjfx</groupId>
      <artifactId>javafx-controls</artifactId>
      <version>21</version>
    </dependency>

    <!-- Logging -->
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.2.13</version>
    </dependency>
  </dependencies>
</project>
```

---

### 3.1.4 Functional Requirements

Functional requirements describe **what the system must do**:

#### FR-01: Live Packet Capture
- The system shall capture live IPv4 network packets from the selected network interface.
- The system shall filter out non-IPv4 packets (ARP, IPv6, etc.) and zero-length packets caused by NIC offloading.

#### FR-02: Local Traffic Filtering
- The system shall ignore packets whose source IP belongs to private/local address ranges:
  - `127.x.x.x` (loopback)
  - `192.168.x.x` (private LAN)
  - `10.x.x.x` (private LAN)
  - `172.16.x.x – 172.31.x.x` (private LAN)

#### FR-03: Traffic Classification
- The system shall classify each source IP into one of four traffic classes:
  - `TRUSTED` – Known safe IPs (max risk cap: 20)
  - `BENIGN_NOISE` – CDN, cloud providers (max risk cap: 40)
  - `SUSPICIOUS` – Unknown or flagged IPs (max risk cap: 75)
  - `CONFIRMED_THREAT` – Blacklisted IPs (max risk cap: 100)

#### FR-04: Port Scan Detection
- The system shall detect port scanning activity when a source IP accesses ≥ 10 unique destination ports within a 10-second sliding time window.

#### FR-05: Brute Force Detection
- The system shall detect brute-force attacks when a source IP makes repeated connection attempts to the same destination port within a short time window.

#### FR-06: DDoS Detection
- The system shall detect DDoS attacks including:
  - **SYN Flood:** High rate of TCP SYN packets from a single source
  - **UDP Flood:** High rate of UDP packets from a single source
  - **ICMP Flood:** High rate of ICMP packets from a single source

#### FR-07: Pattern-Based Detection
- The system shall detect suspicious TCP flag combinations:
  - **Null Scan:** No TCP flags set
  - **XMAS Scan:** FIN + URG + PSH flags set
  - **Stealth FIN Scan:** FIN set without ACK

#### FR-08: Backdoor Port Detection
- The system shall alert when connections are made to known backdoor ports:
  - 31337 (Back Orifice), 12345 (NetBus), 6667 (IRC Botnet C&C), 2323 (Mirai), 4444 (Metasploit), 5555 (Android ADB), 27374 (Sub7)

#### FR-09: Risk Scoring
- The system shall calculate a risk score (0–100) using the formula:
  ```
  RawRisk = BaseRisk + (EvidenceCount × EvidenceMultiplier)
  NewRisk = PreviousRisk + min(RawRisk - PreviousRisk, MAX_INCREASE_PER_EVENT)
  FinalRisk = min(NewRisk, ClassificationMaxRisk)
  ```

#### FR-10: Confidence Level Calculation
- The system shall calculate a confidence level (LOW / MEDIUM / HIGH) based on risk score, event count, duration, and traffic classification.

#### FR-11: System State Management
- The system shall maintain a state machine with four states:
  - `SAFE` (risk < 40)
  - `OBSERVE` (risk ≥ 40)
  - `WARNING` (risk ≥ 60)
  - `CRITICAL` (risk ≥ 80 AND confidence == HIGH)
- Downward state transitions shall require a 30-second cooldown.

#### FR-12: Explainability Output
- Every security observation shall include a human-readable explanation and recommended action.

#### FR-13: Alert Cooldown
- The system shall suppress duplicate alerts from the same source IP for a minimum of 60 seconds.

---

### 3.1.5 Non-Functional Requirements

Non-functional requirements describe **how the system must behave**:

| ID | Requirement | Specification |
|----|-------------|--------------|
| NFR-01 | **Performance** | Must process ≥ 1,000 packets/second without packet loss |
| NFR-02 | **Thread Safety** | All shared data structures must be thread-safe (ConcurrentHashMap) |
| NFR-03 | **Memory Management** | Automatic cleanup of expired time windows to prevent memory leaks |
| NFR-04 | **Usability** | GUI must be intuitive; non-expert users must understand alert outputs |
| NFR-05 | **Reliability** | System must not crash on malformed packets |
| NFR-06 | **Maintainability** | Clean layered architecture; each class has one responsibility |
| NFR-07 | **Extensibility** | New detection rules must be addable without modifying existing layers |
| NFR-08 | **Portability** | Must run on Windows 10/11 with Java 21 and Maven |
| NFR-09 | **Logging** | Pcap4J debug logs must be suppressed; only WARNING and ERROR shown |
| NFR-10 | **Output Quality** | Console output must be clean, professional, and screenshot-ready |

---

*End of Chapter 3*
