package com.threatscope.core.output;

import com.threatscope.core.model.*;
import com.threatscope.core.explanation.ExplanationEngine;
import com.threatscope.ui.service.BackendBridge;
import com.threatscope.ui.model.UiSecurityEvent;

/**
 * OUTPUT GATEWAY
 * 
 * **CRITICAL: This is the SINGLE OUTPUT POINT for all security events.**
 * 
 * Responsibility:
 * - Format and print all security observations
 * - Ensure consistent, professional output
 * - NO other class should print security events
 * 
 * Design Principles:
 * - Clean, non-alarming format
 * - User-friendly language
 * - Professional appearance
 * - Screenshot-ready output
 * 
 * This is the ONLY class that writes security event output to console.
 */
public class OutputGateway {

    /**
     * Prints a security observation to console.
     * 
     * This is the MAIN output method.
     * All security events flow through here.
     * 
     * @param event          Security event
     * @param classification Traffic classification
     * @param riskScore      Calculated risk score
     * @param confidence     Confidence level
     * @param systemState    Current system state
     */
    public static void printSecurityObservation(SecurityEvent event,
            TrafficClass classification,
            int riskScore,
            ConfidenceLevel confidence,
            SystemState systemState) {

        // Generate complete user-friendly message
        String message = ExplanationEngine.generateCompleteMessage(
                event, classification, riskScore, confidence, systemState.toString());

        // Print to console
        System.out.println(message);

        // ===== FORWARD TO UI (if running in GUI mode) =====
        try {
            BackendBridge bridge = BackendBridge.getInstance();

            // Convert backend SecurityEvent to UI-friendly UiSecurityEvent
            UiSecurityEvent uiEvent = new UiSecurityEvent(
                    event.getTimestamp(),
                    event.getSourceIp(),
                    "Unknown", // destination IP (not tracked in current backend)
                    0, // source port (not tracked)
                    0, // destination port (not tracked)
                    "TCP", // protocol (default)
                    event.getEvidenceCount() * 64, // estimated packet size
                    event.getThreatType().toString(),
                    classification.toString(),
                    riskScore,
                    confidence.toString(),
                    ExplanationEngine.explainWhatHappened(event, classification),
                    ExplanationEngine.recommendAction(classification, riskScore, confidence));

            // Add to UI event list
            bridge.addSecurityEvent(uiEvent);

        } catch (Exception e) {
            // Silently ignore if UI is not available (console-only mode)
            // This allows the backend to work standalone
        }
    }

    /**
     * Prints a system state change notification.
     * 
     * Called when system state transitions occur.
     * 
     * @param previousState Previous state
     * @param newState      New state
     */
    public static void printStateChange(SystemState previousState, SystemState newState) {
        System.out.println();
        System.out.println("[STATE CHANGE]");
        System.out.println("SYSTEM STATE: " + previousState + " → " + newState);
        System.out.println();
    }

    /**
     * Prints startup banner.
     * 
     * Called once when application starts.
     */
    public static void printStartupBanner() {
        System.out.println("========================================");
        System.out.println("    ThreatScope v2.0 - Professional Edition");
        System.out.println("    Host-Based Threat Monitoring System");
        System.out.println("========================================");
        System.out.println();
        System.out.println("Tech Stack: Java 8 + Pcap4J + Npcap");
        System.out.println("Architecture: Layered Desktop Backend");
        System.out.println("Detection: Pattern-Based + Risk Scoring");
        System.out.println();
    }

    /**
     * Prints network interface selection instructions.
     */
    public static void printInterfaceInstructions() {
        System.out.println("⚙️  Configuration:");
        System.out.println("   - Edit Main.java to change interface index");
        System.out.println("   - Ensure you have admin privileges");
        System.out.println("   - Ensure Npcap is installed");
        System.out.println();
    }

    /**
     * Prints monitoring start message.
     */
    public static void printMonitoringStart() {
        System.out.println("🔍 Starting threat monitoring...");
        System.out.println("   Listening for suspicious patterns...");
        System.out.println("   (Event aggregation active - no per-packet alerts)");
        System.out.println();
        System.out.println("========================================");
        System.out.println();
    }

    /**
     * Prints technical details (for advanced users).
     * 
     * Optional detailed output for debugging or advanced analysis.
     * 
     * @param event Security event
     */
    public static void printTechnicalDetails(SecurityEvent event) {
        System.out.println("[TECHNICAL DETAILS]");
        System.out.println("Source IP    : " + event.getSourceIp());
        System.out.println("Threat Type  : " + event.getThreatType());
        System.out.println("Evidence     : " + event.getEvidenceCount() + " items");
        System.out.println("Duration     : " + event.getDuration() + " ms");
        System.out.println("Classification: " + event.getClassification());
        System.out.println("Timestamp    : " + event.getTimestamp());
        System.out.println();
    }

    /**
     * Prints error message (for system errors, not security events).
     * 
     * @param message Error message
     */
    public static void printError(String message) {
        System.err.println("[ERROR] " + message);
    }

    /**
     * Prints warning message (for system warnings, not security events).
     * 
     * @param message Warning message
     */
    public static void printWarning(String message) {
        System.out.println("[WARNING] " + message);
    }

    /**
     * Prints info message (for system information).
     * 
     * @param message Info message
     */
    public static void printInfo(String message) {
        System.out.println("[INFO] " + message);
    }
}
