package com.threatscope.core.decode;

import org.pcap4j.packet.*;

/**
 * PACKET DECODER LAYER
 * 
 * Responsibility:
 * - Extract relevant fields from raw packets
 * - Convert packet data to clean data structures
 * - NO filtering, NO classification, NO detection
 * - Pure extraction only
 * 
 * This layer sits between capture and classification.
 */
public class PacketDecoder {

    /**
     * Decodes a raw packet into a DecodedPacket structure.
     * 
     * Extracts:
     * - Source IP address
     * - Destination IP address
     * - Source port (if TCP/UDP)
     * - Destination port (if TCP/UDP)
     * - Protocol (TCP/UDP/ICMP/Other)
     * - Packet length
     * 
     * @param packet Raw packet from Pcap4J
     * @return DecodedPacket with extracted fields, or null if invalid
     */
    public static DecodedPacket decode(Packet packet) {

        if (packet == null) {
            return null;
        }

        // Extract IPv4 packet
        IpV4Packet ipv4Packet = packet.get(IpV4Packet.class);
        if (ipv4Packet == null) {
            return null; // Not IPv4, skip
        }

        IpV4Packet.IpV4Header ipv4Header = ipv4Packet.getHeader();

        // Extract IP addresses
        String srcIp = ipv4Header.getSrcAddr().getHostAddress();
        String dstIp = ipv4Header.getDstAddr().getHostAddress();

        // Extract ports and protocol
        Integer srcPort = null;
        Integer dstPort = null;
        String protocol = "OTHER";

        // Try TCP
        TcpPacket tcpPacket = packet.get(TcpPacket.class);
        if (tcpPacket != null) {
            TcpPacket.TcpHeader tcpHeader = tcpPacket.getHeader();
            srcPort = tcpHeader.getSrcPort().valueAsInt();
            dstPort = tcpHeader.getDstPort().valueAsInt();
            protocol = "TCP";
        } else {
            // Try UDP
            UdpPacket udpPacket = packet.get(UdpPacket.class);
            if (udpPacket != null) {
                UdpPacket.UdpHeader udpHeader = udpPacket.getHeader();
                srcPort = udpHeader.getSrcPort().valueAsInt();
                dstPort = udpHeader.getDstPort().valueAsInt();
                protocol = "UDP";
            } else {
                // Try ICMP
                IcmpV4CommonPacket icmpPacket = packet.get(IcmpV4CommonPacket.class);
                if (icmpPacket != null) {
                    protocol = "ICMP";
                }
            }
        }

        // Get packet length
        int length = packet.length();

        // Create decoded packet
        return new DecodedPacket(srcIp, dstIp, srcPort, dstPort, protocol, length);
    }

    /**
     * Checks if a decoded packet is valid for further processing.
     * 
     * Filters out:
     * - Zero-length packets (NIC offloading artifacts)
     * - Packets without port information (for port-based detection)
     * 
     * @param decoded Decoded packet
     * @return true if valid for processing
     */
    public static boolean isValid(DecodedPacket decoded) {
        if (decoded == null) {
            return false;
        }

        // Must have non-zero length
        if (decoded.getLength() == 0) {
            return false;
        }

        // For port-based detection, must have destination port
        // (ICMP and other protocols without ports are currently not analyzed)
        if (decoded.getDestinationPort() == null) {
            return false;
        }

        return true;
    }
}
