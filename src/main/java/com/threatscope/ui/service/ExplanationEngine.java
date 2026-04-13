package com.threatscope.ui.service;

import com.threatscope.ui.model.UiSecurityEvent;

/**
 * Enhanced Explanation Engine
 * 
 * Generates detailed, educational, and actionable explanations for security
 * events.
 * This is the CORE VALUE of ThreatScope - making security understandable.
 * 
 * For each threat, we provide:
 * - What happened (technical details)
 * - Why it matters (impact assessment)
 * - How it works (educational content)
 * - What to do (actionable recommendations)
 * - Context (reassurance and perspective)
 * 
 * @author ThreatScope Team
 * @version 2.1
 */
public class ExplanationEngine {

        /**
         * Generates comprehensive explanation for a security event
         * 
         * @param event The security event to explain
         * @return Enhanced explanation object
         */
        public static EnhancedExplanation generateExplanation(UiSecurityEvent event) {
                if (event == null) {
                        return EnhancedExplanation.empty();
                }

                String threatType = event.getThreatType();
                String sourceIp = event.getSourceIp();
                int riskScore = event.getRiskScore();
                String classification = event.getClassification();

                // Generate threat-specific explanation
                switch (threatType) {
                        case "DDOS_ATTACK":
                                return explainDDoSAttack(event);
                        case "SYN_FLOOD":
                                return explainSynFlood(event);
                        case "UDP_FLOOD":
                                return explainUdpFlood(event);
                        case "ICMP_FLOOD":
                                return explainIcmpFlood(event);
                        case "PORT_SCAN":
                                return explainPortScan(event);
                        case "BRUTE_FORCE":
                                return explainBruteForce(event);
                        case "BACKDOOR_ATTEMPT":
                                return explainBackdoorAttempt(event);
                        case "SUSPICIOUS_PATTERN":
                                return explainSuspiciousPattern(event);
                        default:
                                return explainGenericThreat(event);
                }
        }

        // ========== DDoS Attack Explanations ==========

        private static EnhancedExplanation explainDDoSAttack(UiSecurityEvent event) {
                String sourceIp = event.getSourceIp();
                int riskScore = event.getRiskScore();
                String classification = event.getClassification();

                EnhancedExplanation explanation = new EnhancedExplanation();

                // WHAT HAPPENED
                explanation.whatHappened = String.format(
                                "🚨 DDoS Attack Detected\n\n" +
                                                "Source: %s\n" +
                                                "Attack Type: Distributed Denial of Service\n" +
                                                "Classification: %s\n\n" +
                                                "A remote computer is sending an unusually high volume of network packets "
                                                +
                                                "to your system, attempting to overwhelm your network resources.",
                                sourceIp, classification);

                // WHY THIS MATTERS
                explanation.whyMatters = String.format(
                                "💡 Impact Assessment\n\n" +
                                                "DDoS attacks can:\n" +
                                                "• Slow down your internet connection\n" +
                                                "• Make legitimate services unavailable\n" +
                                                "• Consume bandwidth and system resources\n" +
                                                "• Mask other malicious activities\n\n" +
                                                "Current Risk: %s (%d/100)\n" +
                                                "This %s indicates %s",
                                getRiskLevel(riskScore), riskScore,
                                classification.toLowerCase(),
                                getClassificationMeaning(classification));

                // HOW IT WORKS
                explanation.howItWorks = "📚 Understanding DDoS Attacks\n\n" +
                                "A DDoS (Distributed Denial of Service) attack works by:\n\n" +
                                "1. Flooding Target: The attacker sends massive amounts of traffic\n" +
                                "2. Resource Exhaustion: Your system tries to process all requests\n" +
                                "3. Service Degradation: Legitimate traffic gets blocked or delayed\n" +
                                "4. Potential Outage: Services may become completely unavailable\n\n" +
                                "Think of it like a crowd of people blocking a store entrance - " +
                                "real customers can't get in because fake customers are taking up all the space.";

                // TECHNICAL DETAILS
                explanation.technicalDetails = String.format(
                                "🔧 Technical Analysis\n\n" +
                                                "Source IP: %s\n" +
                                                "Protocol: %s\n" +
                                                "Packet Rate: High volume detected\n" +
                                                "Pattern: Sustained high-rate traffic\n" +
                                                "Detection Method: Packet rate threshold exceeded\n\n" +
                                                "The system detected an abnormal packet rate from this IP address, " +
                                                "indicating a potential flood attack.",
                                sourceIp, event.getProtocol());

                // RECOMMENDED ACTION
                explanation.recommendedAction = getRecommendedAction(riskScore, classification,
                                "• Monitor your network performance\n" +
                                                "• Check if legitimate services are affected\n" +
                                                "• Consider blocking the source IP if attacks persist\n" +
                                                "• Enable rate limiting on your firewall\n" +
                                                "• Contact your ISP if the attack is severe");

                // REASSURANCE
                explanation.reassurance = getReassurance(riskScore,
                                "ThreatScope has detected and logged this activity. " +
                                                "Your system is monitoring the situation.",
                                "This attack is being monitored. Follow the recommended actions to mitigate impact.",
                                "This is a serious attack. Take immediate action to protect your system.");

                return explanation;
        }

        private static EnhancedExplanation explainSynFlood(UiSecurityEvent event) {
                String sourceIp = event.getSourceIp();
                int riskScore = event.getRiskScore();
                String classification = event.getClassification();

                EnhancedExplanation explanation = new EnhancedExplanation();

                explanation.whatHappened = String.format(
                                "🚨 SYN Flood Attack Detected\n\n" +
                                                "Source: %s\n" +
                                                "Attack Type: TCP SYN Flood (Connection Exhaustion)\n" +
                                                "Classification: %s\n\n" +
                                                "An attacker is sending a high volume of TCP SYN packets (connection requests) "
                                                +
                                                "without completing the handshake, attempting to exhaust your connection resources.",
                                sourceIp, classification);

                explanation.whyMatters = String.format(
                                "💡 Impact Assessment\n\n" +
                                                "SYN Flood attacks can:\n" +
                                                "• Exhaust your connection table (max connections)\n" +
                                                "• Prevent legitimate users from connecting\n" +
                                                "• Cause service timeouts and failures\n" +
                                                "• Degrade overall network performance\n\n" +
                                                "Current Risk: %s (%d/100)\n" +
                                                "Classification: %s - %s",
                                getRiskLevel(riskScore), riskScore,
                                classification, getClassificationMeaning(classification));

                explanation.howItWorks = "📚 Understanding SYN Flood Attacks\n\n" +
                                "TCP connections use a 3-way handshake:\n" +
                                "1. Client sends SYN (\"Let's connect\")\n" +
                                "2. Server sends SYN-ACK (\"OK, ready\")\n" +
                                "3. Client sends ACK (\"Connection established\")\n\n" +
                                "In a SYN flood:\n" +
                                "• Attacker sends thousands of SYN packets\n" +
                                "• Server allocates resources for each\n" +
                                "• Attacker NEVER sends the final ACK\n" +
                                "• Server waits, resources are tied up\n" +
                                "• Eventually, no resources left for real users\n\n" +
                                "It's like making 1000 restaurant reservations and never showing up - " +
                                "real customers can't get a table.";

                explanation.technicalDetails = String.format(
                                "🔧 Technical Analysis\n\n" +
                                                "Source IP: %s\n" +
                                                "Protocol: TCP\n" +
                                                "Attack Vector: SYN packets without ACK completion\n" +
                                                "SYN Packet Rate: Threshold exceeded\n" +
                                                "Detection Method: SYN-specific rate monitoring\n\n" +
                                                "The system detected an abnormally high rate of TCP SYN packets " +
                                                "from this IP without corresponding connection completions.",
                                sourceIp);

                explanation.recommendedAction = getRecommendedAction(riskScore, classification,
                                "• Enable SYN cookies on your server/firewall\n" +
                                                "• Reduce TCP timeout values\n" +
                                                "• Implement connection rate limiting\n" +
                                                "• Block the source IP if attacks persist\n" +
                                                "• Consider using a DDoS protection service");

                explanation.reassurance = getReassurance(riskScore,
                                "ThreatScope is monitoring this SYN flood. Modern systems have built-in protections.",
                                "This attack is being tracked. Implement SYN cookies to mitigate the impact.",
                                "This is a serious connection exhaustion attack. Take immediate protective measures.");

                return explanation;
        }

        private static EnhancedExplanation explainUdpFlood(UiSecurityEvent event) {
                String sourceIp = event.getSourceIp();
                int riskScore = event.getRiskScore();
                String classification = event.getClassification();

                EnhancedExplanation explanation = new EnhancedExplanation();

                explanation.whatHappened = String.format(
                                "🚨 UDP Flood Attack Detected\n\n" +
                                                "Source: %s\n" +
                                                "Attack Type: UDP Flood (Bandwidth Saturation)\n" +
                                                "Classification: %s\n\n" +
                                                "An attacker is sending a high volume of UDP packets to random ports, "
                                                +
                                                "attempting to consume your bandwidth and processing resources.",
                                sourceIp, classification);

                explanation.whyMatters = String.format(
                                "💡 Impact Assessment\n\n" +
                                                "UDP Flood attacks can:\n" +
                                                "• Saturate your network bandwidth\n" +
                                                "• Cause packet loss for legitimate traffic\n" +
                                                "• Overwhelm your network interface\n" +
                                                "• Trigger ICMP \"port unreachable\" responses (amplification)\n\n" +
                                                "Current Risk: %s (%d/100)\n" +
                                                "Classification: %s - %s",
                                getRiskLevel(riskScore), riskScore,
                                classification, getClassificationMeaning(classification));

                explanation.howItWorks = "📚 Understanding UDP Flood Attacks\n\n" +
                                "UDP (User Datagram Protocol) is connectionless:\n" +
                                "• No handshake required (unlike TCP)\n" +
                                "• Packets sent directly without verification\n" +
                                "• Server must process each packet\n\n" +
                                "In a UDP flood:\n" +
                                "1. Attacker sends massive UDP packets to random ports\n" +
                                "2. Your system checks each port for listening services\n" +
                                "3. For closed ports, system sends ICMP \"port unreachable\"\n" +
                                "4. This consumes CPU and bandwidth\n" +
                                "5. Legitimate traffic gets crowded out\n\n" +
                                "It's like someone sending thousands of letters to random addresses - " +
                                "the post office wastes resources returning \"address not found\" notices.";

                explanation.technicalDetails = String.format(
                                "🔧 Technical Analysis\n\n" +
                                                "Source IP: %s\n" +
                                                "Protocol: UDP\n" +
                                                "Attack Vector: High-volume UDP packets to random ports\n" +
                                                "UDP Packet Rate: Threshold exceeded\n" +
                                                "Detection Method: UDP-specific rate monitoring\n\n" +
                                                "The system detected an abnormally high rate of UDP packets " +
                                                "from this IP address, indicating a flood attack.",
                                sourceIp);

                explanation.recommendedAction = getRecommendedAction(riskScore, classification,
                                "• Enable UDP rate limiting on your firewall\n" +
                                                "• Disable ICMP port unreachable responses\n" +
                                                "• Block unnecessary UDP ports\n" +
                                                "• Implement traffic shaping/QoS\n" +
                                                "• Consider upstream filtering at ISP level");

                explanation.reassurance = getReassurance(riskScore,
                                "ThreatScope is monitoring this UDP flood. Your firewall can help filter this traffic.",
                                "This attack is being tracked. Implement rate limiting to reduce impact.",
                                "This is a significant bandwidth attack. Take immediate action to filter traffic.");

                return explanation;
        }

        private static EnhancedExplanation explainIcmpFlood(UiSecurityEvent event) {
                String sourceIp = event.getSourceIp();
                int riskScore = event.getRiskScore();
                String classification = event.getClassification();

                EnhancedExplanation explanation = new EnhancedExplanation();

                explanation.whatHappened = String.format(
                                "🚨 ICMP Flood Attack Detected\n\n" +
                                                "Source: %s\n" +
                                                "Attack Type: ICMP Flood (Ping Flood)\n" +
                                                "Classification: %s\n\n" +
                                                "An attacker is sending excessive ICMP echo requests (pings) to your system, "
                                                +
                                                "attempting to consume bandwidth and processing resources.",
                                sourceIp, classification);

                explanation.whyMatters = String.format(
                                "💡 Impact Assessment\n\n" +
                                                "ICMP Flood attacks can:\n" +
                                                "• Consume network bandwidth\n" +
                                                "• Increase CPU usage (processing ping responses)\n" +
                                                "• Slow down legitimate network operations\n" +
                                                "• Mask other attack activities\n\n" +
                                                "Current Risk: %s (%d/100)\n" +
                                                "Classification: %s - %s",
                                getRiskLevel(riskScore), riskScore,
                                classification, getClassificationMeaning(classification));

                explanation.howItWorks = "📚 Understanding ICMP Flood Attacks\n\n" +
                                "ICMP (Internet Control Message Protocol) is used for diagnostics:\n" +
                                "• Ping uses ICMP echo request/reply\n" +
                                "• Normally used to test connectivity\n" +
                                "• Legitimate use: a few pings per second\n\n" +
                                "In an ICMP flood:\n" +
                                "1. Attacker sends thousands of ping requests\n" +
                                "2. Your system tries to respond to each\n" +
                                "3. Bandwidth and CPU are consumed\n" +
                                "4. Legitimate traffic is delayed\n\n" +
                                "It's like someone repeatedly asking \"Are you there?\" thousands of times - " +
                                "you waste time responding instead of doing real work.";

                explanation.technicalDetails = String.format(
                                "🔧 Technical Analysis\n\n" +
                                                "Source IP: %s\n" +
                                                "Protocol: ICMP\n" +
                                                "Attack Vector: Excessive ICMP echo requests\n" +
                                                "ICMP Packet Rate: Threshold exceeded\n" +
                                                "Detection Method: ICMP-specific rate monitoring\n\n" +
                                                "The system detected an abnormally high rate of ICMP packets " +
                                                "from this IP address.",
                                sourceIp);

                explanation.recommendedAction = getRecommendedAction(riskScore, classification,
                                "• Enable ICMP rate limiting on your firewall\n" +
                                                "• Consider disabling ICMP responses (if not needed)\n" +
                                                "• Block the source IP if attacks persist\n" +
                                                "• Implement ICMP flood protection rules\n" +
                                                "• Monitor for other attack types (ICMP may be a distraction)");

                explanation.reassurance = getReassurance(riskScore,
                                "ThreatScope is monitoring this ICMP flood. ICMP can be safely rate-limited.",
                                "This attack is being tracked. Limit ICMP responses to reduce impact.",
                                "This is a sustained ping flood. Take action to filter ICMP traffic.");

                return explanation;
        }

        private static EnhancedExplanation explainPortScan(UiSecurityEvent event) {
                String sourceIp = event.getSourceIp();
                int riskScore = event.getRiskScore();
                String classification = event.getClassification();

                EnhancedExplanation explanation = new EnhancedExplanation();

                explanation.whatHappened = String.format(
                                "🔍 Port Scan Detected\n\n" +
                                                "Source: %s\n" +
                                                "Attack Type: Network Reconnaissance\n" +
                                                "Classification: %s\n\n" +
                                                "A remote computer is systematically probing multiple ports on your system, "
                                                +
                                                "attempting to discover which services are running and potentially vulnerable.",
                                sourceIp, classification);

                explanation.whyMatters = String.format(
                                "💡 Impact Assessment\n\n" +
                                                "Port scans are reconnaissance activities that:\n" +
                                                "• Map your network services and open ports\n" +
                                                "• Identify potential vulnerabilities\n" +
                                                "• Often precede actual attacks\n" +
                                                "• Indicate someone is actively targeting your system\n\n" +
                                                "Current Risk: %s (%d/100)\n" +
                                                "Classification: %s - %s\n\n" +
                                                "⚠️ Port scans are often the first step in a multi-stage attack.",
                                getRiskLevel(riskScore), riskScore,
                                classification, getClassificationMeaning(classification));

                explanation.howItWorks = "📚 Understanding Port Scans\n\n" +
                                "Attackers use port scans to:\n" +
                                "1. Send connection requests to many ports\n" +
                                "2. See which ports respond (open services)\n" +
                                "3. Identify what software is running\n" +
                                "4. Look for known vulnerabilities\n" +
                                "5. Plan their attack strategy\n\n" +
                                "It's like a burglar checking every door and window to see which ones are unlocked - " +
                                "they're planning their entry point.";

                explanation.technicalDetails = String.format(
                                "🔧 Technical Analysis\n\n" +
                                                "Source IP: %s\n" +
                                                "Scan Type: Multiple port access detected\n" +
                                                "Ports Targeted: Multiple unique destination ports\n" +
                                                "Detection Method: Time-window port aggregation\n\n" +
                                                "The system detected access to multiple unique ports within a short time window, "
                                                +
                                                "indicating systematic reconnaissance activity.",
                                sourceIp);

                explanation.recommendedAction = getRecommendedAction(riskScore, classification,
                                "• Review which ports are open on your system\n" +
                                                "• Close unnecessary ports and services\n" +
                                                "• Enable firewall logging for this IP\n" +
                                                "• Consider blocking the source IP\n" +
                                                "• Monitor for follow-up attack attempts\n" +
                                                "• Update and patch all exposed services");

                explanation.reassurance = getReassurance(riskScore,
                                "Port scans are common internet activity. ThreatScope is monitoring for actual attacks.",
                                "This reconnaissance activity is being tracked. Secure your open ports as recommended.",
                                "This scan indicates active targeting. Take immediate steps to secure your system.");

                return explanation;
        }

        private static EnhancedExplanation explainBruteForce(UiSecurityEvent event) {
                String sourceIp = event.getSourceIp();
                int riskScore = event.getRiskScore();
                String classification = event.getClassification();

                EnhancedExplanation explanation = new EnhancedExplanation();

                explanation.whatHappened = String.format(
                                "🔐 Brute Force Attack Detected\n\n" +
                                                "Source: %s\n" +
                                                "Attack Type: Credential Guessing\n" +
                                                "Classification: %s\n\n" +
                                                "An attacker is making repeated connection attempts to a service, " +
                                                "likely trying to guess passwords or exploit authentication mechanisms.",
                                sourceIp, classification);

                explanation.whyMatters = String.format(
                                "💡 Impact Assessment\n\n" +
                                                "Brute force attacks can:\n" +
                                                "• Compromise user accounts if passwords are weak\n" +
                                                "• Lock out legitimate users (account lockouts)\n" +
                                                "• Consume authentication server resources\n" +
                                                "• Indicate targeted attack on your system\n\n" +
                                                "Current Risk: %s (%d/100)\n" +
                                                "Classification: %s - %s",
                                getRiskLevel(riskScore), riskScore,
                                classification, getClassificationMeaning(classification));

                explanation.howItWorks = "📚 Understanding Brute Force Attacks\n\n" +
                                "Attackers use brute force to:\n" +
                                "1. Try many username/password combinations\n" +
                                "2. Use common passwords (123456, password, etc.)\n" +
                                "3. Use dictionary words and variations\n" +
                                "4. Continue until they find valid credentials\n\n" +
                                "It's like trying every key on a keyring until one opens the lock.";

                explanation.technicalDetails = String.format(
                                "🔧 Technical Analysis\n\n" +
                                                "Source IP: %s\n" +
                                                "Attack Pattern: Repeated connection attempts\n" +
                                                "Target Service: Authentication endpoint\n" +
                                                "Detection Method: Failed connection rate monitoring\n\n" +
                                                "The system detected multiple rapid connection attempts from this IP, "
                                                +
                                                "indicating automated password guessing.",
                                sourceIp);

                explanation.recommendedAction = getRecommendedAction(riskScore, classification,
                                "• Enable account lockout policies\n" +
                                                "• Implement rate limiting on login attempts\n" +
                                                "• Use strong, unique passwords\n" +
                                                "• Enable multi-factor authentication (MFA)\n" +
                                                "• Block the source IP\n" +
                                                "• Review authentication logs for compromised accounts");

                explanation.reassurance = getReassurance(riskScore,
                                "Brute force attempts are common. Strong passwords and MFA provide good protection.",
                                "This attack is being monitored. Implement rate limiting and account lockouts.",
                                "This is an active credential attack. Secure your authentication immediately.");

                return explanation;
        }

        private static EnhancedExplanation explainBackdoorAttempt(UiSecurityEvent event) {
                String sourceIp = event.getSourceIp();
                int riskScore = event.getRiskScore();
                String classification = event.getClassification();

                EnhancedExplanation explanation = new EnhancedExplanation();

                explanation.whatHappened = String.format(
                                "🕵️ Backdoor Connection Attempt\n\n" +
                                                "Source: %s\n" +
                                                "Attack Type: Malicious Port Access\n" +
                                                "Classification: %s\n\n" +
                                                "An external computer attempted to connect to a known malicious \"backdoor\" port "
                                                +
                                                "commonly used by trojans, malware, or remote access tools (RATs).",
                                sourceIp, classification);

                explanation.whyMatters = String.format(
                                "💡 Impact Assessment\n\n" +
                                                "Backdoor connection attempts often indicate:\n" +
                                                "• Presence of malware/trojans on your system\n" +
                                                "• Remote Control (C&C) communication attempts\n" +
                                                "• Active exploitation of known vulnerabilities\n" +
                                                "• Serious security compromise if successful\n\n" +
                                                "Current Risk: %s (%d/100)\n" +
                                                "Classification: %s - %s",
                                getRiskLevel(riskScore), riskScore,
                                classification, getClassificationMeaning(classification));

                explanation.howItWorks = "📚 Understanding Backdoors\n\n" +
                                "A \"backdoor\" is a hidden entry point into a computer system:\n" +
                                "• Often installed by malware (trojans)\n" +
                                "• Listens on specific ports (e.g., 31337 for BackOrifice)\n" +
                                "• Allows attackers to bypass normal authentication\n" +
                                "• Gives full remote control to the attacker\n\n" +
                                "It's like finding a stranger trying to open a hidden trapdoor in your house.";

                explanation.technicalDetails = String.format(
                                "🔧 Technical Analysis\n\n" +
                                                "Source IP: %s\n" +
                                                "Target Port: Known malicious port (e.g. 31337, 12345)\n" +
                                                "Signature: Connection request to blacklisted port\n" +
                                                "Detection Method: Port signature matching\n\n" +
                                                "The system detected a connection set-up packet destined for a port " +
                                                "known to be associated with specific malware families.",
                                sourceIp);

                explanation.recommendedAction = getRecommendedAction(riskScore, classification,
                                "• Run a full system antivirus scan immediately\n" +
                                                "• Check for open listening ports (netstat -an)\n" +
                                                "• Verify if any legitimate process uses this port\n" +
                                                "• Block the source IP at your firewall\n" +
                                                "• Investigate the source IP reputation\n" +
                                                "• Check internal machines for signs of infection");

                explanation.reassurance = getReassurance(riskScore,
                                "ThreatScope blocked the attempt. Ensure your system is clean of malware.",
                                "This is suspicious. A malware scan is highly recommended.",
                                "CRITICAL: Potential active malware infection. Isolate system and scan immediately.");

                return explanation;
        }

        private static EnhancedExplanation explainSuspiciousPattern(UiSecurityEvent event) {
                String sourceIp = event.getSourceIp();
                int riskScore = event.getRiskScore();
                String classification = event.getClassification();

                EnhancedExplanation explanation = new EnhancedExplanation();

                explanation.whatHappened = String.format(
                                "🧿 Suspicious Traffic Pattern\n\n" +
                                                "Source: %s\n" +
                                                "Attack Type: Anomaly / Evasion Technique\n" +
                                                "Classification: %s\n\n" +
                                                "The system detected network packets with unusual characteristics " +
                                                "(e.g., invalid TCP flag combinations) often used to evade detection or crash systems.",
                                sourceIp, classification);

                explanation.whyMatters = String.format(
                                "💡 Impact Assessment\n\n" +
                                                "Suspicious patterns often indicate:\n" +
                                                "• Advanced reconnaissance (Stealth Scans)\n" +
                                                "• Firewalking (mapping firewall rules)\n" +
                                                "• OS Fingerprinting attempts\n" +
                                                "• Broken or malicious network stacks\n\n" +
                                                "Current Risk: %s (%d/100)\n" +
                                                "Classification: %s - %s",
                                getRiskLevel(riskScore), riskScore,
                                classification, getClassificationMeaning(classification));

                explanation.howItWorks = "📚 Understanding Stealth Scans\n\n" +
                                "Attackers use malformed packets to trick firewalls:\n" +
                                "• Null Scan: Sending a packet with NO flags set (illegal)\n" +
                                "• Xmas Scan: Lighting up all flags like a Christmas tree\n" +
                                "• These packets shouldn't exist in normal traffic\n" +
                                "• Different OSes respond differently, revealing their type\n\n" +
                                "It's like someone knocking on your door in a weird, secret code to see who answers.";

                explanation.technicalDetails = String.format(
                                "🔧 Technical Analysis\n\n" +
                                                "Source IP: %s\n" +
                                                "Pattern Type: TCP Flag Anomaly (Null/Xmas/FIN-only)\n" +
                                                "Protocol Compliance: Violation of RFC standards\n" +
                                                "Detection Method: Header flag analysis\n\n" +
                                                "The detected packet violates standard TCP/IP protocol rules, " +
                                                "which strongly suggests malicious intent or a broken implementation.",
                                sourceIp);

                explanation.recommendedAction = getRecommendedAction(riskScore, classification,
                                "• Block the source IP address\n" +
                                                "• Ensure your firewall is configured to drop invalid packets\n" +
                                                "• Monitor for subsequent attack attempts\n" +
                                                "• No response is usually the best defense against mapping");

                explanation.reassurance = getReassurance(riskScore,
                                "ThreatScope dropped the invalid packets. Your system is protected.",
                                "This is technical probing. Your firewall likely blocked it.",
                                "Persistent stealth scanning detected. Ensure firewall rules are strict.");

                return explanation;
        }

        private static EnhancedExplanation explainGenericThreat(UiSecurityEvent event) {
                String sourceIp = event.getSourceIp();
                String threatType = event.getThreatType();
                int riskScore = event.getRiskScore();
                String classification = event.getClassification();

                EnhancedExplanation explanation = new EnhancedExplanation();

                explanation.whatHappened = String.format(
                                "⚠️ Security Event Detected\n\n" +
                                                "Source: %s\n" +
                                                "Event Type: %s\n" +
                                                "Classification: %s\n\n" +
                                                "Unusual network activity has been detected from this IP address.",
                                sourceIp, threatType, classification);

                explanation.whyMatters = String.format(
                                "💡 Impact Assessment\n\n" +
                                                "This activity may indicate:\n" +
                                                "• Reconnaissance or probing\n" +
                                                "• Automated scanning\n" +
                                                "• Potential security threat\n\n" +
                                                "Current Risk: %s (%d/100)\n" +
                                                "Classification: %s - %s",
                                getRiskLevel(riskScore), riskScore,
                                classification, getClassificationMeaning(classification));

                explanation.howItWorks = "📚 Understanding This Event\n\n" +
                                "ThreatScope detected unusual network patterns that don't match normal behavior. " +
                                "This could be legitimate activity or a security concern that requires monitoring.";

                explanation.technicalDetails = String.format(
                                "🔧 Technical Analysis\n\n" +
                                                "Source IP: %s\n" +
                                                "Event Type: %s\n" +
                                                "Protocol: %s\n" +
                                                "Detection Method: Pattern analysis\n\n" +
                                                "The system detected unusual network activity from this source.",
                                sourceIp, threatType, event.getProtocol());

                explanation.recommendedAction = getRecommendedAction(riskScore, classification,
                                "• Monitor this IP for continued activity\n" +
                                                "• Review firewall logs\n" +
                                                "• Verify if this is legitimate traffic\n" +
                                                "• Consider blocking if activity persists");

                explanation.reassurance = getReassurance(riskScore,
                                "ThreatScope is monitoring this activity. Most events are benign.",
                                "This event requires attention. Follow recommended monitoring procedures.",
                                "This event indicates potential threat. Take protective action.");

                return explanation;
        }

        // ========== Helper Methods ==========

        private static String getRiskLevel(int riskScore) {
                if (riskScore < 30)
                        return "Low";
                if (riskScore < 50)
                        return "Moderate";
                if (riskScore < 70)
                        return "High";
                return "Critical";
        }

        private static String getClassificationMeaning(String classification) {
                switch (classification) {
                        case "TRUSTED":
                                return "known safe source, likely false positive";
                        case "BENIGN_NOISE":
                                return "normal internet background noise";
                        case "SUSPICIOUS":
                                return "unusual activity requiring monitoring";
                        case "CONFIRMED_THREAT":
                                return "verified malicious activity";
                        default:
                                return "classification pending";
                }
        }

        private static String getRecommendedAction(int riskScore, String classification, String actions) {
                String urgency;
                if (riskScore < 30) {
                        urgency = "⏱️ Priority: Low - Monitor as needed\n\n";
                } else if (riskScore < 70) {
                        urgency = "⚠️ Priority: Medium - Take action soon\n\n";
                } else {
                        urgency = "🚨 Priority: HIGH - Take immediate action\n\n";
                }

                return urgency + "Recommended Actions:\n" + actions;
        }

        private static String getReassurance(int riskScore, String lowRisk, String medRisk, String highRisk) {
                if (riskScore < 30) {
                        return "✅ " + lowRisk;
                } else if (riskScore < 70) {
                        return "⚠️ " + medRisk;
                } else {
                        return "🚨 " + highRisk;
                }
        }

        // ========== Enhanced Explanation Data Class ==========

        /**
         * Container for enhanced explanation data
         */
        public static class EnhancedExplanation {
                public String whatHappened = "";
                public String whyMatters = "";
                public String howItWorks = "";
                public String technicalDetails = "";
                public String recommendedAction = "";
                public String reassurance = "";

                public static EnhancedExplanation empty() {
                        EnhancedExplanation exp = new EnhancedExplanation();
                        exp.whatHappened = "WHAT HAPPENED\nNo event selected";
                        exp.whyMatters = "WHY THIS MATTERS\nN/A";
                        exp.howItWorks = "HOW IT WORKS\nN/A";
                        exp.technicalDetails = "TECHNICAL DETAILS\nN/A";
                        exp.recommendedAction = "RECOMMENDED ACTION\nN/A";
                        exp.reassurance = "REASSURANCE\nYour system is currently safe.";
                        return exp;
                }
        }
}
