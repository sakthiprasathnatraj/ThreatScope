package com.threatscope.core.explanation;

import com.threatscope.core.model.*;

/**
 * EXPLANATION ENGINE
 * 
 * Generates human-readable explanations for security events.
 * 
 * This is CRITICAL for the "explain-before-alert" philosophy.
 * 
 * Design Principles:
 * - Use simple, non-technical language
 * - Explain WHAT happened
 * - Explain WHY it matters (or why it may NOT matter)
 * - Provide reassurance when appropriate
 * - Recommend clear actions
 * 
 * Target Audience: Non-technical users
 */
public class ExplanationEngine {

    /**
     * Generates complete explanation for a security event.
     * 
     * Includes:
     * - What happened (simple description)
     * - Why it matters
     * - Classification context
     * - Reassurance (if appropriate)
     * 
     * @param event          Security event
     * @param classification Traffic classification
     * @param riskScore      Current risk score
     * @param confidence     Confidence level
     * @return Complete explanation
     */
    public static String explainEvent(SecurityEvent event, TrafficClass classification,
            int riskScore, ConfidenceLevel confidence) {

        StringBuilder explanation = new StringBuilder();

        // WHAT HAPPENED
        explanation.append(explainWhatHappened(event, classification));
        explanation.append("\n\n");

        // WHY IT MATTERS
        explanation.append(explainSignificance(event, classification, riskScore));

        return explanation.toString();
    }

    /**
     * Explains WHAT happened in simple terms.
     * 
     * @param event          Security event
     * @param classification Traffic classification
     * @return Simple description
     */
    public static String explainWhatHappened(SecurityEvent event, TrafficClass classification) {

        String sourceIP = event.getSourceIp();
        ThreatType type = event.getThreatType();
        int evidenceCount = event.getEvidenceCount();

        switch (type) {
            case PORT_SCAN:
                return explainPortScan(sourceIP, evidenceCount, classification);

            case BRUTE_FORCE:
                return explainBruteForce(sourceIP, evidenceCount, classification);

            default:
                return "An external computer (IP: " + sourceIP + ") is interacting with your system.";
        }
    }

    /**
     * Explains port scan activity.
     * 
     * @param sourceIP       Source IP
     * @param portCount      Number of ports
     * @param classification Traffic classification
     * @return Explanation
     */
    private static String explainPortScan(String sourceIP, int portCount, TrafficClass classification) {

        switch (classification) {
            case TRUSTED:
                return "A trusted service (IP: " + sourceIP + ") accessed " + portCount +
                        " different network services on your computer. " +
                        "This is normal network activity from a known provider.";

            case BENIGN_NOISE:
                return "An external computer (IP: " + sourceIP + ") attempted to connect to " +
                        portCount + " different services on your computer. " +
                        "This appears to be automated internet scanning, which is very common.";

            case SUSPICIOUS:
                return "An external computer (IP: " + sourceIP + ") probed " + portCount +
                        " different services on your computer. " +
                        "This behavior is unusual and worth monitoring.";

            case CONFIRMED_THREAT:
                return "An external computer (IP: " + sourceIP + ") aggressively scanned " +
                        portCount + " different services on your computer in rapid succession. " +
                        "This behavior is consistent with an attack.";

            default:
                return "An external computer (IP: " + sourceIP + ") accessed " + portCount +
                        " services on your computer.";
        }
    }

    /**
     * Explains brute-force activity.
     * 
     * @param sourceIP       Source IP
     * @param attemptCount   Number of attempts
     * @param classification Traffic classification
     * @return Explanation
     */
    private static String explainBruteForce(String sourceIP, int attemptCount, TrafficClass classification) {

        switch (classification) {
            case TRUSTED:
                return "A trusted service (IP: " + sourceIP + ") made " + attemptCount +
                        " connection attempts to your system. " +
                        "This is likely normal service behavior.";

            case BENIGN_NOISE:
                return "An external computer (IP: " + sourceIP + ") made " + attemptCount +
                        " connection attempts. " +
                        "This could be a misconfigured service or automated testing.";

            case SUSPICIOUS:
                return "An external computer (IP: " + sourceIP + ") made " + attemptCount +
                        " repeated connection attempts to the same service. " +
                        "This could be an attempt to guess passwords or find vulnerabilities.";

            case CONFIRMED_THREAT:
                return "An external computer (IP: " + sourceIP + ") made " + attemptCount +
                        " aggressive connection attempts to your system. " +
                        "This behavior is consistent with a brute-force attack.";

            default:
                return "An external computer (IP: " + sourceIP + ") made " + attemptCount +
                        " connection attempts.";
        }
    }

    /**
     * Explains WHY the event matters (or doesn't matter).
     * 
     * @param event          Security event
     * @param classification Traffic classification
     * @param riskScore      Risk score
     * @return Significance explanation
     */
    public static String explainSignificance(SecurityEvent event, TrafficClass classification, int riskScore) {

        switch (classification) {
            case TRUSTED:
                return "WHY THIS MATTERS:\n" +
                        "This activity is from a trusted source and is part of normal internet operations. " +
                        "Services like cloud providers, CDNs, and DNS servers regularly communicate with your computer. "
                        +
                        "This does NOT indicate an attack.";

            case BENIGN_NOISE:
                return "WHY THIS MATTERS:\n" +
                        "This type of activity is extremely common on the internet. " +
                        "Thousands of automated scanners probe random computers every day for research purposes. " +
                        "This is similar to someone checking if your door is locked - annoying, but not dangerous. " +
                        "Your computer's firewall is designed to handle this. " +
                        "This does NOT indicate a targeted attack.";

            case SUSPICIOUS:
                return "WHY THIS MATTERS:\n" +
                        "This behavior is unusual and could indicate reconnaissance for an attack. " +
                        "However, it could also be automated scanning or a misconfigured service. " +
                        "We are monitoring this activity. " +
                        "If the behavior escalates or becomes more aggressive, we will alert you.";

            case CONFIRMED_THREAT:
                return "WHY THIS MATTERS:\n" +
                        "This aggressive behavior is NOT normal internet activity. " +
                        "The source is actively probing your system, which could be preparation for an attack. " +
                        "While your firewall should block most attempts, you should be aware of this activity. " +
                        "This warrants your attention.";

            default:
                return "WHY THIS MATTERS:\n" +
                        "We are analyzing this activity to determine if it poses a risk.";
        }
    }

    /**
     * Generates reassurance message.
     * 
     * Only for TRUSTED and BENIGN_NOISE classifications.
     * 
     * @param classification Traffic classification
     * @return Reassurance message, or empty string
     */
    public static String generateReassurance(TrafficClass classification) {

        if (!classification.shouldReassure()) {
            return "";
        }

        switch (classification) {
            case TRUSTED:
                return "\nREASSURANCE:\n" +
                        "This is normal network activity. No action is needed. " +
                        "We will continue to monitor your network and alert you if we detect genuine threats.";

            case BENIGN_NOISE:
                return "\nREASSURANCE:\n" +
                        "This activity does not indicate your computer is under attack. " +
                        "All internet-connected devices experience this type of scanning regularly. " +
                        "Your firewall is protecting you. " +
                        "We will alert you if the behavior escalates or becomes more aggressive.";

            default:
                return "";
        }
    }

    /**
     * Recommends action based on classification and risk.
     * 
     * @param classification Traffic classification
     * @param riskScore      Risk score
     * @param confidence     Confidence level
     * @return Action recommendation
     */
    public static String recommendAction(TrafficClass classification, int riskScore, ConfidenceLevel confidence) {

        StringBuilder action = new StringBuilder("RECOMMENDED ACTION:\n");

        switch (classification) {
            case TRUSTED:
                action.append("No action needed. This is normal network activity.");
                break;

            case BENIGN_NOISE:
                action.append("No action needed. We are monitoring the situation.");
                break;

            case SUSPICIOUS:
                action.append("Monitor your system for unusual activity. ");
                if (riskScore >= 50) {
                    action.append("Consider reviewing your firewall logs. ");
                }
                action.append("No immediate action required.");
                break;

            case CONFIRMED_THREAT:
                action.append("Consider the following actions:\n");
                action.append("  • Review your firewall settings\n");
                action.append("  • Monitor system logs for unusual activity\n");
                action.append("  • Consider blocking this IP address\n");
                if (confidence == ConfidenceLevel.HIGH) {
                    action.append("  • Run a security scan of your system");
                }
                break;

            default:
                action.append("Monitor the situation.");
        }

        return action.toString();
    }

    /**
     * Generates complete user-friendly message.
     * 
     * This is the main output that users will see.
     * 
     * @param event          Security event
     * @param classification Traffic classification
     * @param riskScore      Risk score
     * @param confidence     Confidence level
     * @param systemState    Current system state
     * @return Complete formatted message
     */
    public static String generateCompleteMessage(SecurityEvent event, TrafficClass classification,
            int riskScore, ConfidenceLevel confidence,
            String systemState) {

        StringBuilder message = new StringBuilder();

        message.append("========================================\n");
        message.append("SECURITY OBSERVATION\n");
        message.append("========================================\n\n");

        // WHAT HAPPENED
        message.append("WHAT HAPPENED:\n");
        message.append(explainWhatHappened(event, classification));
        message.append("\n\n");

        // WHY IT MATTERS
        message.append(explainSignificance(event, classification, riskScore));
        message.append("\n\n");

        // CLASSIFICATION & METRICS
        message.append("CLASSIFICATION: ").append(classification.getDisplayName()).append("\n");
        message.append("RISK LEVEL: ").append(getRiskLevelDescription(riskScore)).append(" (").append(riskScore)
                .append("/100)\n");
        message.append("CONFIDENCE: ").append(confidence).append("\n");
        message.append("SYSTEM STATE: ").append(systemState).append("\n\n");

        // RECOMMENDED ACTION
        message.append(recommendAction(classification, riskScore, confidence));
        message.append("\n");

        // REASSURANCE (if appropriate)
        String reassurance = generateReassurance(classification);
        if (!reassurance.isEmpty()) {
            message.append("\n").append(reassurance).append("\n");
        }

        message.append("========================================\n");

        return message.toString();
    }

    /**
     * Converts numeric risk score to descriptive level.
     * 
     * @param riskScore Risk score (0-100)
     * @return Risk level description
     */
    private static String getRiskLevelDescription(int riskScore) {
        if (riskScore < 20)
            return "Minimal";
        if (riskScore < 40)
            return "Low";
        if (riskScore < 60)
            return "Moderate";
        if (riskScore < 80)
            return "Elevated";
        return "High";
    }
}
