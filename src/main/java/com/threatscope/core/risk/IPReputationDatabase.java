package com.threatscope.core.risk;

import java.util.*;

/**
 * IP REPUTATION DATABASE
 * 
 * Maintains lists of known IP addresses and ranges for classification.
 * 
 * This is a simplified, hardcoded version for the MVP.
 * In production, this would query external threat intelligence feeds.
 * 
 * Design Philosophy:
 * - Whitelist known-good IPs (ISP, CDN, cloud providers)
 * - Identify common scanning services
 * - Avoid false positives from legitimate services
 */
public class IPReputationDatabase {

    // ===== TRUSTED IP RANGES =====

    /**
     * Known CDN providers (Cloudflare, Akamai, Fastly, etc.)
     */
    private static final Set<String> CDN_PREFIXES = new HashSet<>(Arrays.asList(
            "104.16.", "104.17.", "104.18.", "104.19.", "104.20.", "104.21.", "104.22.", "104.23.", "104.24.",
            "104.25.", "104.26.", "104.27.", "104.28.", "104.29.", "104.30.", "104.31.", // Cloudflare
            "23.0.", "23.1.", "23.2.", "23.3.", "23.4.", "23.5.", "23.6.", "23.7.", "23.8.", "23.9.", // Akamai
            "151.101.", // Fastly
            "13.32.", "13.33.", "13.34.", "13.35.", // AWS CloudFront
            "172.64.", "172.65.", "172.66.", "172.67." // Cloudflare
    ));

    /**
     * Major cloud providers (AWS, Azure, Google Cloud)
     */
    private static final Set<String> CLOUD_PREFIXES = new HashSet<>(Arrays.asList(
            "3.", "13.", "18.", "34.", "35.", "52.", "54.", // AWS (partial)
            "20.", "40.", "51.", "52.", "104.", // Azure (partial)
            "35.184.", "35.185.", "35.186.", "35.187.", "35.188.", "35.189.", "35.190.", "35.191.", "35.192.",
            "35.193.", "35.194.", "35.195.", "35.196.", "35.197.", "35.198.", "35.199.", "35.200.", "35.201.",
            "35.202.", "35.203.", "35.204.", "35.205.", "35.206.", "35.207.", "35.208.", "35.209.", "35.210.",
            "35.211.", "35.212.", "35.213.", "35.214.", "35.215.", "35.216.", "35.217.", "35.218.", "35.219.",
            "35.220.", "35.221.", "35.222.", "35.223.", "35.224.", "35.225.", "35.226.", "35.227.", "35.228.",
            "35.229.", "35.230.", "35.231.", "35.232.", "35.233.", "35.234.", "35.235.", "35.236.", "35.237.",
            "35.238.", "35.239.", "35.240.", "35.241.", "35.242.", "35.243.", "35.244.", "35.245.", "35.246.", "35.247." // Google
                                                                                                                         // Cloud
                                                                                                                         // (partial)
    ));

    /**
     * Known DNS servers
     */
    private static final Set<String> DNS_SERVERS = new HashSet<>(Arrays.asList(
            "8.8.8.8", "8.8.4.4", // Google DNS
            "1.1.1.1", "1.0.0.1", // Cloudflare DNS
            "208.67.222.222", "208.67.220.220", // OpenDNS
            "9.9.9.9" // Quad9
    ));

    /**
     * Microsoft IP ranges (Windows Update, Office 365, etc.)
     */
    private static final Set<String> MICROSOFT_PREFIXES = new HashSet<>(Arrays.asList(
            "13.64.", "13.65.", "13.66.", "13.67.", "13.68.", "13.69.", "13.70.", "13.71.", "13.72.", "13.73.",
            "13.74.", "13.75.", "13.76.", "13.77.", "13.78.", "13.79.", "13.80.", "13.81.", "13.82.", "13.83.",
            "13.84.", "13.85.", "13.86.", "13.87.", "13.88.", "13.89.", "13.90.", "13.91.", "13.92.", "13.93.",
            "13.94.", "13.95.", "13.96.", "13.97.", "13.98.", "13.99.", "13.100.", "13.101.", "13.102.", "13.103.",
            "13.104.", "13.105.", "13.106.", "13.107.", // Microsoft Azure
            "40.76.", "40.77.", "40.78.", "40.79.", "40.80.", "40.81.", "40.82.", "40.83.", "40.84.", "40.85.",
            "40.86.", "40.87.", "40.88.", "40.89.", "40.90.", "40.91.", "40.92.", "40.93.", "40.94.", "40.95.",
            "40.96.", "40.97.", "40.98.", "40.99.", "40.100.", "40.101.", "40.102.", "40.103.", "40.104.", "40.105.",
            "40.106.", "40.107.", "40.108.", "40.109.", "40.110.", "40.111.", "40.112.", "40.113.", "40.114.",
            "40.115.", "40.116.", "40.117.", "40.118.", "40.119.", "40.120.", "40.121.", "40.122.", "40.123.",
            "40.124.", "40.125.", "40.126.", "40.127.", // Microsoft services
            "52.96.", "52.97.", "52.98.", "52.99.", "52.100.", "52.101.", "52.102.", "52.103.", "52.104.", "52.105.",
            "52.106.", "52.107.", "52.108.", "52.109.", "52.110.", "52.111.", "52.112.", "52.113.", "52.114.",
            "52.115.", "52.116.", "52.117.", "52.118.", "52.119.", "52.120." // Office 365
    ));

    // ===== BENIGN NOISE IP RANGES =====

    /**
     * Known security research scanners (Shodan, Censys, etc.)
     */
    private static final Set<String> RESEARCH_SCANNERS = new HashSet<>(Arrays.asList(
            "198.20.69.", "198.20.70.", "198.20.87.", "198.20.99.", // Shodan
            "162.142.125.", // Censys
            "71.6.135.", "71.6.165.", "71.6.167.", // Shadowserver
            "66.240.192.", "66.240.219.", "66.240.236." // Internet Census
    ));

    /**
     * Common ISP infrastructure ranges
     * (These often show up in scans but are usually benign)
     */
    private static final Set<String> ISP_INFRASTRUCTURE = new HashSet<>(Arrays.asList(
            "100.64.", "100.65.", "100.66.", "100.67.", "100.68.", "100.69.", "100.70.", "100.71.", "100.72.",
            "100.73.", "100.74.", "100.75.", "100.76.", "100.77.", "100.78.", "100.79.", "100.80.", "100.81.",
            "100.82.", "100.83.", "100.84.", "100.85.", "100.86.", "100.87.", "100.88.", "100.89.", "100.90.",
            "100.91.", "100.92.", "100.93.", "100.94.", "100.95.", "100.96.", "100.97.", "100.98.", "100.99.",
            "100.100.", "100.101.", "100.102.", "100.103.", "100.104.", "100.105.", "100.106.", "100.107.", "100.108.",
            "100.109.", "100.110.", "100.111.", "100.112.", "100.113.", "100.114.", "100.115.", "100.116.", "100.117.",
            "100.118.", "100.119.", "100.120.", "100.121.", "100.122.", "100.123.", "100.124.", "100.125.", "100.126.",
            "100.127." // Carrier-grade NAT
    ));

    // ===== PUBLIC METHODS =====

    /**
     * Checks if IP is from a trusted source.
     * 
     * Trusted sources include:
     * - CDN providers
     * - Major cloud providers
     * - DNS servers
     * - Microsoft services
     * 
     * @param ip IP address to check
     * @return true if trusted
     */
    public static boolean isTrustedIP(String ip) {

        // Exact match (DNS servers)
        if (DNS_SERVERS.contains(ip)) {
            return true;
        }

        // Prefix match (CDN, cloud, Microsoft)
        return matchesAnyPrefix(ip, CDN_PREFIXES) ||
                matchesAnyPrefix(ip, CLOUD_PREFIXES) ||
                matchesAnyPrefix(ip, MICROSOFT_PREFIXES);
    }

    /**
     * Checks if IP is a known research scanner.
     * 
     * These are legitimate security research organizations
     * that scan the internet for research purposes.
     * 
     * @param ip IP address to check
     * @return true if known research scanner
     */
    public static boolean isResearchScanner(String ip) {
        return matchesAnyPrefix(ip, RESEARCH_SCANNERS);
    }

    /**
     * Checks if IP is ISP infrastructure.
     * 
     * @param ip IP address to check
     * @return true if ISP infrastructure
     */
    public static boolean isISPInfrastructure(String ip) {
        return matchesAnyPrefix(ip, ISP_INFRASTRUCTURE);
    }

    /**
     * Gets reputation category for IP.
     * 
     * Returns human-readable category:
     * - "CDN Provider"
     * - "Cloud Provider"
     * - "DNS Server"
     * - "Microsoft Service"
     * - "Research Scanner"
     * - "ISP Infrastructure"
     * - "Unknown"
     * 
     * @param ip IP address to check
     * @return Reputation category
     */
    public static String getReputationCategory(String ip) {

        if (DNS_SERVERS.contains(ip)) {
            return "DNS Server";
        }

        if (matchesAnyPrefix(ip, CDN_PREFIXES)) {
            return "CDN Provider";
        }

        if (matchesAnyPrefix(ip, CLOUD_PREFIXES)) {
            return "Cloud Provider";
        }

        if (matchesAnyPrefix(ip, MICROSOFT_PREFIXES)) {
            return "Microsoft Service";
        }

        if (matchesAnyPrefix(ip, RESEARCH_SCANNERS)) {
            return "Research Scanner";
        }

        if (matchesAnyPrefix(ip, ISP_INFRASTRUCTURE)) {
            return "ISP Infrastructure";
        }

        return "Unknown";
    }

    // ===== HELPER METHODS =====

    /**
     * Checks if IP matches any prefix in the set.
     * 
     * @param ip       IP address to check
     * @param prefixes Set of prefixes to match against
     * @return true if matches any prefix
     */
    private static boolean matchesAnyPrefix(String ip, Set<String> prefixes) {
        for (String prefix : prefixes) {
            if (ip.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
