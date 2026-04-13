package com.threatscope.ui;

import com.threatscope.ui.view.DashboardViewV2;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * ThreatScope v2.0 - Dashboard V2 Test Launcher
 * 
 * Quick launcher to test the new professional SOC dashboard layout
 * 
 * Run with:
 * mvn exec:java -Dexec.mainClass="com.threatscope.ui.DashboardV2Launcher"
 * 
 * Or from IntelliJ: Right-click → Run 'DashboardV2Launcher.main()'
 */
public class DashboardV2Launcher extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Set title
            primaryStage.setTitle("ThreatScope v2.0 - Professional SOC Dashboard");

            // Create the new V2 dashboard
            DashboardViewV2 dashboardView = new DashboardViewV2(primaryStage);

            // Set scene
            primaryStage.setScene(dashboardView.getScene());

            // Show maximized
            primaryStage.setMaximized(true);
            primaryStage.show();

            System.out.println("========================================");
            System.out.println("ThreatScope v2.0 - Dashboard V2 Launched");
            System.out.println("========================================");
            System.out.println("Layout: Professional SOC (2×2 Grid)");
            System.out.println("Resolution: Optimized for 1920×1080");
            System.out.println("Features:");
            System.out.println("  ✓ Compact header (70px)");
            System.out.println("  ✓ Live monitoring panel with stats");
            System.out.println("  ✓ Risk overview with progress bar");
            System.out.println("  ✓ Security event timeline table");
            System.out.println("  ✓ Expandable explanation panel");
            System.out.println("  ✓ Control bar with monitoring toggle");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("ERROR: Failed to launch Dashboard V2");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
