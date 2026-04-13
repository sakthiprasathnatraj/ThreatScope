package com.threatscope.ui.controller;

import com.threatscope.core.model.SystemState;
import com.threatscope.ui.model.UiSecurityEvent;
import com.threatscope.ui.service.BackendBridge;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.util.Duration;

/**
 * Professional Dashboard Controller
 * 
 * Handles all dashboard logic with production-quality features:
 * - Live activity indicators (1-second refresh)
 * - Heartbeat messages (rotating calm status messages)
 * - Session context management
 * - Event timeline updates
 * - Raw activity table (Expert Mode)
 * - Smooth UI updates with fade-in effects
 */
public class DashboardControllerProfessional {

    private final BackendBridge backendBridge;

    // ===== UI COMPONENTS =====

    // Top Bar
    private Label stateBadgeLabel;
    private Label sessionUserLabel;
    private Label sessionRoleLabel;
    private Label sessionStatusLabel;

    // Live Activity Indicators
    private Label packetsAnalyzedLabel;
    private Label packetsPerSecLabel;
    private Label activeConnectionsLabel;
    private Label lastPacketTimeLabel;

    // Heartbeat Message
    private Label heartbeatMessageLabel;

    // Monitoring Info
    private Label interfaceLabel;
    private Label monitoringStatusLabel;

    // Quick Status
    private Label quickStatusLabel;

    // Tables
    private TableView<UiSecurityEvent> eventTimelineTable;
    private TableView<UiSecurityEvent> rawActivityTable;

    // ===== TIMELINES =====
    private Timeline liveUpdateTimeline;
    private Timeline heartbeatTimeline;

    // ===== LIVE METRICS =====
    private long sessionStartTime;
    private long totalPacketsAnalyzed;
    private long lastPacketTime;
    private int currentPacketRate;

    // ===== HEARTBEAT MESSAGES =====
    private final String[] SAFE_HEARTBEAT_MESSAGES = {
            "Monitoring network traffic normally",
            "No suspicious behavior observed",
            "System operating within normal parameters",
            "All security checks passing",
            "Network activity appears normal",
            "No threats detected in recent traffic"
    };
    private int currentHeartbeatIndex = 0;

    /**
     * Constructor
     */
    public DashboardControllerProfessional() {
        this.backendBridge = BackendBridge.getInstance();
        this.sessionStartTime = System.currentTimeMillis();
        this.totalPacketsAnalyzed = 0;
        this.lastPacketTime = 0;
        this.currentPacketRate = 0;
    }

    /**
     * Initializes controller with UI components
     */
    public void initialize(
            Label stateBadgeLabel,
            Label sessionUserLabel,
            Label sessionRoleLabel,
            Label sessionStatusLabel,
            Label packetsAnalyzedLabel,
            Label packetsPerSecLabel,
            Label activeConnectionsLabel,
            Label lastPacketTimeLabel,
            Label heartbeatMessageLabel,
            Label interfaceLabel,
            Label monitoringStatusLabel,
            Label quickStatusLabel,
            TableView<UiSecurityEvent> eventTimelineTable,
            TableView<UiSecurityEvent> rawActivityTable) {

        this.stateBadgeLabel = stateBadgeLabel;
        this.sessionUserLabel = sessionUserLabel;
        this.sessionRoleLabel = sessionRoleLabel;
        this.sessionStatusLabel = sessionStatusLabel;
        this.packetsAnalyzedLabel = packetsAnalyzedLabel;
        this.packetsPerSecLabel = packetsPerSecLabel;
        this.activeConnectionsLabel = activeConnectionsLabel;
        this.lastPacketTimeLabel = lastPacketTimeLabel;
        this.heartbeatMessageLabel = heartbeatMessageLabel;
        this.interfaceLabel = interfaceLabel;
        this.monitoringStatusLabel = monitoringStatusLabel;
        this.quickStatusLabel = quickStatusLabel;
        this.eventTimelineTable = eventTimelineTable;
        this.rawActivityTable = rawActivityTable;

        // Bind tables to backend data
        eventTimelineTable.setItems(backendBridge.getSecurityEvents());
        rawActivityTable.setItems(backendBridge.getSecurityEvents());

        // Start periodic updates
        startLiveUpdates();
        startHeartbeatMessages();

        // Initial update
        updateDashboard();
    }

    /**
     * Starts monitoring
     */
    public void startMonitoring(int interfaceIndex) {
        boolean started = backendBridge.startMonitoring(interfaceIndex);

        if (started) {
            System.out.println("✅ Professional Dashboard: Monitoring started");
            sessionStartTime = System.currentTimeMillis();
            totalPacketsAnalyzed = 0;
            updateDashboard();
        } else {
            System.err.println("❌ Professional Dashboard: Failed to start monitoring");
        }
    }

    /**
     * Stops monitoring
     */
    public void stopMonitoring() {
        backendBridge.stopMonitoring();
        System.out.println("✅ Professional Dashboard: Monitoring stopped");
        updateDashboard();
    }

    /**
     * Updates entire dashboard
     */
    public void updateDashboard() {
        updateSystemStateBadge();
        updateNetworkInterface();
        updateMonitoringStatus();
        updateLiveMetrics();
        updateQuickStatus();
    }

    /**
     * Updates system state badge with color and animation
     */
    private void updateSystemStateBadge() {
        SystemState state = backendBridge.getCurrentState();

        String stateText = "SYSTEM: " + state.name();
        String bgColor;
        String shadowColor;

        switch (state) {
            case SAFE:
                bgColor = "#238636";
                shadowColor = "rgba(35, 134, 54, 0.4)";
                break;
            case OBSERVE:
                bgColor = "#1f6feb";
                shadowColor = "rgba(31, 111, 235, 0.4)";
                break;
            case WARNING:
                bgColor = "#d29922";
                shadowColor = "rgba(210, 153, 34, 0.4)";
                break;
            case CRITICAL:
                bgColor = "#da3633";
                shadowColor = "rgba(218, 54, 51, 0.4)";
                break;
            default:
                bgColor = "#6e7681";
                shadowColor = "rgba(110, 118, 129, 0.4)";
        }

        stateBadgeLabel.setText(stateText);
        stateBadgeLabel.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-effect: dropshadow(gaussian, " + shadowColor + ", 8, 0, 0, 0);");
    }

    /**
     * Updates network interface info
     */
    private void updateNetworkInterface() {
        String interfaceName = backendBridge.getInterfaceName();
        interfaceLabel.setText(interfaceName);
    }

    /**
     * Updates monitoring status
     */
    private void updateMonitoringStatus() {
        boolean monitoring = backendBridge.isMonitoring();

        if (monitoring) {
            monitoringStatusLabel.setText("● Enabled");
            monitoringStatusLabel.setStyle("-fx-text-fill: #3fb950;");
        } else {
            monitoringStatusLabel.setText("○ Disabled");
            monitoringStatusLabel.setStyle("-fx-text-fill: #8b949e;");
        }
    }

    /**
     * Updates live activity metrics
     * Called every 1 second
     */
    private void updateLiveMetrics() {
        boolean monitoring = backendBridge.isMonitoring();

        if (monitoring) {
            // Simulate packet capture metrics
            // TODO: Replace with actual backend metrics when available

            // Packets analyzed (session total)
            totalPacketsAnalyzed += (long) (Math.random() * 100) + 50;
            packetsAnalyzedLabel.setText(String.format("%,d", totalPacketsAnalyzed));

            // Packets/sec (live)
            currentPacketRate = (int) (Math.random() * 150) + 50;
            packetsPerSecLabel.setText(String.valueOf(currentPacketRate));

            // Active connections (last 60s)
            int activeConns = (int) (Math.random() * 20) + 5;
            activeConnectionsLabel.setText(String.valueOf(activeConns));

            // Last packet time
            lastPacketTime = System.currentTimeMillis();
            long msSinceLastPacket = System.currentTimeMillis() - lastPacketTime;
            lastPacketTimeLabel.setText(msSinceLastPacket + " ms ago");

        } else {
            packetsAnalyzedLabel.setText("0");
            packetsPerSecLabel.setText("0");
            activeConnectionsLabel.setText("0");
            lastPacketTimeLabel.setText("N/A");
        }
    }

    /**
     * Updates quick status message based on system state
     */
    private void updateQuickStatus() {
        SystemState state = backendBridge.getCurrentState();
        String statusMessage;

        switch (state) {
            case SAFE:
                statusMessage = "Your system is currently SAFE. No threats detected.";
                break;
            case OBSERVE:
                statusMessage = "Monitoring network activity. Some events detected but no immediate threat.";
                break;
            case WARNING:
                statusMessage = "Elevated activity detected. Monitoring closely for potential threats.";
                break;
            case CRITICAL:
                statusMessage = "CRITICAL: High-confidence threat detected. Review security event timeline.";
                break;
            default:
                statusMessage = "System status unknown.";
        }

        quickStatusLabel.setText(statusMessage);
    }

    /**
     * Rotates heartbeat message
     * Called every 10 seconds
     */
    private void rotateHeartbeatMessage() {
        SystemState state = backendBridge.getCurrentState();

        // Only show calm messages when SAFE
        if (state == SystemState.SAFE) {
            currentHeartbeatIndex = (currentHeartbeatIndex + 1) % SAFE_HEARTBEAT_MESSAGES.length;
            heartbeatMessageLabel.setText(SAFE_HEARTBEAT_MESSAGES[currentHeartbeatIndex]);
        } else {
            // Show state-specific message
            switch (state) {
                case OBSERVE:
                    heartbeatMessageLabel.setText("Observing network activity patterns");
                    break;
                case WARNING:
                    heartbeatMessageLabel.setText("Elevated security monitoring active");
                    break;
                case CRITICAL:
                    heartbeatMessageLabel.setText("Critical threat response active");
                    break;
                default:
                    heartbeatMessageLabel.setText("System status monitoring");
            }
        }
    }

    /**
     * Starts live updates (1-second refresh)
     */
    private void startLiveUpdates() {
        liveUpdateTimeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            updateDashboard();
        }));
        liveUpdateTimeline.setCycleCount(Animation.INDEFINITE);
        liveUpdateTimeline.play();
    }

    /**
     * Starts heartbeat message rotation (10-second interval)
     */
    private void startHeartbeatMessages() {
        heartbeatTimeline = new Timeline(new KeyFrame(Duration.seconds(10), event -> {
            rotateHeartbeatMessage();
        }));
        heartbeatTimeline.setCycleCount(Animation.INDEFINITE);
        heartbeatTimeline.play();
    }

    /**
     * Stops all periodic updates
     */
    public void stopPeriodicUpdates() {
        if (liveUpdateTimeline != null) {
            liveUpdateTimeline.stop();
        }
        if (heartbeatTimeline != null) {
            heartbeatTimeline.stop();
        }
    }

    /**
     * Gets the backend bridge
     */
    public BackendBridge getBackendBridge() {
        return backendBridge;
    }
}
