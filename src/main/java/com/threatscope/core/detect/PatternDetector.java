package com.threatscope.core.detect;

import com.threatscope.core.model.SecurityEvent;
import com.threatscope.core.model.TrafficClass;
import com.threatscope.core.model.ThreatType;
import com.threatscope.core.classify.TrafficClassifier;
import com.threatscope.core.risk.RiskEngine;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Pattern Detector
 * 
 * Detects suspicious network patterns and known malicious signatures:
 * - Backdoor port access (e.g., 31337, 12345)
 * - Suspicious TCP flag combinations (Null Scan, XMAS Scan)
 * - Invalid packet anomalies
 * 
 * Thread-safe implementation using ConcurrentHashMap and non-blocking checks.
 * 
 * @author ThreatScope Team
 * @version 1.0
 */
public class PatternDetector {

    // Known backdoor ports
    private static final Set<Integer> BACKDOOR_PORTS = new HashSet<>(Arrays.asList(
            31337, // Back Orifice
            12345, // NetBus
            6667, // IRC Botnet (often used for C&C)
            2323, // Mirai Botnet Telnet
            4444, // Metasploit default
            5555, // Android ADB (often targeted)
            27374 // Sub7
    ));

    // Alert suppression (prevent spam)
    private static final Map<String, Long> lastAlertTime = new ConcurrentHashMap<>();
    private static final long ALERT_COOLDOWN_MS = 60000; // 60 seconds (1 minute) per IP+Type

    /**
     * Checks a packet for suspicious patterns
     * 
     * @param packet   The raw packet
     * @param sourceIp Source IP address
     * @param destPort Destination port (if applicable, or -1)
     */
    public static void checkPattern(Packet packet, String sourceIp, int destPort) {
        if (packet == null)
            return;

        long now = System.currentTimeMillis();

        // 1. Check for Backdoor Ports
        if (destPort > 0 && BACKDOOR_PORTS.contains(destPort)) {
            triggerAlert(sourceIp, ThreatType.BACKDOOR_ATTEMPT, destPort,
                    "Connection attempt to known backdoor port: " + destPort, now);
            return;
        }

        // 2. Check TCP Flags for Suspicious Scans
        if (packet.contains(TcpPacket.class)) {
            TcpPacket tcpPacket = packet.get(TcpPacket.class);
            TcpPacket.TcpHeader header = tcpPacket.getHeader();

            boolean syn = header.getSyn();
            boolean ack = header.getAck();
            boolean fin = header.getFin();
            boolean rst = header.getRst();
            boolean psh = header.getPsh();
            boolean urg = header.getUrg();

            // Null Scan: No flags set
            if (!syn && !ack && !fin && !rst && !psh && !urg) {
                triggerAlert(sourceIp, ThreatType.SUSPICIOUS_PATTERN, 0, "Null Scan detected (No TCP flags set)", now);
                return;
            }

            // XMAS Scan: FIN, URG, PSH set
            if (fin && urg && psh) {
                triggerAlert(sourceIp, ThreatType.SUSPICIOUS_PATTERN, 0, "XMAS Scan detected (FIN+URG+PSH flags set)",
                        now);
                return;
            }

            // FIN Scan: Only FIN set (without ACK)
            // Legitimate FIN packets usually have ACK set
            if (fin && !ack && !syn && !rst) {
                triggerAlert(sourceIp, ThreatType.SUSPICIOUS_PATTERN, 0, "Stealth FIN Scan detected (FIN without ACK)",
                        now);
                return;
            }
        }
    }

    /**
     * Triggers a pattern alert
     */
    private static void triggerAlert(String sourceIp, ThreatType type, int port, String details, long now) {
        // Check cooldown
        String cooldownKey = sourceIp + "_" + type + "_" + details;
        Long lastTime = lastAlertTime.get(cooldownKey);

        if (lastTime != null && (now - lastTime) < ALERT_COOLDOWN_MS) {
            return; // Suppress alert
        }

        // Update cooldown
        lastAlertTime.put(cooldownKey, now);

        // Classify
        // For pattern detection, we typically treat it as PROBING or SUSPICIOUS
        TrafficClass classification = TrafficClass.SUSPICIOUS;
        // Or re-use classifier if suitable, but hardcoding for specific patterns is
        // often safer
        // Let's use the classifier for consistency if possible, or defaulting to
        // SUSPICIOUS is fine.
        // Actually, let's use the classifier to get consistent risk scoring logic
        classification = TrafficClassifier.classify(sourceIp, type, 1, 0);

        // Create Event
        SecurityEvent event;
        if (type == ThreatType.BACKDOOR_ATTEMPT) {
            event = SecurityEvent.backdoorAttempt(sourceIp, port, classification);
        } else {
            event = SecurityEvent.suspiciousPattern(sourceIp, details, classification);
        }

        // Send to Risk Engine
        RiskEngine.processEvent(event);

        System.out.println("🚨 PATTERN DETECTED: " + sourceIp + " - " + details);

        // Cleanup old keys periodically
        if (Math.random() < 0.01) {
            cleanupCooldowns(now);
        }
    }

    private static void cleanupCooldowns(long now) {
        lastAlertTime.entrySet().removeIf(entry -> (now - entry.getValue()) > ALERT_COOLDOWN_MS * 2);
    }
}
