package com.threatscope.core.model;

/**
 * System security states.
 * 
 * States progress gradually: SAFE → OBSERVE → WARNING → CRITICAL
 * 
 * Downgrade requires sustained calm period to prevent flapping.
 */
public enum SystemState {
    SAFE, // No threats detected
    OBSERVE, // Suspicious activity, monitoring
    WARNING, // Repeated suspicious behavior
    CRITICAL // Strong evidence of threat
}
