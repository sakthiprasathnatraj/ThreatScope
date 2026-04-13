package com.threatscope.detect;

import java.util.HashMap;
import java.util.Map;

public class PacketRateTracker {

    private static final Map<String, Integer> packetCount = new HashMap<>();
    private static long lastReset = System.currentTimeMillis();

    public static synchronized int recordPacket(String sourceIp) {
        long now = System.currentTimeMillis();

        if (now - lastReset >= 1000) {
            packetCount.clear();
            lastReset = now;
        }

        packetCount.put(sourceIp, packetCount.getOrDefault(sourceIp, 0) + 1);
        return packetCount.get(sourceIp);
    }
}
