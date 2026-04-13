package com.threatscope.core.decode;

/**
 * DECODED PACKET DATA STRUCTURE
 * 
 * Immutable data class representing extracted packet fields.
 * 
 * This is the clean interface between the decode layer and
 * subsequent processing layers (classify, detect).
 */
public class DecodedPacket {

    private final String sourceIp;
    private final String destinationIp;
    private final Integer sourcePort;
    private final Integer destinationPort;
    private final String protocol;
    private final int length;
    private final long timestamp;

    /**
     * Creates a new decoded packet.
     * 
     * @param sourceIp        Source IP address
     * @param destinationIp   Destination IP address
     * @param sourcePort      Source port (null if not TCP/UDP)
     * @param destinationPort Destination port (null if not TCP/UDP)
     * @param protocol        Protocol name (TCP/UDP/ICMP/OTHER)
     * @param length          Packet length in bytes
     */
    public DecodedPacket(String sourceIp, String destinationIp,
            Integer sourcePort, Integer destinationPort,
            String protocol, int length) {
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.sourcePort = sourcePort;
        this.destinationPort = destinationPort;
        this.protocol = protocol;
        this.length = length;
        this.timestamp = System.currentTimeMillis();
    }

    // ===== GETTERS =====

    public String getSourceIp() {
        return sourceIp;
    }

    public String getDestinationIp() {
        return destinationIp;
    }

    public Integer getSourcePort() {
        return sourcePort;
    }

    public Integer getDestinationPort() {
        return destinationPort;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getLength() {
        return length;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "DecodedPacket{" +
                "src=" + sourceIp + ":" + sourcePort +
                ", dst=" + destinationIp + ":" + destinationPort +
                ", proto=" + protocol +
                ", len=" + length +
                '}';
    }
}
