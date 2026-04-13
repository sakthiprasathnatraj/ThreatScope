package com.threatscope.ui;

import com.threatscope.ui.view.LoginViewEnhanced;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * ThreatScope v2.0 - JavaFX Main Application
 * 
 * Professional Desktop Frontend Entry Point
 * 
 * This is the JavaFX GUI mode.
 * For console-only mode, run: com.threatscope.Main
 * 
 * Architecture:
 * - LoginView → DashboardView → EventsView / PacketView / SettingsView
 * - Backend integration via BackendBridge
 * - Dark SOC-style theme
 * 
 * Run with:
 * mvn exec:java -Dexec.mainClass="com.threatscope.ui.MainApp"
 */
public class MainApp extends Application {

    private static final String APP_TITLE = "ThreatScope v2.0 - Professional Edition";
    private static final int WINDOW_WIDTH = 1280;
    private static final int WINDOW_HEIGHT = 800;
    private static final int MIN_WIDTH = 1024;
    private static final int MIN_HEIGHT = 600;

    @Override
    public void start(Stage primaryStage) {
        try {
            // Set application title
            primaryStage.setTitle(APP_TITLE);

            // Create enhanced login view
            LoginViewEnhanced loginView = new LoginViewEnhanced(primaryStage);
            Scene loginScene = loginView.getScene();

            // Load dark theme CSS
            String css = getClass().getResource("/theme/dark-theme.css").toExternalForm();
            loginScene.getStylesheets().add(css);

            // Configure window
            primaryStage.setScene(loginScene);
            primaryStage.setWidth(WINDOW_WIDTH);
            primaryStage.setHeight(WINDOW_HEIGHT);
            primaryStage.setMinWidth(MIN_WIDTH);
            primaryStage.setMinHeight(MIN_HEIGHT);

            // Center on screen
            primaryStage.centerOnScreen();

            // Show window
            primaryStage.show();

            System.out.println("========================================");
            System.out.println("ThreatScope v2.0 - JavaFX UI Started");
            System.out.println("========================================");
            System.out.println("Mode: Desktop GUI (Enhanced)");
            System.out.println("Theme: Professional Dark (SOC Style)");
            System.out.println("Window: " + WINDOW_WIDTH + "x" + WINDOW_HEIGHT);
            System.out.println("Features: Password Toggle, Loading Animation, Keyboard Shortcuts");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("ERROR: Failed to start JavaFX application");
            e.printStackTrace();
        }
    }

    @Override
    public void stop() {
        System.out.println("ThreatScope JavaFX UI shutting down...");
        // Cleanup will be added here when backend integration is complete
    }

    /**
     * Main entry point for JavaFX mode
     */
    public static void main(String[] args) {
        launch(args);
    }
}
