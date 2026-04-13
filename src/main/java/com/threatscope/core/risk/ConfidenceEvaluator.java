package com.threatscope.core.risk;

/**
 * CONFIDENCE EVALUATOR
 * 
 * Responsibility:
 * - Evaluate confidence level based on risk score and evidence count
 * - Return human-readable confidence level (LOW/MEDIUM/HIGH)
 * - Use clear thresholds (explainable for viva)
 * 
 * Confidence Logic:
 * - HIGH: Strong risk + strong evidence
 * - MEDIUM: Moderate risk or moderate evidence
 * - LOW: Weak risk or weak evidence
 */
public class ConfidenceEvaluator {

    /**
     * Evaluates confidence level for a detection.
     * 
     * Confidence depends on:
     * 1. Risk score (how severe is the threat?)
     * 2. Evidence count (how much proof do we have?)
     * 
     * Thresholds:
     * - HIGH: risk >= 80 AND evidence >= 10
     * - MEDIUM: risk >= 50 OR evidence >= 5
     * - LOW: everything else
     * 
     * @param riskScore     Risk score (0-100)
     * @param evidenceCount Number of evidence items
     * @return Confidence level (LOW/MEDIUM/HIGH)
     */
    public static String evaluate(int riskScore, int evidenceCount) {

        // HIGH confidence: strong risk + strong evidence
        if (riskScore >= 80 && evidenceCount >= 10) {
            return "HIGH";
        }

        // MEDIUM confidence: moderate risk or moderate evidence
        if (riskScore >= 50 || evidenceCount >= 5) {
            return "MEDIUM";
        }

        // LOW confidence: weak indicators
        return "LOW";
    }
}
