package com.threatscope.ui.model;

import java.io.*;
import java.util.Properties;

/**
 * Application Settings Model
 * 
 * Manages user-configurable settings and persists them to disk.
 * Supports detection thresholds, UI preferences, and system behavior.
 */
public class AppSettings {

    private static final String CONFIG_FILE = "threatscope.properties";
    private static AppSettings instance;
    private Properties properties;

    // Defaults
    private static final int DEFAULT_PORT_SCAN_THRESHOLD = 50; // Increased to avoid false positives
    private static final int DEFAULT_DDOS_THRESHOLD = 1000; // Increased for modern networks
    private static final boolean DEFAULT_NOTIFICATIONS_ENABLED = false; // Disabled by default
    private static final boolean DEFAULT_PLAY_SOUND = false;
    private static final int DEFAULT_DATA_RETENTION_MINUTES = 60;
    private static final int DEFAULT_INTERFACE_INDEX = 0;

    private AppSettings() {
        properties = new Properties();
        loadSettings();
    }

    public static AppSettings getInstance() {
        if (instance == null) {
            instance = new AppSettings();
        }
        return instance;
    }

    private void loadSettings() {
        File file = new File(CONFIG_FILE);
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                properties.load(fis);
            } catch (IOException e) {
                System.err.println("❌ Failed to load settings: " + e.getMessage());
            }
        }
    }

    public void saveSettings() {
        try (FileOutputStream fos = new FileOutputStream(CONFIG_FILE)) {
            properties.store(fos, "ThreatScope Configuration");
            System.out.println("✅ Settings saved to " + CONFIG_FILE);
        } catch (IOException e) {
            System.err.println("❌ Failed to save settings: " + e.getMessage());
        }
    }

    // Getters & Setters

    public int getPortScanThreshold() {
        return Integer
                .parseInt(properties.getProperty("threshold.portscan", String.valueOf(DEFAULT_PORT_SCAN_THRESHOLD)));
    }

    public void setPortScanThreshold(int threshold) {
        properties.setProperty("threshold.portscan", String.valueOf(threshold));
    }

    public int getDDoSThreshold() {
        return Integer.parseInt(properties.getProperty("threshold.ddos", String.valueOf(DEFAULT_DDOS_THRESHOLD)));
    }

    public void setDDoSThreshold(int threshold) {
        properties.setProperty("threshold.ddos", String.valueOf(threshold));
    }

    public boolean isNotificationsEnabled() {
        return Boolean.parseBoolean(
                properties.getProperty("ui.notifications", String.valueOf(DEFAULT_NOTIFICATIONS_ENABLED)));
    }

    public void setNotificationsEnabled(boolean enabled) {
        properties.setProperty("ui.notifications", String.valueOf(enabled));
    }

    public boolean isPlaySoundEnabled() {
        return Boolean.parseBoolean(properties.getProperty("ui.sound", String.valueOf(DEFAULT_PLAY_SOUND)));
    }

    public void setPlaySoundEnabled(boolean enabled) {
        properties.setProperty("ui.sound", String.valueOf(enabled));
    }

    public int getDataRetentionMinutes() {
        return Integer
                .parseInt(properties.getProperty("data.retention", String.valueOf(DEFAULT_DATA_RETENTION_MINUTES)));
    }

    public void setDataRetentionMinutes(int minutes) {
        properties.setProperty("data.retention", String.valueOf(minutes));
    }

    public int getInterfaceIndex() {
        return Integer.parseInt(properties.getProperty("network.interface", String.valueOf(DEFAULT_INTERFACE_INDEX)));
    }

    public void setInterfaceIndex(int index) {
        properties.setProperty("network.interface", String.valueOf(index));
    }
}
