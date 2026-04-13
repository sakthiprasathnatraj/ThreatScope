package com.threatscope.core.risk;

import com.threatscope.core.model.TrafficClass;
import com.threatscope.core.model.ThreatType;

/**
 * TRAFFIC CLASSIFIER
 * 
 * Classifies network traffic into categories to prevent false positives.
 * 
 * This is the MOST CRITICAL component for avoiding user panic.
 * 
 * Classification Logic:
 * 1. Check IP reputation (trusted sources)
 * 2. Analyze behavior pattern (port count, timing)
 * 3. Consider threat type
 * 4. Assign classification with justification
 * 
 * Design Philosophy:
 * - When in doubt, classify as BENIGN_NOISE (avoid false alarms)
 * - Only classify as CONFIRMED_THREAT with strong evidence
 * - Always provide reasoning for classification
 */
public class TrafficClassifier {

    // ===== CLASSIFICATION THRESHOLDS =====

    // Port scan thresholds
    private static final int BENIGN_PORT_THRESHOLD = 15; // < 15 ports = likely benign
    private static final int SUSPICIOUS_PORT_THRESHOLD = 25; // 15-25 ports = suspicious
    private static final int THREAT_PORT_THRESHOLD = 40; // > 40 ports = likely threat

    // Time window thresholds (milliseconds)
    private static final long AGGRESSIVE_SCAN_WINDOW = 5_000; // < 5 seconds = aggressive
    private static final long NORMAL_SCAN_WINDOW = 30_000; // < 30 seconds = normal

    /**
     * Classifies traffic based on IP, behavior, and threat type.
     * 
     * This is the main entry point for classification.
     * 
     * @param sourceIP   Source IP address
     * @param threatType Type of threat detected
     * @param portCount  Number of unique ports accessed
     * @param duration   Duration of activity (milliseconds)
     * @return Traffic classification
     */
    public static TrafficClass classify(String sourceIP, ThreatType threatType, int portCount, long duration) {

        // ===== STEP 1: CHECK IP REPUTATION =====

        // Trusted IPs (CDN, cloud, DNS, Microsoft)
        if (IPReputationDatabase.isTrustedIP(sourceIP)) {
            return TrafficClass.TRUSTED;
        }

        // Known research scanners (Shodan, Censys)
        if (IPReputationDatabase.isResearchScanner(sourceIP)) {
            return TrafficClass.BENIGN_NOISE;
        }

        // ISP infrastructure
        if (IPReputationDatabase.isISPInfrastructure(sourceIP)) {
            return TrafficClass.BENIGN_NOISE;
        }

        // ===== STEP 2: ANALYZE BEHAVIOR PATTERN =====

        // For PORT_SCAN threats, analyze scan characteristics
        if (threatType == ThreatType.PORT_SCAN) {
            return classifyPortScan(portCount, duration);
        }

        // For BRUTE_FORCE threats (future implementation)
        if (threatType == ThreatType.BRUTE_FORCE) {
            return classifyBruteForce(portCount, duration);
        }

        // Default: treat as suspicious
        return TrafficClass.SUSPICIOUS;
    }

    /**
     * Classifies port scan behavior.
     * 
     * Classification Logic:
     * - Low port count (< 15) = BENIGN_NOISE (common internet scanning)
     * - Medium port count (15-25) = SUSPICIOUS (worth monitoring)
     * - High port count (25-40) + slow = SUSPICIOUS (methodical scan)
     * - High port count (> 40) + fast = CONFIRMED_THREAT (aggressive attack)
     * 
     * @param portCount Number of unique ports
     * @param duration  Duration in milliseconds
     * @return Traffic classification
     */
    private static TrafficClass classifyPortScan(int portCount, long duration) {

        // Low port count: common internet noise
        if (portCount < BENIGN_PORT_THRESHOLD) {
            return TrafficClass.BENIGN_NOISE;
        }

        // Medium port count: suspicious but not alarming
        if (portCount < SUSPICIOUS_PORT_THRESHOLD) {
            return TrafficClass.SUSPICIOUS;
        }

        // High port count: check timing
        if (portCount >= THREAT_PORT_THRESHOLD) {

            // Aggressive scan (many ports, short time) = likely threat
            if (duration < AGGRESSIVE_SCAN_WINDOW) {
                return TrafficClass.CONFIRMED_THREAT;
            }

            // Methodical scan (many ports, longer time) = suspicious
            return TrafficClass.SUSPICIOUS;
        }

        // Default: suspicious
        return TrafficClass.SUSPICIOUS;
    }

    /**
     * Classifies brute-force behavior.
     * 
     * (Future implementation - placeholder for now)
     * 
     * @param attemptCount Number of attempts
     * @param duration     Duration in milliseconds
     * @return Traffic classification
     */
    private static TrafficClass classifyBruteForce(int attemptCount, long duration) {

        // Placeholder logic
        // In production, this would analyze:
        // - Number of failed login attempts
        // - Target services (SSH, RDP, HTTP auth)
        // - Timing patterns
        // - Credential patterns

        if (attemptCount > 50) {
            return TrafficClass.CONFIRMED_THREAT;
        }

        if (attemptCount > 20) {
            return TrafficClass.SUSPICIOUS;
        }

        return TrafficClass.BENIGN_NOISE;
    }

    /**
     * Gets human-readable explanation for classification.
     * 
     * This helps users understand WHY traffic was classified a certain way.
     * 
     * @param classification Traffic classification
     * @param sourceIP       Source IP
     * @param portCount      Port count
     * @return Explanation string
     */
    public static String explainClassification(TrafficClass classification, String sourceIP, int portCount) {

        String reputation = IPReputationDatabase.getReputationCategory(sourceIP);

        switch (classification) {
            case TRUSTED:
                return "Source is a " + reputation + ", which is a known trusted service.";

            case BENIGN_NOISE:
                if (!reputation.equals("Unknown")) {
                    return "Source is a " + reputation + ". This type of scanning is normal and expected.";
                }
                return "This appears to be automated internet scanning. " +
                        "Thousands of scanners probe random IPs daily. " +
                        "The low port count (" + portCount + ") suggests routine scanning, not an attack.";

            case SUSPICIOUS:
                return "Source accessed " + portCount + " ports, which is unusual. " +
                        "This could be reconnaissance for an attack, or automated scanning. " +
                        "We are monitoring this behavior.";

            case CONFIRMED_THREAT:
                return "Source accessed " + portCount + " ports in rapid succession. " +
                        "This aggressive behavior is consistent with an attack. " +
                        "This is NOT normal internet scanning.";

            default:
                return "Unknown classification.";
        }
    }

    /**
     * Checks if classification should trigger an alert.
     * 
     * TRUSTED and BENIGN_NOISE should NOT trigger alerts.
     * Only SUSPICIOUS and CONFIRMED_THREAT should alert.
     * 
     * @param classification Traffic classification
     * @return true if should alert
     */
    public static boolean shouldAlert(TrafficClass classification) {
        return classification == TrafficClass.SUSPICIOUS ||
                classification == TrafficClass.CONFIRMED_THREAT;
    }

    /**
     * Gets recommended action for classification.
     * 
     * @param classification Traffic classification
     * @return Recommended action string
     */
    public static String getRecommendedAction(TrafficClass classification) {

        switch (classification) {
            case TRUSTED:
                return "No action needed. This is normal network activity.";

            case BENIGN_NOISE:
                return "No action needed. We are monitoring the situation.";

            case SUSPICIOUS:
                return "Monitor your system for unusual activity. " +
                        "Consider reviewing firewall logs. " +
                        "No immediate action required.";

            case CONFIRMED_THREAT:
                return "Consider blocking this IP address in your firewall. " +
                        "Monitor your system for signs of compromise. " +
                        "Review security logs for related activity.";

            default:
                return "Monitor the situation.";
        }
    }
}
