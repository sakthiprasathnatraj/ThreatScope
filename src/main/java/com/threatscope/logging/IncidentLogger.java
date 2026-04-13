package com.threatscope.logging;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class IncidentLogger {

    private static final String LOG_DIR = "threatscope-logs";
    private static final String LOG_FILE = LOG_DIR + "/incidents.csv";
    private static boolean initialized = false;

    public static synchronized void log(IncidentLogEntry entry) {
        try {
            initIfNeeded();

            try (FileWriter writer = new FileWriter(LOG_FILE, true)) {
                writer.write(entry.toCsv());
                writer.write("\n");
            }

        } catch (IOException e) {
            System.err.println("[LOGGER ERROR] " + e.getMessage());
        }
    }

    private static void initIfNeeded() throws IOException {
        if (initialized) return;

        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(LOG_FILE);
        if (!file.exists()) {
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(IncidentLogEntry.csvHeader());
                writer.write("\n");
            }
        }

        initialized = true;
    }
}
