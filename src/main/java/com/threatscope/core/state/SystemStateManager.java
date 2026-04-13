package com.threatscope.core.state;

import com.threatscope.core.model.ConfidenceLevel;
import com.threatscope.core.model.SystemState;

/**
 * SYSTEM STATE MANAGER
 * 
 * Responsibility:
 * - Manage system security state transitions
 * - Prevent rapid state flapping
 * - Require sustained evidence for state changes
 * - Print state changes only when they occur
 * 
 * States: SAFE → OBSERVE → WARNING → CRITICAL
 * 
 * Key Principles:
 * - Upward transitions: respond to threats
 * - Downward transitions: require cooldown (prevent flapping)
 * - CRITICAL: requires HIGH confidence
 * - State changes printed once only
 */
public class SystemStateManager {

    // Current system state
    private static SystemState currentState = SystemState.SAFE;

    // Last state change timestamp
    private static long lastStateChange = 0;

    // Minimum time between state changes (prevents flapping)
    private static final long STATE_CHANGE_COOLDOWN_MS = 30_000; // 30 seconds

    /**
     * Evaluates and updates system state based on risk and confidence.
     * 
     * State Transition Rules:
     * - SAFE: risk < 30
     * - OBSERVE: risk >= 30
     * - WARNING: risk >= 50
     * - CRITICAL: risk >= 70 AND confidence == HIGH
     * 
     * Anti-Flapping Rules:
     * - Upward transitions: allowed anytime (respond to threats)
     * - Downward transitions: require cooldown (prevent oscillation)
     * - State change printed only on actual transition
     * 
     * @param riskScore  Current risk score (0-100)
     * @param confidence Current confidence level
     */
    public static void evaluateState(int riskScore, ConfidenceLevel confidence) {

        long now = System.currentTimeMillis();

        // Determine target state based on risk and confidence
        SystemState targetState = determineTargetState(riskScore, confidence);

        // Check if state change is needed
        if (targetState == currentState) {
            return; // No change needed
        }

        // Check if this is an upward or downward transition
        boolean isUpward = targetState.ordinal() > currentState.ordinal();
        boolean isDownward = targetState.ordinal() < currentState.ordinal();

        // Upward transitions: allowed anytime (respond to threats quickly)
        if (isUpward) {
            performStateChange(targetState, now);
            return;
        }

        // Downward transitions: require cooldown (prevent flapping)
        if (isDownward) {
            long timeSinceLastChange = now - lastStateChange;

            if (timeSinceLastChange >= STATE_CHANGE_COOLDOWN_MS) {
                performStateChange(targetState, now);
            }
            // else: in cooldown, skip downward transition
        }
    }

    /**
     * Determines target state based on risk score and confidence.
     * 
     * Thresholds are set to ensure gradual progression:
     * - SAFE: Low risk
     * - OBSERVE: Suspicious activity detected
     * - WARNING: Repeated suspicious behavior
     * - CRITICAL: Strong evidence of threat
     * 
     * @param riskScore  Risk score (0-100)
     * @param confidence Confidence level
     * @return Target state
     */
    private static SystemState determineTargetState(int riskScore, ConfidenceLevel confidence) {

        // CRITICAL: high risk + high confidence required
        // This prevents false CRITICAL alarms
        if (riskScore >= 70 && confidence == ConfidenceLevel.HIGH) {
            return SystemState.CRITICAL;
        }

        // WARNING: moderate-high risk
        if (riskScore >= 50) {
            return SystemState.WARNING;
        }

        // OBSERVE: low-moderate risk
        if (riskScore >= 30) {
            return SystemState.OBSERVE;
        }

        // SAFE: low risk
        return SystemState.SAFE;
    }

    /**
     * Performs state change and prints notification.
     * 
     * This is the ONLY place where state changes are printed,
     * ensuring no duplicate state change messages.
     * 
     * @param newState  New state to transition to
     * @param timestamp Current timestamp
     */
    private static void performStateChange(SystemState newState, long timestamp) {

        SystemState previousState = currentState;
        currentState = newState;
        lastStateChange = timestamp;

        // Print state change notification via OutputGateway (SINGLE OUTPUT POINT)
        com.threatscope.core.output.OutputGateway.printStateChange(previousState, newState);
    }

    /**
     * Gets current system state (for monitoring/testing).
     * 
     * @return Current state
     */
    public static SystemState getCurrentState() {
        return currentState;
    }

    /**
     * Resets system to SAFE state (for testing or manual intervention).
     */
    public static void reset() {
        currentState = SystemState.SAFE;
        lastStateChange = 0;
    }
}
