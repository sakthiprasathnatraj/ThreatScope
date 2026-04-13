package com.threatscope.ui.model;

import javafx.beans.property.*;

/**
 * Raw Packet Data Model (Expert Mode)
 * 
 * Represents a single captured network packet with all raw details
 * This is different from UiSecurityEvent which represents aggregated threat
 * events
 * 
 * Used exclusively for the "Raw Activity" table in Expert Mode
 */
public class RawPacketData {

    private final LongProperty timestamp;
    private final StringProperty sourceIp;
    private final StringProperty destinationIp;
    private final IntegerProperty sourcePort;
    private final IntegerProperty destinationPort;
    private final StringProperty protocol;
    private final IntegerProperty packetSize;
    private final StringProperty flags;
    private final StringProperty payloadPreview;

    /**
     * Constructor for raw packet data
     */
    public RawPacketData(
            long timestamp,
            String sourceIp,
            String destinationIp,
            int sourcePort,
            int destinationPort,
            String protocol,
            int packetSize,
            String flags,
            String payloadPreview) {
        this.timestamp = new SimpleLongProperty(timestamp);
        this.sourceIp = new SimpleStringProperty(sourceIp != null ? sourceIp : "N/A");
        this.destinationIp = new SimpleStringProperty(destinationIp != null ? destinationIp : "N/A");
        this.sourcePort = new SimpleIntegerProperty(sourcePort);
        this.destinationPort = new SimpleIntegerProperty(destinationPort);
        this.protocol = new SimpleStringProperty(protocol != null ? protocol : "UNKNOWN");
        this.packetSize = new SimpleIntegerProperty(packetSize);
        this.flags = new SimpleStringProperty(flags != null ? flags : "");
        this.payloadPreview = new SimpleStringProperty(payloadPreview != null ? payloadPreview : "");
    }

    // ===== PROPERTY GETTERS (for JavaFX binding) =====

    public LongProperty timestampProperty() {
        return timestamp;
    }

    public StringProperty sourceIpProperty() {
        return sourceIp;
    }

    public StringProperty destinationIpProperty() {
        return destinationIp;
    }

    public IntegerProperty sourcePortProperty() {
        return sourcePort;
    }

    public IntegerProperty destinationPortProperty() {
        return destinationPort;
    }

    public StringProperty protocolProperty() {
        return protocol;
    }

    public IntegerProperty packetSizeProperty() {
        return packetSize;
    }

    public StringProperty flagsProperty() {
        return flags;
    }

    public StringProperty payloadPreviewProperty() {
        return payloadPreview;
    }

    // ===== VALUE GETTERS =====

    public long getTimestamp() {
        return timestamp.get();
    }

    public String getSourceIp() {
        return sourceIp.get();
    }

    public String getDestinationIp() {
        return destinationIp.get();
    }

    public int getSourcePort() {
        return sourcePort.get();
    }

    public int getDestinationPort() {
        return destinationPort.get();
    }

    public String getProtocol() {
        return protocol.get();
    }

    public int getPacketSize() {
        return packetSize.get();
    }

    public String getFlags() {
        return flags.get();
    }

    public String getPayloadPreview() {
        return payloadPreview.get();
    }

    /**
     * Gets formatted timestamp for table display
     */
    public String getFormattedTimestamp() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss.SSS");
        return sdf.format(new java.util.Date(timestamp.get()));
    }

    @Override
    public String toString() {
        return "RawPacketData{" +
                "timestamp=" + getFormattedTimestamp() +
                ", sourceIp='" + sourceIp.get() + '\'' +
                ", destinationIp='" + destinationIp.get() + '\'' +
                ", sourcePort=" + sourcePort.get() +
                ", destinationPort=" + destinationPort.get() +
                ", protocol='" + protocol.get() + '\'' +
                ", packetSize=" + packetSize.get() +
                ", flags='" + flags.get() + '\'' +
                '}';
    }
}
