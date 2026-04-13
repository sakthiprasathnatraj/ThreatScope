package com.threatscope.core.capture;

import com.threatscope.core.decode.DecodedPacket;
import com.threatscope.core.decode.PacketDecoder;
import com.threatscope.core.classify.TrafficFilter;
import com.threatscope.core.detect.EventAggregator;
import com.threatscope.core.detect.DDoSDetector;
import com.threatscope.core.detect.PatternDetector;
import com.threatscope.core.output.OutputGateway;
import org.pcap4j.core.*;
import org.pcap4j.packet.*;

import java.util.List;

/**
 * PACKET CAPTURE LAYER
 * 
 * Responsibility:
 * - Capture live packets using Pcap4J
 * - Forward to decode layer
 * - NO decoding, NO filtering, NO detection
 * 
 * This layer is ONLY responsible for packet capture.
 * All other logic is delegated to appropriate layers.
 */
public class PacketSniffer {

    // Debug counter for raw packet UI updates
    private static int rawPacketCount = 0;

    /**
     * Starts packet capture on specified network interface.
     * 
     * @param interfaceIndex Interface index from user selection
     */
    public static void startSniffing(int interfaceIndex) {

        try {
            List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();

            if (interfaces == null || interfaces.isEmpty()) {
                OutputGateway.printError("No network interfaces found.");
                OutputGateway.printError("Make sure Npcap is installed and you have admin privileges.");
                return;
            }

            if (interfaceIndex < 0 || interfaceIndex >= interfaces.size()) {
                OutputGateway.printError("Invalid interface index: " + interfaceIndex);
                return;
            }

            PcapNetworkInterface nif = interfaces.get(interfaceIndex);

            // Open interface for live capture
            PcapHandle handle = nif.openLive(
                    65536, // snaplen - capture full packets
                    PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,
                    10 // timeout in milliseconds
            );

            OutputGateway.printInfo("Monitoring: " + nif.getDescription());

            // Create packet listener (Java 8 compatible)
            PacketListener listener = new PacketListener() {
                @Override
                public void gotPacket(Packet packet) {
                    processPacket(packet);
                }
            };

            // Start infinite capture loop
            handle.loop(-1, listener);
            handle.close();

        } catch (PcapNativeException e) {
            OutputGateway.printError("Pcap error: " + e.getMessage());
            OutputGateway.printError("Make sure Npcap is installed and you have admin privileges.");
        } catch (NotOpenException e) {
            OutputGateway.printError("Handle not open: " + e.getMessage());
        } catch (InterruptedException e) {
            OutputGateway.printWarning("Capture interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            OutputGateway.printError("Unexpected error: " + e.getMessage());
        }
    }

    /**
     * Processes a captured packet.
     * 
     * Delegates to:
     * 1. Decode layer - extract fields
     * 2. Send to UI (Expert Mode) - raw packet data
     * 3. Filter layer - check if should process for threat detection
     * 4. Aggregation layer - record for pattern detection
     * 
     * @param packet Raw packet from Pcap4J
     */
    private static int totalPacketsProcessed = 0;

    private static void processPacket(Packet packet) {

        try {
            totalPacketsProcessed++;

            // Debug: Print status every 50 packets
            if (totalPacketsProcessed % 50 == 0) {
                System.out.println("🔄 Processed " + totalPacketsProcessed + " total packets");
            }

            // STEP 1: DECODE - Extract packet fields
            DecodedPacket decoded = PacketDecoder.decode(packet);

            if (decoded == null || !PacketDecoder.isValid(decoded)) {
                return; // Invalid or unsupported packet
            }

            // STEP 2: SEND TO UI (Expert Mode) - Show ALL packets
            sendRawPacketToUI(packet, decoded);

            // STEP 3: FILTER - Check if local/private traffic
            if (TrafficFilter.isLocalTraffic(decoded.getSourceIp())) {
                return; // Ignore local traffic for threat detection
            }

            // STEP 4: DDOS DETECTION - Check for flood attacks (NEW)
            boolean isSyn = isSynPacket(packet);
            DDoSDetector.recordPacket(decoded.getSourceIp(), decoded.getProtocol(), isSyn);

            // STEP 4.2: PATTERN DETECTION - Check for suspicious signatures (NEW)
            // Use raw packet for flag analysis, decoded for IP/Port
            PatternDetector.checkPattern(packet, decoded.getSourceIp(),
                    decoded.getDestinationPort() != null ? decoded.getDestinationPort() : -1);

            // STEP 5: FORWARD - Send to aggregation layer for threat detection
            EventAggregator.recordPacket(
                    decoded.getSourceIp(),
                    decoded.getDestinationPort(),
                    decoded.getProtocol());

        } catch (Exception e) {
            // Silently skip malformed packets
            // (Common in real network traffic - don't spam console)
        }
    }

    /**
     * Checks if a packet is a TCP SYN packet
     * 
     * @param packet Raw packet
     * @return true if TCP SYN packet
     */
    private static boolean isSynPacket(Packet packet) {
        try {
            TcpPacket tcpPacket = packet.get(TcpPacket.class);
            if (tcpPacket != null) {
                TcpPacket.TcpHeader tcpHeader = tcpPacket.getHeader();
                return tcpHeader.getSyn(); // Check SYN flag
            }
        } catch (Exception e) {
            // Ignore errors
        }
        return false;
    }

    /**
     * Sends raw packet data to UI for Expert Mode display
     * 
     * @param packet  Raw Pcap4J packet
     * @param decoded Decoded packet information
     */
    private static void sendRawPacketToUI(Packet packet, DecodedPacket decoded) {
        try {
            // Try to get BackendBridge instance
            Class<?> bridgeClass = Class.forName("com.threatscope.ui.service.BackendBridge");
            Object bridgeInstance = bridgeClass.getMethod("getInstance").invoke(null);

            // Extract TCP flags if available
            String flags = extractTcpFlags(packet);

            // Extract payload preview (first 32 bytes as hex)
            String payloadPreview = extractPayloadPreview(packet);

            // Create RawPacketData object
            Class<?> rawPacketClass = Class.forName("com.threatscope.ui.model.RawPacketData");
            Object rawPacket = rawPacketClass.getConstructor(
                    long.class, String.class, String.class, int.class, int.class,
                    String.class, int.class, String.class, String.class).newInstance(
                            System.currentTimeMillis(),
                            decoded.getSourceIp(),
                            decoded.getDestinationIp(),
                            decoded.getSourcePort(),
                            decoded.getDestinationPort(),
                            decoded.getProtocol(),
                            packet.length(),
                            flags,
                            payloadPreview);

            // Add to BackendBridge
            bridgeClass.getMethod("addRawPacket", rawPacketClass).invoke(bridgeInstance, rawPacket);

            // Debug: Print first 10 packets and then every 100th packet
            rawPacketCount++;
            if (rawPacketCount <= 10) {
                System.out.println("📦 Raw packet #" + rawPacketCount + " sent to UI: " +
                        decoded.getSourceIp() + ":" + decoded.getSourcePort() + " → " +
                        decoded.getDestinationIp() + ":" + decoded.getDestinationPort() +
                        " [" + decoded.getProtocol() + "]");
            } else if (rawPacketCount % 100 == 0) {
                System.out.println("📊 Status: " + rawPacketCount + " raw packets sent to UI");
            }

        } catch (ClassNotFoundException e) {
            // UI classes not available (console-only mode) - only print once
            if (rawPacketCount == 0) {
                System.out.println("⚠️ UI classes not found - running in console-only mode");
            }
        } catch (Exception e) {
            // Print error for debugging - only print first 3 errors to avoid spam
            if (rawPacketCount < 3) {
                System.err.println("❌ Error sending packet to UI: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Extracts TCP flags from packet
     */
    private static String extractTcpFlags(Packet packet) {
        try {
            TcpPacket tcpPacket = packet.get(TcpPacket.class);
            if (tcpPacket != null) {
                TcpPacket.TcpHeader header = tcpPacket.getHeader();
                StringBuilder flags = new StringBuilder();
                if (header.getSyn())
                    flags.append("SYN ");
                if (header.getAck())
                    flags.append("ACK ");
                if (header.getFin())
                    flags.append("FIN ");
                if (header.getRst())
                    flags.append("RST ");
                if (header.getPsh())
                    flags.append("PSH ");
                if (header.getUrg())
                    flags.append("URG ");
                return flags.toString().trim();
            }
        } catch (Exception e) {
            // Not a TCP packet or error extracting flags
        }
        return "";
    }

    /**
     * Extracts payload preview (first 32 bytes as hex)
     */
    private static String extractPayloadPreview(Packet packet) {
        try {
            byte[] rawData = packet.getRawData();
            if (rawData != null && rawData.length > 0) {
                int previewLength = Math.min(32, rawData.length);
                StringBuilder hex = new StringBuilder();
                for (int i = 0; i < previewLength; i++) {
                    hex.append(String.format("%02X ", rawData[i]));
                }
                if (rawData.length > 32) {
                    hex.append("...");
                }
                return hex.toString().trim();
            }
        } catch (Exception e) {
            // Error extracting payload
        }
        return "";
    }
}
