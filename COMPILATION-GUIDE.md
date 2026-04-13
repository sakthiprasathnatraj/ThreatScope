# ThreatScope v2.0 - Compilation & Testing Guide

## 🔧 COMPILATION

### Step 1: Clean and Compile

Open a terminal in the ThreatScope directory and run:

```bash
cd d:\Sakthi\Java\ThreatScope
mvn clean compile
```

**Expected Output:**
```
[INFO] BUILD SUCCESS
[INFO] Compiling 25 source files
[INFO] Total time: ~4-5 seconds
```

**If compilation fails**, check for:
1. Missing imports
2. Typos in class names
3. Package declaration issues

---

## 🧪 TESTING

### Step 2: Run the Application

```bash
mvn exec:java -Dexec.mainClass="com.threatscope.Main"
```

**Note:** You need administrator privileges for packet capture.

---

## 📊 EXPECTED BEHAVIOR

### Startup Output

```
========================================
    ThreatScope v1.0 - Academic Edition
    Host-Based Threat Monitoring System
========================================

Tech Stack: Java 8 + Pcap4J + Npcap
Architecture: Layered Desktop Backend
Detection: Event Aggregation + Gradual Risk

📋 Available Network Interfaces:
[list of interfaces]

⚙️  Configuration:
   - Edit Main.java to change interface index
   - Current interface: index 4
   - Ensure you have admin privileges
   - Ensure Npcap is installed

🔍 Starting threat monitoring...
   Listening for suspicious patterns...
   (Event aggregation active - no per-packet alerts)

========================================
```

### Example Security Observation (Benign Traffic)

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

## 🐛 TROUBLESHOOTING

### Compilation Errors

**Error:** "cannot find symbol: class TrafficClass"
**Fix:** Ensure `TrafficClass.java` is in `src/main/java/com/threatscope/core/model/`

**Error:** "package com.threatscope.core.explanation does not exist"
**Fix:** Ensure `ExplanationEngine.java` is in `src/main/java/com/threatscope/core/explanation/`

**Error:** "method portScan has wrong number of arguments"
**Fix:** Update all calls to `SecurityEvent.portScan()` to include classification and duration

### Runtime Errors

**Error:** "No network interfaces found"
**Fix:** Run as administrator (packet capture requires elevated privileges)

**Error:** "NullPointerException in TrafficClassifier"
**Fix:** Ensure classification is not null before using

---

## ✅ VERIFICATION CHECKLIST

After compilation and first run, verify:

- [ ] Project compiles without errors
- [ ] Application starts without crashes
- [ ] Network interfaces are listed
- [ ] Packet capture starts successfully
- [ ] Security observations use new user-friendly format
- [ ] TRUSTED traffic (if any) shows reassurance messages
- [ ] Risk scores are capped based on classification
- [ ] No false CRITICAL alerts from benign traffic

---

## 📝 TEST SCENARIOS

### Test 1: Trusted Traffic
**Setup:** Trigger traffic from Cloudflare IP (104.16.x.x)
**Expected:** Risk capped at 20, reassurance message, SAFE state

### Test 2: Benign Noise
**Setup:** Trigger traffic from Shodan scanner (198.20.69.x)
**Expected:** Risk capped at 40, "common internet scanning" message, SAFE/OBSERVE state

### Test 3: Suspicious Activity
**Setup:** Trigger 20 ports from unknown IP
**Expected:** Risk ~60, "monitoring unusual activity" message, WARNING state

### Test 4: Confirmed Threat
**Setup:** Trigger 50+ ports in < 5 seconds from unknown IP
**Expected:** Risk ~80-100, HIGH confidence, action recommendations, CRITICAL state (if sustained)

---

## 🔍 DEBUGGING TIPS

### Enable Debug Logging

Add to `Main.java` (after line 40):

```java
Logger.getLogger("com.threatscope").setLevel(Level.ALL);
```

### Check Classification

Add temporary debug output in `EventAggregator.java` (after line 107):

```java
System.out.println("[DEBUG] IP: " + srcIp + 
                   " | Classification: " + classification + 
                   " | Ports: " + uniquePortCount + 
                   " | Duration: " + duration + "ms");
```

### Verify Risk Capping

Add temporary debug output in `RiskEngine.java` (after line 97):

```java
System.out.println("[DEBUG] Raw Risk: " + newRisk + 
                   " | Max Allowed: " + maxAllowedRisk + 
                   " | Final Risk: " + Math.min(newRisk, maxAllowedRisk));
```

---

## 📚 DOCUMENTATION

For complete implementation details, see:
- `IMPLEMENTATION-COMPLETE.md` - Full implementation summary
- `IMPLEMENTATION-PLAN.md` - Original implementation plan
- `README.md` - Project overview

---

## 🎯 SUCCESS CRITERIA

✅ **Compilation:** All files compile without errors  
✅ **Execution:** Application runs without crashes  
✅ **Classification:** Traffic is correctly classified  
✅ **Risk Capping:** TRUSTED/BENIGN traffic capped appropriately  
✅ **User Output:** Messages are user-friendly and non-technical  
✅ **No False Alarms:** Benign traffic doesn't trigger CRITICAL  
✅ **Explanations:** All events include what/why/action  

---

**Prepared by:** Antigravity AI  
**Date:** 2026-02-05  
**Project:** ThreatScope v2.0 - User-Centric Edition
