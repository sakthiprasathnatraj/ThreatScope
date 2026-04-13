package com.threatscope.ui.view;

import com.threatscope.ui.controller.DashboardController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Dashboard View - Full Implementation
 * 
 * Main dashboard for ThreatScope v2.0
 * 
 * Features:
 * - Top bar with system state badge
 * - System overview panel (interface, status, packet rate)
 * - Latest security observation panel
 * - Quick status message
 * - Control buttons (start/stop monitoring, add mock event)
 * 
 * Layout:
 * ┌─────────────────────────────────────────┐
 * │ Top Bar (Title + State Badge) │
 * ├─────────────────────────────────────────┤
 * │ │
 * │ ┌─────────────┐ ┌─────────────────┐ │
 * │ │ System │ │ Latest │ │
 * │ │ Overview │ │ Security │ │
 * │ │ │ │ Observation │ │
 * │ └─────────────┘ └─────────────────┘ │
 * │ │
 * │ ┌─────────────────────────────────┐ │
 * │ │ Quick Status │ │
 * │ └─────────────────────────────────┘ │
 * │ │
 * │ [Start Monitoring] [Add Mock Event] │
 * │ │
 * └─────────────────────────────────────────┘
 */
public class DashboardView {

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
    private Button startButton;
    private Button mockEventButton;

    public DashboardView(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.controller = new DashboardController();

        // Create UI
        BorderPane root = createDashboardUI();

        // Create scene
        this.scene = new Scene(root, 1280, 800);

        // Initialize controller with UI components
        controller.initialize(
                stateBadgeLabel,
                interfaceLabel,
                monitoringStatusLabel,
                packetRateLabel,
                latestObservationLabel,
                riskScoreLabel,
                confidenceLabel,
                quickStatusLabel);
    }

    /**
     * Creates the full dashboard UI
     */
    private BorderPane createDashboardUI() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #1e1e1e;");

        // Top bar
        HBox topBar = createTopBar();
        root.setTop(topBar);

        // Center content
        VBox center = createCenterContent();
        root.setCenter(center);

        return root;
    }

    /**
     * Creates top bar with title and state badge
     */
    private HBox createTopBar() {
        HBox topBar = new HBox(20);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(16, 24, 16, 24));
        topBar.setStyle(
                "-fx-background-color: #252525;" +
                        "-fx-border-color: #333333;" +
                        "-fx-border-width: 0 0 1px 0;");

        // App title
        Label titleLabel = new Label("ThreatScope");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        // Version
        Label versionLabel = new Label("v2.0 Professional");
        versionLabel.setFont(Font.font("System", 12));
        versionLabel.setStyle("-fx-text-fill: #888888;");

        // Spacer
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // System state badge
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
     * Creates center content with panels
     */
    private VBox createCenterContent() {
        VBox center = new VBox(20);
        center.setPadding(new Insets(30));
        center.setAlignment(Pos.TOP_CENTER);

        // Panels row
        HBox panelsRow = new HBox(20);
        panelsRow.setAlignment(Pos.TOP_CENTER);
        HBox.setHgrow(panelsRow, Priority.ALWAYS);

        // System overview panel
        VBox systemPanel = createSystemOverviewPanel();
        HBox.setHgrow(systemPanel, Priority.ALWAYS);

        // Latest observation panel
        VBox observationPanel = createLatestObservationPanel();
        HBox.setHgrow(observationPanel, Priority.ALWAYS);

        panelsRow.getChildren().addAll(systemPanel, observationPanel);

        // Quick status panel
        VBox statusPanel = createQuickStatusPanel();

        // Control buttons
        HBox controlButtons = createControlButtons();

        center.getChildren().addAll(panelsRow, statusPanel, controlButtons);

        return center;
    }

    /**
     * Creates system overview panel
     */
    private VBox createSystemOverviewPanel() {
        VBox panel = new VBox(15);
        panel.setStyle(
                "-fx-background-color: #2d2d2d;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 20px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 2);");
        panel.setPrefWidth(400);

        // Panel title
        Label titleLabel = new Label("System Overview");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        // Separator
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #444444;");

        // Network interface
        HBox interfaceRow = createInfoRow("Network Interface:", "Not selected");
        interfaceLabel = (Label) interfaceRow.getChildren().get(1);

        // Monitoring status
        HBox statusRow = createInfoRow("Monitoring Status:", "○ Stopped");
        monitoringStatusLabel = (Label) statusRow.getChildren().get(1);

        // Packet rate
        HBox rateRow = createInfoRow("Packet Rate:", "0 packets/sec");
        packetRateLabel = (Label) rateRow.getChildren().get(1);

        panel.getChildren().addAll(
                titleLabel,
                separator,
                interfaceRow,
                statusRow,
                rateRow);

        return panel;
    }

    /**
     * Creates latest security observation panel
     */
    private VBox createLatestObservationPanel() {
        VBox panel = new VBox(15);
        panel.setStyle(
                "-fx-background-color: #2d2d2d;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 20px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 2);");
        panel.setPrefWidth(600);

        // Panel title
        Label titleLabel = new Label("Latest Security Observation");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        // Separator
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #444444;");

        // Observation text
        latestObservationLabel = new Label("No security observations yet. Start monitoring to begin.");
        latestObservationLabel.setFont(Font.font("System", 13));
        latestObservationLabel.setStyle("-fx-text-fill: #cccccc;");
        latestObservationLabel.setWrapText(true);
        latestObservationLabel.setMaxWidth(560);

        // Risk score
        riskScoreLabel = new Label("Risk: 0/100");
        riskScoreLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        riskScoreLabel.setStyle("-fx-text-fill: #4caf50;");

        // Confidence
        confidenceLabel = new Label("Confidence: N/A");
        confidenceLabel.setFont(Font.font("System", 13));
        confidenceLabel.setStyle("-fx-text-fill: #888888;");

        panel.getChildren().addAll(
                titleLabel,
                separator,
                latestObservationLabel,
                riskScoreLabel,
                confidenceLabel);

        return panel;
    }

    /**
     * Creates quick status panel
     */
    private VBox createQuickStatusPanel() {
        VBox panel = new VBox(10);
        panel.setStyle(
                "-fx-background-color: #2d2d2d;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 16px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 2);");
        panel.setMaxWidth(1020);

        // Quick status label
        quickStatusLabel = new Label("Your system is currently SAFE. No threats detected.");
        quickStatusLabel.setFont(Font.font("System", 13));
        quickStatusLabel.setStyle("-fx-text-fill: #cccccc;");
        quickStatusLabel.setWrapText(true);

        panel.getChildren().add(quickStatusLabel);

        return panel;
    }

    /**
     * Creates control buttons
     */
    private HBox createControlButtons() {
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER);
        buttons.setPadding(new Insets(10, 0, 0, 0));

        // Start monitoring button
        startButton = new Button("Start Monitoring");
        startButton.setPrefWidth(180);
        startButton.setPrefHeight(40);
        startButton.setStyle(
                "-fx-background-color: #0078d4;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-cursor: hand;");
        startButton.setOnAction(e -> handleStartMonitoring());

        // Add mock event button (for testing)
        mockEventButton = new Button("Add Mock Event (Test)");
        mockEventButton.setPrefWidth(180);
        mockEventButton.setPrefHeight(40);
        mockEventButton.setStyle(
                "-fx-background-color: #444444;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-cursor: hand;");
        mockEventButton.setOnAction(e -> controller.addMockEvent());

        buttons.getChildren().addAll(startButton, mockEventButton);

        return buttons;
    }

    /**
     * Creates an info row (label + value)
     */
    private HBox createInfoRow(String label, String value) {
        HBox row = new HBox(10);
        row.setAlignment(Pos.CENTER_LEFT);

        Label labelWidget = new Label(label);
        labelWidget.setFont(Font.font("System", 13));
        labelWidget.setStyle("-fx-text-fill: #888888;");
        labelWidget.setPrefWidth(150);

        Label valueWidget = new Label(value);
        valueWidget.setFont(Font.font("System", FontWeight.BOLD, 13));
        valueWidget.setStyle("-fx-text-fill: #ffffff;");

        row.getChildren().addAll(labelWidget, valueWidget);

        return row;
    }

    /**
     * Handles start monitoring button
     */
    private void handleStartMonitoring() {
        // For now, start with interface 0
        // TODO: Add interface selection dialog
        controller.startMonitoring(0);

        startButton.setText("Stop Monitoring");
        startButton.setStyle(
                "-fx-background-color: #f44336;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-cursor: hand;");
        startButton.setOnAction(e -> handleStopMonitoring());
    }

    /**
     * Handles stop monitoring button
     */
    private void handleStopMonitoring() {
        controller.stopMonitoring();

        startButton.setText("Start Monitoring");
        startButton.setStyle(
                "-fx-background-color: #0078d4;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-cursor: hand;");
        startButton.setOnAction(e -> handleStartMonitoring());
    }

    /**
     * Gets the scene
     */
    public Scene getScene() {
        return scene;
    }
}
