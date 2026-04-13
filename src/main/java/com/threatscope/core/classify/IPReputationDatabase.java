package com.threatscope.core.classify;

import java.util.*;

/**
 * IP REPUTATION DATABASE
 * 
 * Maintains lists of trusted, benign, and known malicious IP ranges.
 * 
 * This is CRITICAL for preventing false positives.
 * 
 * Categories:
 * - TRUSTED: CDNs, cloud providers, DNS, Microsoft services
 * - RESEARCH_SCANNER: Known security research organizations
 * - ISP_INFRASTRUCTURE: ISP management IPs
 * - MALICIOUS: Known threat actors (future implementation)
 * 
 * Design Philosophy:
 * - When in doubt, classify as BENIGN
 * - Only flag as THREAT with strong evidence
 * - Regularly update trusted IP ranges
 */
public class IPReputationDatabase {

    // ===== TRUSTED IP RANGES =====

    // Cloudflare CDN (1.1.1.1, 1.0.0.1, and CDN ranges)
    private static final Set<String> CLOUDFLARE_RANGES = new HashSet<>(Arrays.asList(
            "1.1.1.", "1.0.0.", "104.16.", "104.17.", "104.18.", "104.19.",
            "104.20.", "104.21.", "104.22.", "104.23.", "104.24.", "104.25.",
            "104.26.", "104.27.", "104.28.", "104.29.", "104.30.", "104.31.",
            "172.64.", "172.65.", "172.66.", "172.67.", "172.68.", "172.69.",
            "172.70.", "172.71."));

    // Google (DNS, Cloud, Services)
    private static final Set<String> GOOGLE_RANGES = new HashSet<>(Arrays.asList(
            "8.8.8.", "8.8.4.", "8.34.", "8.35.",
            "34.64.", "34.65.", "34.66.", "34.67.", "34.68.", "34.69.",
            "35.184.", "35.185.", "35.186.", "35.187.", "35.188.", "35.189.",
            "35.190.", "35.191.", "35.192.", "35.193.", "35.194.", "35.195."));

    // Amazon AWS
    private static final Set<String> AWS_RANGES = new HashSet<>(Arrays.asList(
            "3.0.", "3.1.", "3.2.", "3.3.", "3.4.", "3.5.",
            "13.32.", "13.33.", "13.34.", "13.35.",
            "52.0.", "52.1.", "52.2.", "52.3.", "52.4.", "52.5.",
            "54.0.", "54.1.", "54.2.", "54.3.", "54.4.", "54.5."));

    // Microsoft Azure & Services
    private static final Set<String> MICROSOFT_RANGES = new HashSet<>(Arrays.asList(
            "13.64.", "13.65.", "13.66.", "13.67.", "13.68.", "13.69.",
            "20.0.", "20.1.", "20.2.", "20.3.", "20.4.", "20.5.",
            "40.64.", "40.65.", "40.66.", "40.67.", "40.68.", "40.69.",
            "52.96.", "52.97.", "52.98.", "52.99.",
            "104.40.", "104.41.", "104.42.", "104.43.", "104.44.", "104.45."));

    // Akamai CDN
    private static final Set<String> AKAMAI_RANGES = new HashSet<>(Arrays.asList(
            "23.0.", "23.1.", "23.2.", "23.3.", "23.4.", "23.5.",
            "23.32.", "23.33.", "23.34.", "23.35.",
            "104.64.", "104.65.", "104.66.", "104.67."));

    // ===== RESEARCH SCANNERS =====

    // Shodan (security research scanner)
    private static final Set<String> SHODAN_RANGES = new HashSet<>(Arrays.asList(
            "198.20.69.", "198.20.70.", "198.20.87.", "198.20.99.",
            "66.240.192.", "66.240.219.", "66.240.236.",
            "71.6.135.", "71.6.165.", "71.6.167.",
            "82.221.105.", "85.25.43.", "85.25.103.",
            "93.120.27.", "188.138.9."));

    // Censys (security research scanner)
    private static final Set<String> CENSYS_RANGES = new HashSet<>(Arrays.asList(
            "162.142.125.", "167.248.133.", "192.35.168.",
            "198.108.66.", "198.108.67.", "198.108.68."));

    // ===== PUBLIC METHODS =====

    /**
     * Checks if IP is from a trusted source.
     * 
     * Trusted sources: CDNs, cloud providers, DNS, major services
     * 
     * @param ip IP address to check
     * @return true if trusted
     */
    public static boolean isTrustedIP(String ip) {
        if (ip == null) {
            return false;
        }

        return matchesAnyPrefix(ip, CLOUDFLARE_RANGES) ||
                matchesAnyPrefix(ip, GOOGLE_RANGES) ||
                matchesAnyPrefix(ip, AWS_RANGES) ||
                matchesAnyPrefix(ip, MICROSOFT_RANGES) ||
                matchesAnyPrefix(ip, AKAMAI_RANGES);
    }

    /**
     * Checks if IP is a known research scanner.
     * 
     * Research scanners: Shodan, Censys, etc.
     * These are benign security research organizations.
     * 
     * @param ip IP address to check
     * @return true if research scanner
     */
    public static boolean isResearchScanner(String ip) {
        if (ip == null) {
            return false;
        }

        return matchesAnyPrefix(ip, SHODAN_RANGES) ||
                matchesAnyPrefix(ip, CENSYS_RANGES);
    }

    /**
     * Checks if IP is ISP infrastructure.
     * 
     * (Future implementation - placeholder)
     * 
     * @param ip IP address to check
     * @return true if ISP infrastructure
     */
    public static boolean isISPInfrastructure(String ip) {
        // Placeholder for ISP infrastructure detection
        // Could be enhanced with ASN lookups or ISP ranges
        return false;
    }

    /**
     * Gets reputation category for an IP.
     * 
     * @param ip IP address
     * @return Reputation category name
     */
    public static String getReputationCategory(String ip) {
        if (isTrustedIP(ip)) {
            if (matchesAnyPrefix(ip, CLOUDFLARE_RANGES)) {
                return "Cloudflare CDN";
            } else if (matchesAnyPrefix(ip, GOOGLE_RANGES)) {
                return "Google Service";
            } else if (matchesAnyPrefix(ip, AWS_RANGES)) {
                return "Amazon AWS";
            } else if (matchesAnyPrefix(ip, MICROSOFT_RANGES)) {
                return "Microsoft Azure";
            } else if (matchesAnyPrefix(ip, AKAMAI_RANGES)) {
                return "Akamai CDN";
            }
            return "Trusted Service";
        }

        if (isResearchScanner(ip)) {
            if (matchesAnyPrefix(ip, SHODAN_RANGES)) {
                return "Shodan Research Scanner";
            } else if (matchesAnyPrefix(ip, CENSYS_RANGES)) {
                return "Censys Research Scanner";
            }
            return "Security Research Scanner";
        }

        return "Unknown";
    }

    // ===== HELPER METHODS =====

    /**
     * Checks if IP matches any prefix in the given set.
     * 
     * @param ip       IP address to check
     * @param prefixes Set of IP prefixes
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
