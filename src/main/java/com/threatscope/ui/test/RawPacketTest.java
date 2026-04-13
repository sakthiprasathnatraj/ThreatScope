package com.threatscope.ui.test;

import com.threatscope.ui.model.RawPacketData;
import com.threatscope.ui.service.BackendBridge;

/**
 * Quick test to verify BackendBridge raw packet functionality
 * 
 * Run this to test if raw packets can be added to the UI
 */
public class RawPacketTest {

    public static void main(String[] args) {
        System.out.println("=== Raw Packet Test ===");

        // Get BackendBridge instance
        BackendBridge bridge = BackendBridge.getInstance();
        System.out.println("✅ BackendBridge instance created");

        // Check if raw packets list is accessible
        System.out.println("Raw packets list size: " + bridge.getRawPackets().size());

        // Create a test raw packet
        RawPacketData testPacket = new RawPacketData(
                System.currentTimeMillis(),
                "192.168.1.100",
                "8.8.8.8",
                54321,
                443,
                "TCP",
                1400,
                "SYN ACK",
                "48 65 6C 6C 6F 20 57 6F 72 6C 64");

        System.out.println("✅ Test packet created: " + testPacket);

        // Add test packet to BackendBridge
        bridge.addRawPacket(testPacket);
        System.out.println("✅ Test packet added to BackendBridge");

        // Wait a moment for JavaFX thread to process
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Check if packet was added
        System.out.println("Raw packets list size after add: " + bridge.getRawPackets().size());

        if (bridge.getRawPackets().size() > 0) {
            System.out.println("✅ SUCCESS: Raw packet was added to the list!");
            System.out.println("Packet details: " + bridge.getRawPackets().get(0));
        } else {
            System.out.println("❌ FAILED: Raw packet was NOT added to the list");
        }

        System.out.println("\n=== Test Complete ===");
        System.out.println("If this test succeeds, the BackendBridge is working correctly.");
        System.out.println("The issue is likely with packet capture, not the UI binding.");
    }
}
