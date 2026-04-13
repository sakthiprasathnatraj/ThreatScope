package com.threatscope.ui.controller;

import com.threatscope.ui.service.AuthService;
import com.threatscope.ui.view.DashboardViewV2;
import javafx.animation.FadeTransition;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Login Controller
 * 
 * Handles login authentication and transition to dashboard
 */
public class LoginController {

    private final Stage primaryStage;
    private final AuthService authService;
    private final Label errorLabel;

    public LoginController(Stage primaryStage, Label errorLabel) {
        this.primaryStage = primaryStage;
        this.authService = new AuthService();
        this.errorLabel = errorLabel;
    }

    /**
     * Handles login attempt
     * 
     * @param username Username input
     * @param password Password input
     */
    public void handleLogin(String username, String password) {
        // Clear previous errors
        errorLabel.setText("");

        // Validate inputs
        if (username == null || username.trim().isEmpty()) {
            showError("Username is required");
            return;
        }

        if (password == null || password.trim().isEmpty()) {
            showError("Password is required");
            return;
        }

        // Authenticate
        boolean authenticated = authService.authenticate(username, password);

        if (authenticated) {
            // Success - transition to dashboard
            transitionToDashboard();
        } else {
            // Failure - show error
            showError("Invalid username or password");
        }
    }

    /**
     * Shows error message
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setStyle("-fx-text-fill: #f44336;");
    }

    /**
     * Transitions to dashboard with fade animation
     */
    private void transitionToDashboard() {
        try {
            // Create professional dashboard view V2 (updated scrollable version)
            DashboardViewV2 dashboardView = new DashboardViewV2(primaryStage);
            Scene dashboardScene = dashboardView.getScene();

            // Load dark theme
            String css = getClass().getResource("/theme/dark-theme.css").toExternalForm();
            dashboardScene.getStylesheets().add(css);

            // Fade transition
            FadeTransition fadeOut = new FadeTransition(Duration.millis(300), primaryStage.getScene().getRoot());
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            fadeOut.setOnFinished(event -> {
                // Switch scene
                primaryStage.setScene(dashboardScene);

                // Fade in
                FadeTransition fadeIn = new FadeTransition(Duration.millis(300), dashboardScene.getRoot());
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();

                System.out.println("✅ Login successful - Enhanced Dashboard loaded");
            });

            fadeOut.play();

        } catch (Exception e) {
            System.err.println("ERROR: Failed to load dashboard");
            e.printStackTrace();
            showError("Failed to load dashboard");
        }
    }
}
