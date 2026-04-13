package com.threatscope.core.capture;

import org.pcap4j.core.*;

import java.util.List;

public class InterfaceLister {

    public static void main(String[] args) throws PcapNativeException {
        List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();

        if (interfaces == null || interfaces.isEmpty()) {
            System.out.println("No network interfaces found.");
            return;
        }

        int index = 0;
        for (PcapNetworkInterface nif : interfaces) {
            System.out.println("[" + index + "]");
            System.out.println(" Name: " + nif.getName());
            System.out.println(" Desc: " + nif.getDescription());

            nif.getAddresses().forEach(addr -> {
                if (addr.getAddress() != null) {
                    System.out.println(" IP: " + addr.getAddress().getHostAddress());
                }
            });

            System.out.println("-------------------------------------");
            index++;
        }
    }
}
