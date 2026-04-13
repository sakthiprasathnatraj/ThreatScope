package com.threatscope.core.capture;

import org.pcap4j.core.PcapNetworkInterface;
import org.pcap4j.core.Pcaps;
import java.util.List;

public class NetworkInterfaceScanner {

    public static void listInterfaces() {
        try {
            List<PcapNetworkInterface> interfaces = Pcaps.findAllDevs();
            int i = 0;
            for (PcapNetworkInterface nif : interfaces) {
                System.out.println(i++ + " : " + nif.getName() + " | " + nif.getDescription());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
