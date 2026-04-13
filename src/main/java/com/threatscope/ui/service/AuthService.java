package com.threatscope.ui.service;

/**
 * Authentication Service
 * 
 * Handles local authentication for ThreatScope UI
 * 
 * IMPORTANT: This is LOCAL authentication only.
 * No network calls, no external services.
 * 
 * Default credentials:
 * - Username: admin
 * - Password: admin123
 */
public class AuthService {

    // Hardcoded credentials (for now)
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    /**
     * Authenticates user with username and password
     * 
     * @param username Username
     * @param password Password
     * @return true if authenticated, false otherwise
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        // Simple credential check
        boolean valid = DEFAULT_USERNAME.equals(username.trim()) &&
                DEFAULT_PASSWORD.equals(password.trim());

        if (valid) {
            System.out.println("✅ Authentication successful for user: " + username);
        } else {
            System.out.println("❌ Authentication failed for user: " + username);
        }

        return valid;
    }

    /**
     * Gets default username (for development/testing)
     */
    public String getDefaultUsername() {
        return DEFAULT_USERNAME;
    }
}
