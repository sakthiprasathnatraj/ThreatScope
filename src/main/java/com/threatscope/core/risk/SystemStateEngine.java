package com.threatscope.core.risk;

/**
 * SYSTEM STATE ENGINE
 * 
 * Responsibility:
 * - Manage system security state transitions
 * - Prevent rapid state flapping
 * - Require sustained risk for state changes
 * - Only escalate to CRITICAL with HIGH confidence
 * 
 * States: SAFE → OBSERVE → WARNING → CRITICAL
 * 
 * Design Philosophy:
 * - Upward transitions are easy (respond to threats)
 * - Downward transitions require cooldown (avoid flapping)
 * - CRITICAL state requires strong evidence (avoid panic)
 */
public class SystemStateEngine {

    /**
     * System security states (in order of severity)
     */
    public enum State {
        SAFE, // No threats detected
        OBSERVE, // Low-level activity detected
        WARNING, // Moderate threat activity
        CRITICAL // High-confidence threat detected
    }

    // Current system state
    private static State currentState = State.SAFE;

    // Last state change timestamp
    private static long lastStateChange = 0;

    // Minimum time between state changes (prevents flapping)
    private static final long STATE_CHANGE_COOLDOWN_MS = 30_000; // 30 seconds

    /**
     * Evaluates and updates system state based on risk and confidence.
     * 
     * State Transition Rules:
     * - SAFE: risk < 40
     * - OBSERVE: risk >= 40
     * - WARNING: risk >= 60
     * - CRITICAL: risk >= 80 AND confidence == HIGH
     * 
     * Additional Rules:
     * - Upward transitions allowed anytime (respond to threats)
     * - Downward transitions require cooldown (prevent flapping)
     * - State change printed only on actual transition
     * 
     * @param riskScore  Current risk score
     * @param confidence Current confidence level
     */
    public static void evaluate(int riskScore, String confidence) {

        long now = System.currentTimeMillis();

        // Determine target state based on risk and confidence
        State targetState = determineTargetState(riskScore, confidence);

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
     * @param riskScore  Risk score (0-100)
     * @param confidence Confidence level (LOW/MEDIUM/HIGH)
     * @return Target state
     */
    private static State determineTargetState(int riskScore, String confidence) {

        // CRITICAL: high risk + high confidence required
        if (riskScore >= 80 && confidence.equals("HIGH")) {
            return State.CRITICAL;
        }

        // WARNING: moderate-high risk
        if (riskScore >= 60) {
            return State.WARNING;
        }

        // OBSERVE: low-moderate risk
        if (riskScore >= 40) {
            return State.OBSERVE;
        }

        // SAFE: low risk
        return State.SAFE;
    }

    /**
     * Performs state change and prints notification.
     * 
     * @param newState  New state to transition to
     * @param timestamp Current timestamp
     */
    private static void performStateChange(State newState, long timestamp) {

        State previousState = currentState;
        currentState = newState;
        lastStateChange = timestamp;

        // Print state change notification
        System.out.println();
        System.out.println("[STATE CHANGE]");
        System.out.println("SYSTEM STATE: " + previousState + " → " + newState);
        System.out.println();
    }

    /**
     * Gets current system state (for testing/monitoring).
     * 
     * @return Current state
     */
    public static State getCurrentState() {
        return currentState;
    }

    /**
     * Resets system to SAFE state (for testing or manual intervention).
     */
    public static void reset() {
        currentState = State.SAFE;
        lastStateChange = 0;
    }
}
