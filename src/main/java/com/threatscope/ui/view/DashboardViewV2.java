package com.threatscope.ui.view;

import com.threatscope.ui.controller.DashboardControllerV2;
import com.threatscope.ui.model.UiSecurityEvent;
import com.threatscope.ui.model.RawPacketData;
import com.threatscope.core.model.TopTalker; // Phase 4 Model
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * ThreatScope v2.0 - Professional SOC Dashboard (Scrollable Version)
 * 
 * REVISED LAYOUT - Vertical scrollable design for maximum visibility
 * All data clearly visible with proper sizing
 */
public class DashboardViewV2 {

    private final Stage primaryStage;
    private final Scene scene;
    private final DashboardControllerV2 controller;

    // Header Components
    private Label stateBadgeLabel;
    private Label userLabel;
    private Label logoutLabel;

    // Live Monitoring Panel
    private Label packetsAnalyzedLabel;
    private Label packetsPerSecLabel;
    private Label activeConnectionsLabel;
    private Label lastPacketLabel;
    private Label systemStatusTextLabel;

    // Risk Overview Panel
    private ProgressBar riskProgressBar;
    private Label riskPercentageLabel;
    private BarChart<String, Number> riskDistributionChart;

    // Visual Analytics Components (Phase 4)
    private LineChart<String, Number> liveTrafficChart;
    private PieChart protocolPieChart;
    private TableView<TopTalker> topTalkersTable;

    // Timeline Table
    // Timeline Table & Controls
    private TableView<UiSecurityEvent> timelineTable;
    private ComboBox<String> eventFilterCombo;
    private TextField eventSearchField;
    private Button exportEventsButton;

    // Raw Activity Table (Expert Mode)
    private VBox rawActivitySection;
    private TableView<RawPacketData> rawActivityTable;

    // Explanation Panel
    private TitledPane explanationPane;
    private Label whatHappenedLabel;
    private Label whyMattersLabel;
    private Label howItWorksLabel;
    private Label technicalDetailsLabel;
    private Label classificationLabel;
    private Label riskLevelLabel;
    private Label confidenceLabel;
    private Label recommendedActionLabel;
    private Label reassuranceLabel;
    private Label statusLabel; // New Status Label

    // Control Bar
    private Button monitoringToggleButton;
    private Button testEventButton;
    private Button settingsButton; // New Settings Button
    private ToggleButton expertModeToggle;

    // Action Buttons (Phase 4 - Interactive)
    private Button acknowledgeButton;
    private Button falsePositiveButton;
    private Button resolveButton;

    public DashboardViewV2(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new DashboardControllerV2();

        // Create UI
        BorderPane root = createDashboardUI();

        // Create scene with larger size for better visibility
        this.scene = new Scene(root, 1600, 1000);

        // Initialize controller
        controller.initialize(this);
    }

    /**
     * Creates the complete dashboard UI with scrollable content
     */
    private BorderPane createDashboardUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #0f0f0f;");

        // TOP: Header Bar
        HBox header = createHeaderBar();
        root.setTop(header);

        // CENTER: Scrollable content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false); // Allow vertical scrolling
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER); // No horizontal scroll
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED); // Vertical scroll when needed
        scrollPane.setStyle("-fx-background: #0f0f0f; -fx-background-color: #0f0f0f;");
        scrollPane.setPannable(true); // Allow mouse drag scrolling

        VBox mainContent = createMainContent();
        scrollPane.setContent(mainContent);

        // Make scrollpane grow to fill available space
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        root.setCenter(scrollPane);

        // BOTTOM: Control Bar
        HBox controlBar = createControlBar();
        root.setBottom(controlBar);

        return root;
    }

    /**
     * TOP SECTION - Header Bar
     */
    private HBox createHeaderBar() {
        HBox header = new HBox();
        header.setPrefHeight(70);
        header.setMinHeight(70);
        header.setMaxHeight(70);
        header.setPadding(new Insets(15, 30, 15, 30));
        header.setAlignment(Pos.CENTER_LEFT);
        header.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-border-color: #2a2a2a;" +
                        "-fx-border-width: 0 0 1px 0;");

        // Left side: Logo and version
        VBox leftBox = new VBox(2);
        leftBox.setAlignment(Pos.CENTER_LEFT);

        Label logoLabel = new Label("ThreatScope");
        logoLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 22));
        logoLabel.setStyle("-fx-text-fill: #00d4ff;");

        Label versionLabel = new Label("v2.0 Professional");
        versionLabel.setFont(Font.font("Segoe UI", 11));
        versionLabel.setStyle("-fx-text-fill: #666666;");

        leftBox.getChildren().addAll(logoLabel, versionLabel);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right side: State badge and user card
        HBox rightBox = new HBox(20);
        rightBox.setAlignment(Pos.CENTER_RIGHT);

        // System state badge
        stateBadgeLabel = new Label("SYSTEM: SAFE");
        stateBadgeLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        stateBadgeLabel.setStyle(
                "-fx-background-color: #1b4d3e;" +
                        "-fx-text-fill: #4ade80;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-background-radius: 6px;");

        // User card
        HBox userCard = new HBox(10);
        userCard.setAlignment(Pos.CENTER);
        userCard.setStyle(
                "-fx-background-color: #252525;" +
                        "-fx-padding: 8px 16px;" +
                        "-fx-background-radius: 6px;");

        userLabel = new Label("Admin");
        userLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 12));
        userLabel.setStyle("-fx-text-fill: #ffffff;");

        logoutLabel = new Label("Logout");
        logoutLabel.setFont(Font.font("Segoe UI", 11));
        logoutLabel.setStyle("-fx-text-fill: #888888; -fx-cursor: hand;");

        userCard.getChildren().addAll(userLabel, new Label("|") {
            {
                setStyle("-fx-text-fill: #444444;");
            }
        }, logoutLabel);

        rightBox.getChildren().addAll(stateBadgeLabel, userCard);

        header.getChildren().addAll(leftBox, spacer, rightBox);

        return header;
    }

    /**
     * MAIN CONTENT - Scrollable vertical layout
     */
    private VBox createMainContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(25));
        content.setStyle("-fx-background-color: #0f0f0f;");

        // Section 1: Live Monitoring Stats
        VBox statsSection = createStatsSection();

        // Section 2: System Status
        VBox statusSection = createStatusSection();

        // Section 3: Risk Overview
        VBox riskSection = createRiskSection();

        // Section 3.5: Visual Analytics (Phase 4)
        VBox visualAnalyticsSection = createVisualAnalyticsSection();

        // Section 4: Security Event Timeline
        VBox timelineSection = createTimelineSection();

        // Section 5: Raw Activity (Expert Mode - Hidden by default)
        rawActivitySection = createRawActivitySection();
        rawActivitySection.setVisible(false);
        rawActivitySection.setManaged(false);

        // Section 6: Explanation Panel
        VBox explanationSection = createExplanationSection();

        content.getChildren().addAll(
                statsSection,
                statusSection,
                riskSection,
                visualAnalyticsSection,
                timelineSection,
                rawActivitySection,
                explanationSection);

        return content;
    }

    /**
     * Statistics Section - 4 cards in a row
     */
    private VBox createStatsSection() {
        VBox section = new VBox(15);

        Label titleLabel = new Label("Live Monitoring Statistics");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        HBox statsRow = new HBox(20);
        statsRow.setAlignment(Pos.CENTER);

        // Card 1: Packets Analyzed
        VBox card1 = createStatCard("Packets Analyzed", "0");
        packetsAnalyzedLabel = (Label) ((VBox) card1.getChildren().get(0)).getChildren().get(0);

        // Card 2: Packets/sec
        VBox card2 = createStatCard("Packets/sec", "0");
        packetsPerSecLabel = (Label) ((VBox) card2.getChildren().get(0)).getChildren().get(0);

        // Card 3: Active Connections
        VBox card3 = createStatCard("Active Connections", "0");
        activeConnectionsLabel = (Label) ((VBox) card3.getChildren().get(0)).getChildren().get(0);

        // Card 4: Last Packet
        VBox card4 = createStatCard("Last Packet", "N/A");
        lastPacketLabel = (Label) ((VBox) card4.getChildren().get(0)).getChildren().get(0);

        statsRow.getChildren().addAll(card1, card2, card3, card4);
        HBox.setHgrow(card1, Priority.ALWAYS);
        HBox.setHgrow(card2, Priority.ALWAYS);
        HBox.setHgrow(card3, Priority.ALWAYS);
        HBox.setHgrow(card4, Priority.ALWAYS);

        section.getChildren().addAll(titleLabel, statsRow);
        return section;
    }

    /**
     * Individual Stat Card
     */
    private VBox createStatCard(String label, String value) {
        VBox card = new VBox();
        card.setPrefHeight(120);
        card.setAlignment(Pos.CENTER);
        card.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #2a2a2a;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 20px;");

        VBox content = new VBox(8);
        content.setAlignment(Pos.CENTER);

        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 32));
        valueLabel.setStyle("-fx-text-fill: #00d4ff;");

        Label labelText = new Label(label);
        labelText.setFont(Font.font("Segoe UI", 12));
        labelText.setStyle("-fx-text-fill: #888888;");

        content.getChildren().addAll(valueLabel, labelText);
        card.getChildren().add(content);

        return card;
    }

    /**
     * System Status Section
     */
    private VBox createStatusSection() {
        VBox section = new VBox(15);

        Label titleLabel = new Label("System Status");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        VBox statusCard = new VBox(10);
        statusCard.setPrefHeight(100);
        statusCard.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #2a2a2a;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 20px;");

        systemStatusTextLabel = new Label("Monitoring inactive. Click 'Enable Monitoring' to start.");
        systemStatusTextLabel.setFont(Font.font("Segoe UI", 14));
        systemStatusTextLabel.setStyle("-fx-text-fill: #cccccc;");
        systemStatusTextLabel.setWrapText(true);

        statusCard.getChildren().add(systemStatusTextLabel);
        section.getChildren().addAll(titleLabel, statusCard);

        return section;
    }

    /**
     * Risk Overview Section
     */
    private VBox createRiskSection() {
        VBox section = new VBox(15);

        Label titleLabel = new Label("Risk Overview");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        // Risk Progress Bar
        VBox progressContainer = new VBox(10);
        progressContainer.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #2a2a2a;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 20px;");

        Label riskLabel = new Label("Current Risk Level");
        riskLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        riskLabel.setStyle("-fx-text-fill: #cccccc;");

        StackPane progressStack = new StackPane();

        riskProgressBar = new ProgressBar(0.0);
        riskProgressBar.setPrefHeight(40);
        riskProgressBar.setMaxWidth(Double.MAX_VALUE);
        riskProgressBar.setStyle("-fx-accent: #4ade80;");

        riskPercentageLabel = new Label("0%");
        riskPercentageLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 16));
        riskPercentageLabel.setStyle("-fx-text-fill: #ffffff;");

        progressStack.getChildren().addAll(riskProgressBar, riskPercentageLabel);
        progressContainer.getChildren().addAll(riskLabel, progressStack);

        // Risk Distribution Chart
        VBox chartContainer = new VBox(10);
        chartContainer.setPrefHeight(250);
        chartContainer.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #2a2a2a;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 20px;");

        Label chartLabel = new Label("Risk Distribution");
        chartLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        chartLabel.setStyle("-fx-text-fill: #cccccc;");

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setTickLabelFill(javafx.scene.paint.Color.web("#888888"));
        yAxis.setTickLabelFill(javafx.scene.paint.Color.web("#888888"));

        riskDistributionChart = new BarChart<>(xAxis, yAxis);
        riskDistributionChart.setLegendVisible(false);
        riskDistributionChart.setPrefHeight(180);
        riskDistributionChart.setStyle("-fx-background-color: transparent;");

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Low", 0));
        series.getData().add(new XYChart.Data<>("Medium", 0));
        series.getData().add(new XYChart.Data<>("High", 0));
        riskDistributionChart.getData().add(series);

        chartContainer.getChildren().addAll(chartLabel, riskDistributionChart);

        section.getChildren().addAll(titleLabel, progressContainer, chartContainer);
        return section;
    }

    /**
     * Visual Analytics Section (Phase 4)
     * Includes Live Traffic Chart, Top Talkers, and Protocol Distribution
     */
    private VBox createVisualAnalyticsSection() {
        VBox section = new VBox(15);

        Label titleLabel = new Label("Visual Analytics");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        // Container for analytics
        VBox analyticsContainer = new VBox(20);
        analyticsContainer.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #2a2a2a;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 20px;");

        // 1. Live Traffic Chart
        VBox chartBox = new VBox(10);
        Label chartLabel = new Label("Live Traffic (Packets/Second)");
        chartLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        chartLabel.setStyle("-fx-text-fill: #cccccc;");

        CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Time");
        xAxis.setTickLabelFill(javafx.scene.paint.Color.web("#888888"));

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Packets/Sec");
        yAxis.setTickLabelFill(javafx.scene.paint.Color.web("#888888"));

        liveTrafficChart = new LineChart<>(xAxis, yAxis);
        liveTrafficChart.setPrefHeight(250);
        liveTrafficChart.setStyle("-fx-background-color: transparent;");
        liveTrafficChart.setCreateSymbols(false); // smoother lines
        liveTrafficChart.setAnimated(false); // better performance for real-time
        liveTrafficChart.setLegendVisible(true);

        chartBox.getChildren().addAll(chartLabel, liveTrafficChart);

        // 2. Bottom Row: Top Talkers + Pie Chart
        HBox bottomRow = new HBox(20);
        bottomRow.setAlignment(Pos.CENTER_LEFT);

        // Top Talkers Table
        VBox tableBox = new VBox(10);
        Label tableLabel = new Label("Top Talkers (Active IPs)");
        tableLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        tableLabel.setStyle("-fx-text-fill: #cccccc;");

        topTalkersTable = new TableView<>();
        topTalkersTable.setPrefHeight(200);
        topTalkersTable.setPrefWidth(450);
        topTalkersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); // Fix extra column
        topTalkersTable.setStyle("-fx-background-color: #222222; -fx-text-fill: white;");

        TableColumn<TopTalker, String> ipCol = new TableColumn<>("IP Address");
        ipCol.setPrefWidth(120);
        ipCol.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getIpAddress()));

        TableColumn<TopTalker, String> packetsCol = new TableColumn<>("Packets");
        packetsCol.setPrefWidth(80);
        packetsCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(data.getValue().getPacketCount())));

        TableColumn<TopTalker, String> protocolCol = new TableColumn<>("Protocol");
        protocolCol.setPrefWidth(80);
        protocolCol.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPrimaryProtocol()));

        TableColumn<TopTalker, String> riskCol = new TableColumn<>("Risk");
        riskCol.setPrefWidth(100);
        riskCol.setCellValueFactory(
                data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getRiskLevel()));

        topTalkersTable.getColumns().addAll(ipCol, packetsCol, protocolCol, riskCol);
        tableBox.getChildren().addAll(tableLabel, topTalkersTable);

        // Protocol Pie Chart
        VBox pieBox = new VBox(10);
        Label pieLabel = new Label("Protocol Distribution");
        pieLabel.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        pieLabel.setStyle("-fx-text-fill: #cccccc;");

        protocolPieChart = new PieChart();
        protocolPieChart.setPrefHeight(200);
        protocolPieChart.setPrefWidth(300);
        protocolPieChart.setLegendVisible(true);
        protocolPieChart.setLegendSide(javafx.geometry.Side.RIGHT);

        pieBox.getChildren().addAll(pieLabel, protocolPieChart);

        bottomRow.getChildren().addAll(tableBox, pieBox);

        analyticsContainer.getChildren().addAll(chartBox, bottomRow);
        section.getChildren().addAll(titleLabel, analyticsContainer);

        return section;
    }

    /**
     * Timeline Section
     */
    private VBox createTimelineSection() {
        VBox section = new VBox(15);

        // Header with Controls
        HBox headerRow = new HBox(15);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label titleLabel = new Label("Security Event Timeline");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Filter Combo
        eventFilterCombo = new ComboBox<>();
        eventFilterCombo.getItems().addAll("All Events", "PORT_SCAN", "DDOS_ATTACK", "BRUTE_FORCE",
                "SUSPICIOUS_PATTERN", "BACKDOOR_ATTEMPT");
        eventFilterCombo.setValue("All Events");
        eventFilterCombo.setPrefWidth(150);
        eventFilterCombo.setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-base: #333333;");

        // Search Field
        eventSearchField = new TextField();
        eventSearchField.setPromptText("Search events...");
        eventSearchField.setPrefWidth(200);
        eventSearchField
                .setStyle("-fx-background-color: #333333; -fx-text-fill: white; -fx-prompt-text-fill: #888888;");

        // Export Button
        exportEventsButton = new Button("Export CSV");
        exportEventsButton.setStyle(
                "-fx-background-color: #2563eb;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-cursor: hand;" +
                        "-fx-padding: 5 15;");

        headerRow.getChildren().addAll(titleLabel, spacer, eventFilterCombo, eventSearchField, exportEventsButton);

        VBox tableContainer = new VBox();
        tableContainer.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #2a2a2a;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 20px;");

        timelineTable = new TableView<>();
        timelineTable.setPrefHeight(400);
        timelineTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        timelineTable.setStyle(
                "-fx-background-color: #252525;" +
                        "-fx-control-inner-background: #252525;" +
                        "-fx-table-cell-border-color: #333333;" +
                        "-fx-text-fill: #ffffff;");

        // Columns
        TableColumn<UiSecurityEvent, String> timeCol = new TableColumn<>("Time");
        timeCol.setPrefWidth(120);
        timeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFormattedTimestamp()));

        TableColumn<UiSecurityEvent, String> threatCol = new TableColumn<>("Threat");
        threatCol.setPrefWidth(150);
        threatCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getThreatType()));

        TableColumn<UiSecurityEvent, String> riskCol = new TableColumn<>("Risk");
        riskCol.setPrefWidth(80);
        riskCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                String.valueOf(cellData.getValue().getRiskScore())));

        TableColumn<UiSecurityEvent, String> classCol = new TableColumn<>("Classification");
        classCol.setPrefWidth(150);
        classCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getClassification()));

        TableColumn<UiSecurityEvent, String> summaryCol = new TableColumn<>("Summary");
        summaryCol.setPrefWidth(300);
        summaryCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSummary()));

        TableColumn<UiSecurityEvent, String> statusCol = new TableColumn<>("Status");
        statusCol.setPrefWidth(120);
        statusCol.setCellValueFactory(cellData -> cellData.getValue().statusProperty().asString());

        timelineTable.getColumns().addAll(timeCol, threatCol, riskCol, classCol, summaryCol, statusCol);

        Label placeholder = new Label("No security events detected");
        placeholder.setFont(Font.font("Segoe UI", 14));
        placeholder.setStyle("-fx-text-fill: #666666;");
        timelineTable.setPlaceholder(placeholder);

        tableContainer.getChildren().add(timelineTable);
        section.getChildren().addAll(headerRow, tableContainer); // Updated to include controls

        return section;
    }

    /**
     * Raw Activity Section (Expert Mode)
     */
    private VBox createRawActivitySection() {
        VBox section = new VBox(15);

        Label titleLabel = new Label("Raw Packet Activity (Expert Mode)");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #00d4ff;");

        VBox tableContainer = new VBox();
        tableContainer.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #2a2a2a;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 20px;");

        rawActivityTable = new TableView<>();
        rawActivityTable.setPrefHeight(400);
        rawActivityTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rawActivityTable.setStyle(
                "-fx-background-color: #252525;" +
                        "-fx-control-inner-background: #252525;" +
                        "-fx-table-cell-border-color: #333333;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-family: 'Consolas', 'Courier New', monospace;");

        // Columns for raw packet data
        TableColumn<RawPacketData, String> timeCol = new TableColumn<>("Timestamp");
        timeCol.setPrefWidth(120);
        timeCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(
                cellData.getValue().getFormattedTimestamp()));

        TableColumn<RawPacketData, String> sourceIpCol = new TableColumn<>("Source IP");
        sourceIpCol.setPrefWidth(130);
        sourceIpCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSourceIp()));

        TableColumn<RawPacketData, String> destIpCol = new TableColumn<>("Dest IP");
        destIpCol.setPrefWidth(130);
        destIpCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDestinationIp()));

        TableColumn<RawPacketData, String> sourcePortCol = new TableColumn<>("Src Port");
        sourcePortCol.setPrefWidth(70);
        sourcePortCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(cellData.getValue().getSourcePort())));

        TableColumn<RawPacketData, String> destPortCol = new TableColumn<>("Dst Port");
        destPortCol.setPrefWidth(70);
        destPortCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(
                        String.valueOf(cellData.getValue().getDestinationPort())));

        TableColumn<RawPacketData, String> protocolCol = new TableColumn<>("Protocol");
        protocolCol.setPrefWidth(70);
        protocolCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getProtocol()));

        TableColumn<RawPacketData, String> packetSizeCol = new TableColumn<>("Size");
        packetSizeCol.setPrefWidth(60);
        packetSizeCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(
                        cellData.getValue().getPacketSize() + " B"));

        TableColumn<RawPacketData, String> flagsCol = new TableColumn<>("TCP Flags");
        flagsCol.setPrefWidth(100);
        flagsCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getFlags()));

        TableColumn<RawPacketData, String> payloadCol = new TableColumn<>("Payload (Hex)");
        payloadCol.setPrefWidth(250);
        payloadCol.setCellValueFactory(
                cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPayloadPreview()));

        rawActivityTable.getColumns().addAll(timeCol, sourceIpCol, destIpCol, sourcePortCol,
                destPortCol, protocolCol, packetSizeCol, flagsCol, payloadCol);

        Label placeholder = new Label("No raw packet data available. Enable monitoring to capture packets.");
        placeholder.setFont(Font.font("Segoe UI", 14));
        placeholder.setStyle("-fx-text-fill: #666666;");
        rawActivityTable.setPlaceholder(placeholder);

        tableContainer.getChildren().add(rawActivityTable);
        section.getChildren().addAll(titleLabel, tableContainer);

        return section;
    }

    /**
     * Explanation Section
     */
    private VBox createExplanationSection() {
        VBox section = new VBox(15);

        Label titleLabel = new Label("Security Event Explanation");
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        VBox explanationContainer = new VBox(15);
        explanationContainer.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-border-color: #2a2a2a;" +
                        "-fx-border-width: 1px;" +
                        "-fx-border-radius: 8px;" +
                        "-fx-padding: 20px;");

        // Create explanation sections
        VBox statusBox = createExplanationSection("CURRENT STATUS", "N/A");
        statusLabel = (Label) ((VBox) statusBox.getChildren().get(0)).getChildren().get(1);
        statusLabel.setStyle("-fx-text-fill: #ffffff; -fx-font-weight: bold; -fx-font-size: 14px;");

        VBox whatHappenedBox = createExplanationSection("WHAT HAPPENED", "No event selected");
        whatHappenedLabel = (Label) ((VBox) whatHappenedBox.getChildren().get(0)).getChildren().get(1);

        VBox whyMattersBox = createExplanationSection("WHY THIS MATTERS", "N/A");
        whyMattersLabel = (Label) ((VBox) whyMattersBox.getChildren().get(0)).getChildren().get(1);

        VBox howItWorksBox = createExplanationSection("HOW IT WORKS", "N/A");
        howItWorksLabel = (Label) ((VBox) howItWorksBox.getChildren().get(0)).getChildren().get(1);

        VBox technicalDetailsBox = createExplanationSection("TECHNICAL DETAILS", "N/A");
        technicalDetailsLabel = (Label) ((VBox) technicalDetailsBox.getChildren().get(0)).getChildren().get(1);

        VBox classificationBox = createExplanationSection("CLASSIFICATION", "N/A");
        classificationLabel = (Label) ((VBox) classificationBox.getChildren().get(0)).getChildren().get(1);

        VBox riskLevelBox = createExplanationSection("RISK LEVEL", "N/A");
        riskLevelLabel = (Label) ((VBox) riskLevelBox.getChildren().get(0)).getChildren().get(1);

        VBox confidenceBox = createExplanationSection("CONFIDENCE", "N/A");
        confidenceLabel = (Label) ((VBox) confidenceBox.getChildren().get(0)).getChildren().get(1);

        VBox recommendedActionBox = createExplanationSection("RECOMMENDED ACTION", "N/A");
        recommendedActionLabel = (Label) ((VBox) recommendedActionBox.getChildren().get(0)).getChildren().get(1);

        VBox reassuranceBox = createExplanationSection("REASSURANCE", "Your system is currently safe.");
        reassuranceLabel = (Label) ((VBox) reassuranceBox.getChildren().get(0)).getChildren().get(1);

        // Action Buttons Panel
        HBox actionsBox = new HBox(15);
        actionsBox.setAlignment(Pos.CENTER_LEFT);
        actionsBox.setPadding(new Insets(10, 0, 0, 0));

        acknowledgeButton = new Button("Acknowledge");
        acknowledgeButton.setStyle("-fx-background-color: #0078d4; -fx-text-fill: white; -fx-cursor: hand;");

        falsePositiveButton = new Button("Mark False Positive");
        falsePositiveButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white; -fx-cursor: hand;");

        resolveButton = new Button("Resolve");
        resolveButton.setStyle("-fx-background-color: #107c10; -fx-text-fill: white; -fx-cursor: hand;");

        actionsBox.getChildren().addAll(acknowledgeButton, falsePositiveButton, resolveButton);

        explanationContainer.getChildren().addAll(
                statusBox,
                new Separator(),
                whatHappenedBox,
                new Separator(),
                whyMattersBox,
                new Separator(),
                howItWorksBox,
                new Separator(),
                technicalDetailsBox,
                new Separator(),
                classificationBox,
                new Separator(),
                riskLevelBox,
                new Separator(),
                confidenceBox,
                new Separator(),
                recommendedActionBox,
                new Separator(),
                reassuranceBox,
                new Separator(),
                actionsBox);

        section.getChildren().addAll(titleLabel, explanationContainer);
        return section;
    }

    /**
     * Creates an explanation section with title and content
     */
    private VBox createExplanationSection(String title, String content) {
        VBox section = new VBox(5);

        VBox innerBox = new VBox(5);

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Segoe UI", FontWeight.BOLD, 12));
        titleLabel.setStyle("-fx-text-fill: #00d4ff;");

        Label contentLabel = new Label(content);
        contentLabel.setFont(Font.font("Segoe UI", 13));
        contentLabel.setStyle("-fx-text-fill: #cccccc;");
        contentLabel.setWrapText(true);

        innerBox.getChildren().addAll(titleLabel, contentLabel);
        section.getChildren().add(innerBox);

        return section;
    }

    /**
     * BOTTOM SECTION - Control Bar
     */
    private HBox createControlBar() {
        HBox controlBar = new HBox(20);
        controlBar.setPrefHeight(80);
        controlBar.setMinHeight(80);
        controlBar.setMaxHeight(80);
        controlBar.setPadding(new Insets(15, 30, 15, 30));
        controlBar.setAlignment(Pos.CENTER_LEFT);
        controlBar.setStyle(
                "-fx-background-color: #1a1a1a;" +
                        "-fx-border-color: #2a2a2a;" +
                        "-fx-border-width: 1px 0 0 0;");

        // Left side: Control buttons
        HBox leftButtons = new HBox(15);
        leftButtons.setAlignment(Pos.CENTER_LEFT);

        monitoringToggleButton = new Button("Enable Monitoring");
        monitoringToggleButton.setPrefHeight(45);
        monitoringToggleButton.setPrefWidth(200);
        monitoringToggleButton.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        monitoringToggleButton.setStyle(
                "-fx-background-color: #0078d4;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-cursor: hand;");

        testEventButton = new Button("Generate Test Events");
        testEventButton.setPrefHeight(45);
        testEventButton.setPrefWidth(200);
        testEventButton.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        testEventButton.setStyle(
                "-fx-background-color: #444444;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-cursor: hand;");

        settingsButton = new Button("⚙ Settings");
        settingsButton.setPrefHeight(45);
        settingsButton.setPrefWidth(120);
        settingsButton.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        settingsButton.setStyle(
                "-fx-background-color: #333333;" +
                        "-fx-text-fill: #cccccc;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-cursor: hand;");

        leftButtons.getChildren().addAll(monitoringToggleButton, testEventButton, settingsButton);

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Right side: Expert Mode toggle
        HBox rightButtons = new HBox();
        rightButtons.setAlignment(Pos.CENTER_RIGHT);

        expertModeToggle = new ToggleButton("Expert Mode");
        expertModeToggle.setPrefHeight(45);
        expertModeToggle.setPrefWidth(160);
        expertModeToggle.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        expertModeToggle.setStyle(
                "-fx-background-color: #2a2a2a;" +
                        "-fx-text-fill: #888888;" +
                        "-fx-background-radius: 6px;" +
                        "-fx-cursor: hand;");

        rightButtons.getChildren().add(expertModeToggle);

        controlBar.getChildren().addAll(leftButtons, spacer, rightButtons);

        return controlBar;
    }

    /**
     * Gets the scene
     */
    public Scene getScene() {
        return scene;
    }

    // Getters for controller access
    public Label getStateBadgeLabel() {
        return stateBadgeLabel;
    }

    public Label getPacketsAnalyzedLabel() {
        return packetsAnalyzedLabel;
    }

    public Label getPacketsPerSecLabel() {
        return packetsPerSecLabel;
    }

    public Label getActiveConnectionsLabel() {
        return activeConnectionsLabel;
    }

    public Label getLastPacketLabel() {
        return lastPacketLabel;
    }

    public Label getSystemStatusTextLabel() {
        return systemStatusTextLabel;
    }

    public ProgressBar getRiskProgressBar() {
        return riskProgressBar;
    }

    public Label getRiskPercentageLabel() {
        return riskPercentageLabel;
    }

    public BarChart<String, Number> getRiskDistributionChart() {
        return riskDistributionChart;
    }

    public LineChart<String, Number> getLiveTrafficChart() {
        return liveTrafficChart;
    }

    public PieChart getProtocolPieChart() {
        return protocolPieChart;
    }

    public TableView<TopTalker> getTopTalkersTable() {
        return topTalkersTable;
    }

    public TableView<UiSecurityEvent> getTimelineTable() {
        return timelineTable;
    }

    public ComboBox<String> getEventFilterCombo() {
        return eventFilterCombo;
    }

    public TextField getEventSearchField() {
        return eventSearchField;
    }

    public Button getExportEventsButton() {
        return exportEventsButton;
    }

    public Label getWhatHappenedLabel() {
        return whatHappenedLabel;
    }

    public Label getWhyMattersLabel() {
        return whyMattersLabel;
    }

    public Label getHowItWorksLabel() {
        return howItWorksLabel;
    }

    public Label getTechnicalDetailsLabel() {
        return technicalDetailsLabel;
    }

    public Label getClassificationLabel() {
        return classificationLabel;
    }

    public Label getRiskLevelLabel() {
        return riskLevelLabel;
    }

    public Label getConfidenceLabel() {
        return confidenceLabel;
    }

    public Label getRecommendedActionLabel() {
        return recommendedActionLabel;
    }

    public Label getReassuranceLabel() {
        return reassuranceLabel;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public Button getMonitoringToggleButton() {
        return monitoringToggleButton;
    }

    public Button getTestEventButton() {
        return testEventButton;
    }

    public Button getSettingsButton() {
        return settingsButton;
    }

    public ToggleButton getExpertModeToggle() {
        return expertModeToggle;
    }

    public VBox getRawActivitySection() {
        return rawActivitySection;
    }

    public TableView<RawPacketData> getRawActivityTable() {
        return rawActivityTable;
    }

    // Action Button Getters
    public Button getAcknowledgeButton() {
        return acknowledgeButton;
    }

    public Button getFalsePositiveButton() {
        return falsePositiveButton;
    }

    public Button getResolveButton() {
        return resolveButton;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Label getLogoutLabel() {
        return logoutLabel;
    }
}
