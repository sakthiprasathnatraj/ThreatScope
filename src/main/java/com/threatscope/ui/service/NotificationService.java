package com.threatscope.ui.service;

import com.threatscope.ui.model.AppSettings;
import com.threatscope.ui.model.UiSecurityEvent;

import java.awt.*;

/**
 * Service for handling desktop notifications
 */
public class NotificationService {

    private static NotificationService instance;
    private TrayIcon trayIcon;
    private boolean isSupported;

    private NotificationService() {
        initializeTray();
    }

    public static synchronized NotificationService getInstance() {
        if (instance == null) {
            instance = new NotificationService();
        }
        return instance;
    }

    private void initializeTray() {
        if (!SystemTray.isSupported()) {
            System.err.println("❌ SystemTray is not supported on this platform.");
            isSupported = false;
            return;
        }

        try {
            SystemTray tray = SystemTray.getSystemTray();

            // Create a simple red icon programmatically
            // (BufferedImage is AWT, so this should be fine)
            int size = 16;
            java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(size, size,
                    java.awt.image.BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = img.createGraphics();
            g.setColor(Color.RED);
            g.fillOval(0, 0, size, size);
            g.dispose();

            trayIcon = new TrayIcon(img, "ThreatScope Active");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("ThreatScope Security Monitor");

            tray.add(trayIcon);
            isSupported = true;
            System.out.println("✅ Notification Service initialized");

        } catch (AWTException e) {
            System.err.println("❌ Failed to add TrayIcon: " + e.getMessage());
            isSupported = false;
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize notification service: " + e.getMessage());
            isSupported = false;
        }
    }

    public void showNotification(UiSecurityEvent event) {
        if (!isSupported || trayIcon == null)
            return;

        // Check user preference
        if (!AppSettings.getInstance().isNotificationsEnabled())
            return;

        String title = "Threat Detected: " + event.getThreatType();
        String message = event.getSummary() + "\nSource: " + event.getSourceIp();

        TrayIcon.MessageType type = TrayIcon.MessageType.INFO;
        if (event.getRiskScore() >= 70) {
            type = TrayIcon.MessageType.ERROR;
        } else if (event.getRiskScore() >= 30) {
            type = TrayIcon.MessageType.WARNING;
        }

        trayIcon.displayMessage(title, message, type);
    }

    public void showSystemMessage(String title, String message) {
        if (!isSupported || trayIcon == null)
            return;
        trayIcon.displayMessage(title, message, TrayIcon.MessageType.INFO);
    }
}
