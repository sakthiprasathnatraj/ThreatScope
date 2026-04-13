package com.threatscope.core.classify;

/**
 * TRAFFIC FILTER
 * 
 * Responsibility:
 * - Filter out local/private traffic
 * - Identify traffic that should NOT be analyzed
 * - Prevent false positives from internal network activity
 * 
 * This is the FIRST line of defense against false positives.
 */
public class TrafficFilter {

    /**
     * Checks if source IP should be ignored (local/private traffic).
     * 
     * Filters:
     * - Loopback (127.0.0.0/8)
     * - Private Class A (10.0.0.0/8)
     * - Private Class B (172.16.0.0/12)
     * - Private Class C (192.168.0.0/16)
     * - Link-local (169.254.0.0/16)
     * - Multicast (224.0.0.0/4)
     * - Broadcast (255.255.255.255)
     * 
     * @param ip IP address to check
     * @return true if should be ignored
     */
    public static boolean isLocalTraffic(String ip) {
        if (ip == null || ip.isEmpty()) {
            return true;
        }

        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return true; // Invalid IP
        }

        try {
            int first = Integer.parseInt(parts[0]);
            int second = Integer.parseInt(parts[1]);

            // Loopback: 127.0.0.0/8
            if (first == 127) {
                return true;
            }

            // Private Class A: 10.0.0.0/8
            if (first == 10) {
                return true;
            }

            // Private Class B: 172.16.0.0/12 (172.16.0.0 - 172.31.255.255)
            if (first == 172 && second >= 16 && second <= 31) {
                return true;
            }

            // Private Class C: 192.168.0.0/16
            if (first == 192 && second == 168) {
                return true;
            }

            // Link-local: 169.254.0.0/16
            if (first == 169 && second == 254) {
                return true;
            }

            // Multicast: 224.0.0.0/4 (224-239)
            if (first >= 224 && first <= 239) {
                return true;
            }

            // Broadcast: 255.255.255.255
            if (first == 255) {
                return true;
            }

            return false; // External traffic

        } catch (NumberFormatException e) {
            return true; // Invalid IP, ignore
        }
    }

    /**
     * Checks if destination IP is this machine (ignore outbound-only traffic).
     * 
     * For host-based monitoring, we primarily care about INBOUND threats.
     * Outbound connections initiated by this machine are generally benign.
     * 
     * @param destIp Destination IP
     * @return true if destination is external (potential threat source)
     */
    public static boolean isInboundThreat(String destIp) {
        // If destination is local/private, this is outbound from us
        // We focus on inbound threats (external source → us)
        return !isLocalTraffic(destIp);
    }
}
