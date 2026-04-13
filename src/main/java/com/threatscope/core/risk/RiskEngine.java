package com.threatscope.core.risk;

import com.threatscope.core.model.*;
import com.threatscope.core.state.SystemStateManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * RISK SCORING ENGINE
 * 
 * Responsibility:
 * - Calculate risk score (0-100) based on evidence
 * - Ensure GRADUAL risk escalation (no sudden jumps)
 * - Track risk history per source IP
 * - Calculate confidence level
 * - Forward to state manager and output
 * 
 * Key Principle: Risk must increase gradually over time,
 * never jump from SAFE → CRITICAL in one step.
 */
public class RiskEngine {

    // ===== RISK HISTORY TRACKING =====

    // Track current risk score per source IP
    private static final Map<String, Integer> currentRisk = new ConcurrentHashMap<>();

    // Track event count per source IP (for confidence calculation)
    private static final Map<String, Integer> eventCount = new ConcurrentHashMap<>();

    // Track first event time per source IP (for duration calculation)
    private static final Map<String, Long> firstEventTime = new ConcurrentHashMap<>();

    // ===== CONFIGURATION =====

    // Base risk for PORT_SCAN
    private static final int PORT_SCAN_BASE_RISK = 30;

    // Evidence multiplier: each piece of evidence adds this much risk
    private static final int EVIDENCE_MULTIPLIER = 3;

    // Maximum risk increase per event (prevents sudden jumps)
    private static final int MAX_RISK_INCREASE_PER_EVENT = 20;

    // Risk decay rate: risk decreases over time if no new events
    private static final long RISK_DECAY_INTERVAL_MS = 60_000; // 1 minute
    private static final int RISK_DECAY_AMOUNT = 10;

    /**
     * Processes a security event and calculates risk.
     * 
     * NEW IMPLEMENTATION with classification-based risk capping:
     * 1. Calculate raw risk from evidence
     * 2. Apply classification-based risk cap (CRITICAL!)
     * 3. Update risk gradually
     * 4. Calculate confidence
     * 5. Forward to state manager
     * 6. Generate user-friendly output
     * 
     * @param event Security event to process
     */
    public static void processEvent(SecurityEvent event) {

        String srcIp = event.getSourceIp();
        long now = System.currentTimeMillis();
        TrafficClass classification = event.getClassification();

        // ===== EVENT COUNTING =====

        // Increment event count for this IP
        int count = eventCount.getOrDefault(srcIp, 0) + 1;
        eventCount.put(srcIp, count);

        // Track first event time (for duration calculation)
        firstEventTime.putIfAbsent(srcIp, now);

        // ===== RISK CALCULATION =====

        // Get current risk for this IP
        int previousRisk = currentRisk.getOrDefault(srcIp, 0);

        // Calculate raw risk from evidence
        int rawRisk = calculateRawRisk(event);

        // Calculate risk increase (capped to prevent sudden jumps)
        int riskIncrease = Math.min(rawRisk - previousRisk, MAX_RISK_INCREASE_PER_EVENT);

        // Ensure risk increase is positive
        if (riskIncrease < 0) {
            riskIncrease = 0;
        }

        // Calculate new risk (gradual increase)
        int newRisk = previousRisk + riskIncrease;

        // ===== CLASSIFICATION-BASED RISK CAPPING (CRITICAL!) =====

        // This prevents TRUSTED/BENIGN traffic from triggering high-risk states
        int maxAllowedRisk = classification.getMaxRisk();
        newRisk = Math.min(newRisk, maxAllowedRisk);

        // Cap at 100 (redundant but safe)
        newRisk = Math.min(newRisk, 100);

        // Update risk
        currentRisk.put(srcIp, newRisk);

        // ===== CONFIDENCE CALCULATION =====

        long duration = now - firstEventTime.get(srcIp);
        ConfidenceLevel confidence = calculateConfidence(newRisk, count, duration, classification);

        // ===== FORWARD TO STATE MANAGER =====

        SystemStateManager.evaluateState(newRisk, confidence);

        // ===== USER-FRIENDLY OUTPUT =====

        printUserFriendlyOutput(event, newRisk, confidence);
    }

    /**
     * Calculates raw risk score from event evidence.
     * 
     * Formula: BaseRisk + (EvidenceCount × Multiplier)
     * 
     * @param event Security event
     * @return Raw risk score (0-100)
     */
    private static int calculateRawRisk(SecurityEvent event) {

        int baseRisk = getBaseRisk(event.getThreatType());
        int evidenceBonus = event.getEvidenceCount() * EVIDENCE_MULTIPLIER;

        int totalRisk = baseRisk + evidenceBonus;

        // Cap at 100
        return Math.min(totalRisk, 100);
    }

    /**
     * Returns base risk score for threat type.
     * 
     * @param threatType Threat type
     * @return Base risk score
     */
    private static int getBaseRisk(ThreatType threatType) {
        switch (threatType) {
            case PORT_SCAN:
                return PORT_SCAN_BASE_RISK;
            case BRUTE_FORCE:
                return 40; // Higher base risk for brute-force
            default:
                return 30; // Default moderate-low risk
        }
    }

    /**
     * Calculates confidence level based on risk, event count, duration, and
     * classification.
     * 
     * NEW: Classification affects confidence calculation.
     * TRUSTED/BENIGN traffic can never reach HIGH confidence.
     * 
     * @param risk           Current risk score
     * @param count          Number of events
     * @param duration       Duration in milliseconds
     * @param classification Traffic classification
     * @return Confidence level
     */
    private static ConfidenceLevel calculateConfidence(int risk, int count, long duration,
            TrafficClass classification) {

        // TRUSTED traffic: always LOW confidence (it's not a threat)
        if (classification == TrafficClass.TRUSTED) {
            return ConfidenceLevel.LOW;
        }

        // BENIGN_NOISE: cap at MEDIUM confidence
        if (classification == TrafficClass.BENIGN_NOISE) {
            if (risk >= 30 && count >= 2) {
                return ConfidenceLevel.MEDIUM;
            }
            return ConfidenceLevel.LOW;
        }

        // SUSPICIOUS: can reach HIGH confidence with strong evidence
        if (classification == TrafficClass.SUSPICIOUS) {
            if (risk >= 60 && count >= 3 && duration >= 5000) {
                return ConfidenceLevel.HIGH;
            }
            if (risk >= 40 || count >= 2) {
                return ConfidenceLevel.MEDIUM;
            }
            return ConfidenceLevel.LOW;
        }

        // CONFIRMED_THREAT: easier to reach HIGH confidence
        if (classification == TrafficClass.CONFIRMED_THREAT) {
            if (risk >= 50 && count >= 2) {
                return ConfidenceLevel.HIGH;
            }
            if (risk >= 30) {
                return ConfidenceLevel.MEDIUM;
            }
            return ConfidenceLevel.LOW;
        }

        // Default
        return ConfidenceLevel.LOW;
    }

    /**
     * Prints user-friendly security event output.
     * 
     * Uses OutputGateway (single output point) and ExplanationEngine.
     * 
     * @param event      Security event
     * @param riskScore  Calculated risk score
     * @param confidence Confidence level
     */
    private static void printUserFriendlyOutput(SecurityEvent event, int riskScore, ConfidenceLevel confidence) {

        // Get current system state
        SystemState systemState = SystemStateManager.getCurrentState();

        // Forward to OutputGateway (SINGLE OUTPUT POINT)
        com.threatscope.core.output.OutputGateway.printSecurityObservation(
                event,
                event.getClassification(),
                riskScore,
                confidence,
                systemState);
    }

    /**
     * Performs risk decay for inactive IPs.
     * 
     * Optional maintenance - can be called periodically
     * to gradually reduce risk for IPs with no recent activity.
     */
    public static void performRiskDecay() {
        long now = System.currentTimeMillis();

        firstEventTime.forEach((ip, firstTime) -> {
            long timeSinceFirst = now - firstTime;

            // If no activity for decay interval, reduce risk
            if (timeSinceFirst >= RISK_DECAY_INTERVAL_MS) {
                int risk = currentRisk.getOrDefault(ip, 0);
                int newRisk = Math.max(0, risk - RISK_DECAY_AMOUNT);

                if (newRisk > 0) {
                    currentRisk.put(ip, newRisk);
                } else {
                    // Risk decayed to zero, clean up
                    currentRisk.remove(ip);
                    eventCount.remove(ip);
                    firstEventTime.remove(ip);
                }
            }
        });
    }
}
