package com.threatscope.core.model;

/**
 * Model class for Top Talker (Most active IP)
 */
public class TopTalker {
    private String ipAddress;
    private long packetCount;
    private long byteCount;
    private String primaryProtocol;
    private String riskLevel;

    public TopTalker(String ipAddress, long packetCount, long byteCount, String primaryProtocol, String riskLevel) {
        this.ipAddress = ipAddress;
        this.packetCount = packetCount;
        this.byteCount = byteCount;
        this.primaryProtocol = primaryProtocol;
        this.riskLevel = riskLevel;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public long getPacketCount() {
        return packetCount;
    }

    public long getByteCount() {
        return byteCount;
    }

    public String getPrimaryProtocol() {
        return primaryProtocol;
    }

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setPacketCount(long packetCount) {
        this.packetCount = packetCount;
    }

    public void setByteCount(long byteCount) {
        this.byteCount = byteCount;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }
}
