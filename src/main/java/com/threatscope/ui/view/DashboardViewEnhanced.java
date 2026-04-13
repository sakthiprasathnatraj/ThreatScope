package com.threatscope.ui.view;

import com.threatscope.ui.controller.DashboardController;
import com.threatscope.ui.model.UiSecurityEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

/**
 * Dashboard View - Enhanced with Charts & Statistics
 * 
 * Professional security monitoring dashboard with:
 * - Real-time packet rate chart
 * - Risk distribution chart
 * - Event statistics
 * - Traffic classification pie chart
 * - System overview
 * - Latest security observation
 * 
 * Layout:
 * ┌─────────────────────────────────────────────────────┐
 * │ Top Bar (Title + State Badge) │
 * ├─────────────────────────────────────────────────────┤
 * │ │
 * │ ┌──────────┐ ┌──────────┐ ┌──────────────────┐ │
 * │ │ System │ │ Event │ │ Latest │ │
 * │ │ Overview │ │ Stats │ │ Observation │ │
 * │ └──────────┘ └──────────┘ └──────────────────┘ │
 * │ │
 * │ ┌──────────────────┐ ┌──────────────────┐ │
 * │ │ Packet Rate │ │ Risk Distribution│ │
 * │ │ Chart (Line) │ │ Chart (Bar) │ │
 * │ └──────────────────┘ └──────────────────┘ │
 * │ │
 * │ ┌──────────────────────────────────────────────┐ │
 * │ │ Traffic Classification (Pie Chart) │ │
 * │ └──────────────────────────────────────────────┘ │
 * │ │
 * │ [Start Monitoring] [Stop] [Add Mock Event] │
 * │ │
 * └─────────────────────────────────────────────────────┘
 */
public class DashboardViewEnhanced {

    private final Stage primaryStage;
    private final Scene scene;
    private final DashboardController controller;

    // UI Components
    private Label stateBadgeLabel;
    private Label interfaceLabel;
    private Label monitoringStatusLabel;
    private Label packetRateLabel;
    private Label latestObservationLabel;
    private Label riskScoreLabel;
    private Label confidenceLabel;
    private Label quickStatusLabel;

    // Statistics labels
    private Label totalEventsLabel;
    private Label safeEventsLabel;
    private Label suspiciousEventsLabel;
    private Label criticalEventsLabel;

    // Charts
    private LineChart<Number, Number> packetRateChart;
    private BarChart<String, Number> riskDistributionChart;
    private PieChart trafficClassificationChart;

    // Chart data
    private ObservableList<XYChart.Data<Number, Number>> packetRateData;
    private int chartTimeCounter = 0;

    // Buttons
    private Button startButton;
    private Button stopButton;
    private Button mockEventButton;

    public DashboardViewEnhanced(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new DashboardController();

        // Create UI
        BorderPane root = createEnhancedDashboardUI();

        // Create scene
        this.scene = new Scene(root, 1400, 900);

        // Initialize controller
        controller.initialize(
                stateBadgeLabel,
                interfaceLabel,
                monitoringStatusLabel,
                packetRateLabel,
                latestObservationLabel,
                riskScoreLabel,
                confidenceLabel,
                quickStatusLabel);

        // Start chart updates
        startChartUpdates();
    }

    /**
     * Creates the enhanced dashboard UI with charts
     */
    private BorderPane createEnhancedDashboardUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        // Top bar
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // Center content with charts
        VBox center = createEnhancedCenterContent();
        ScrollPane scrollPane = new ScrollPane(center);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: #1e1e1e; -fx-background-color: #1e1e1e;");
        root.setCenter(scrollPane);

        return root;
    }

    /**
     * Creates top bar
     */
    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 24, 16, 24));
        topBar.setStyle(
                "-fx-background-color: #252525;" +
                        "-fx-border-color: #333333;" +
                        "-fx-border-width: 0 0 1px 0;");

        Label titleLabel = new Label("ThreatScope");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        Label versionLabel = new Label("v2.0 Professional - Enhanced Dashboard");
        versionLabel.setFont(Font.font("System", 12));
        versionLabel.setStyle("-fx-text-fill: #888888;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        stateBadgeLabel = new Label("SYSTEM: SAFE");
        stateBadgeLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
        stateBadgeLabel.setStyle(
                "-fx-background-color: #4caf5022;" +
                        "-fx-text-fill: #4caf50;" +
                        "-fx-padding: 6px 12px;" +
                        "-fx-background-radius: 4px;");

        topBar.getChildren().addAll(titleLabel, versionLabel, spacer, stateBadgeLabel);
        return topBar;
    }

    /**
     * Creates enhanced center content with charts
     */
    private VBox createEnhancedCenterContent() {
        VBox center = new VBox(20);
        center.setPadding(new Insets(30));

        // Top panels row (System Overview, Event Stats, Latest Observation)
        HBox topPanelsRow = new HBox(15);
        topPanelsRow.setAlignment(Pos.TOP_CENTER);

        VBox systemPanel = createSystemOverviewPanel();
        VBox statsPanel = createEventStatisticsPanel();
        VBox observationPanel = createLatestObservationPanel();

        HBox.setHgrow(systemPanel, Priority.ALWAYS);
        HBox.setHgrow(statsPanel, Priority.ALWAYS);
        HBox.setHgrow(observationPanel, Priority.ALWAYS);

        topPanelsRow.getChildren().addAll(systemPanel, statsPanel, observationPanel);

        // Charts row (Packet Rate + Risk Distribution)
        HBox chartsRow = new HBox(15);
        chartsRow.setAlignment(Pos.TOP_CENTER);

        VBox packetRatePanel = createPacketRateChartPanel();
        VBox riskDistPanel = createRiskDistributionChartPanel();

        HBox.setHgrow(packetRatePanel, Priority.ALWAYS);
        HBox.setHgrow(riskDistPanel, Priority.ALWAYS);

        chartsRow.getChildren().addAll(packetRatePanel, riskDistPanel);

        // Traffic classification chart
        VBox trafficPanel = createTrafficClassificationPanel();

        // Control buttons
        HBox controlButtons = createControlButtons();

        center.getChildren().addAll(
                topPanelsRow,
                chartsRow,
                trafficPanel,
                controlButtons);

        return center;
    }

    /**
     * Creates system overview panel
     */
    private VBox createSystemOverviewPanel() {
        VBox panel = createPanel("System Overview", 350);

        HBox interfaceRow = createInfoRow("Interface:", "Not selected");
        interfaceLabel = (Label) interfaceRow.getChildren().get(1);

        HBox statusRow = createInfoRow("Status:", "○ Stopped");
        monitoringStatusLabel = (Label) statusRow.getChildren().get(1);

        HBox rateRow = createInfoRow("Packet Rate:", "0 pkt/s");
        packetRateLabel = (Label) rateRow.getChildren().get(1);

        panel.getChildren().addAll(interfaceRow, statusRow, rateRow);
        return panel;
    }

    /**
     * Creates event statistics panel
     */
    private VBox createEventStatisticsPanel() {
        VBox panel = createPanel("Event Statistics", 350);

        HBox totalRow = createStatRow("Total Events:", "0", "#2196f3");
        totalEventsLabel = (Label) totalRow.getChildren().get(1);

        HBox safeRow = createStatRow("Safe/Benign:", "0", "#4caf50");
        safeEventsLabel = (Label) safeRow.getChildren().get(1);

        HBox suspiciousRow = createStatRow("Suspicious:", "0", "#ff9800");
        suspiciousEventsLabel = (Label) suspiciousRow.getChildren().get(1);

        HBox criticalRow = createStatRow("Critical:", "0", "#f44336");
        criticalEventsLabel = (Label) criticalRow.getChildren().get(1);

        panel.getChildren().addAll(totalRow, safeRow, suspiciousRow, criticalRow);
        return panel;
    }

    /**
     * Creates latest observation panel
     */
    private VBox createLatestObservationPanel() {
        VBox panel = createPanel("Latest Security Observation", 550);

        latestObservationLabel = new Label("No observations yet. Start monitoring to begin.");
        latestObservationLabel.setFont(Font.font("System", 13));
        latestObservationLabel.setStyle("-fx-text-fill: #cccccc;");
        latestObservationLabel.setWrapText(true);
        latestObservationLabel.setMaxWidth(510);

        riskScoreLabel = new Label("Risk: 0/100");
        riskScoreLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        riskScoreLabel.setStyle("-fx-text-fill: #4caf50;");

        confidenceLabel = new Label("Confidence: N/A");
        confidenceLabel.setFont(Font.font("System", 13));
        confidenceLabel.setStyle("-fx-text-fill: #888888;");

        // Quick status label
        quickStatusLabel = new Label("Your system is currently SAFE. No threats detected.");
        quickStatusLabel.setFont(Font.font("System", 13));
        quickStatusLabel.setStyle("-fx-text-fill: #4caf50;");
        quickStatusLabel.setWrapText(true);
        quickStatusLabel.setMaxWidth(510);
        quickStatusLabel.setPadding(new Insets(10, 0, 0, 0));

        panel.getChildren().addAll(latestObservationLabel, riskScoreLabel, confidenceLabel, quickStatusLabel);
        return panel;
    }

    /**
     * Creates packet rate chart panel
     */
    private VBox createPacketRateChartPanel() {
        VBox panel = createPanel("Real-Time Packet Rate", 650);

        // Create line chart
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time (seconds)");
        xAxis.setAutoRanging(false);
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(60);
        xAxis.setTickUnit(10);
        xAxis.setStyle("-fx-tick-label-fill: #888888;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Packets/sec");
        yAxis.setAutoRanging(true);
        yAxis.setStyle("-fx-tick-label-fill: #888888;");

        packetRateChart = new LineChart<>(xAxis, yAxis);
        packetRateChart.setTitle("");
        packetRateChart.setLegendVisible(false);
        packetRateChart.setCreateSymbols(false);
        packetRateChart.setStyle(
                "-fx-background-color: #2d2d2d;" +
                        "-fx-border-color: #444444;" +
                        "-fx-border-width: 1px;");
        packetRateChart.setPrefHeight(250);

        // Create series
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Packet Rate");
        packetRateData = series.getData();
        packetRateChart.getData().add(series);

        // Initialize with zeros
        for (int i = 0; i <= 60; i++) {
            packetRateData.add(new XYChart.Data<>(i, 0));
        }

        panel.getChildren().add(packetRateChart);
        return panel;
    }

    /**
     * Creates risk distribution chart panel
     */
    private VBox createRiskDistributionChartPanel() {
        VBox panel = createPanel("Risk Distribution", 650);

        // Create bar chart
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Risk Level");
        xAxis.setStyle("-fx-tick-label-fill: #888888;");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Count");
        yAxis.setStyle("-fx-tick-label-fill: #888888;");

        riskDistributionChart = new BarChart<>(xAxis, yAxis);
        riskDistributionChart.setTitle("");
        riskDistributionChart.setLegendVisible(false);
        riskDistributionChart.setStyle(
                "-fx-background-color: #2d2d2d;" +
                        "-fx-border-color: #444444;" +
                        "-fx-border-width: 1px;");
        riskDistributionChart.setPrefHeight(250);

        // Create series
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Low (0-29)", 0));
        series.getData().add(new XYChart.Data<>("Moderate (30-49)", 0));
        series.getData().add(new XYChart.Data<>("High (50-69)", 0));
        series.getData().add(new XYChart.Data<>("Critical (70-100)", 0));
        riskDistributionChart.getData().add(series);

        panel.getChildren().add(riskDistributionChart);
        return panel;
    }

    /**
     * Creates traffic classification pie chart panel
     */
    private VBox createTrafficClassificationPanel() {
        VBox panel = createPanel("Traffic Classification", 1300);

        trafficClassificationChart = new PieChart();
        trafficClassificationChart.setTitle("");
        trafficClassificationChart.setLegendSide(Side.RIGHT);
        trafficClassificationChart.setStyle(
                "-fx-background-color: #2d2d2d;" +
                        "-fx-border-color: #444444;" +
                        "-fx-border-width: 1px;");
        trafficClassificationChart.setPrefHeight(200);

        // Initialize data
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList(
                new PieChart.Data("Trusted", 0),
                new PieChart.Data("Benign Noise", 0),
                new PieChart.Data("Suspicious", 0),
                new PieChart.Data("Confirmed Threat", 0));
        trafficClassificationChart.setData(pieData);

        panel.getChildren().add(trafficClassificationChart);
        return panel;
    }

    /**
     * Creates control buttons
     */
    private HBox createControlButtons() {
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 0, 0));

        startButton = createButton("Start Monitoring", "#0078d4");
        startButton.setOnAction(e -> handleStartMonitoring());

        stopButton = createButton("Stop Monitoring", "#f44336");
        stopButton.setDisable(true);
        stopButton.setOnAction(e -> handleStopMonitoring());

        mockEventButton = createButton("Add Mock Event", "#444444");
        mockEventButton.setOnAction(e -> {
            controller.addMockEvent();
            updateCharts();
        });

        buttons.getChildren().addAll(startButton, stopButton, mockEventButton);
        return buttons;
    }

    /**
     * Creates a panel with title
     */
    private VBox createPanel(String title, int width) {
        VBox panel = new VBox(15);
        panel.setStyle(
                "-fx-background-color: #2d2d2d;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 20px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 2);");
        panel.setPrefWidth(width);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #444444;");

        panel.getChildren().addAll(titleLabel, separator);
        return panel;
    }

    /**
     * Creates an info row
     */
    private HBox createInfoRow(String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelWidget = new Label(label);
        labelWidget.setFont(Font.font("System", 13));
        labelWidget.setStyle("-fx-text-fill: #888888;");
        labelWidget.setPrefWidth(120);

        Label valueWidget = new Label(value);
        valueWidget.setFont(Font.font("System", FontWeight.BOLD, 13));
        valueWidget.setStyle("-fx-text-fill: #ffffff;");

        row.getChildren().addAll(labelWidget, valueWidget);
        return row;
    }

    /**
     * Creates a statistics row with color
     */
    private HBox createStatRow(String label, String value, String color) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelWidget = new Label(label);
        labelWidget.setFont(Font.font("System", 13));
        labelWidget.setStyle("-fx-text-fill: #888888;");
        labelWidget.setPrefWidth(120);

        Label valueWidget = new Label(value);
        valueWidget.setFont(Font.font("System", FontWeight.BOLD, 18));
        valueWidget.setStyle("-fx-text-fill: " + color + ";");

        row.getChildren().addAll(labelWidget, valueWidget);
        return row;
    }

    /**
     * Creates a button
     */
    private Button createButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(180);
        button.setPrefHeight(40);
        button.setStyle(
                "-fx-background-color: " + color + ";" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-cursor: hand;");
        return button;
    }

    /**
     * Handles start monitoring
     */
    private void handleStartMonitoring() {
        controller.startMonitoring(0);
        startButton.setDisable(true);
        stopButton.setDisable(false);
    }

    /**
     * Handles stop monitoring
     */
    private void handleStopMonitoring() {
        controller.stopMonitoring();
        startButton.setDisable(false);
        stopButton.setDisable(true);
    }

    /**
     * Starts chart updates
     */
    private void startChartUpdates() {
        javafx.animation.Timeline timeline = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.seconds(1), e -> updateCharts()));
        timeline.setCycleCount(javafx.animation.Animation.INDEFINITE);
        timeline.play();
    }

    /**
     * Updates all charts with real backend data
     */
    private void updateCharts() {
        // Get real packet rate from backend
        chartTimeCounter++;
        int packetRate = controller.getBackendBridge().getPacketRate();

        // Update packet rate chart with real data
        packetRateData.remove(0);
        packetRateData.add(new XYChart.Data<>(chartTimeCounter, packetRate));

        // Get real security events from backend
        List<UiSecurityEvent> events = controller.getBackendBridge().getSecurityEvents();

        // Calculate real statistics from events
        int totalEvents = events.size();
        int safeEvents = 0;
        int suspiciousEvents = 0;
        int criticalEvents = 0;

        // Count events by classification
        int trustedCount = 0;
        int benignCount = 0;
        int suspiciousCount = 0;
        int threatCount = 0;

        // Count events by risk level
        int lowRisk = 0;
        int moderateRisk = 0;
        int highRisk = 0;
        int criticalRisk = 0;

        for (UiSecurityEvent event : events) {
            // Classification counts
            String classification = event.getClassification();
            if (classification != null) {
                switch (classification) {
                    case "TRUSTED":
                        trustedCount++;
                        safeEvents++;
                        break;
                    case "BENIGN_NOISE":
                        benignCount++;
                        safeEvents++;
                        break;
                    case "SUSPICIOUS":
                        suspiciousCount++;
                        suspiciousEvents++;
                        break;
                    case "CONFIRMED_THREAT":
                        threatCount++;
                        criticalEvents++;
                        break;
                }
            }

            // Risk level counts
            int riskScore = event.getRiskScore();
            if (riskScore < 30) {
                lowRisk++;
            } else if (riskScore < 50) {
                moderateRisk++;
            } else if (riskScore < 70) {
                highRisk++;
            } else {
                criticalRisk++;
            }
        }

        // Update statistics labels with real data
        totalEventsLabel.setText(String.valueOf(totalEvents));
        safeEventsLabel.setText(String.valueOf(safeEvents));
        suspiciousEventsLabel.setText(String.valueOf(suspiciousEvents));
        criticalEventsLabel.setText(String.valueOf(criticalEvents));

        // Update risk distribution chart with real data
        XYChart.Series<String, Number> riskSeries = riskDistributionChart.getData().get(0);
        riskSeries.getData().get(0).setYValue(lowRisk);
        riskSeries.getData().get(1).setYValue(moderateRisk);
        riskSeries.getData().get(2).setYValue(highRisk);
        riskSeries.getData().get(3).setYValue(criticalRisk);

        // Update traffic classification chart with real data
        trafficClassificationChart.getData().get(0).setPieValue(trustedCount);
        trafficClassificationChart.getData().get(1).setPieValue(benignCount);
        trafficClassificationChart.getData().get(2).setPieValue(suspiciousCount);
        trafficClassificationChart.getData().get(3).setPieValue(threatCount);
    }

    /**
     * Gets the scene
     */
    public Scene getScene() {
        return scene;
    }
}
