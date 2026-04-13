package com.threatscope.ui.controller;

import com.threatscope.ui.model.AppSettings;
import com.threatscope.ui.view.SettingsView;
import javafx.scene.control.Slider;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.ComboBox;

/**
 * Controller for Settings View
 */
public class SettingsController {

    private final SettingsView view;
    private final AppSettings settings;

    public SettingsController(SettingsView view) {
        this.view = view;
        this.settings = AppSettings.getInstance();
    }

    public void loadSettings() {
        // Load Thresholds
        int portScan = settings.getPortScanThreshold();
        view.getPortScanThresholdSlider().setValue(portScan);
        view.getPortScanThresholdLabel().setText(portScan + " packets/sec");

        int ddos = settings.getDDoSThreshold();
        view.getDDoSThresholdSlider().setValue(ddos); // Fixed method name in View
        view.getDDoSThresholdLabel().setText(ddos + " packets/sec");

        // Load Preferences
        view.getNotificationsCheck().setSelected(settings.isNotificationsEnabled());
        view.getSoundCheck().setSelected(settings.isPlaySoundEnabled());

        // Load System Data
        view.getRetentionSpinner().getValueFactory().setValue(settings.getDataRetentionMinutes());

        // Interface (Mock for now)
        int iface = settings.getInterfaceIndex();
        if (iface >= 0 && iface < view.getInterfaceCombo().getItems().size()) {
            view.getInterfaceCombo().getSelectionModel().select(iface);
        }
    }

    public void saveSettings() {
        // Save Thresholds
        settings.setPortScanThreshold((int) view.getPortScanThresholdSlider().getValue());
        settings.setDDoSThreshold((int) view.getDDoSThresholdSlider().getValue());

        // Save Preferences
        settings.setNotificationsEnabled(view.getNotificationsCheck().isSelected());
        settings.setPlaySoundEnabled(view.getSoundCheck().isSelected());

        // Save System Data
        settings.setDataRetentionMinutes(view.getRetentionSpinner().getValue());

        // Save Interface
        int iface = view.getInterfaceCombo().getSelectionModel().getSelectedIndex();
        settings.setInterfaceIndex(iface >= 0 ? iface : 0);

        // Persist to disk
        settings.saveSettings();

        // Apply to Backend
        com.threatscope.ui.service.BackendBridge.getInstance().updateDetectionThresholds(
                settings.getDDoSThreshold(),
                settings.getPortScanThreshold());

        System.out.println("✅ Configuration saved and applied successfully.");
    }

    public void resetDefaults() {
        // Reset View Components
        view.getPortScanThresholdSlider().setValue(10);
        view.getDDoSThresholdSlider().setValue(100);
        view.getNotificationsCheck().setSelected(true);
        view.getSoundCheck().setSelected(false);
        view.getRetentionSpinner().getValueFactory().setValue(60);
        view.getInterfaceCombo().getSelectionModel().select(0);

        System.out.println("✅ Defaults restored (not saved until Apply is clicked).");
    }
}
