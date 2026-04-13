package com.threatscope.ui.controller;

import com.threatscope.core.model.SystemState;
import com.threatscope.ui.model.UiSecurityEvent;
import com.threatscope.ui.service.BackendBridge;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Dashboard Controller
 * 
 * Handles dashboard logic and backend integration
 * 
 * Responsibilities:
 * - Start/stop monitoring
 * - Update system state badge
 * - Refresh latest security observation
 * - Update statistics (packet rate, etc.)
 * - Handle periodic UI updates
 */
public class DashboardController {

    private final BackendBridge backendBridge;

    // UI Components (injected from view)
    private Label stateBadgeLabel;
    private Label interfaceLabel;
    private Label monitoringStatusLabel;
    private Label packetRateLabel;
    private Label latestObservationLabel;
    private Label riskScoreLabel;
    private Label confidenceLabel;
    private Label quickStatusLabel;

    // Update timeline
    private Timeline updateTimeline;

    /**
     * Constructor
     */
    public DashboardController() {
        this.backendBridge = BackendBridge.getInstance();
    }

    /**
     * Initializes controller with UI components
     */
    public void initialize(
            Label stateBadgeLabel,
            Label interfaceLabel,
            Label monitoringStatusLabel,
            Label packetRateLabel,
            Label latestObservationLabel,
            Label riskScoreLabel,
            Label confidenceLabel,
            Label quickStatusLabel) {
        this.stateBadgeLabel = stateBadgeLabel;
        this.interfaceLabel = interfaceLabel;
        this.monitoringStatusLabel = monitoringStatusLabel;
        this.packetRateLabel = packetRateLabel;
        this.latestObservationLabel = latestObservationLabel;
        this.riskScoreLabel = riskScoreLabel;
        this.confidenceLabel = confidenceLabel;
        this.quickStatusLabel = quickStatusLabel;

        // Start periodic updates
        startPeriodicUpdates();

        // Initial update
        updateDashboard();
    }

    /**
     * Starts monitoring
     */
    public void startMonitoring(int interfaceIndex) {
        boolean started = backendBridge.startMonitoring(interfaceIndex);

        if (started) {
            System.out.println("✅ Dashboard: Monitoring started");
            updateDashboard();
        } else {
            System.err.println("❌ Dashboard: Failed to start monitoring");
        }
    }

    /**
     * Stops monitoring
     */
    public void stopMonitoring() {
        backendBridge.stopMonitoring();
        System.out.println("✅ Dashboard: Monitoring stopped");
        updateDashboard();
    }

    /**
     * Updates dashboard with latest data
     */
    public void updateDashboard() {
        // Update system state badge
        updateSystemStateBadge();

        // Update network interface
        updateNetworkInterface();

        // Update monitoring status
        updateMonitoringStatus();

        // Update packet rate
        updatePacketRate();

        // Update latest observation
        updateLatestObservation();

        // Update quick status
        updateQuickStatus();
    }

    /**
     * Updates system state badge
     */
    private void updateSystemStateBadge() {
        SystemState state = backendBridge.getCurrentState();

        String stateText = "SYSTEM: " + state.name();
        String stateColor;
        String stateBgColor;

        switch (state) {
            case SAFE:
                stateColor = "#4caf50";
                stateBgColor = "#4caf5022";
                break;
            case OBSERVE:
                stateColor = "#2196f3";
                stateBgColor = "#2196f322";
                break;
            case WARNING:
                stateColor = "#ff9800";
                stateBgColor = "#ff980022";
                break;
            case CRITICAL:
                stateColor = "#f44336";
                stateBgColor = "#f4433622";
                break;
            default:
                stateColor = "#888888";
                stateBgColor = "#88888822";
        }

        stateBadgeLabel.setText(stateText);
        stateBadgeLabel.setStyle(
                "-fx-background-color: " + stateBgColor + ";" +
                        "-fx-text-fill: " + stateColor + ";" +
                        "-fx-padding: 6px 12px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 12px;");
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
            monitoringStatusLabel.setText("● Active");
            monitoringStatusLabel.setStyle("-fx-text-fill: #4caf50;");
        } else {
            monitoringStatusLabel.setText("○ Stopped");
            monitoringStatusLabel.setStyle("-fx-text-fill: #888888;");
        }
    }

    /**
     * Updates packet rate
     */
    private void updatePacketRate() {
        int rate = backendBridge.getPacketRate();
        packetRateLabel.setText(rate + " packets/sec");
    }

    /**
     * Updates latest security observation
     */
    private void updateLatestObservation() {
        UiSecurityEvent latestEvent = backendBridge.getLatestEvent();

        if (latestEvent == null) {
            latestObservationLabel.setText("No security observations yet. Monitoring network traffic...");
            riskScoreLabel.setText("Risk: 0/100");
            confidenceLabel.setText("Confidence: N/A");
        } else {
            latestObservationLabel.setText(latestEvent.getExplanation());
            riskScoreLabel.setText("Risk: " + latestEvent.getRiskScore() + "/100 (" + latestEvent.getRiskLevel() + ")");
            confidenceLabel.setText("Confidence: " + latestEvent.getConfidence());
        }
    }

    /**
     * Updates quick status message
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
                statusMessage = "CRITICAL: High-confidence threat detected. Review security observations.";
                break;
            default:
                statusMessage = "System status unknown.";
        }

        quickStatusLabel.setText(statusMessage);
    }

    /**
     * Starts periodic dashboard updates
     */
    private void startPeriodicUpdates() {
        // Update every 2 seconds
        updateTimeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            updateDashboard();
        }));
        updateTimeline.setCycleCount(Animation.INDEFINITE);
        updateTimeline.play();
    }

    /**
     * Stops periodic updates
     */
    public void stopPeriodicUpdates() {
        if (updateTimeline != null) {
            updateTimeline.stop();
        }
    }

    /**
     * Adds a mock event for testing
     */
    public void addMockEvent() {
        UiSecurityEvent mockEvent = backendBridge.createMockEvent();
        backendBridge.addSecurityEvent(mockEvent);
        updateDashboard();
        System.out.println("✅ Mock event added");
    }

    /**
     * Gets the backend bridge (for chart updates)
     */
    public BackendBridge getBackendBridge() {
        return backendBridge;
    }
}
