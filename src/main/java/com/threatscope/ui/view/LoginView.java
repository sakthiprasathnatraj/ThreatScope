package com.threatscope.ui.view;

import com.threatscope.ui.controller.LoginController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

/**
 * Login View
 * 
 * Professional dark SOC-style login screen
 * 
 * Features:
 * - Centered card layout
 * - Dark theme
 * - Clean, professional design
 * - Inline error messages (no popups)
 */
public class LoginView {

    private final Stage primaryStage;
    private final Scene scene;
    private final LoginController controller;

    public LoginView(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Create UI components
        VBox root = createLoginUI();

        // Create scene
        this.scene = new Scene(root, 1280, 800);

        // Create controller (after error label is created)
        Label errorLabel = (Label) root.lookup("#errorLabel");
        this.controller = new LoginController(primaryStage, errorLabel);
    }

    /**
     * Creates the login UI
     */
    private VBox createLoginUI() {
        // Root container
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("login-container");
        root.setStyle("-fx-background-color: #1e1e1e;");

        // Login card
        VBox loginCard = new VBox(20);
        loginCard.setAlignment(Pos.CENTER);
        loginCard.getStyleClass().add("login-card");
        loginCard.setStyle(
                "-fx-background-color: #2d2d2d;" +
                        "-fx-background-radius: 8px;" +
                        "-fx-padding: 40px;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.5), 20, 0, 0, 5);" +
                        "-fx-min-width: 400px;" +
                        "-fx-max-width: 400px;");

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

        // Spacer
        Region spacer1 = new Region();
        spacer1.setPrefHeight(10);

        // Username field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setPrefHeight(40);
        usernameField.setStyle(
                "-fx-background-color: #1e1e1e;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-prompt-text-fill: #666666;" +
                        "-fx-padding: 12px;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-border-color: #444444;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-border-width: 1px;");

        // Password field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setPrefHeight(40);
        passwordField.setStyle(
                "-fx-background-color: #1e1e1e;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-prompt-text-fill: #666666;" +
                        "-fx-padding: 12px;" +
                        "-fx-font-size: 14px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-border-color: #444444;" +
                        "-fx-border-radius: 4px;" +
                        "-fx-border-width: 1px;");

        // Error label
        Label errorLabel = new Label();
        errorLabel.setId("errorLabel");
        errorLabel.setFont(Font.font("System", 12));
        errorLabel.setStyle("-fx-text-fill: #f44336;");
        errorLabel.setVisible(false);
        errorLabel.setManaged(false);

        // Login button
        Button loginButton = new Button("Secure Login");
        loginButton.setPrefHeight(40);
        loginButton.setPrefWidth(Double.MAX_VALUE);
        loginButton.setStyle(
                "-fx-background-color: #0078d4;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12px 24px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-cursor: hand;");

        // Login button hover effect
        loginButton.setOnMouseEntered(e -> loginButton.setStyle(
                "-fx-background-color: #005a9e;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12px 24px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-cursor: hand;"));

        loginButton.setOnMouseExited(e -> loginButton.setStyle(
                "-fx-background-color: #0078d4;" +
                        "-fx-text-fill: #ffffff;" +
                        "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-padding: 12px 24px;" +
                        "-fx-background-radius: 4px;" +
                        "-fx-cursor: hand;"));

        // Login button action
        loginButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            // Show error label if needed
            errorLabel.setVisible(true);
            errorLabel.setManaged(true);

            controller.handleLogin(username, password);
        });

        // Enter key support
        passwordField.setOnAction(e -> loginButton.fire());

        // Footer
        Region spacer2 = new Region();
        spacer2.setPrefHeight(20);

        Label footerLabel = new Label("Secure access to network threat monitoring");
        footerLabel.setFont(Font.font("System", 11));
        footerLabel.setStyle("-fx-text-fill: #666666;");

        // Assemble card
        loginCard.getChildren().addAll(
                titleLabel,
                subtitleLabel,
                versionLabel,
                spacer1,
                usernameField,
                passwordField,
                errorLabel,
                loginButton,
                spacer2,
                footerLabel);

        // Add card to root
        root.getChildren().add(loginCard);

        return root;
    }

    /**
     * Gets the scene
     */
    public Scene getScene() {
        return scene;
    }
}
