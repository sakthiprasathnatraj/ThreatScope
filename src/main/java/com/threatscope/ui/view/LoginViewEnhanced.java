package com.threatscope.ui.view;

import com.threatscope.ui.controller.LoginController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Login View - Enhanced Professional Edition
 * 
 * Professional dark SOC-style login screen with advanced features
 * 
 * NEW FEATURES:
 * - Security badge icon
 * - Password visibility toggle
 * - Remember me checkbox
 * - Loading animation during authentication
 * - Keyboard shortcuts (Ctrl+L, Ctrl+P)
 * - Enhanced hover effects with glow
 * - System status indicator
 * - Professional footer with system info
 * - Focus effects on input fields
 */
public class LoginViewEnhanced {

    private final Stage primaryStage;
    private final Scene scene;
    private final LoginController controller;

    // UI Components
    private TextField usernameField;
    private PasswordField passwordField;
    private TextField passwordVisibleField;
    private CheckBox rememberMeCheckBox;
    private CheckBox showPasswordCheckBox;
    private Button loginButton;
    private Label errorLabel;
    private Label statusLabel;
    private ProgressIndicator loadingIndicator;
    private HBox passwordContainer;

    public LoginViewEnhanced(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Create UI components
        VBox root = createEnhancedLoginUI();

        // Create scene
        this.scene = new Scene(root, 1280, 800);

        // Setup keyboard shortcuts
        setupKeyboardShortcuts();

        // Create controller
        this.controller = new LoginController(primaryStage, errorLabel);
    }

    /**
     * Creates the enhanced login UI
     */
    private VBox createEnhancedLoginUI() {
        // Root container
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #1e1e1e;");

        // Login card
        VBox loginCard = new VBox(18);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.setStyle(
                "-fx-background-color: #2d2d2d;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 40px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 20, 0, 0, 5);" +
                        "-fx-min-width: 450px;" +
                        "-fx-max-width: 450px;");

        // Security Badge (visual indicator)
        Circle securityBadge = new Circle(30);
        securityBadge.setStyle("-fx-fill: #0078d4; -fx-stroke: #005a9e; -fx-stroke-width: 3;");

        Label badgeIcon = new Label("🔒");
        badgeIcon.setFont(Font.font("System", 24));

        StackPane badgeContainer = new StackPane(securityBadge, badgeIcon);
        badgeContainer.setAlignment(Pos.CENTER);

        // Title
        Label titleLabel = new Label("ThreatScope");
        titleLabel.setFont(Font.font("System", FontWeight.BOLD, 28));
        titleLabel.setStyle("-fx-text-fill: #ffffff;");

        // Subtitle
        Label subtitleLabel = new Label("Host-Based Threat Monitoring System");
        subtitleLabel.setFont(Font.font("System", 13));
        subtitleLabel.setStyle("-fx-text-fill: #888888;");

        // Version
        Label versionLabel = new Label("v2.0 Professional Edition");
        versionLabel.setFont(Font.font("System", 11));
        versionLabel.setStyle("-fx-text-fill: #666666;");

        // System Status Indicator
        statusLabel = new Label("● System Ready");
        statusLabel.setFont(Font.font("System", 11));
        statusLabel.setStyle("-fx-text-fill: #4caf50;");

        // Spacer
        Region spacer1 = new Region();
        spacer1.setPrefHeight(10);

        // Username field with enhanced styling
        usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(42);
        usernameField.setStyle(getFieldStyle(false));

        // Focus effect for username
        usernameField.focusedProperty()
                .addListener((obs, oldVal, newVal) -> usernameField.setStyle(getFieldStyle(newVal)));

        // Password field container (for toggle functionality)
        passwordContainer = new HBox();
        passwordContainer.setAlignment(Pos.CENTER_LEFT);

        // Password field (hidden)
        passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(42);
        HBox.setHgrow(passwordField, Priority.ALWAYS);
        passwordField.setStyle(getFieldStyle(false));

        // Password visible field (shown when toggle is on)
        passwordVisibleField = new TextField();
        passwordVisibleField.setPromptText("Password");
        passwordVisibleField.setPrefHeight(42);
        passwordVisibleField.setVisible(false);
        passwordVisibleField.setManaged(false);
        HBox.setHgrow(passwordVisibleField, Priority.ALWAYS);
        passwordVisibleField.setStyle(getFieldStyle(false));

        // Bind visible and hidden password fields
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());

        // Focus effect for password
        passwordField.focusedProperty()
                .addListener((obs, oldVal, newVal) -> passwordField.setStyle(getFieldStyle(newVal)));
        passwordVisibleField.focusedProperty()
                .addListener((obs, oldVal, newVal) -> passwordVisibleField.setStyle(getFieldStyle(newVal)));

        passwordContainer.getChildren().addAll(passwordField, passwordVisibleField);

        // Show password checkbox
        showPasswordCheckBox = new CheckBox("Show password");
        showPasswordCheckBox.setFont(Font.font("System", 11));
        showPasswordCheckBox.setStyle("-fx-text-fill: #888888;");
        showPasswordCheckBox.setOnAction(e -> togglePasswordVisibility());

        // Remember me checkbox
        rememberMeCheckBox = new CheckBox("Remember me");
        rememberMeCheckBox.setFont(Font.font("System", 11));
        rememberMeCheckBox.setStyle("-fx-text-fill: #888888;");

        // Options row
        HBox optionsRow = new HBox(20);
        optionsRow.setAlignment(Pos.CENTER_LEFT);
        optionsRow.getChildren().addAll(rememberMeCheckBox, showPasswordCheckBox);

        // Error label
        errorLabel = new Label();
        errorLabel.setId("errorLabel");
        errorLabel.setFont(Font.font("System", 12));
        errorLabel.setStyle("-fx-text-fill: #f44336;");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);
        errorLabel.setWrapText(true);
        errorLabel.setMaxWidth(370);

        // Loading indicator
        loadingIndicator = new ProgressIndicator();
        loadingIndicator.setPrefSize(20, 20);
        loadingIndicator.setVisible(false);
        loadingIndicator.setManaged(false);
        loadingIndicator.setStyle("-fx-progress-color: #0078d4;");

        // Login button
        loginButton = new Button("Secure Login");
        loginButton.setPrefHeight(42);
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setStyle(getButtonStyle(false));

        // Enhanced hover effects
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(getButtonStyle(true)));
        loginButton.setOnMouseExited(e -> loginButton.setStyle(getButtonStyle(false)));

        // Login button action
        loginButton.setOnAction(e -> handleLoginClick());

        // Enter key support
        passwordField.setOnAction(e -> loginButton.fire());
        passwordVisibleField.setOnAction(e -> loginButton.fire());

        // Footer with system info
        Region spacer2 = new Region();
        spacer2.setPrefHeight(15);

        Label footerLabel = new Label("Secure access to network threat monitoring");
        footerLabel.setFont(Font.font("System", 11));
        footerLabel.setStyle("-fx-text-fill: #666666;");

        Label systemInfoLabel = new Label("Java 8 • Desktop Application • Local Authentication");
        systemInfoLabel.setFont(Font.font("System", 9));
        systemInfoLabel.setStyle("-fx-text-fill: #555555;");

        // Keyboard shortcut hint
        Label shortcutHint = new Label("Tip: Press Ctrl+L to focus username field");
        shortcutHint.setFont(Font.font("System", 9));
        shortcutHint.setStyle("-fx-text-fill: #555555; -fx-font-style: italic;");

        // Assemble card
        loginCard.getChildren().addAll(
                badgeContainer,
                titleLabel,
                subtitleLabel,
                versionLabel,
                statusLabel,
                spacer1,
                usernameField,
                passwordContainer,
                optionsRow,
                errorLabel,
                loadingIndicator,
                loginButton,
                spacer2,
                footerLabel,
                systemInfoLabel,
                shortcutHint);

        // Add card to root
        root.getChildren().add(loginCard);

        return root;
    }

    /**
     * Gets field style based on focus state
     */
    private String getFieldStyle(boolean focused) {
        String borderColor = focused ? "#0078d4" : "#444444";
        String borderWidth = focused ? "2px" : "1px";
        String bgColor = focused ? "#252525" : "#1e1e1e";

        return "-fx-background-color: " + bgColor + ";" +
                "-fx-text-fill: #ffffff;" +
                "-fx-prompt-text-fill: #666666;" +
                "-fx-padding: 12px;" +
                "-fx-font-size: 14px;" +
                "-fx-background-radius: 4px;" +
                "-fx-border-color: " + borderColor + ";" +
                "-fx-border-radius: 4px;" +
                "-fx-border-width: " + borderWidth + ";";
    }

    /**
     * Gets button style based on hover state
     */
    private String getButtonStyle(boolean hover) {
        String bgColor = hover ? "#005a9e" : "#0078d4";
        String effect = hover ? "-fx-effect: dropshadow(gaussian, rgba(0, 120, 212, 0.4), 10, 0, 0, 2);" : "";

        return "-fx-background-color: " + bgColor + ";" +
                "-fx-text-fill: #ffffff;" +
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-padding: 12px 24px;" +
                "-fx-background-radius: 4px;" +
                "-fx-cursor: hand;" +
                effect;
    }

    /**
     * Toggles password visibility
     */
    private void togglePasswordVisibility() {
        if (showPasswordCheckBox.isSelected()) {
            // Show password
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);
            passwordVisibleField.requestFocus();
        } else {
            // Hide password
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordField.requestFocus();
        }
    }

    /**
     * Handles login button click with loading animation
     */
    private void handleLoginClick() {
        String username = usernameField.getText();
        String password = showPasswordCheckBox.isSelected() ? passwordVisibleField.getText() : passwordField.getText();

        // Show loading state
        loginButton.setDisable(true);
        loginButton.setText("Authenticating...");
        loadingIndicator.setVisible(true);
        loadingIndicator.setManaged(true);
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        // Simulate authentication delay (for professional feel)
        new Thread(() -> {
            try {
                Thread.sleep(500); // 500ms delay for professional feel

                // Run on JavaFX thread
                javafx.application.Platform.runLater(() -> {
                    // Hide loading
                    loadingIndicator.setVisible(false);
                    loadingIndicator.setManaged(false);
                    loginButton.setDisable(false);
                    loginButton.setText("Secure Login");

                    // Show error label container
                    errorLabel.setVisible(true);
                    errorLabel.setManaged(true);

                    // Attempt login
                    controller.handleLogin(username, password);
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Setup keyboard shortcuts
     */
    private void setupKeyboardShortcuts() {
        // Ctrl+L to focus username field
        KeyCombination ctrlL = new KeyCodeCombination(KeyCode.L, KeyCombination.CONTROL_DOWN);
        scene.getAccelerators().put(ctrlL, () -> usernameField.requestFocus());

        // Ctrl+P to focus password field
        KeyCombination ctrlP = new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN);
        scene.getAccelerators().put(ctrlP, () -> {
            if (showPasswordCheckBox.isSelected()) {
                passwordVisibleField.requestFocus();
            } else {
                passwordField.requestFocus();
            }
        });
    }

    /**
     * Gets the scene
     */
    public Scene getScene() {
        return scene;
    }
}
