package com.threatscope.core.model;

/**
 * TRAFFIC CLASSIFICATION
 * 
 * Every observed IP must be classified into one of these categories.
 * This is CRITICAL for preventing false positives and user panic.
 * 
 * Design Philosophy:
 * - TRUSTED traffic should NEVER raise CRITICAL alerts
 * - BENIGN_NOISE should be explained, not alarmed
 * - Only SUSPICIOUS and CONFIRMED_THREAT can escalate to high risk
 * 
 * Classification Order (from safest to most dangerous):
 * TRUSTED → BENIGN_NOISE → SUSPICIOUS → CONFIRMED_THREAT
 */
public enum TrafficClass {

    /**
     * TRUSTED - Safe, expected traffic
     * 
     * Examples:
     * - Local network devices (routers, printers)
     * - ISP infrastructure
     * - Known CDNs (Cloudflare, Akamai)
     * - Major cloud providers (AWS, Azure, Google Cloud)
     * - DNS servers (8.8.8.8, 1.1.1.1)
     * - Microsoft services
     * 
     * Risk Cap: Maximum 20 (never WARNING or CRITICAL)
     * User Message: "This is normal network activity"
     */
    TRUSTED("Trusted Source", 20),

    /**
     * BENIGN_NOISE - Common internet background noise
     * 
     * Examples:
     * - Automated internet scanners (Shodan, Censys)
     * - Security research bots
     * - Misconfigured services
     * - Random port probes (happens to all internet-connected devices)
     * 
     * Risk Cap: Maximum 40 (never WARNING or CRITICAL)
     * User Message: "This is common internet scanning, not an attack"
     */
    BENIGN_NOISE("Common Internet Noise", 40),

    /**
     * SUSPICIOUS - Unusual activity worth monitoring
     * 
     * Examples:
     * - Unknown IPs with moderate scanning
     * - Repeated connection attempts
     * - Unusual port combinations
     * - Behavior that could be malicious OR benign
     * 
     * Risk Cap: Maximum 70 (can reach WARNING, rarely CRITICAL)
     * User Message: "Monitoring unusual activity"
     */
    SUSPICIOUS("Suspicious Activity", 70),

    /**
     * CONFIRMED_THREAT - High confidence malicious activity
     * 
     * Examples:
     * - Known malicious IPs
     * - Aggressive brute-force attempts
     * - Exploit attempts
     * - Sustained, escalating attacks
     * 
     * Risk Cap: Maximum 100 (can reach CRITICAL with HIGH confidence)
     * User Message: "Potential threat detected"
     */
    CONFIRMED_THREAT("Confirmed Threat", 100);

    // Human-readable name
    private final String displayName;

    // Maximum risk score allowed for this classification
    private final int maxRisk;

    TrafficClass(String displayName, int maxRisk) {
        this.displayName = displayName;
        this.maxRisk = maxRisk;
    }

    /**
     * Gets human-readable display name.
     * 
     * @return Display name
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Gets maximum risk score allowed for this classification.
     * 
     * This prevents TRUSTED/BENIGN traffic from triggering high-risk states.
     * 
     * @return Maximum risk score (0-100)
     */
    public int getMaxRisk() {
        return maxRisk;
    }

    /**
     * Checks if this classification can trigger CRITICAL state.
     * 
     * Only CONFIRMED_THREAT can reach CRITICAL.
     * 
     * @return true if can reach CRITICAL
     */
    public boolean canReachCritical() {
        return this == CONFIRMED_THREAT;
    }

    /**
     * Checks if this classification should show reassurance messages.
     * 
     * TRUSTED and BENIGN_NOISE should always reassure users.
     * 
     * @return true if should reassure
     */
    public boolean shouldReassure() {
        return this == TRUSTED || this == BENIGN_NOISE;
    }
}
