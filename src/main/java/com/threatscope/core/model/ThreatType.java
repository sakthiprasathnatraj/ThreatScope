package com.threatscope.core.model;

/**
 * Enumeration of threat types detected by ThreatScope.
 * 
 * Supported threat types:
 * - PORT_SCAN: Network reconnaissance
 * - BRUTE_FORCE: Login/service attack attempts
 * - DDOS_ATTACK: Distributed Denial of Service attacks
 * - SYN_FLOOD: TCP SYN flood attack
 * - UDP_FLOOD: UDP flood attack
 * - ICMP_FLOOD: ICMP flood attack
 */
public enum ThreatType {
    /**
     * Port scan: Multiple unique destination ports accessed
     * from same source IP within time window
     */
    PORT_SCAN,

    /**
     * Brute force: Repeated connection attempts to same service,
     * suggesting password guessing or exploit attempts
     */
    BRUTE_FORCE,

    /**
     * DDoS attack: High packet rate from single source
     */
    DDOS_ATTACK,

    /**
     * SYN flood: High rate of TCP SYN packets
     */
    SYN_FLOOD,

    /**
     * UDP flood: High volume of UDP packets
     */
    UDP_FLOOD,

    /**
     * ICMP flood: Excessive ICMP (ping) requests
     */
    ICMP_FLOOD,

    /**
     * Backdoor attempt: Connection to known malicious ports (e.g., 31337)
     */
    BACKDOOR_ATTEMPT,

    /**
     * Suspicious pattern: Unusual flag combinations or non-standard behavior
     */
    SUSPICIOUS_PATTERN
}
