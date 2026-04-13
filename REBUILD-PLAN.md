# ThreatScope Professional Backend Rebuild - Master Plan

**Date:** 2026-02-09  
**Objective:** Professional desktop backend architecture rebuild  
**Status:** IN PROGRESS

---

## 🎯 REBUILD OBJECTIVES

### Core Goals
1. **Professional Architecture** - Clean layered backend with clear separation
2. **Accurate Threat Detection** - Real threats only, no false panic
3. **User-Centric Design** - Explain-before-alert philosophy
4. **Academic Quality** - Demo-ready, paper-ready, viva-ready
5. **Desktop Backend** - NO web code, frontend-ready APIs

### Critical Problems to Fix
- ❌ False positives from CDN/trusted IPs
- ❌ Event spam and duplicate alerts
- ❌ Scattered output across multiple classes
- ❌ Inconsistent package structure
- ❌ Missing proper decode/classify layers

---

## 🏗 TARGET ARCHITECTURE

```
com.threatscope
 ├── capture        → Packet capture ONLY (Pcap4J wrapper)
 ├── decode         → Extract packet fields (IP, port, protocol)
 ├── classify       → Traffic classification (trusted/benign/suspicious/threat)
 ├── detect         → Attack detection logic (port scan, brute force)
 ├── correlate      → Event aggregation & deduplication
 ├── risk           → Risk scoring & confidence evaluation
 ├── explain        → Human-friendly explanation engine
 ├── model          → Domain models (SecurityEvent, enums)
 ├── state          → System state machine (SAFE→OBSERVE→WARNING→CRITICAL)
 └── output         → SINGLE controlled output layer
```

---

## 📋 IMPLEMENTATION PHASES

### Phase 1: Package Restructuring ✅
- Create proper package structure
- Move existing classes to correct packages
- Ensure clean layer boundaries

### Phase 2: Decode Layer (NEW)
- Create `PacketDecoder` - extract IP, port, protocol
- Separate decoding from capture logic
- Clean data structures for decoded packets

### Phase 3: Classify Layer (ENHANCE)
- Move `TrafficClassifier` to classify package
- Add `IPReputationDatabase` with real CDN/cloud ranges
- Implement whitelist/blacklist logic
- Add ASN-based classification

### Phase 4: Detect Layer (REFACTOR)
- Clean detection rules (port scan, brute force)
- Time-windowed pattern detection
- NO per-packet alerts
- Evidence-based thresholds

### Phase 5: Correlate Layer (NEW)
- Event aggregation by (IP + threat type)
- Cooldown mechanism
- Rolling evidence counters
- Single event per time window

### Phase 6: Risk Engine (ENHANCE)
- Gradual risk escalation (0-100)
- Classification-based risk capping
- Confidence evaluation (LOW/MEDIUM/HIGH)
- Historical risk tracking

### Phase 7: Explanation Engine (ENHANCE)
- Simple explanations for non-technical users
- Technical details for advanced users
- Reassurance messages for benign traffic
- Clear action recommendations

### Phase 8: State Engine (REFACTOR)
- SAFE → OBSERVE → WARNING → CRITICAL
- Anti-flapping mechanism
- Confidence-gated transitions
- State change announcements

### Phase 9: Output Layer (NEW - CRITICAL)
- **SINGLE OUTPUT GATEWAY**
- Formatted console output
- Professional security tool appearance
- No scattered prints

---

## 🛡️ FALSE POSITIVE PREVENTION

### Multi-Layer Filtering
1. **Packet Layer**
   - Ignore private IP ranges (RFC 1918)
   - Filter loopback traffic
   - Validate packet structure

2. **Classification Layer**
   - Whitelist trusted ASNs (Cloudflare, Google, AWS, Azure)
   - Identify research scanners (Shodan, Censys)
   - Context-aware classification

3. **Detection Layer**
   - Time-windowed aggregation (not per-packet)
   - Pattern-based (not single event)
   - Behavioral thresholds

4. **Risk Layer**
   - Classification-based risk caps
   - Gradual escalation only
   - Confidence requirements for CRITICAL

---

## 🔍 THREAT DETECTION RULES

### Port Scan Detection
```
Trigger Conditions:
- Same source IP
- ≥ 10 unique destination ports
- Within 10-second window
- NOT from trusted/benign sources

Classification:
- < 15 ports → BENIGN_NOISE (cap risk at 40)
- 15-25 ports → SUSPICIOUS (cap risk at 70)
- > 40 ports + fast → CONFIRMED_THREAT (cap risk at 100)
```

### Brute Force Detection (Future)
```
Trigger Conditions:
- Same source IP
- ≥ 20 connection attempts
- Same destination port
- Within 30-second window

Classification:
- < 20 attempts → BENIGN_NOISE
- 20-50 attempts → SUSPICIOUS
- > 50 attempts → CONFIRMED_THREAT
```

---

## 📊 RISK & CONFIDENCE MODEL

### Risk Calculation
```
Raw Risk = Base Risk + (Evidence Count × Multiplier)
Capped Risk = min(Raw Risk, Classification.maxRisk)

Base Risk:
- PORT_SCAN: 40
- BRUTE_FORCE: 50

Multiplier: 2 per evidence item
```

### Confidence Levels
```
HIGH:    Risk ≥ 70 AND Evidence ≥ 10
MEDIUM:  Risk ≥ 40 OR Evidence ≥ 5
LOW:     Everything else
```

### State Transitions
```
SAFE:     Risk < 30
OBSERVE:  Risk ≥ 30
WARNING:  Risk ≥ 50
CRITICAL: Risk ≥ 70 AND Confidence = HIGH
```

---

## 📤 OUTPUT FORMAT

### Console Output Example
```
========================================
SECURITY OBSERVATION
========================================

WHAT HAPPENED:
An external computer (IP: 198.20.69.42) attempted to connect to 12 different 
services on your computer. This appears to be automated internet scanning, 
which is very common.

WHY THIS MATTERS:
This type of activity is extremely common on the internet. Thousands of 
automated scanners probe random computers every day for research purposes. 
This is similar to someone checking if your door is locked - annoying, but 
not dangerous. Your computer's firewall is designed to handle this. This 
does NOT indicate a targeted attack.

CLASSIFICATION: Common Internet Noise
RISK LEVEL: Low (25/100)
CONFIDENCE: MEDIUM
SYSTEM STATE: SAFE

RECOMMENDED ACTION:
No action needed. We are monitoring the situation.

REASSURANCE:
This activity does not indicate your computer is under attack. All 
internet-connected devices experience this type of scanning regularly. 
Your firewall is protecting you. We will alert you if the behavior 
escalates or becomes more aggressive.
========================================
```

---

## ✅ SUCCESS CRITERIA

- [ ] Clean layered architecture
- [ ] All classes in correct packages
- [ ] NO detection logic in capture layer
- [ ] NO scattered output (single gateway only)
- [ ] Trusted IPs never trigger CRITICAL
- [ ] CDN traffic properly classified
- [ ] Event aggregation prevents spam
- [ ] Gradual risk escalation
- [ ] User-friendly explanations
- [ ] Professional console output
- [ ] Compiles without errors
- [ ] Ready for frontend integration

---

**Prepared by:** Antigravity AI  
**Project:** ThreatScope Professional Backend Rebuild
