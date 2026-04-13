
# CHAPTER 9 – REFERENCES

## 9.1 References

The following academic papers, books, technical documentation, and online resources were referenced during the design and development of ThreatScope.

---

### 9.1.1 Research Papers and Journals

[1] V. Paxson, "Bro: A System for Detecting Network Intruders in Real-Time," *Computer Networks*, vol. 31, no. 23–24, pp. 2435–2463, 1999. DOI: 10.1016/S1389-1286(99)00112-7

[2] M. Roesch, "Snort - Lightweight Intrusion Detection for Networks," *USENIX LISA '99 Proceedings*, 1999. Available at: https://www.usenix.org/legacy/publications/library/proceedings/lisa99/roesch.html

[3] S. Axelsson, "The Base-Rate Fallacy and the Difficulty of Intrusion Detection," *ACM Transactions on Information and System Security*, vol. 3, no. 3, pp. 186–205, Aug. 2000. DOI: 10.1145/357830.357849

[4] R. Heady, G. Luger, A. Maccabe, and M. Servilla, "The Architecture of a Network Level Intrusion Detection System," Technical Report, Department of Computer Science, University of New Mexico, 1990.

[5] P. Garcia-Teodoro, J. Diaz-Verdejo, G. Maciá-Fernández, and E. Vázquez, "Anomaly-based Network Intrusion Detection: Techniques, Systems and Challenges," *Computers & Security*, vol. 28, no. 1–2, pp. 18–28, Feb. 2009. DOI: 10.1016/j.cose.2008.08.003

[6] A. K. Ghosh and A. Schwartzbard, "A Study in Using Neural Networks for Anomaly and Misuse Detection," *Proceedings of the 8th USENIX Security Symposium*, 1999.

[7] S. Mukherjee, H. Heberlein, and K. Levitt, "Network Intrusion Detection," *IEEE Network*, vol. 8, no. 3, pp. 26–41, May/June 1994. DOI: 10.1109/65.283931

[8] C. Modi, D. Patel, B. Borisaniya, H. Patel, A. Patel, and M. Rajarajan, "A Survey of Intrusion Detection Techniques in Cloud," *Journal of Network and Computer Applications*, vol. 36, no. 1, pp. 42–57, 2013. DOI: 10.1016/j.jnca.2012.05.003

[9] W. Lee and S. J. Stolfo, "Data Mining Approaches for Intrusion Detection," *Proceedings of the 7th USENIX Security Symposium*, San Antonio, TX, USA, 1998.

[10] D. Anderson et al., "Detecting Unusual Program Behavior Using the Statistical Component of the Next-Generation Intrusion Detection Expert System (NIDES)," Technical Report, SRI International, 1995.

---

### 9.1.2 Books and Textbooks

[11] W. Stallings, *Network Security Essentials: Applications and Standards*, 6th ed. Pearson, 2017. ISBN: 978-0-13-452733-7

[12] C. Kaufman, R. Perlman, and M. Speciner, *Network Security: Private Communication in a Public World*, 2nd ed. Prentice Hall, 2002. ISBN: 978-0-13-046019-6

[13] K. Scarfone and P. Mell, "Guide to Intrusion Detection and Prevention Systems (IDPS)," *NIST Special Publication 800-94*, National Institute of Standards and Technology, Feb. 2007. Available: https://doi.org/10.6028/NIST.SP.800-94

[14] E. Cole, *Network Security Bible*, 2nd ed. Wiley Publishing, 2009. ISBN: 978-0-470-50715-3

---

### 9.1.3 Technical Documentation and Standards

[15] NIST, "Computer Security Incident Handling Guide," *Special Publication 800-61 Rev. 2*, Aug. 2012. Available: https://nvlpubs.nist.gov/nistpubs/SpecialPublications/NIST.SP.800-61r2.pdf

[16] Pcap4J Project, "Pcap4J Documentation," Version 1.8.2, GitHub. Available: https://github.com/kaitoy/pcap4j

[17] Oracle Corporation, "Java Platform, Standard Edition 21 API Specification," 2023. Available: https://docs.oracle.com/en/java/release/21/

[18] OpenJFX Project, "JavaFX 21 API Documentation," 2023. Available: https://openjfx.io/javadoc/21/

[19] Npcap, "Npcap: Windows Packet Capture Library and Driver," Nmap Project. Available: https://npcap.com/

[20] Apache Maven Project, "Maven Build Lifecycle Reference," Apache Software Foundation, 2023. Available: https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html

---

### 9.1.4 Online Resources, CVE Databases, and Threat Intelligence

[21] MITRE Corporation, "Common Vulnerabilities and Exposures (CVE)," 2024. Available: https://cve.mitre.org/

[22] AbuseIPDB, "IP Address Reputation and Threat Intelligence," 2024. Available: https://www.abuseipdb.com/

[23] Shodan, "The Search Engine for Internet-Connected Devices," 2024. Available: https://www.shodan.io/

[24] CIRCL (Computer Incident Response Center Luxembourg), "MISP Threat Sharing," 2024. Available: https://www.misp-project.org/

[25] Verizon, "2023 Data Breach Investigations Report (DBIR)," Verizon Business, 2023. Available: https://www.verizon.com/business/resources/reports/dbir/

---

*End of Chapter 9*

---

# CHAPTER 10 – APPENDIX

## 10.1 Source Code Listings

This appendix contains complete source code listings for the core classes of ThreatScope v1.0.

---

### Appendix A: Main.java – Application Entry Point

```java
package com.threatscope;

import com.threatscope.core.capture.NetworkInterfaceScanner;
import com.threatscope.core.capture.PacketSniffer;
import com.threatscope.core.output.OutputGateway;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ThreatScope v1.0 - Real-Time Network Threat Detection Platform
 *
 * Architecture (9-Layer Backend):
 * 1. CAPTURE  → PacketSniffer
 * 2. DECODE   → PacketDecoder
 * 3. CLASSIFY → TrafficClassifier, IPReputationDatabase
 * 4. DETECT   → EventAggregator, DDoSDetector, PatternDetector
 * 5. RISK     → RiskEngine, ConfidenceEvaluator
 * 6. EXPLAIN  → ExplanationEngine
 * 7. STATE    → SystemStateManager
 * 8. OUTPUT   → OutputGateway
 * 9. MODEL    → SecurityEvent, ThreatType, TrafficClass
 */
public class Main {

    public static void main(String[] args) {

        // Suppress Pcap4J DEBUG noise - only show WARN/ERROR
        Logger.getLogger("org.pcap4j").setLevel(Level.WARNING);
        Logger.getLogger("").setLevel(Level.WARNING);

        // Print startup banner
        OutputGateway.printStartupBanner();

        // List available network interfaces
        System.out.println("📋 Available Network Interfaces:");
        NetworkInterfaceScanner.listInterfaces();
        System.out.println();

        OutputGateway.printInterfaceInstructions();
        OutputGateway.printMonitoringStart();

        // Start packet capture on interface index 4
        // (Adjust based on your network setup)
        PacketSniffer.startSniffing(4);
    }
}
```

---

### Appendix B: SecurityEvent.java – Core Data Model

```java
package com.threatscope.core.model;

/**
 * Immutable security event representing a detected threat.
 * Core data structure passed between all backend layers.
 */
public class SecurityEvent {

    private final String sourceIp;
    private final ThreatType threatType;
    private final String description;
    private final int evidenceCount;
    private final long timestamp;
    private final TrafficClass classification;
    private final long duration;

    public SecurityEvent(String sourceIp, ThreatType threatType,
            String description, int evidenceCount,
            TrafficClass classification, long duration) {
        this.sourceIp = sourceIp;
        this.threatType = threatType;
        this.description = description;
        this.evidenceCount = evidenceCount;
        this.timestamp = System.currentTimeMillis();
        this.classification = classification;
        this.duration = duration;
    }

    // Factory Methods
    public static SecurityEvent portScan(String sourceIp, int portCount,
            TrafficClass classification, long duration) {
        return new SecurityEvent(sourceIp, ThreatType.PORT_SCAN,
                "Multiple destination ports accessed (" + portCount
                + ") within time window",
                portCount, classification, duration);
    }

    public static SecurityEvent ddosAttack(String sourceIp, int packetsPerSec,
            TrafficClass classification, long duration) {
        return new SecurityEvent(sourceIp, ThreatType.DDOS_ATTACK,
                "DDoS attack: " + packetsPerSec + " packets/sec",
                packetsPerSec, classification, duration);
    }

    public static SecurityEvent backdoorAttempt(String sourceIp, int port,
            TrafficClass classification) {
        return new SecurityEvent(sourceIp, ThreatType.BACKDOOR_ATTEMPT,
                "Connection attempt to known backdoor port: " + port,
                1, classification, 0);
    }

    // Getters
    public String getSourceIp()         { return sourceIp; }
    public ThreatType getThreatType()   { return threatType; }
    public String getDescription()      { return description; }
    public int getEvidenceCount()       { return evidenceCount; }
    public long getTimestamp()          { return timestamp; }
    public TrafficClass getClassification() { return classification; }
    public long getDuration()           { return duration; }
}
```

---

### Appendix C: System Configuration (threatscope.properties)

```properties
# ThreatScope Runtime Configuration
# Network interface index (0-based, run app to see list)
network.interface.index=4

# Detection thresholds
detection.portScan.threshold=10
detection.portScan.windowMs=10000
detection.alert.cooldownMs=60000

# State machine cooldown
state.transition.cooldownMs=30000

# Risk escalation cap per event
risk.maxIncreasePerEvent=20

# Risk decay (per minute of inactivity)
risk.decayAmount=10
risk.decayIntervalMs=60000
```

---

### Appendix D: Maven Build Configuration (pom.xml)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.threatscope</groupId>
    <artifactId>threatscope</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>21</maven.compiler.source>
        <maven.compiler.target>21</maven.compiler.target>
        <maven.compiler.release>21</maven.compiler.release>
    </properties>

    <dependencies>
        <!-- Pcap4J: Live packet capture -->
        <dependency>
            <groupId>org.pcap4j</groupId>
            <artifactId>pcap4j-core</artifactId>
            <version>1.8.2</version>
        </dependency>
        <dependency>
            <groupId>org.pcap4j</groupId>
            <artifactId>pcap4j-packetfactory-static</artifactId>
            <version>1.8.2</version>
        </dependency>

        <!-- JavaFX GUI -->
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-controls</artifactId>
            <version>21</version>
        </dependency>
        <dependency>
            <groupId>org.openjfx</groupId>
            <artifactId>javafx-graphics</artifactId>
            <version>21</version>
        </dependency>

        <!-- Logging -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>1.7.36</version>
        </dependency>
        <dependency>
            <groupId>ch.qos.logback</groupId>
            <artifactId>logback-classic</artifactId>
            <version>1.2.13</version>
        </dependency>
    </dependencies>
</project>
```

---

### Appendix E: How to Run ThreatScope

**Step 1: Install Prerequisites**
```
1. Install Java 21 JDK: https://adoptium.net/
2. Install Apache Maven 3.8+: https://maven.apache.org/
3. Install Npcap: https://npcap.com/
   ✓ Enable "WinPcap API-compatible Mode" during install
```

**Step 2: Compile the Project**
```bash
cd d:\Sakthi\Java\ThreatScope
mvn compile
```

**Step 3: Run the Application**
```bash
mvn exec:java -Dexec.mainClass="com.threatscope.Main"
```

**Step 4: Select Network Interface**
```
The application will list available network interfaces.
Edit Main.java line 75 to change the interface index:
  PacketSniffer.startSniffing(4);  // Change 4 to your interface index
```

**Step 5: Monitor Output**
```
ThreatScope will print security observations when threats are detected.
Press Ctrl+C to stop monitoring.
```

---

*End of Chapter 9 & 10 – References and Appendix*

---

**Document Prepared For:**
ThreatScope v1.0 – Academic Project Report
**Institution:** [Your College Name]
**Department:** Computer Science and Engineering
**Academic Year:** 2025–2026
