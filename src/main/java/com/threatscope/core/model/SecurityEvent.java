package com.threatscope.core.model;

/**
 * Immutable security event representing a detected threat.
 * 
 * This is the core data structure passed between detection,
 * risk scoring, and output layers.
 */
public class SecurityEvent {

    private final String sourceIp;
    private final ThreatType threatType;
    private final String description;
    private final int evidenceCount;
    private final long timestamp;
    private final TrafficClass classification; // NEW: Traffic classification
    private final long duration; // NEW: Duration of activity (ms)

    /**
     * Creates a new security event.
     * 
     * @param sourceIp       Source IP address of threat
     * @param threatType     Type of threat detected
     * @param description    Human-readable description
     * @param evidenceCount  Number of evidence items (e.g., port count)
     * @param classification Traffic classification
     * @param duration       Duration of activity in milliseconds
     */
    public SecurityEvent(String sourceIp, ThreatType threatType, String description,
            int evidenceCount, TrafficClass classification, long duration) {
        this.sourceIp = sourceIp;
        this.threatType = threatType;
        this.description = description;
        this.evidenceCount = evidenceCount;
        this.timestamp = System.currentTimeMillis();
        this.classification = classification;
        this.duration = duration;
    }

    // ===== FACTORY METHODS =====

    /**
     * Creates a PORT_SCAN security event.
     * 
     * @param sourceIp       Source IP performing scan
     * @param portCount      Number of unique ports accessed
     * @param classification Traffic classification
     * @param duration       Duration of scan activity (ms)
     * @return SecurityEvent instance
     */
    public static SecurityEvent portScan(String sourceIp, int portCount,
            TrafficClass classification, long duration) {
        String description = "Multiple destination ports accessed (" + portCount + ") within time window";
        return new SecurityEvent(sourceIp, ThreatType.PORT_SCAN, description,
                portCount, classification, duration);
    }

    /**
     * Creates a BRUTE_FORCE security event.
     * 
     * @param sourceIp       Source IP performing attack
     * @param attemptCount   Number of connection attempts
     * @param classification Traffic classification
     * @param duration       Duration of attack activity (ms)
     * @return SecurityEvent instance
     */
    public static SecurityEvent bruteForce(String sourceIp, int attemptCount,
            TrafficClass classification, long duration) {
        String description = "Repeated connection attempts (" + attemptCount + ") to same service";
        return new SecurityEvent(sourceIp, ThreatType.BRUTE_FORCE, description,
                attemptCount, classification, duration);
    }

    /**
     * Creates a DDOS_ATTACK security event.
     * 
     * @param sourceIp       Source IP performing attack
     * @param packetsPerSec  Packet rate (packets per second)
     * @param classification Traffic classification
     * @param duration       Duration of attack activity (ms)
     * @return SecurityEvent instance
     */
    public static SecurityEvent ddosAttack(String sourceIp, int packetsPerSec,
            TrafficClass classification, long duration) {
        String description = "DDoS attack detected: " + packetsPerSec
                + " packets/sec (high packet rate from single source)";
        return new SecurityEvent(sourceIp, ThreatType.DDOS_ATTACK, description,
                packetsPerSec, classification, duration);
    }

    /**
     * Creates a SYN_FLOOD security event.
     * 
     * @param sourceIp       Source IP performing attack
     * @param synPerSec      SYN packet rate (packets per second)
     * @param classification Traffic classification
     * @param duration       Duration of attack activity (ms)
     * @return SecurityEvent instance
     */
    public static SecurityEvent synFlood(String sourceIp, int synPerSec,
            TrafficClass classification, long duration) {
        String description = "DDoS attack detected: " + synPerSec
                + " packets/sec (SYN flood - overwhelming with connection requests)";
        return new SecurityEvent(sourceIp, ThreatType.SYN_FLOOD, description,
                synPerSec, classification, duration);
    }

    /**
     * Creates a UDP_FLOOD security event.
     * 
     * @param sourceIp       Source IP performing attack
     * @param udpPerSec      UDP packet rate (packets per second)
     * @param classification Traffic classification
     * @param duration       Duration of attack activity (ms)
     * @return SecurityEvent instance
     */
    public static SecurityEvent udpFlood(String sourceIp, int udpPerSec,
            TrafficClass classification, long duration) {
        String description = "DDoS attack detected: " + udpPerSec
                + " packets/sec (UDP flood - high volume UDP traffic)";
        return new SecurityEvent(sourceIp, ThreatType.UDP_FLOOD, description,
                udpPerSec, classification, duration);
    }

    /**
     * Creates an ICMP_FLOOD security event.
     * 
     * @param sourceIp       Source IP performing attack
     * @param icmpPerSec     ICMP packet rate (packets per second)
     * @param classification Traffic classification
     * @param duration       Duration of attack activity (ms)
     * @return SecurityEvent instance
     */
    public static SecurityEvent icmpFlood(String sourceIp, int icmpPerSec,
            TrafficClass classification, long duration) {
        String description = "DDoS attack detected: " + icmpPerSec
                + " packets/sec (ICMP flood - excessive ping requests)";
        return new SecurityEvent(sourceIp, ThreatType.ICMP_FLOOD, description,
                icmpPerSec, classification, duration);
    }

    /**
     * Creates a BACKDOOR_ATTEMPT security event.
     * 
     * @param sourceIp       Source IP
     * @param port           The target port (e.g., 31337)
     * @param classification Traffic classification
     * @return SecurityEvent instance
     */
    public static SecurityEvent backdoorAttempt(String sourceIp, int port,
            TrafficClass classification) {
        String description = "Connection attempt to known backdoor port: " + port;
        return new SecurityEvent(sourceIp, ThreatType.BACKDOOR_ATTEMPT, description,
                1, classification, 0);
    }

    /**
     * Creates a SUSPICIOUS_PATTERN security event.
     * 
     * @param sourceIp       Source IP
     * @param details        Details of the pattern (e.g., "XMAS Scan detected")
     * @param classification Traffic classification
     * @return SecurityEvent instance
     */
    public static SecurityEvent suspiciousPattern(String sourceIp, String details,
            TrafficClass classification) {
        return new SecurityEvent(sourceIp, ThreatType.SUSPICIOUS_PATTERN, details,
                1, classification, 0);
    }

    // ===== GETTERS =====

    public String getSourceIp() {
        return sourceIp;
    }

    public ThreatType getThreatType() {
        return threatType;
    }

    public String getDescription() {
        return description;
    }

    public int getEvidenceCount() {
        return evidenceCount;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public TrafficClass getClassification() {
        return classification;
    }

    public long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "SecurityEvent{" +
                "sourceIp='" + sourceIp + '\'' +
                ", threatType=" + threatType +
                ", evidenceCount=" + evidenceCount +
                ", classification=" + classification +
                '}';
    }
}
