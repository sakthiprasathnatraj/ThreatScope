package com.threatscope.ui.view;

import com.threatscope.ui.controller.DashboardControllerProfessional;
import com.threatscope.ui.model.UiSecurityEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;

/**
 * ThreatScope v2.0 - Professional Edition Dashboard
 * 
 * Production-quality SOC-style security monitoring interface
 * 
 * Features:
 * - Live activity indicators (packets/sec, connections, session totals)
 * - Dashboard heartbeat messages (calm system status)
 * - User/session context panel (always visible)
 * - Expert Mode toggle (Simple Mode / Expert Mode)
 * - Security Event Timeline with expandable details
 * - Professional styling (Splunk/Elastic SIEM inspired)
 * - Subtle animations (pulse, fade-in)
 * - No demo feel, production-ready
 */
public class DashboardViewProfessional {

        private final Stage primaryStage;
        private final Scene scene;
        private final DashboardControllerProfessional controller;

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

        // Monitoring Controls
        private Label interfaceLabel;
        private Label monitoringStatusLabel;
        private Button startButton;

        // Expert Mode
        private ToggleButton expertModeToggle;
        private VBox rawActivityPanel;
        private TableView<UiSecurityEvent> rawActivityTable;

        // Event Timeline
        private TableView<UiSecurityEvent> eventTimelineTable;
        private TextArea eventDetailArea;

        // Quick Status
        private Label quickStatusLabel;

        public DashboardViewProfessional(Stage primaryStage) {
                this.primaryStage = primaryStage;
                this.controller = new DashboardControllerProfessional();

                // Create UI
                BorderPane root = createDashboardUI();

                // Create scene
                this.scene = new Scene(root, 1400, 900);

                // Initialize controller with UI components
                controller.initialize(
                                stateBadgeLabel,
                                sessionUserLabel,
                                sessionRoleLabel,
                                sessionStatusLabel,
                                packetsAnalyzedLabel,
                                packetsPerSecLabel,
                                activeConnectionsLabel,
                                lastPacketTimeLabel,
                                heartbeatMessageLabel,
                                interfaceLabel,
                                monitoringStatusLabel,
                                quickStatusLabel,
                                eventTimelineTable,
                                rawActivityTable);
        }

        /**
         * Creates the full professional dashboard UI
         */
        private BorderPane createDashboardUI() {
                BorderPane root = new BorderPane();
                root.setStyle("-fx-background-color: #0d1117;"); // Dark professional background

                // Top bar with session context
                HBox topBar = createTopBar();
                root.setTop(topBar);

                // Center content
                VBox center = createCenterContent();
                root.setCenter(center);

                return root;
        }

        /**
         * Creates top bar with branding and session context
         */
        private HBox createTopBar() {
                HBox topBar = new HBox(20);
                topBar.setAlignment(Pos.CENTER_LEFT);
                topBar.setPadding(new Insets(16, 24, 16, 24));
                topBar.setStyle(
                                "-fx-background-color: #161b22;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 0 0 1px 0;");

                // App title
                Label titleLabel = new Label("ThreatScope");
                titleLabel.setFont(Font.font("System", FontWeight.BOLD, 22));
                titleLabel.setStyle("-fx-text-fill: #58a6ff;");

                // Version
                Label versionLabel = new Label("v2.0 Professional Edition");
                versionLabel.setFont(Font.font("System", 11));
                versionLabel.setStyle("-fx-text-fill: #8b949e;");

                // Spacer
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // System state badge (with pulse animation)
                stateBadgeLabel = new Label("SYSTEM: SAFE");
                stateBadgeLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
                stateBadgeLabel.setStyle(
                                "-fx-background-color: #238636;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-padding: 8px 16px;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(35, 134, 54, 0.4), 8, 0, 0, 0);");
                stateBadgeLabel.setId("stateBadge"); // For CSS animation

                // Session panel
                VBox sessionPanel = createSessionPanel();

                topBar.getChildren().addAll(titleLabel, versionLabel, spacer, stateBadgeLabel, sessionPanel);

                return topBar;
        }

        /**
         * Creates session/user context panel (always visible)
         */
        private VBox createSessionPanel() {
                VBox panel = new VBox(4);
                panel.setAlignment(Pos.CENTER_RIGHT);
                panel.setStyle(
                                "-fx-background-color: #21262d;" +
                                                "-fx-padding: 10px 14px;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 6px;");

                // User
                HBox userRow = new HBox(6);
                userRow.setAlignment(Pos.CENTER_RIGHT);
                Label userIcon = new Label("👤");
                userIcon.setStyle("-fx-font-size: 12px;");
                sessionUserLabel = new Label("Admin");
                sessionUserLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
                sessionUserLabel.setStyle("-fx-text-fill: #c9d1d9;");
                userRow.getChildren().addAll(userIcon, sessionUserLabel);

                // Role
                sessionRoleLabel = new Label("Security Analyst");
                sessionRoleLabel.setFont(Font.font("System", 10));
                sessionRoleLabel.setStyle("-fx-text-fill: #8b949e;");

                // Session status
                HBox statusRow = new HBox(6);
                statusRow.setAlignment(Pos.CENTER_RIGHT);
                Label statusDot = new Label("●");
                statusDot.setStyle("-fx-text-fill: #3fb950; -fx-font-size: 10px;");
                sessionStatusLabel = new Label("Active");
                sessionStatusLabel.setFont(Font.font("System", 10));
                sessionStatusLabel.setStyle("-fx-text-fill: #3fb950;");
                statusRow.getChildren().addAll(statusDot, sessionStatusLabel);

                // Logout button (subtle)
                Button logoutBtn = new Button("Logout");
                logoutBtn.setFont(Font.font("System", 9));
                logoutBtn.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #8b949e;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 3px;" +
                                                "-fx-padding: 2px 8px;" +
                                                "-fx-cursor: hand;");
                logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle(
                                "-fx-background-color: #30363d;" +
                                                "-fx-text-fill: #c9d1d9;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 3px;" +
                                                "-fx-padding: 2px 8px;" +
                                                "-fx-cursor: hand;"));
                logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle(
                                "-fx-background-color: transparent;" +
                                                "-fx-text-fill: #8b949e;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 3px;" +
                                                "-fx-padding: 2px 8px;" +
                                                "-fx-cursor: hand;"));

                panel.getChildren().addAll(userRow, sessionRoleLabel, statusRow, logoutBtn);

                return panel;
        }

        /**
         * Creates center content with all panels
         */
        private VBox createCenterContent() {
                VBox center = new VBox(20);
                center.setPadding(new Insets(24));
                center.setAlignment(Pos.TOP_CENTER);

                // Live activity indicators panel
                HBox liveActivityPanel = createLiveActivityPanel();

                // Heartbeat message panel
                VBox heartbeatPanel = createHeartbeatPanel();

                // Control panel (monitoring controls + expert mode toggle)
                HBox controlPanel = createControlPanel();

                // Event timeline panel
                VBox eventTimelinePanel = createEventTimelinePanel();

                // Raw activity panel (Expert Mode only, initially hidden)
                rawActivityPanel = createRawActivityPanel();
                rawActivityPanel.setVisible(false);
                rawActivityPanel.setManaged(false);

                // Quick status panel
                VBox statusPanel = createQuickStatusPanel();

                center.getChildren().addAll(
                                liveActivityPanel,
                                heartbeatPanel,
                                controlPanel,
                                eventTimelinePanel,
                                rawActivityPanel,
                                statusPanel);

                return center;
        }

        /**
         * Creates live activity indicators panel
         */
        private HBox createLiveActivityPanel() {
                HBox panel = new HBox(16);
                panel.setAlignment(Pos.CENTER);
                panel.setStyle(
                                "-fx-background-color: #161b22;" +
                                                "-fx-background-radius: 8px;" +
                                                "-fx-padding: 16px;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 8px;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 12, 0, 0, 2);");

                // Packets Analyzed
                VBox packetsAnalyzed = createMetricCard("Packets Analyzed", "0", "#58a6ff");
                packetsAnalyzedLabel = (Label) ((VBox) packetsAnalyzed.getChildren().get(1)).getChildren().get(0);

                // Packets/sec
                VBox packetsPerSec = createMetricCard("Packets/sec", "0", "#3fb950");
                packetsPerSecLabel = (Label) ((VBox) packetsPerSec.getChildren().get(1)).getChildren().get(0);

                // Active Connections
                VBox activeConns = createMetricCard("Active Connections", "0", "#d29922");
                activeConnectionsLabel = (Label) ((VBox) activeConns.getChildren().get(1)).getChildren().get(0);

                // Last Packet
                VBox lastPacket = createMetricCard("Last Packet", "N/A", "#8b949e");
                lastPacketTimeLabel = (Label) ((VBox) lastPacket.getChildren().get(1)).getChildren().get(0);

                panel.getChildren().addAll(packetsAnalyzed, createSeparator(), packetsPerSec,
                                createSeparator(), activeConns, createSeparator(), lastPacket);

                return panel;
        }

        /**
         * Creates a metric card for live indicators
         */
        private VBox createMetricCard(String label, String value, String color) {
                VBox card = new VBox(6);
                card.setAlignment(Pos.CENTER);
                card.setPrefWidth(200);

                Label labelWidget = new Label(label);
                labelWidget.setFont(Font.font("System", 11));
                labelWidget.setStyle("-fx-text-fill: #8b949e;");

                VBox valueBox = new VBox();
                valueBox.setAlignment(Pos.CENTER);
                Label valueWidget = new Label(value);
                valueWidget.setFont(Font.font("System", FontWeight.BOLD, 24));
                valueWidget.setStyle("-fx-text-fill: " + color + ";");
                valueBox.getChildren().add(valueWidget);

                card.getChildren().addAll(labelWidget, valueBox);

                return card;
        }

        /**
         * Creates vertical separator
         */
        private Region createSeparator() {
                Region sep = new Region();
                sep.setPrefWidth(1);
                sep.setStyle("-fx-background-color: #30363d;");
                return sep;
        }

        /**
         * Creates heartbeat message panel
         */
        private VBox createHeartbeatPanel() {
                VBox panel = new VBox(8);
                panel.setAlignment(Pos.CENTER);
                panel.setStyle(
                                "-fx-background-color: #161b22;" +
                                                "-fx-background-radius: 8px;" +
                                                "-fx-padding: 14px 20px;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 8px;");

                Label titleLabel = new Label("System Status");
                titleLabel.setFont(Font.font("System", FontWeight.BOLD, 11));
                titleLabel.setStyle("-fx-text-fill: #8b949e;");

                heartbeatMessageLabel = new Label("Monitoring network traffic normally");
                heartbeatMessageLabel.setFont(Font.font("System", 13));
                heartbeatMessageLabel.setStyle("-fx-text-fill: #c9d1d9;");
                heartbeatMessageLabel.setWrapText(true);
                heartbeatMessageLabel.setAlignment(Pos.CENTER);

                panel.getChildren().addAll(titleLabel, heartbeatMessageLabel);

                return panel;
        }

        /**
         * Creates control panel with monitoring controls and expert mode toggle
         */
        private HBox createControlPanel() {
                HBox panel = new HBox(20);
                panel.setAlignment(Pos.CENTER);
                panel.setPadding(new Insets(10, 0, 10, 0));

                // Left: Monitoring controls
                VBox monitoringControls = createMonitoringControls();

                // Spacer
                Region spacer = new Region();
                HBox.setHgrow(spacer, Priority.ALWAYS);

                // Right: Expert mode toggle
                HBox expertModeControl = createExpertModeToggle();

                panel.getChildren().addAll(monitoringControls, spacer, expertModeControl);

                return panel;
        }

        /**
         * Creates monitoring controls
         */
        private VBox createMonitoringControls() {
                VBox controls = new VBox(10);
                controls.setAlignment(Pos.CENTER_LEFT);

                // Interface info
                HBox interfaceRow = new HBox(8);
                interfaceRow.setAlignment(Pos.CENTER_LEFT);
                Label interfaceTitle = new Label("Interface:");
                interfaceTitle.setFont(Font.font("System", 12));
                interfaceTitle.setStyle("-fx-text-fill: #8b949e;");
                interfaceLabel = new Label("Not selected");
                interfaceLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
                interfaceLabel.setStyle("-fx-text-fill: #c9d1d9;");
                interfaceRow.getChildren().addAll(interfaceTitle, interfaceLabel);

                // Monitoring status
                HBox statusRow = new HBox(8);
                statusRow.setAlignment(Pos.CENTER_LEFT);
                Label statusTitle = new Label("Status:");
                statusTitle.setFont(Font.font("System", 12));
                statusTitle.setStyle("-fx-text-fill: #8b949e;");
                monitoringStatusLabel = new Label("○ Disabled");
                monitoringStatusLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
                monitoringStatusLabel.setStyle("-fx-text-fill: #8b949e;");
                statusRow.getChildren().addAll(statusTitle, monitoringStatusLabel);

                // Start button
                startButton = new Button("Enable Monitoring");
                startButton.setPrefWidth(180);
                startButton.setPrefHeight(36);
                startButton.setStyle(
                                "-fx-background-color: #238636;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(35, 134, 54, 0.3), 8, 0, 0, 0);");
                startButton.setOnAction(e -> handleStartMonitoring());
                startButton.setOnMouseEntered(e -> startButton.setStyle(
                                "-fx-background-color: #2ea043;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(35, 134, 54, 0.5), 10, 0, 0, 0);"));
                startButton.setOnMouseExited(e -> startButton.setStyle(
                                "-fx-background-color: #238636;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(35, 134, 54, 0.3), 8, 0, 0, 0);"));

                // Test button (for demonstration)
                Button testButton = new Button("Generate Test Events");
                testButton.setPrefWidth(180);
                testButton.setPrefHeight(32);
                testButton.setStyle(
                                "-fx-background-color: #6e7681;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-border-color: #8b949e;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 6px;");
                testButton.setOnAction(e -> generateTestEvents());
                testButton.setOnMouseEntered(e -> testButton.setStyle(
                                "-fx-background-color: #8b949e;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-border-color: #8b949e;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 6px;"));
                testButton.setOnMouseExited(e -> testButton.setStyle(
                                "-fx-background-color: #6e7681;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-border-color: #8b949e;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 6px;"));

                controls.getChildren().addAll(interfaceRow, statusRow, startButton, testButton);

                return controls;
        }

        /**
         * Creates expert mode toggle
         */
        private HBox createExpertModeToggle() {
                HBox toggleBox = new HBox(12);
                toggleBox.setAlignment(Pos.CENTER_RIGHT);

                Label toggleLabel = new Label("Mode:");
                toggleLabel.setFont(Font.font("System", 12));
                toggleLabel.setStyle("-fx-text-fill: #8b949e;");

                expertModeToggle = new ToggleButton("Simple Mode");
                expertModeToggle.setPrefWidth(140);
                expertModeToggle.setPrefHeight(32);
                expertModeToggle.setStyle(
                                "-fx-background-color: #21262d;" +
                                                "-fx-text-fill: #c9d1d9;" +
                                                "-fx-font-size: 12px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 6px;" +
                                                "-fx-cursor: hand;");

                expertModeToggle.selectedProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal) {
                                expertModeToggle.setText("Expert Mode");
                                expertModeToggle.setStyle(
                                                "-fx-background-color: #58a6ff;" +
                                                                "-fx-text-fill: #0d1117;" +
                                                                "-fx-font-size: 12px;" +
                                                                "-fx-font-weight: bold;" +
                                                                "-fx-background-radius: 6px;" +
                                                                "-fx-border-color: #58a6ff;" +
                                                                "-fx-border-width: 1px;" +
                                                                "-fx-border-radius: 6px;" +
                                                                "-fx-cursor: hand;");
                                rawActivityPanel.setVisible(true);
                                rawActivityPanel.setManaged(true);
                        } else {
                                expertModeToggle.setText("Simple Mode");
                                expertModeToggle.setStyle(
                                                "-fx-background-color: #21262d;" +
                                                                "-fx-text-fill: #c9d1d9;" +
                                                                "-fx-font-size: 12px;" +
                                                                "-fx-font-weight: bold;" +
                                                                "-fx-background-radius: 6px;" +
                                                                "-fx-border-color: #30363d;" +
                                                                "-fx-border-width: 1px;" +
                                                                "-fx-border-radius: 6px;" +
                                                                "-fx-cursor: hand;");
                                rawActivityPanel.setVisible(false);
                                rawActivityPanel.setManaged(false);
                        }
                });

                toggleBox.getChildren().addAll(toggleLabel, expertModeToggle);

                return toggleBox;
        }

        /**
         * Creates event timeline panel
         */
        private VBox createEventTimelinePanel() {
                VBox panel = new VBox(12);
                panel.setStyle(
                                "-fx-background-color: #161b22;" +
                                                "-fx-background-radius: 8px;" +
                                                "-fx-padding: 18px;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 8px;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 12, 0, 0, 2);");
                panel.setPrefHeight(280);

                // Panel title
                Label titleLabel = new Label("Security Event Timeline");
                titleLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
                titleLabel.setStyle("-fx-text-fill: #c9d1d9;");

                // Event table
                eventTimelineTable = new TableView<>();
                eventTimelineTable.setStyle(
                                "-fx-background-color: #0d1117;" +
                                                "-fx-control-inner-background: #0d1117;" +
                                                "-fx-table-cell-border-color: #30363d;" +
                                                "-fx-text-fill: #c9d1d9;");
                eventTimelineTable.setPrefHeight(200);
                eventTimelineTable.setPlaceholder(new Label("No security events detected"));

                // Columns
                TableColumn<UiSecurityEvent, String> timeCol = new TableColumn<>("Time");
                timeCol.setCellValueFactory(
                                cellData -> new SimpleStringProperty(cellData.getValue().getFormattedTime()));
                timeCol.setPrefWidth(100);

                TableColumn<UiSecurityEvent, String> threatCol = new TableColumn<>("Threat Type");
                threatCol.setCellValueFactory(new PropertyValueFactory<>("threatType"));
                threatCol.setPrefWidth(150);

                TableColumn<UiSecurityEvent, String> riskCol = new TableColumn<>("Risk");
                riskCol.setCellValueFactory(
                                cellData -> new SimpleStringProperty(cellData.getValue().getRiskScore() + "/100"));
                riskCol.setPrefWidth(80);

                TableColumn<UiSecurityEvent, String> classCol = new TableColumn<>("Classification");
                classCol.setCellValueFactory(new PropertyValueFactory<>("classification"));
                classCol.setPrefWidth(150);

                TableColumn<UiSecurityEvent, String> summaryCol = new TableColumn<>("Summary");
                summaryCol.setCellValueFactory(cellData -> {
                        String explanation = cellData.getValue().getExplanation();
                        String summary = explanation.length() > 60 ? explanation.substring(0, 60) + "..." : explanation;
                        return new SimpleStringProperty(summary);
                });
                summaryCol.setPrefWidth(450);

                eventTimelineTable.getColumns().addAll(timeCol, threatCol, riskCol, classCol, summaryCol);

                // Event detail area (expandable)
                eventDetailArea = new TextArea();
                eventDetailArea.setEditable(false);
                eventDetailArea.setWrapText(true);
                eventDetailArea.setPrefHeight(0);
                eventDetailArea.setMaxHeight(0);
                eventDetailArea.setStyle(
                                "-fx-control-inner-background: #0d1117;" +
                                                "-fx-text-fill: #c9d1d9;" +
                                                "-fx-background-color: #0d1117;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 4px;");

                // Row click handler
                eventTimelineTable.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1) {
                                UiSecurityEvent selected = eventTimelineTable.getSelectionModel().getSelectedItem();
                                if (selected != null) {
                                        String details = "═══ SECURITY EVENT DETAILS ═══\n\n" +
                                                        "WHAT HAPPENED:\n" + selected.getExplanation() + "\n\n" +
                                                        "WHY THIS MATTERS:\n" +
                                                        "Risk Score: " + selected.getRiskScore() + "/100 ("
                                                        + selected.getRiskLevel() + ")\n" +
                                                        "Classification: " + selected.getClassification() + "\n" +
                                                        "Confidence: " + selected.getConfidence() + "\n\n" +
                                                        "RECOMMENDED ACTION:\n" + selected.getRecommendation() + "\n\n"
                                                        +
                                                        "REASSURANCE:\n" +
                                                        (selected.getRiskScore() < 50
                                                                        ? "This is a low-priority event. Your system remains secure."
                                                                        : "We are monitoring this situation closely. Follow the recommended action if needed.");
                                        eventDetailArea.setText(details);
                                        eventDetailArea.setPrefHeight(150);
                                        eventDetailArea.setMaxHeight(150);
                                } else {
                                        eventDetailArea.setText("");
                                        eventDetailArea.setPrefHeight(0);
                                        eventDetailArea.setMaxHeight(0);
                                }
                        }
                });

                panel.getChildren().addAll(titleLabel, eventTimelineTable, eventDetailArea);

                return panel;
        }

        /**
         * Creates raw activity panel (Expert Mode only)
         */
        private VBox createRawActivityPanel() {
                VBox panel = new VBox(12);
                panel.setStyle(
                                "-fx-background-color: #161b22;" +
                                                "-fx-background-radius: 8px;" +
                                                "-fx-padding: 18px;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 8px;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.4), 12, 0, 0, 2);");
                panel.setPrefHeight(280);

                // Panel title
                Label titleLabel = new Label("Raw Activity (Expert Mode)");
                titleLabel.setFont(Font.font("System", FontWeight.BOLD, 15));
                titleLabel.setStyle("-fx-text-fill: #58a6ff;");

                // Raw activity table
                rawActivityTable = new TableView<>();
                rawActivityTable.setStyle(
                                "-fx-background-color: #0d1117;" +
                                                "-fx-control-inner-background: #0d1117;" +
                                                "-fx-table-cell-border-color: #30363d;" +
                                                "-fx-text-fill: #c9d1d9;");
                rawActivityTable.setPrefHeight(220);
                rawActivityTable.setPlaceholder(new Label("No raw activity data"));

                // Columns
                TableColumn<UiSecurityEvent, String> timeCol = new TableColumn<>("Timestamp");
                timeCol.setCellValueFactory(
                                cellData -> new SimpleStringProperty(cellData.getValue().getFormattedTime()));
                timeCol.setPrefWidth(100);

                TableColumn<UiSecurityEvent, String> srcIpCol = new TableColumn<>("Source IP");
                srcIpCol.setCellValueFactory(new PropertyValueFactory<>("sourceIp"));
                srcIpCol.setPrefWidth(140);

                TableColumn<UiSecurityEvent, String> threatTypeCol = new TableColumn<>("Threat Type");
                threatTypeCol.setCellValueFactory(new PropertyValueFactory<>("threatType"));
                threatTypeCol.setPrefWidth(140);

                TableColumn<UiSecurityEvent, String> riskCol = new TableColumn<>("Risk");
                riskCol.setCellValueFactory(
                                cellData -> new SimpleStringProperty(
                                                String.valueOf(cellData.getValue().getRiskScore())));
                riskCol.setPrefWidth(60);

                TableColumn<UiSecurityEvent, String> confCol = new TableColumn<>("Confidence");
                confCol.setCellValueFactory(new PropertyValueFactory<>("confidence"));
                confCol.setPrefWidth(100);

                TableColumn<UiSecurityEvent, String> classCol = new TableColumn<>("Classification");
                classCol.setCellValueFactory(new PropertyValueFactory<>("classification"));
                classCol.setPrefWidth(150);

                rawActivityTable.getColumns().addAll(timeCol, srcIpCol, threatTypeCol, riskCol, confCol, classCol);

                panel.getChildren().addAll(titleLabel, rawActivityTable);

                return panel;
        }

        /**
         * Creates quick status panel
         */
        private VBox createQuickStatusPanel() {
                VBox panel = new VBox(8);
                panel.setAlignment(Pos.CENTER);
                panel.setStyle(
                                "-fx-background-color: #161b22;" +
                                                "-fx-background-radius: 8px;" +
                                                "-fx-padding: 14px 20px;" +
                                                "-fx-border-color: #30363d;" +
                                                "-fx-border-width: 1px;" +
                                                "-fx-border-radius: 8px;");

                quickStatusLabel = new Label("Your system is currently SAFE. No threats detected.");
                quickStatusLabel.setFont(Font.font("System", 13));
                quickStatusLabel.setStyle("-fx-text-fill: #c9d1d9;");
                quickStatusLabel.setWrapText(true);
                quickStatusLabel.setAlignment(Pos.CENTER);

                panel.getChildren().add(quickStatusLabel);

                return panel;
        }

        /**
         * Handles start monitoring button
         */
        private void handleStartMonitoring() {
                controller.startMonitoring(0);

                startButton.setText("Disable Monitoring");
                startButton.setStyle(
                                "-fx-background-color: #da3633;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(218, 54, 51, 0.3), 8, 0, 0, 0);");
                startButton.setOnAction(e -> handleStopMonitoring());
                startButton.setOnMouseEntered(e -> startButton.setStyle(
                                "-fx-background-color: #f85149;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(218, 54, 51, 0.5), 10, 0, 0, 0);"));
                startButton.setOnMouseExited(e -> startButton.setStyle(
                                "-fx-background-color: #da3633;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(218, 54, 51, 0.3), 8, 0, 0, 0);"));
        }

        /**
         * Handles stop monitoring button
         */
        private void handleStopMonitoring() {
                controller.stopMonitoring();

                startButton.setText("Enable Monitoring");
                startButton.setStyle(
                                "-fx-background-color: #238636;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(35, 134, 54, 0.3), 8, 0, 0, 0);");
                startButton.setOnAction(e -> handleStartMonitoring());
                startButton.setOnMouseEntered(e -> startButton.setStyle(
                                "-fx-background-color: #2ea043;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(35, 134, 54, 0.5), 10, 0, 0, 0);"));
                startButton.setOnMouseExited(e -> startButton.setStyle(
                                "-fx-background-color: #238636;" +
                                                "-fx-text-fill: #ffffff;" +
                                                "-fx-font-size: 13px;" +
                                                "-fx-font-weight: bold;" +
                                                "-fx-background-radius: 6px;" +
                                                "-fx-cursor: hand;" +
                                                "-fx-effect: dropshadow(gaussian, rgba(35, 134, 54, 0.3), 8, 0, 0, 0);"));
        }

        /**
         * Gets the scene
         */
        public Scene getScene() {
                return scene;
        }

        /**
         * Generates test security events for demonstration
         */
        private void generateTestEvents() {
                // Create diverse test events
                UiSecurityEvent event1 = new UiSecurityEvent(
                                System.currentTimeMillis() - 5000,
                                "198.20.69.42",
                                "192.168.1.1", // destination IP
                                45123, // source port
                                80, // destination port
                                "TCP", // protocol
                                512, // packet size
                                "PORT_SCAN",
                                "BENIGN_NOISE",
                                25,
                                "MEDIUM",
                                "An external computer attempted to connect to 12 different services. This appears to be automated internet scanning, which is very common.",
                                "No action needed. We are monitoring the situation.");

                UiSecurityEvent event2 = new UiSecurityEvent(
                                System.currentTimeMillis() - 3000,
                                "203.45.12.88",
                                "192.168.1.1", // destination IP
                                52341, // source port
                                443, // destination port
                                "TCP", // protocol
                                768, // packet size
                                "PORT_SCAN",
                                "SUSPICIOUS",
                                45,
                                "MEDIUM",
                                "Multiple connection attempts detected from this IP address. Pattern suggests reconnaissance activity.",
                                "Continue monitoring. Consider blocking if activity escalates.");

                UiSecurityEvent event3 = new UiSecurityEvent(
                                System.currentTimeMillis() - 1000,
                                "192.168.1.100",
                                "192.168.1.1", // destination IP
                                61234, // source port
                                22, // destination port
                                "TCP", // protocol
                                256, // packet size
                                "PORT_SCAN",
                                "TRUSTED",
                                10,
                                "LOW",
                                "Internal network scan from known device. This is normal administrative activity.",
                                "No action required. This is expected behavior.");

                UiSecurityEvent event4 = new UiSecurityEvent(
                                System.currentTimeMillis(),
                                "45.142.212.61",
                                "192.168.1.1", // destination IP
                                38912, // source port
                                3389, // destination port
                                "TCP", // protocol
                                1024, // packet size
                                "PORT_SCAN",
                                "SUSPICIOUS",
                                55,
                                "HIGH",
                                "Aggressive port scanning detected. Multiple services targeted in rapid succession.",
                                "Review firewall rules. Consider temporary IP blocking.");

                // Add events to backend
                controller.getBackendBridge().addSecurityEvent(event1);
                controller.getBackendBridge().addSecurityEvent(event2);
                controller.getBackendBridge().addSecurityEvent(event3);
                controller.getBackendBridge().addSecurityEvent(event4);

                System.out.println("✅ Generated 4 test security events");
        }

        /**
         * Gets the controller
         */
        public DashboardControllerProfessional getController() {
                return controller;
        }
}
