package com.threatscope.ui.controller;

import com.threatscope.core.model.SystemState;
import com.threatscope.ui.model.UiSecurityEvent;
import com.threatscope.ui.service.BackendBridge;
import com.threatscope.ui.view.DashboardViewV2;
import com.threatscope.ui.view.SettingsView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Alert; // Phase 4 Alert

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.threatscope.core.model.TopTalker; // Phase 4 Model
import com.threatscope.ui.model.RawPacketData;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import com.threatscope.ui.model.EventStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Dashboard Controller V2 - Professional SOC Dashboard
 * 
 * Handles all dashboard logic and backend integration for the v2.0 layout
 * 
 * Responsibilities:
 * - Start/stop monitoring
 * - Update all UI components with real-time data
 * - Handle timeline table updates
 * - Update risk metrics and charts
 * - Manage explanation panel content
 * - Handle test event generation
 * - Expert mode toggle
 */
public class DashboardControllerV2 {

    private DashboardViewV2 view;
    private final BackendBridge backendBridge;
    private Timeline updateTimeline;
    private boolean isMonitoring = false;
    private final Random random = new Random();

    // Phase 4 Analytics Data
    private XYChart.Series<String, Number> trafficSeries;
    private FilteredList<UiSecurityEvent> filteredEvents;

    public DashboardControllerV2() {
        this.backendBridge = BackendBridge.getInstance();
    }

    /**
     * Initializes controller with view components
     */
    public void initialize(DashboardViewV2 view) {
        this.view = view;

        // Bind timeline table to FilteredList (Phase 4)
        filteredEvents = new FilteredList<>(backendBridge.getSecurityEvents(), p -> true);
        view.getTimelineTable().setItems(filteredEvents);

        // Bind Filter Controls
        view.getEventFilterCombo().valueProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        view.getEventSearchField().textProperty().addListener((obs, oldVal, newVal) -> updateFilter());
        view.getExportEventsButton().setOnAction(e -> exportEventsToCSV());

        // Bind raw activity table to BackendBridge raw packet list (Expert Mode)
        view.getRawActivityTable().setItems(backendBridge.getRawPackets());

        // Set up button handlers
        view.getMonitoringToggleButton().setOnAction(e -> toggleMonitoring());
        view.getTestEventButton().setOnAction(e -> generateTestEvent());
        view.getExpertModeToggle().setOnAction(e -> toggleExpertMode());

        // Start periodic updates
        startPeriodicUpdates();

        // Initial update
        updateDashboard();

        // Initialize Charts (Phase 4)
        trafficSeries = new XYChart.Series<>();
        trafficSeries.setName("Total Traffic");
        view.getLiveTrafficChart().getData().add(trafficSeries);

        // Bind Action Buttons (Phase 4)
        view.getAcknowledgeButton().setOnAction(e -> handleEventAction(EventStatus.ACKNOWLEDGED));
        view.getFalsePositiveButton().setOnAction(e -> handleEventAction(EventStatus.FALSE_POSITIVE));
        view.getResolveButton().setOnAction(e -> handleEventAction(EventStatus.RESOLVED));

        // Bind Settings Button
        view.getSettingsButton().setOnAction(e -> showSettings());

        // Bind Logout Label
        view.getLogoutLabel().setOnMouseClicked(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Terminate Session");
            alert.setContentText("Are you sure you want to capture session data and exit?");

            // Set Owner for modal behavior
            if (view.getPrimaryStage() != null) {
                alert.initOwner(view.getPrimaryStage());
            }

            alert.showAndWait().ifPresent(response -> {
                if (response == javafx.scene.control.ButtonType.OK) {
                    System.out.println("👋 User logged out. Returning to login.");

                    // Stop backend monitoring
                    backendBridge.stopMonitoring();

                    // Get primary stage
                    javafx.stage.Stage primaryStage = view.getPrimaryStage();

                    // Navigate to Login Screen (Enhanced)
                    com.threatscope.ui.view.LoginViewEnhanced loginView = new com.threatscope.ui.view.LoginViewEnhanced(
                            primaryStage);
                    primaryStage.setScene(loginView.getScene());
                    primaryStage.centerOnScreen();
                    primaryStage.show();
                }
            });
        });
    }

    /**
     * Toggles monitoring on/off
     */
    private void toggleMonitoring() {
        if (!isMonitoring) {
            startMonitoring();
        } else {
            stopMonitoring();
        }
    }

    /**
     * Starts monitoring
     */
    private void startMonitoring() {
        // Start backend monitoring (interface 0 by default)
        backendBridge.startMonitoring(0);
        isMonitoring = true;

        // Update button
        view.getMonitoringToggleButton().setText("Disable Monitoring");
        view.getMonitoringToggleButton().setStyle(
                "-fx-background-color: #dc2626;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-cursor: hand;");

        // Update system status
        view.getSystemStatusTextLabel().setText("Monitoring active. Analyzing network traffic in real-time.");

        System.out.println("[Dashboard] Monitoring started");
    }

    /**
     * Stops monitoring
     */
    private void stopMonitoring() {
        backendBridge.stopMonitoring();
        isMonitoring = false;

        // Update button
        view.getMonitoringToggleButton().setText("Enable Monitoring");
        view.getMonitoringToggleButton().setStyle(
                "-fx-background-color: #0078d4;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-cursor: hand;");

        // Update system status
        view.getSystemStatusTextLabel().setText("Monitoring inactive. Click 'Enable Monitoring' to start.");

        System.out.println("[Dashboard] Monitoring stopped");
    }

    /**
     * Generates a test security event
     */
    private void generateTestEvent() {
        backendBridge.addMockEvent();
        System.out.println("[Dashboard] Test event generated");
    }

    /**
     * Shows the settings configuration panel
     */
    private void showSettings() {
        SettingsView settingsView = new SettingsView(view.getPrimaryStage());
        settingsView.show();
    }

    /**
     * Toggles expert mode
     */
    private void toggleExpertMode() {
        boolean isSelected = view.getExpertModeToggle().isSelected();

        if (isSelected) {
            view.getExpertModeToggle().setStyle(
                    "-fx-background-color: #0078d4;" +
                            "-fx-text-fill: #ffffff;" +
                            "-fx-background-radius: 6px;" +
                            "-fx-cursor: hand;");
            System.out.println("[Dashboard] Expert mode enabled");

            // Show raw activity section
            view.getRawActivitySection().setVisible(true);
            view.getRawActivitySection().setManaged(true);

            // Raw activity table already bound in initialize(), no need to set items again
        } else {
            view.getExpertModeToggle().setStyle(
                    "-fx-background-color: #2a2a2a;" +
                            "-fx-text-fill: #888888;" +
                            "-fx-background-radius: 6px;" +
                            "-fx-cursor: hand;");
            System.out.println("[Dashboard] Expert mode disabled");

            // Hide raw activity section
            view.getRawActivitySection().setVisible(false);
            view.getRawActivitySection().setManaged(false);
        }
    }

    /**
     * Starts periodic dashboard updates (every 1 second)
     */
    private void startPeriodicUpdates() {
        updateTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateDashboard()));
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
     * Main dashboard update method - called every second
     */
    private void updateDashboard() {
        Platform.runLater(() -> {
            updateSystemStateBadge();
            updateLiveMonitoringStats();
            updateRiskOverview();

            updateTimelineTable();
            updateVisualAnalytics(); // Phase 4 Update
        });
    }

    /**
     * Updates system state badge in header
     */
    private void updateSystemStateBadge() {
        SystemState state = backendBridge.getCurrentSystemState();

        String badgeText;
        String badgeStyle;

        switch (state) {
            case SAFE:
                badgeText = "SYSTEM: SAFE";
                badgeStyle = "-fx-background-color: #1b4d3e;" +
                        "-fx-text-fill: #4ade80;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(74, 222, 128, 0.3), 8, 0, 0, 0);";
                break;

            case OBSERVE:
                badgeText = "SYSTEM: OBSERVE";
                badgeStyle = "-fx-background-color: #1e3a8a;" +
                        "-fx-text-fill: #60a5fa;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(96, 165, 250, 0.3), 8, 0, 0, 0);";
                break;

            case WARNING:
                badgeText = "SYSTEM: WARNING";
                badgeStyle = "-fx-background-color: #7c2d12;" +
                        "-fx-text-fill: #fb923c;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(251, 146, 60, 0.3), 8, 0, 0, 0);";
                break;

            case CRITICAL:
                badgeText = "SYSTEM: CRITICAL";
                badgeStyle = "-fx-background-color: #7f1d1d;" +
                        "-fx-text-fill: #f87171;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(248, 113, 113, 0.3), 8, 0, 0, 0);";
                break;

            default:
                badgeText = "SYSTEM: UNKNOWN";
                badgeStyle = "-fx-background-color: #374151;" +
                        "-fx-text-fill: #9ca3af;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-background-radius: 6px;";
                break;
        }

        view.getStateBadgeLabel().setText(badgeText);
        view.getStateBadgeLabel().setStyle(badgeStyle);
    }

    /**
     * Updates live monitoring statistics
     */
    private void updateLiveMonitoringStats() {
        // Get stats from backend
        long totalPackets = backendBridge.getTotalPacketsAnalyzed();
        double packetRate = backendBridge.getCurrentPacketRate();
        int activeConnections = backendBridge.getActiveConnectionCount();

        // Update labels
        view.getPacketsAnalyzedLabel().setText(String.format("%,d", totalPackets));
        view.getPacketsPerSecLabel().setText(String.format("%.1f", packetRate));
        view.getActiveConnectionsLabel().setText(String.valueOf(activeConnections));

        // Update last packet time
        if (isMonitoring && totalPackets > 0) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
            view.getLastPacketLabel().setText(timestamp);
        } else {
            view.getLastPacketLabel().setText("N/A");
        }
    }

    /**
     * Updates risk overview panel
     */
    private void updateRiskOverview() {
        // Get current risk score (0-100)
        int currentRisk = backendBridge.getCurrentRiskScore();
        double riskPercent = currentRisk / 100.0;

        // Update progress bar
        view.getRiskProgressBar().setProgress(riskPercent);
        view.getRiskPercentageLabel().setText(currentRisk + "%");

        // Update progress bar color based on risk level
        String barColor;
        if (currentRisk < 30) {
            barColor = "#4ade80"; // Green
        } else if (currentRisk < 70) {
            barColor = "#fb923c"; // Orange
        } else {
            barColor = "#f87171"; // Red
        }

        view.getRiskProgressBar().setStyle(
                "-fx-accent: " + barColor + ";" +
                        "-fx-background-radius: 6px;");

        // Update risk distribution chart
        updateRiskDistributionChart();
    }

    /**
     * Updates risk distribution chart
     */
    private void updateRiskDistributionChart() {
        List<UiSecurityEvent> recentEvents = backendBridge.getRecentSecurityEvents(50);

        int lowCount = 0;
        int medCount = 0;
        int highCount = 0;

        for (UiSecurityEvent event : recentEvents) {
            int risk = event.getRiskScore();
            if (risk < 30) {
                lowCount++;
            } else if (risk < 70) {
                medCount++;
            } else {
                highCount++;
            }
        }

        // Update chart data
        XYChart.Series<String, Number> series = view.getRiskDistributionChart().getData().get(0);
        series.getData().get(0).setYValue(lowCount);
        series.getData().get(1).setYValue(medCount);
        series.getData().get(2).setYValue(highCount);
    }

    /**
     * Updates timeline table with recent events
     */
    private void updateTimelineTable() {
        // Table is bound to backendBridge.getSecurityEvents() - updates automatically
        // Just handle selection and explanation panel updates

        // If an event is selected, update explanation panel
        UiSecurityEvent selectedEvent = view.getTimelineTable().getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            updateExplanationPanel(selectedEvent);
        } else {
            // Auto-select most recent event if available
            if (!backendBridge.getSecurityEvents().isEmpty()) {
                view.getTimelineTable().getSelectionModel().select(0);
                updateExplanationPanel(backendBridge.getSecurityEvents().get(0));
            }
        }
    }

    /**
     * Updates explanation panel with event details
     */
    private void updateExplanationPanel(UiSecurityEvent event) {
        if (event == null) {
            // Show empty state
            com.threatscope.ui.service.ExplanationEngine.EnhancedExplanation empty = com.threatscope.ui.service.ExplanationEngine.EnhancedExplanation
                    .empty();

            view.getWhatHappenedLabel().setText(empty.whatHappened);
            view.getWhyMattersLabel().setText(empty.whyMatters);
            view.getHowItWorksLabel().setText(empty.howItWorks);
            view.getTechnicalDetailsLabel().setText(empty.technicalDetails);
            view.getClassificationLabel().setText("CLASSIFICATION\\nN/A");
            view.getRiskLevelLabel().setText("RISK LEVEL\\nN/A");
            view.getConfidenceLabel().setText("CONFIDENCE\\nN/A");
            view.getRecommendedActionLabel().setText(empty.recommendedAction);
            view.getReassuranceLabel().setText(empty.reassurance);
            view.getStatusLabel().setText("N/A");
            view.getStatusLabel().setStyle("-fx-text-fill: #888888; -fx-font-weight: bold; -fx-font-size: 14px;");

            // Disable buttons
            view.getAcknowledgeButton().setDisable(true);
            view.getFalsePositiveButton().setDisable(true);
            view.getResolveButton().setDisable(true);
            return;
        }

        // Generate comprehensive explanation using ExplanationEngine
        com.threatscope.ui.service.ExplanationEngine.EnhancedExplanation explanation = com.threatscope.ui.service.ExplanationEngine
                .generateExplanation(event);

        // Update all explanation sections with rich content
        view.getWhatHappenedLabel().setText(explanation.whatHappened);
        view.getWhyMattersLabel().setText(explanation.whyMatters);
        view.getHowItWorksLabel().setText(explanation.howItWorks);
        view.getTechnicalDetailsLabel().setText(explanation.technicalDetails);
        view.getClassificationLabel().setText("CLASSIFICATION\n" + event.getClassification());

        // Update Status Label
        com.threatscope.ui.model.EventStatus status = event.getStatus();
        String statusColor;

        switch (status) {
            case NEW:
                statusColor = "#fb923c"; // Orange
                break;
            case ACKNOWLEDGED:
                statusColor = "#60a5fa"; // Blue
                break;
            case RESOLVED:
                statusColor = "#4ade80"; // Green
                break;
            case FALSE_POSITIVE:
                statusColor = "#9ca3af"; // Gray
                break;
            default:
                statusColor = "#ffffff";
        }

        view.getStatusLabel().setText(status.toString());
        view.getStatusLabel()
                .setStyle("-fx-text-fill: " + statusColor + "; -fx-font-weight: bold; -fx-font-size: 14px;");

        // Action Button State Management
        view.getAcknowledgeButton().setDisable(status == EventStatus.ACKNOWLEDGED || status == EventStatus.RESOLVED);
        view.getResolveButton().setDisable(status == EventStatus.RESOLVED);
        view.getFalsePositiveButton().setDisable(status == EventStatus.FALSE_POSITIVE);

        view.getRiskLevelLabel().setText("RISK LEVEL\n" + event.getRiskScore() + "/100");

        view.getConfidenceLabel().setText("CONFIDENCE\n" + event.getConfidence() + "%");
        view.getRecommendedActionLabel().setText(explanation.recommendedAction);
        view.getReassuranceLabel().setText(explanation.reassurance);
    }

    /**
     * Updates Visual Analytics (Charts & Tables) - Phase 4
     */
    private void updateVisualAnalytics() {
        if (!isMonitoring)
            return;

        // 1. Update Live Traffic Graph
        double currentRate = backendBridge.getCurrentPacketRate();
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        trafficSeries.getData().add(new XYChart.Data<>(timestamp, currentRate));

        // Keep last 60 seconds
        if (trafficSeries.getData().size() > 60) {
            trafficSeries.getData().remove(0);
        }

        // 2. Aggregate Data for Top Talkers & Protocol Chart
        // We use the raw packet buffer (last 5000 packets) for statistics
        List<RawPacketData> packets = new ArrayList<>(backendBridge.getRawPackets()); // Copy to avoid concurrent mod

        if (packets.isEmpty())
            return;

        Map<String, Long> ipCounts = new HashMap<>();
        Map<String, String> ipProtocols = new HashMap<>();
        Map<String, Integer> protocolCounts = new HashMap<>();

        for (RawPacketData p : packets) {
            // Count IPs
            ipCounts.merge(p.getSourceIp(), 1L, Long::sum);
            ipProtocols.putIfAbsent(p.getSourceIp(), p.getProtocol()); // Just take first seen

            // Count Protocols
            protocolCounts.merge(p.getProtocol(), 1, Integer::sum);
        }

        // 3. Update Top Talkers Table
        List<TopTalker> topTalkers = ipCounts.entrySet().stream()
                .sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())) // Sort desc
                .limit(10) // Top 10
                .map(e -> new TopTalker(
                        e.getKey(),
                        e.getValue(),
                        0, // Bytes not tracked yet
                        ipProtocols.get(e.getKey()),
                        "UNKNOWN" // Risk level requires correlation
                ))
                .collect(Collectors.toList());

        view.getTopTalkersTable().setItems(FXCollections.observableArrayList(topTalkers));

        // 4. Update Protocol Pie Chart
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        for (Map.Entry<String, Integer> entry : protocolCounts.entrySet()) {
            pieData.add(new PieChart.Data(entry.getKey(), entry.getValue()));
        }
        view.getProtocolPieChart().setData(pieData);
    }

    /**
     * Updates the timeline filter based on UI controls
     */
    private void updateFilter() {
        String filterType = view.getEventFilterCombo().getValue();
        String searchText = view.getEventSearchField().getText() != null
                ? view.getEventSearchField().getText().toLowerCase()
                : "";

        filteredEvents.setPredicate(event -> {
            // Safety check
            if (event == null)
                return false;

            // 1. Type Filter
            if (filterType != null && !"All Events".equals(filterType)) {
                String type = event.getThreatType();
                if (type == null || !type.equals(filterType)) {
                    return false;
                }
            }

            // 2. Search Text
            if (!searchText.isEmpty()) {
                String summary = event.getSummary();
                String ip = event.getSourceIp();
                String classification = event.getClassification();

                boolean matchesSummary = summary != null && summary.toLowerCase().contains(searchText);
                boolean matchesIp = ip != null && ip.toLowerCase().contains(searchText);
                boolean matchesClass = classification != null && classification.toLowerCase().contains(searchText);

                if (!matchesSummary && !matchesIp && !matchesClass) {
                    return false;
                }
            }

            return true;
        });
    }

    /**
     * Exports all security events to CSV
     */
    private void exportEventsToCSV() {
        try {
            String filename = "threatscope_events_" + System.currentTimeMillis() + ".csv";
            java.io.File file = new java.io.File(filename);
            String fullPath = file.getAbsolutePath();

            java.io.PrintWriter writer = new java.io.PrintWriter(file);

            // Header
            writer.println("Time,Source IP,Threat Type,Risk,Classification,Summary,Recommended Action");

            // Data
            for (UiSecurityEvent event : backendBridge.getSecurityEvents()) { // Export ALL events, not just filtered
                writer.println(String.format("%s,%s,%s,%d,%s,\"%s\",\"%s\"",
                        event.getFormattedTimestamp(),
                        event.getSourceIp(),
                        event.getThreatType(),
                        event.getRiskScore(),
                        event.getClassification(),
                        event.getSummary(),
                        event.getRecommendedAction()));
            }
            writer.close();

            System.out.println("✅ Exported events to " + fullPath);
            view.getSystemStatusTextLabel().setText("Export succesful");

            // Show Success Alert
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export Successful");
                alert.setHeaderText("Data Exported Successfully");
                alert.setContentText("Events saved to:\n" + fullPath);
                alert.showAndWait();
            });

        } catch (Exception e) {
            System.err.println("❌ Export failed: " + e.getMessage());

            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Export Failed");
                alert.setHeaderText("Could not export data");
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            });
        }
    }

    /**
     * Handles event action (Acknowledge, False Positive, Resolve)
     */
    private void handleEventAction(EventStatus status) {
        UiSecurityEvent selectedEvent = view.getTimelineTable().getSelectionModel().getSelectedItem();

        if (selectedEvent == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Event Selected");
            alert.setContentText("Please select an event from the timeline to perform this action.");
            alert.showAndWait();
            return;
        }

        // Update Event
        selectedEvent.setStatus(status);

        // Visual Feedback
        updateExplanationPanel(selectedEvent);

        // Log
        System.out.println("✅ Event " + status + ": " + selectedEvent.getSummary());

        // Update Status label in explanation panel (hack via title or creating new
        // label?)
        // Since we can't easily add a new label without modifying View, we'll append to
        // Title
        // Actually updateExplanationPanel handles the text content.
    }

    public BackendBridge getBackendBridge() {
        return backendBridge;
    }
}
