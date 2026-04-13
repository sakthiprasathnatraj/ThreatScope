package com.threatscope.core.capture;

public class SnifferRunner {

    public static void main(String[] args) {

        System.out.println("====================================");
        System.out.println(" ThreatScope Packet Capture Started ");
        System.out.println("====================================");

        // CHANGE THIS INDEX TO YOUR REAL INTERFACE
        int interfaceIndex = 4;

        PacketSniffer.startSniffing(interfaceIndex);
    }
}
