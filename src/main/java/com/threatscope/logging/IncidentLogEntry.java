package com.threatscope.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class IncidentLogEntry {

    private final String timestamp;
    private final String sourceIp;
    private final String threatType;
    private final String severity;
    private final int packetsPerSecond;
    private final String networkInterface;

    public IncidentLogEntry(
            String sourceIp,
            String threatType,
            String severity,
            int packetsPerSecond,
            String networkInterface
    ) {
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.sourceIp = sourceIp;
        this.threatType = threatType;
        this.severity = severity;
        this.packetsPerSecond = packetsPerSecond;
        this.networkInterface = networkInterface;
    }

    public String toCsv() {
        return String.join(",",
                timestamp,
                sourceIp,
                threatType,
                severity,
                String.valueOf(packetsPerSecond),
                networkInterface
        );
    }

    public static String csvHeader() {
        return "timestamp,source_ip,threat_type,severity,pps,interface";
    }
}
