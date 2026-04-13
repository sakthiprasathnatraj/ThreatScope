package com.threatscope.ui.service;

import com.threatscope.core.capture.PacketSniffer;
import com.threatscope.core.capture.InterfaceSelector;
import com.threatscope.core.model.SystemState;
import com.threatscope.ui.model.UiSecurityEvent;
import com.threatscope.ui.model.RawPacketData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong; // Phase 4 Stats

/**
 * Backend Bridge Service
 * 
 * Connects JavaFX UI with ThreatScope backend
 * 
 * IMPORTANT: This is the ONLY class that interacts with backend.
 * UI components should NEVER directly access backend classes.
 * 
 * Responsibilities:
 * - Start/stop packet capture
 * - Retrieve security events
 * - Get current system state
 * - Provide network interface information
 * - Thread-safe UI updates
 * 
 * Architecture:
 * UI ← BackendBridge → Backend (OutputGateway, SystemStateManager, etc.)
 */
public class BackendBridge {

    // Observable list for security events (thread-safe for JavaFX)
    private final ObservableList<UiSecurityEvent> securityEvents;

    // Observable list for raw packet data (Expert Mode)
    private final ObservableList<RawPacketData> rawPackets;

    // Current system state
    private SystemState currentState;

    // Monitoring status
    private boolean isMonitoring;

    // Selected network interface
    private int selectedInterfaceIndex;

    // For cycling through demo events
    private int mockEventIndex = 0;

    // Phase 4 Statistics
    private final AtomicInteger packetCounter = new AtomicInteger(0);
    private final AtomicLong totalPackets = new AtomicLong(0);
    private long lastRateCheckTime = System.currentTimeMillis();
    private double lastCalculatedRate = 0.0;

    // Singleton instance
    private static BackendBridge instance;

    /**
     * Private constructor (singleton pattern)
     */
    private BackendBridge() {
        this.securityEvents = FXCollections.observableArrayList();
        this.rawPackets = FXCollections.observableArrayList();
        this.currentState = SystemState.SAFE;
        this.isMonitoring = false;
        this.selectedInterfaceIndex = -1;
    }

    /**
     * Gets singleton instance
     */
    public static synchronized BackendBridge getInstance() {
        if (instance == null) {
            instance = new BackendBridge();
        }
        return instance;
    }

    /**
     * Starts packet capture and monitoring
     * 
     * @param interfaceIndex Network interface index (ignored, auto-selected)
     * @return true if started successfully
     */
    public boolean startMonitoring(int interfaceIndex) {
        if (isMonitoring) {
            System.out.println("⚠️ Monitoring already running");
            return false;
        }

        try {
            // Auto-select the best network interface
            System.out.println("🔍 Auto-selecting best network interface...");
            int bestInterface = InterfaceSelector.selectBestInterface();
            this.selectedInterfaceIndex = bestInterface;

            // Start packet capture in background thread
            new Thread(() -> {
                try {
                    System.out.println("🚀 Starting packet capture on interface " + bestInterface);
                    PacketSniffer.startSniffing(bestInterface);
                } catch (Exception e) {
                    System.err.println("❌ Failed to start packet capture: " + e.getMessage());
                    e.printStackTrace();

                    // Update UI on JavaFX thread
                    Platform.runLater(() -> {
                        isMonitoring = false;
                    });
                }
            }, "PacketCaptureThread").start();

            this.isMonitoring = true;
            System.out.println("✅ Monitoring started");
            return true;

        } catch (Exception e) {
            System.err.println("❌ Failed to start monitoring: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Stops packet capture and monitoring
     */
    public void stopMonitoring() {
        if (!isMonitoring) {
            System.out.println("⚠️ Monitoring not running");
            return;
        }

        try {
            // TODO: Implement proper packet capture stop mechanism
            // For now, just update status
            this.isMonitoring = false;
            System.out.println("✅ Monitoring stopped");

        } catch (Exception e) {
            System.err.println("❌ Failed to stop monitoring: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Gets current system state
     */
    public SystemState getCurrentState() {
        // TODO: Get from SystemStateManager
        // For now, return mock state
        return currentState;
    }

    /**
     * Gets observable list of security events
     * UI can bind to this list for automatic updates
     */
    public ObservableList<UiSecurityEvent> getSecurityEvents() {
        return securityEvents;
    }

    /**
     * Adds a security event (called by backend)
     * Thread-safe for JavaFX
     */
    public void addSecurityEvent(UiSecurityEvent event) {
        Platform.runLater(() -> {
            securityEvents.add(0, event); // Add to beginning (newest first)

            // Keep only last 100 events
            if (securityEvents.size() > 100) {
                securityEvents.remove(100, securityEvents.size());
            }
        });

        // Phase 4: Desktop Notification
        NotificationService.getInstance().showNotification(event);
    }

    /**
     * Gets latest security event
     */
    public UiSecurityEvent getLatestEvent() {
        if (securityEvents.isEmpty()) {
            return null;
        }
        return securityEvents.get(0);
    }

    /**
     * Gets monitoring status
     */
    public boolean isMonitoring() {
        return isMonitoring;
    }

    /**
     * Gets selected interface index
     */
    public int getSelectedInterfaceIndex() {
        return selectedInterfaceIndex;
    }

    /**
     * Gets network interface name
     */
    public String getInterfaceName() {
        if (selectedInterfaceIndex < 0) {
            return "Not selected";
        }
        // TODO: Get actual interface name from PacketSniffer
        return "Interface " + selectedInterfaceIndex;
    }

    /**
     * Gets packet rate (approximate)
     */
    public int getPacketRate() {
        // TODO: Implement actual packet rate calculation
        // For now, return mock value
        if (!isMonitoring) {
            return 0;
        }
        return (int) (Math.random() * 100) + 50; // Mock: 50-150 packets/sec
    }

    /**
     * Clears all security events
     */
    public void clearEvents() {
        Platform.runLater(() -> {
            securityEvents.clear();
            System.out.println("✅ Security events cleared");
        });
    }

    /**
     * Updates system state (called by backend)
     * Thread-safe for JavaFX
     */
    public void updateSystemState(SystemState newState) {
        Platform.runLater(() -> {
            SystemState oldState = this.currentState;
            this.currentState = newState;

            System.out.println("🔄 System state changed: " + oldState + " → " + newState);
        });
    }

    /**
     * Creates a mock security event for testing (Cycles through all types)
     */
    public UiSecurityEvent createMockEvent() {
        mockEventIndex++;
        int typeIndex = mockEventIndex % 8;

        long now = System.currentTimeMillis();
        String sourceIp = "192.168.1." + (100 + (int) (Math.random() * 100));

        switch (typeIndex) {
            case 0: // PORT SCAN
                return new UiSecurityEvent(now, sourceIp, "10.0.0.5", 4444, 80, "TCP", 64,
                        "PORT_SCAN", "SUSPICIOUS", 45, "HIGH",
                        "Port scan detected", "Monitor source IP");

            case 1: // BRUTE FORCE
                return new UiSecurityEvent(now, sourceIp, "10.0.0.5", 5678, 22, "SSH", 128,
                        "BRUTE_FORCE", "CONFIRMED_THREAT", 75, "HIGH",
                        "SSH Brute force detected", "Block source IP");

            case 2: // DDOS
                return new UiSecurityEvent(now, "142.251.12.1", "10.0.0.5", 0, 80, "TCP", 1500,
                        "DDOS_ATTACK", "CONFIRMED_THREAT", 90, "HIGH",
                        "DDoS attack detected", "Activate mitigation");

            case 3: // SYN FLOOD
                return new UiSecurityEvent(now, "45.33.22.11", "10.0.0.5", 0, 443, "TCP", 60,
                        "SYN_FLOOD", "CONFIRMED_THREAT", 85, "HIGH",
                        "SYN flood detected", "Enable SYN cookies");

            case 4: // BACKDOOR
                return new UiSecurityEvent(now, "185.10.10.5", "10.0.0.5", 31337, 31337, "TCP", 64,
                        "BACKDOOR_ATTEMPT", "CRITICAL", 95, "VERY_HIGH",
                        "Backdoor attempt on port 31337", "Isolate system immediately");

            case 5: // SUSPICIOUS PATTERN (XMAS)
                return new UiSecurityEvent(now, "8.8.4.4", "10.0.0.5", 443, 80, "TCP", 0,
                        "SUSPICIOUS_PATTERN", "SUSPICIOUS", 40, "MEDIUM",
                        "XMAS Scan detected", "Monitor firewall logs");

            case 6: // UDP FLOOD
                return new UiSecurityEvent(now, "23.45.67.89", "10.0.0.5", 0, 53, "UDP", 512,
                        "UDP_FLOOD", "HIGH", 70, "HIGH",
                        "UDP Flood detected", "Rate limit UDP");

            case 7: // ICMP FLOOD
                return new UiSecurityEvent(now, "1.1.1.1", "10.0.0.5", 0, 0, "ICMP", 32,
                        "ICMP_FLOOD", "MODERATE", 50, "MEDIUM",
                        "Ping flood detected", "Disable ICMP echo");

            default:
                return new UiSecurityEvent(now, sourceIp, "10.0.0.5", 1234, 80, "TCP", 64,
                        "GENERIC_THREAT", "LOW", 10, "LOW",
                        "Unknown event", "Monitor");
        }
    }

    /**
     * Gets current system state (alias for getCurrentState)
     */
    public SystemState getCurrentSystemState() {
        return getCurrentState();
    }

    /**
     * Gets total packets analyzed
     */
    public long getTotalPacketsAnalyzed() {
        return totalPackets.get(); // Real count from Phase 4 stats
    }

    /**
     * Gets current packet rate (packets per second)
     */
    public double getCurrentPacketRate() {
        if (!isMonitoring) {
            return 0.0;
        }

        long now = System.currentTimeMillis();
        double seconds = (now - lastRateCheckTime) / 1000.0;

        // Update rate every 1 second to prevent jitter
        if (seconds >= 1.0) {
            int count = packetCounter.getAndSet(0);
            lastCalculatedRate = count / seconds;
            lastRateCheckTime = now;
        }

        return lastCalculatedRate;
    }

    /**
     * Gets active connection count
     */
    public int getActiveConnectionCount() {
        // TODO: Get from backend
        // For now, return mock value
        if (!isMonitoring) {
            return 0;
        }
        return (int) (Math.random() * 50) + 10; // 10-60 connections
    }

    /**
     * Gets current risk score (0-100)
     */
    public int getCurrentRiskScore() {
        // TODO: Get from RiskEngine
        // For now, calculate based on events
        if (securityEvents.isEmpty()) {
            return 0;
        }

        // Average risk of recent events
        int totalRisk = 0;
        int count = Math.min(10, securityEvents.size());
        for (int i = 0; i < count; i++) {
            totalRisk += securityEvents.get(i).getRiskScore();
        }
        return totalRisk / count;
    }

    /**
     * Gets recent security events (limited to specified count)
     */
    public List<UiSecurityEvent> getRecentSecurityEvents(int maxCount) {
        List<UiSecurityEvent> events = new ArrayList<>();
        int count = Math.min(maxCount, securityEvents.size());
        for (int i = 0; i < count; i++) {
            events.add(securityEvents.get(i));
        }
        return events;
    }

    /**
     * Adds a mock event for testing
     */
    public void addMockEvent() {
        UiSecurityEvent mockEvent = createMockEvent();
        addSecurityEvent(mockEvent);
        System.out.println("✅ Mock event added");
    }

    /**
     * Gets observable list of raw packets (Expert Mode)
     * UI can bind to this list for automatic updates
     */
    public ObservableList<RawPacketData> getRawPackets() {
        return rawPackets;
    }

    /**
     * Adds a raw packet (called by packet capture layer)
     * Thread-safe for JavaFX
     */
    public void addRawPacket(RawPacketData packet) {
        // Phase 4: Track stats
        packetCounter.incrementAndGet();
        totalPackets.incrementAndGet();

        Platform.runLater(() -> {
            rawPackets.add(0, packet); // Add to beginning (newest first)

            // Keep only last 5000 packets (Increased for Phase 4 Analytics)
            if (rawPackets.size() > 5000) {
                rawPackets.remove(5000, rawPackets.size());
            }
        });
    }

    /**
     * Clears all raw packet data
     */
    public void clearRawPackets() {
        Platform.runLater(() -> {
            rawPackets.clear();
            System.out.println("✅ Raw packet data cleared");
        });
    }

    /**
     * Updates detection thresholds from AppSettings
     * 
     * @param ddosThreshold     Packets per second for DDoS detection
     * @param portScanThreshold Packets per second for Port Scan detection
     */
    public void updateDetectionThresholds(int ddosThreshold, int portScanThreshold) {
        // Update DDoS Detector
        com.threatscope.core.detect.DDoSDetector.setPacketRateThreshold(ddosThreshold);

        // TODO: Update Port Scan Detector
        // com.threatscope.core.detect.PortScanDetector.setThreshold(portScanThreshold);

        System.out.println("⚙ Detection thresholds updated: DDoS=" + ddosThreshold + ", PortScan=" + portScanThreshold);
    }
}
