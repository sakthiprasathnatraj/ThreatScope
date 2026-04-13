package com.threatscope;

import com.threatscope.core.capture.NetworkInterfaceScanner;
import com.threatscope.core.capture.PacketSniffer;
import com.threatscope.core.output.OutputGateway;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * ThreatScope v2.0 - Professional Desktop Cybersecurity Backend
 * 
 * A professional, host-based threat monitoring system with clean architecture.
 * 
 * Architecture (Layered Backend):
 * 1. CAPTURE LAYER - Packet capture (PacketSniffer)
 * 2. DECODE LAYER - Extract packet fields (PacketDecoder)
 * 3. CLASSIFY LAYER - Traffic classification (TrafficClassifier,
 * IPReputationDatabase)
 * 4. DETECT LAYER - Pattern detection (EventAggregator)
 * 5. RISK LAYER - Risk scoring (RiskEngine, ConfidenceEvaluator)
 * 6. EXPLAIN LAYER - User-friendly explanations (ExplanationEngine)
 * 7. STATE LAYER - System state management (SystemStateManager)
 * 8. OUTPUT LAYER - Single output gateway (OutputGateway)
 * 9. MODEL LAYER - Data structures (SecurityEvent, enums)
 * 
 * Key Features:
 * - Clean separation of concerns
 * - Event aggregation (NO per-packet alerts)
 * - Gradual risk escalation (NO sudden jumps)
 * - Classification-based risk capping (NO false CRITICAL from CDNs)
 * - Explain-before-alert philosophy
 * - Professional console output
 * 
 * Tech Stack:
 * - Java 8
 * - Maven
 * - Pcap4J + Npcap (Windows)
 * - Console-based backend (frontend-ready)
 */
public class Main {

    public static void main(String[] args) {

        // ===== LOGGING CONFIGURATION =====

        // Suppress ALL DEBUG logs from Pcap4J
        // Only show WARN and ERROR to keep console clean
        Logger.getLogger("org.pcap4j").setLevel(Level.WARNING);
        Logger.getLogger("").setLevel(Level.WARNING);

        // ===== STARTUP BANNER =====

        OutputGateway.printStartupBanner();

        // ===== NETWORK INTERFACE SELECTION =====

        System.out.println("📋 Available Network Interfaces:");
        System.out.println();
        NetworkInterfaceScanner.listInterfaces();
        System.out.println();

        // ===== CONFIGURATION INSTRUCTIONS =====

        OutputGateway.printInterfaceInstructions();

        // ===== START MONITORING =====

        OutputGateway.printMonitoringStart();

        // Start packet capture on interface index 4
        // (Adjust this index based on your network setup)
        // Run NetworkInterfaceScanner.listInterfaces() first to see available
        // interfaces
        PacketSniffer.startSniffing(4);
    }
}
