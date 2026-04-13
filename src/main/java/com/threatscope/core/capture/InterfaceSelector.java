package com.threatscope.core.capture;

import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import org.pcap4j.core.PcapNativeException;

import java.util.List;

/**
 * Network Interface Selector
 * 
 * Helps select the correct network interface for packet capture
 */
public class InterfaceSelector {

    /**
     * Lists all available network interfaces
     */
    public static void listAllInterfaces() {
        try {
            List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();

            if (interfaces == null || interfaces.isEmpty()) {
                System.out.println("❌ No network interfaces found!");
                System.out.println("   Make sure npcap is installed and you're running as Administrator");
                return;
            }

            System.out.println("\n=== Available Network Interfaces ===");
            for (int i = 0; i < interfaces.size(); i++) {
                PcapNetworkInterface nif = interfaces.get(i);

                System.out.println("\n[" + i + "] " + nif.getName());
                System.out.println("    Description: " + nif.getDescription());
                System.out.println("    Addresses: " + nif.getAddresses());

                // Check if this looks like a real network interface
                boolean isReal = isRealInterface(nif);
                if (isReal) {
                    System.out.println("    ✅ RECOMMENDED - This looks like a real network interface");
                } else {
                    System.out.println("    ⚠️  Virtual/Loopback interface - may not capture real traffic");
                }
            }
            System.out.println("\n====================================\n");

        } catch (PcapNativeException e) {
            System.err.println("❌ Error listing interfaces: " + e.getMessage());
        }
    }

    /**
     * Automatically selects the best network interface
     * 
     * @return Index of the best interface, or 0 if none found
     */
    public static int selectBestInterface() {
        try {
            List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();

            if (interfaces == null || interfaces.isEmpty()) {
                System.out.println("❌ No network interfaces found!");
                return 0;
            }

            // Strategy 1: Find first interface with IP address that's not loopback
            for (int i = 0; i < interfaces.size(); i++) {
                PcapNetworkInterface nif = interfaces.get(i);

                if (isRealInterface(nif)) {
                    System.out.println("✅ Auto-selected interface [" + i + "]: " + nif.getDescription());
                    return i;
                }
            }

            // Strategy 2: If no "real" interface found, use first non-loopback
            for (int i = 0; i < interfaces.size(); i++) {
                PcapNetworkInterface nif = interfaces.get(i);
                String desc = nif.getDescription();

                if (desc != null && !desc.toLowerCase().contains("loopback")) {
                    System.out.println("⚠️  Using interface [" + i + "]: " + desc);
                    return i;
                }
            }

            // Fallback: use interface 0
            System.out.println("⚠️  Using default interface [0]: " + interfaces.get(0).getDescription());
            return 0;

        } catch (PcapNativeException e) {
            System.err.println("❌ Error selecting interface: " + e.getMessage());
            return 0;
        }
    }

    /**
     * Checks if an interface looks like a real network interface
     * (not virtual, not loopback, has IP addresses)
     */
    private static boolean isRealInterface(PcapNetworkInterface nif) {
        if (nif == null)
            return false;

        String desc = nif.getDescription();
        if (desc == null)
            return false;

        String descLower = desc.toLowerCase();

        // Exclude virtual/loopback/bluetooth interfaces
        if (descLower.contains("loopback"))
            return false;
        if (descLower.contains("miniport"))
            return false;
        if (descLower.contains("pseudo"))
            return false;
        if (descLower.contains("virtual"))
            return false;
        if (descLower.contains("bluetooth"))
            return false; // Exclude Bluetooth
        if (descLower.contains("vmware"))
            return false; // Exclude VMware
        if (descLower.contains("virtualbox"))
            return false; // Exclude VirtualBox
        if (descLower.contains("hyper-v"))
            return false; // Exclude Hyper-V

        // Must have at least one address
        if (nif.getAddresses().isEmpty())
            return false;

        // Strongly prefer Ethernet and Wi-Fi
        if (descLower.contains("ethernet"))
            return true;
        if (descLower.contains("wi-fi"))
            return true;
        if (descLower.contains("wireless"))
            return true;
        if (descLower.contains("802.11"))
            return true;
        if (descLower.contains("wlan"))
            return true;
        if (descLower.contains("realtek"))
            return true;
        if (descLower.contains("intel"))
            return true;
        if (descLower.contains("broadcom"))
            return true;
        if (descLower.contains("qualcomm"))
            return true;

        // If it has addresses and isn't virtual/bluetooth, it might be real
        return !nif.getAddresses().isEmpty();
    }

    /**
     * Test program to list interfaces
     */
    public static void main(String[] args) {
        System.out.println("=== Network Interface Selector Test ===\n");

        listAllInterfaces();

        int bestIndex = selectBestInterface();
        System.out.println("\nBest interface index: " + bestIndex);
    }
}
