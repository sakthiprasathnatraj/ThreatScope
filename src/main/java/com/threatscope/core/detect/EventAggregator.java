package com.threatscope.core.detect;

import com.threatscope.core.model.SecurityEvent;
import com.threatscope.core.model.TrafficClass;
import com.threatscope.core.model.ThreatType;
import com.threatscope.core.classify.TrafficClassifier;
import com.threatscope.core.risk.RiskEngine;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * EVENT AGGREGATION LAYER
 * 
 * CRITICAL: This prevents per-packet alerts.
 * 
 * Responsibility:
 * - Maintain sliding time window per source IP
 * - Track unique destination ports accessed
 * - Only trigger detection when threshold crossed
 * - Automatic cleanup of expired windows
 * 
 * This is the KEY to preventing false positives and alert spam.
 */
public class EventAggregator {

    // ===== SLIDING WINDOW DATA STRUCTURES =====

    // Track unique destination ports per source IP
    private static final Map<String, Set<Integer>> portMap = new ConcurrentHashMap<>();

    // Track first packet time per source IP (window start)
    private static final Map<String, Long> windowStartTime = new ConcurrentHashMap<>();

    // Track last alert time per source IP (for deduplication)
    private static final Map<String, Long> lastAlertTime = new ConcurrentHashMap<>();

    // ===== CONFIGURATION =====

    // Time window: pattern must occur within this duration
    private static final long TIME_WINDOW_MS = 10_000; // 10 seconds

    // Port scan threshold: minimum unique ports to trigger
    private static final int PORT_SCAN_THRESHOLD = 10;

    // Alert cooldown: minimum time between alerts from same IP
    private static final long ALERT_COOLDOWN_MS = 60_000; // 60 seconds

    /**
     * Records a packet observation.
     * 
     * This is called for EVERY packet from external sources.
     * We aggregate data and only trigger detection when patterns emerge.
     * 
     * @param srcIp    Source IP address
     * @param dstPort  Destination port accessed
     * @param protocol Protocol (TCP/UDP)
     */
    public static void recordPacket(String srcIp, int dstPort, String protocol) {

        long now = System.currentTimeMillis();

        // ===== TIME WINDOW MANAGEMENT =====

        // Check if we have existing window for this IP
        if (windowStartTime.containsKey(srcIp)) {
            long windowStart = windowStartTime.get(srcIp);

            // If time window expired, reset aggregation
            if (now - windowStart > TIME_WINDOW_MS) {
                resetWindow(srcIp);
                windowStartTime.put(srcIp, now);
            }
        } else {
            // First packet from this IP - start new window
            windowStartTime.put(srcIp, now);
        }

        // ===== PORT AGGREGATION =====

        // Add destination port to this IP's aggregated data
        portMap.computeIfAbsent(srcIp, k -> new HashSet<>()).add(dstPort);

        // Count unique ports accessed
        int uniquePortCount = portMap.get(srcIp).size();

        // ===== PATTERN DETECTION =====

        // PORT SCAN: >= threshold unique ports within time window
        if (uniquePortCount >= PORT_SCAN_THRESHOLD) {

            // Check alert cooldown - prevent spam
            if (isInCooldown(srcIp, now)) {
                return; // Already alerted recently, skip
            }

            // ===== TRAFFIC CLASSIFICATION =====

            // Calculate duration of activity
            long windowStart = windowStartTime.get(srcIp);
            long duration = now - windowStart;

            // Classify traffic based on IP, behavior, and timing
            TrafficClass classification = TrafficClassifier.classify(
                    srcIp,
                    ThreatType.PORT_SCAN,
                    uniquePortCount,
                    duration);

            // Pattern detected - create security event with classification
            SecurityEvent event = SecurityEvent.portScan(srcIp, uniquePortCount, classification, duration);

            // Forward to risk engine
            RiskEngine.processEvent(event);

            // Record alert time for cooldown
            lastAlertTime.put(srcIp, now);

            // Reset window after alert
            resetWindow(srcIp);
        }
    }

    /**
     * Checks if source IP is in alert cooldown period.
     * 
     * Prevents alert spam - same IP can only trigger alert once per cooldown.
     * 
     * @param srcIp Source IP to check
     * @param now   Current timestamp
     * @return true if in cooldown (skip alert)
     */
    private static boolean isInCooldown(String srcIp, long now) {
        if (lastAlertTime.containsKey(srcIp)) {
            long lastAlert = lastAlertTime.get(srcIp);
            return (now - lastAlert) < ALERT_COOLDOWN_MS;
        }
        return false;
    }

    /**
     * Resets sliding window for a source IP.
     * 
     * Called when:
     * - Time window expires
     * - Alert is triggered
     * 
     * @param srcIp Source IP to reset
     */
    private static void resetWindow(String srcIp) {
        portMap.remove(srcIp);
        windowStartTime.remove(srcIp);
    }

    /**
     * Cleanup method for expired entries.
     * 
     * Optional maintenance - can be called periodically
     * to prevent memory growth from inactive IPs.
     */
    public static void performMaintenance() {
        long now = System.currentTimeMillis();

        // Remove expired time windows
        windowStartTime.entrySet().removeIf(entry -> now - entry.getValue() > TIME_WINDOW_MS);

        // Remove expired cooldowns
        lastAlertTime.entrySet().removeIf(entry -> now - entry.getValue() > ALERT_COOLDOWN_MS);

        // Remove orphaned port maps
        portMap.keySet().removeIf(ip -> !windowStartTime.containsKey(ip));
    }
}
