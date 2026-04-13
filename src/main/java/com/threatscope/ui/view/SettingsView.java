package com.threatscope.ui.view;

import com.threatscope.ui.controller.SettingsController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * Modern Settings Panel for ThreatScope
 */
public class SettingsView {

    private final Stage parentStage;
    private Stage settingsStage;
    private SettingsController controller;

    // UI Components
    private Slider portScanThresholdSlider;
    private Label portScanThresholdLabel;
    private Slider ddosThresholdSlider;
    private Label ddosThresholdLabel;

    private CheckBox notificationsCheck;
    private CheckBox soundCheck;

    private Spinner<Integer> retentionSpinner;
    private ComboBox<String> interfaceCombo;

    private Button saveButton;
    private Button cancelButton;
    private Button resetButton;

    public SettingsView(Stage parentStage) {
        this.parentStage = parentStage;
        this.controller = new SettingsController(this);
    }

    public void show() {
        if (settingsStage == null) {
            createSettingsWindow();
        }
        controller.loadSettings();
        settingsStage.showAndWait();
    }

    private void createSettingsWindow() {
        settingsStage = new Stage();
        settingsStage.initOwner(parentStage);
        settingsStage.initModality(Modality.APPLICATION_MODAL);
        settingsStage.initStyle(StageStyle.UTILITY);
        settingsStage.setTitle("ThreatScope Configuration");

        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: #1a1a1a; -fx-text-fill: white;");
        root.setPrefWidth(500);

        // Header
        Label header = new Label("System Configuration");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #00d4ff;");

        // 1. Detection Settings
        VBox detectionBox = createSection("Threat Detection Thresholds");

        // Port Scan
        VBox portScanBox = new VBox(5);
        Label lbl1 = new Label("Port Scan Sensitivity");
        lbl1.setStyle("-fx-text-fill: #cccccc;");

        portScanThresholdSlider = new Slider(1, 100, 10);
        portScanThresholdSlider.setShowTickMarks(true);
        portScanThresholdSlider.setShowTickLabels(true);
        portScanThresholdLabel = new Label("10 packets/sec");
        portScanThresholdLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-weight: bold;");

        portScanThresholdSlider.valueProperty().addListener((obs, oldVal, newVal) -> portScanThresholdLabel
                .setText(String.format("%d packets/sec", newVal.intValue())));

        portScanBox.getChildren().addAll(lbl1, portScanThresholdSlider, portScanThresholdLabel);

        // DDoS
        VBox ddosBox = new VBox(5);
        Label lbl2 = new Label("DDoS Attack Sensitivity");
        lbl2.setStyle("-fx-text-fill: #cccccc;");

        ddosThresholdSlider = new Slider(50, 1000, 100);
        ddosThresholdSlider.setShowTickMarks(true);
        ddosThresholdSlider.setShowTickLabels(true);
        ddosThresholdLabel = new Label("100 packets/sec");
        ddosThresholdLabel.setStyle("-fx-text-fill: #00d4ff; -fx-font-weight: bold;");

        ddosThresholdSlider.valueProperty().addListener((obs, oldVal, newVal) -> ddosThresholdLabel
                .setText(String.format("%d packets/sec", newVal.intValue())));

        ddosBox.getChildren().addAll(lbl2, ddosThresholdSlider, ddosThresholdLabel);

        detectionBox.getChildren().addAll(portScanBox, new Separator(), ddosBox);

        // 2. Notification Settings
        VBox notifyBox = createSection("Notifications & Alerts");
        notificationsCheck = new CheckBox("Enable Desktop Notifications");
        notificationsCheck.setStyle("-fx-text-fill: white;");
        soundCheck = new CheckBox("Play Alert Sound");
        soundCheck.setStyle("-fx-text-fill: white;");

        notifyBox.getChildren().addAll(notificationsCheck, soundCheck);

        // 3. System Data
        VBox systemBox = createSection("System Data Management");

        HBox retentionRow = new HBox(15);
        retentionRow.setAlignment(Pos.CENTER_LEFT);
        Label retLabel = new Label("Raw Packet Retention (min):");
        retLabel.setStyle("-fx-text-fill: #cccccc;");
        retentionSpinner = new Spinner<>(10, 1440, 60);
        retentionSpinner.setEditable(true);
        retentionSpinner.setPrefWidth(100);
        retentionRow.getChildren().addAll(retLabel, retentionSpinner);

        HBox interfaceRow = new HBox(15);
        interfaceRow.setAlignment(Pos.CENTER_LEFT);
        Label intLabel = new Label("Network Interface:");
        intLabel.setStyle("-fx-text-fill: #cccccc;");
        interfaceCombo = new ComboBox<>();
        interfaceCombo.getItems().addAll("Interface 0 (Default)", "Interface 1", "Interface 2"); // Mock for now
        interfaceCombo.setValue("Interface 0 (Default)");
        interfaceRow.getChildren().addAll(intLabel, interfaceCombo);

        systemBox.getChildren().addAll(retentionRow, interfaceRow);

        // Footer Buttons
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));

        resetButton = new Button("Reset Defaults");
        resetButton.setStyle("-fx-background-color: #444444; -fx-text-fill: white;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        cancelButton = new Button("Cancel");
        cancelButton.setStyle("-fx-background-color: #666666; -fx-text-fill: white;");

        saveButton = new Button("Apply & Save");
        saveButton.setStyle("-fx-background-color: #0078d4; -fx-text-fill: white; -fx-font-weight: bold;");

        // Button Actions
        saveButton.setOnAction(e -> {
            controller.saveSettings();
            settingsStage.close();
        });

        cancelButton.setOnAction(e -> settingsStage.close());

        resetButton.setOnAction(e -> controller.resetDefaults());

        buttonBox.getChildren().addAll(resetButton, spacer, cancelButton, saveButton);

        root.getChildren().addAll(header, detectionBox, notifyBox, systemBox, buttonBox);
        settingsStage.setScene(new Scene(root));
    }

    private VBox createSection(String title) {
        VBox box = new VBox(15);
        box.setPadding(new Insets(15));
        box.setStyle(
                "-fx-background-color: #252525; -fx-background-radius: 8px; -fx-border-color: #333333; -fx-border-radius: 8px;");

        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-font-size: 14px;");

        box.getChildren().add(titleLabel);
        return box;
    }

    // Getters for Controller
    public Slider getPortScanThresholdSlider() {
        return portScanThresholdSlider;
    }

    public Label getPortScanThresholdLabel() {
        return portScanThresholdLabel;
    }

    public Slider getDDoSThresholdSlider() {
        return ddosThresholdSlider;
    }

    public Label getDDoSThresholdLabel() {
        return ddosThresholdLabel;
    }

    public CheckBox getNotificationsCheck() {
        return notificationsCheck;
    }

    public CheckBox getSoundCheck() {
        return soundCheck;
    }

    public Spinner<Integer> getRetentionSpinner() {
        return retentionSpinner;
    }

    public ComboBox<String> getInterfaceCombo() {
        return interfaceCombo;
    }
}
