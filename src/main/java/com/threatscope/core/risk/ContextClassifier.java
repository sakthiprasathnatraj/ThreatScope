package com.threatscope.core.risk;

import com.threatscope.core.model.SecurityEvent;

public class ContextClassifier {

    public static String classify(SecurityEvent event) {

        String ip = event.getSourceIp();

        // Local / Private IP ranges
        if (ip.startsWith("192.168.") ||
                ip.startsWith("10.") ||
                ip.startsWith("172.16.") ||
                ip.startsWith("172.31.")) {

            return "Likely internal network activity (router, device scan, or OS service)";
        }

        return "External source – potentially suspicious";
    }
}

