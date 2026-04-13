package com.threatscope.core.detect;
import com.threatscope.core.model.SecurityEvent;
import com.threatscope.core.model.TrafficClass;
import com.threatscope.core.model.ThreatType;
import com.threatscope.core.classify.TrafficClassifier;
import com.threatscope.core.risk.RiskEngine;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * DDoS Attack Detector
 * 
 * Detects Distributed Denial of Service attacks by monitoring:
 * - Packet rate per source IP (packets/second)
 * - SYN flood attacks (high rate of SYN packets without ACK)
 * - UDP flood attacks (high volume of UDP packets)
 * - ICMP flood attacks (excessive ping requests)
 * 
 * Detection Strategy:
 * - Track packet rate in 5-second windows
 * - Threshold: 100+ packets/sec = potential DDoS
 * - Alert on sustained high volume
 * 
 * Thread-safe implementation using ConcurrentHashMap
 * 
 * @author ThreatScope Team
 * @version 2.0
 */
public class DDoSDetector {

    // Detection thresholds (adjusted for desktop environment)
    private static int packetRateThreshold = 1000; // packets per second (was 100)
    private static int synFloodThreshold = 200; // SYN packets per second (was 50)
    private static int udpFloodThreshold = 500; // UDP packets per second (was 80)
    private static int icmpFloodThreshold = 100; // ICMP packets per second (was 30)
    private static final long TIME_WINDOW_MS = 5000; // 5 seconds

    /**
     * Sets the generic DDoS packet rate threshold
     * 
     * @param threshold packets per second
     */
    public static void setPacketRateThreshold(int threshold) {
        packetRateThreshold = threshold;
        System.out.println("⚙ DDoS Threshold updated to: " + threshold + " pps");
    }

    /**
     * Sets the SYN flood threshold
     * 
     * @param threshold packets per second
     */
    public static void setSynFloodThreshold(int threshold) {
        synFloodThreshold = threshold;
    }

    /**
     * Sets the UDP flood threshold
     * 
     * @param threshold packets per second
     */
    public static void setUdpFloodThreshold(int threshold) {
        udpFloodThreshold = threshold;
    }

    /**
     * Sets the ICMP flood threshold
     * 
     * @param threshold packets per second
     */
    public static void setIcmpFloodThreshold(int threshold) {
        icmpFloodThreshold = threshold;
    }

    // Packet counters per source IP
    private static final Map<String, PacketRateTracker> packetRates = new ConcurrentHashMap<>();
    private static final Map<String, PacketRateTracker> synRates = new ConcurrentHashMap<>();
    private static final Map<String, PacketRateTracker> udpRates = new ConcurrentHashMap<>();
    private static final Map<String, PacketRateTracker> icmpRates = new ConcurrentHashMap<>();

    // Alert suppression (prevent spam)
    private static final Map<String, Long> lastAlertTime = new ConcurrentHashMap<>();
    private static final long ALERT_COOLDOWN_MS = 30000; // 30 seconds

    /**
     * Records a packet for DDoS analysis
     * 
     * @param sourceIp Source IP address
     * @param protocol Protocol (TCP, UDP, ICMP)
     * @param isSyn    True if TCP SYN packet
     */
    public static void recordPacket(String sourceIp, String protocol, boolean isSyn) {
        long now = System.currentTimeMillis();

        // Track overall packet rate
        PacketRateTracker tracker = packetRates.computeIfAbsent(sourceIp, k -> new PacketRateTracker());
        tracker.recordPacket(now);

        // Track protocol-specific rates
        if ("TCP".equalsIgnoreCase(protocol) && isSyn) {
            PacketRateTracker synTracker = synRates.computeIfAbsent(sourceIp, k -> new PacketRateTracker());
            synTracker.recordPacket(now);
        } else if ("UDP".equalsIgnoreCase(protocol)) {
            PacketRateTracker udpTracker = udpRates.computeIfAbsent(sourceIp, k -> new PacketRateTracker());
            udpTracker.recordPacket(now);
        } else if ("ICMP".equalsIgnoreCase(protocol)) {
            PacketRateTracker icmpTracker = icmpRates.computeIfAbsent(sourceIp, k -> new PacketRateTracker());
            icmpTracker.recordPacket(now);
        }

        // Check for DDoS attacks
        checkForDDoS(sourceIp, now);

        // Cleanup old trackers periodically
        if (Math.random() < 0.01) { // 1% chance per packet
            cleanupOldTrackers(now);
        }
    }

    /**
     * Checks if source IP is performing a DDoS attack
     */
    private static void checkForDDoS(String sourceIp, long now) {
        // Check if we're in cooldown period
        Long lastAlert = lastAlertTime.get(sourceIp);
        if (lastAlert != null && (now - lastAlert) < ALERT_COOLDOWN_MS) {
            return; // Still in cooldown, don't alert
        }

        // Get packet rates
        PacketRateTracker tracker = packetRates.get(sourceIp);
        if (tracker == null)
            return;

        long duration = tracker.getDuration(now);

        // CRITICAL FIX: Ignore micro-bursts (less than 1 second)
        // This prevents false positives from legitimate traffic spikes (e.g. web
        // loading)
        if (duration < 1000) {
            return;
        }

        int packetsPerSec = tracker.getPacketsPerSecond(now);

        // Check SYN flood first (most specific)
        PacketRateTracker synTracker = synRates.get(sourceIp);
        if (synTracker != null) {
            int synPerSec = synTracker.getPacketsPerSecond(now);
            if (synPerSec >= synFloodThreshold) {
                triggerDDoSAlert(sourceIp, ThreatType.SYN_FLOOD, synPerSec, duration, now);
                return;
            }
        }

        // Check UDP flood
        PacketRateTracker udpTracker = udpRates.get(sourceIp);
        if (udpTracker != null) {
            int udpPerSec = udpTracker.getPacketsPerSecond(now);
            if (udpPerSec >= udpFloodThreshold) {
                triggerDDoSAlert(sourceIp, ThreatType.UDP_FLOOD, udpPerSec, duration, now);
                return;
            }
        }

        // Check ICMP flood
        PacketRateTracker icmpTracker = icmpRates.get(sourceIp);
        if (icmpTracker != null) {
            int icmpPerSec = icmpTracker.getPacketsPerSecond(now);
            if (icmpPerSec >= icmpFloodThreshold) {
                triggerDDoSAlert(sourceIp, ThreatType.ICMP_FLOOD, icmpPerSec, duration, now);
                return;
            }
        }

        // Check overall packet rate (generic DDoS)
        if (packetsPerSec >= packetRateThreshold) {
            triggerDDoSAlert(sourceIp, ThreatType.DDOS_ATTACK, packetsPerSec, duration, now);
        }
    }

    /**
     * Triggers a DDoS attack alert
     */
    private static void triggerDDoSAlert(String sourceIp, ThreatType threatType, int packetsPerSec, long duration,
            long now) {
        // Classify traffic
        TrafficClass classification = TrafficClassifier.classify(sourceIp, threatType, packetsPerSec, duration);

        // Create security event using factory methods
        SecurityEvent event;
        switch (threatType) {
            case SYN_FLOOD:
                event = SecurityEvent.synFlood(sourceIp, packetsPerSec, classification, duration);
                break;
            case UDP_FLOOD:
                event = SecurityEvent.udpFlood(sourceIp, packetsPerSec, classification, duration);
                break;
            case ICMP_FLOOD:
                event = SecurityEvent.icmpFlood(sourceIp, packetsPerSec, classification, duration);
                break;
            default:
                event = SecurityEvent.ddosAttack(sourceIp, packetsPerSec, classification, duration);
                break;
        }

        // Send to risk engine (follows existing architecture)
        RiskEngine.processEvent(event);

        // Update last alert time
        lastAlertTime.put(sourceIp, now);

        // Debug output
        System.out.println("🚨 DDoS DETECTED: " + sourceIp + " - " + threatType + " (" + packetsPerSec + " pkt/s)");
    }

    /**
     * Cleans up old packet rate trackers
     */
    private static void cleanupOldTrackers(long now) {
        packetRates.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        synRates.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        udpRates.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        icmpRates.entrySet().removeIf(entry -> entry.getValue().isExpired(now));
        lastAlertTime.entrySet().removeIf(entry -> (now - entry.getValue()) > ALERT_COOLDOWN_MS * 2);
    }

    /**
     * Gets current packet rate for an IP
     * 
     * @param sourceIp Source IP address
     * @return Packets per second, or 0 if not tracked
     */
    public static int getPacketRate(String sourceIp) {
        PacketRateTracker tracker = packetRates.get(sourceIp);
        return (tracker != null) ? tracker.getPacketsPerSecond(System.currentTimeMillis()) : 0;
    }

    /**
     * Resets all tracking data (for testing)
     */
    public static void reset() {
        packetRates.clear();
        synRates.clear();
        udpRates.clear();
        icmpRates.clear();
        lastAlertTime.clear();
    }

    // ========== Inner Class: PacketRateTracker ==========

    /**
     * Tracks packet rate in a sliding time window
     */
    private static class PacketRateTracker {
        private final AtomicInteger packetCount = new AtomicInteger(0);
        private volatile long windowStart;

        public PacketRateTracker() {
            this.windowStart = System.currentTimeMillis();
        }

        /**
         * Records a packet
         */
        public void recordPacket(long timestamp) {
            // Reset window if expired
            if (timestamp - windowStart > TIME_WINDOW_MS) {
                windowStart = timestamp;
                packetCount.set(1);
            } else {
                packetCount.incrementAndGet();
            }
        }

        /**
         * Gets packets per second in current window
         */
        public int getPacketsPerSecond(long now) {
            long windowDuration = now - windowStart;
            if (windowDuration <= 0)
                return 0;

            // Calculate rate (packets per second)
            int count = packetCount.get();
            double seconds = windowDuration / 1000.0;
            return (int) (count / seconds);
        }

        /**
         * Gets duration of current window
         */
        public long getDuration(long now) {
            return now - windowStart;
        }

        /**
         * Checks if tracker is expired (no activity for 2x window)
         */
        public boolean isExpired(long now) {
            return (now - windowStart) > (TIME_WINDOW_MS * 2);
        }
    }
}
