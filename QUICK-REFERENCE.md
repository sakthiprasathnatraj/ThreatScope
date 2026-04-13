# ThreatScope v1.0 - Quick Reference Card

## 🎯 CRITICAL CHANGES AT A GLANCE

### 1. Packet Filtering (PacketSniffer.java)
```java
// NEW: Ignore NIC offloading noise
if (ipPacket.getHeader().getTotalLength() == 0) {
    return;
}
```

### 2. Port Scan Detection (ThreatDetector.java)
**OLD:** Simple port count threshold  
**NEW:** Time-window based (10 ports in 10 seconds)

```java
private static final Map<String, Set<Integer>> portMap;
private static final Map<String, Long> timestampMap;
private static final long TIME_WINDOW_MS = 10_000;

// Auto-cleanup expired windows
if (now - lastSeen > TIME_WINDOW_MS) {
    portMap.remove(srcIp);
    timestampMap.remove(srcIp);
}
```

### 3. System State Thresholds (SystemStateEngine.java)
**OLD:** CRITICAL at risk >= 80 + HIGH confidence  
**NEW:** CRITICAL at risk >= 70 + HIGH confidence

```
SAFE     : risk < 40
OBSERVE  : risk >= 40
WARNING  : risk >= 60
CRITICAL : risk >= 70 AND confidence == HIGH
```

### 4. Output Format (All Files)
**OLD:** Box-drawing characters (╔═╗)  
**NEW:** Simple dashes (----)

```
----------------------------------------
ThreatScope v1.0
Live Packet Capture + Threat Detection
----------------------------------------

[STATE CHANGE]
SYSTEM STATE: SAFE → WARNING

[SECURITY EVENT]
Source IP   : 192.168.1.100
Type        : PORT_SCAN
Description : Multiple destination ports accessed (12)
Risk Score  : 65
Confidence  : MEDIUM
----------------------------------------
```

## 🔒 ANTI-SPAM MECHANISMS

1. **AlertSuppressor:** 30-second cooldown per (IP + attack type)
2. **Time-window cleanup:** Auto-removes expired port tracking
3. **Local IP filtering:** Ignores 127.0.0.1, 192.168.*, 10.*, 172.*
4. **Packet validation:** Skips null, non-IPv4, zero-length packets

## 📊 DETECTION FLOW

```
Packet Received
    ↓
IPv4 only? → NO → Skip
    ↓ YES
Total length > 0? → NO → Skip (NIC offload)
    ↓ YES
Local IP? → YES → Skip
    ↓ NO
Extract destination port
    ↓
Update time window
    ↓
Port count >= 10? → NO → Continue monitoring
    ↓ YES
Cooldown active? → YES → Skip (already alerted)
    ↓ NO
Calculate risk & confidence
    ↓
Update system state
    ↓
Emit security event
    ↓
Reset tracking for this IP
```

## 🚀 QUICK START

```bash
# Compile
mvn compile

# Run
mvn exec:java -Dexec.mainClass="com.threatscope.Main"

# Or use batch file
.\build-and-run.bat
```

## 🎓 DEMO TALKING POINTS

1. **Time-window detection** prevents false positives from slow scans
2. **Confidence-based escalation** ensures CRITICAL is meaningful
3. **Automatic cleanup** prevents memory leaks
4. **Clean output** makes it easy to understand what's happening
5. **No spam** - one alert per IP per 30 seconds max

## ✅ VERIFICATION CHECKLIST

- [ ] Project compiles without errors
- [ ] No pcap4j debug logs visible
- [ ] Clean startup banner displayed
- [ ] Interface list shows correctly
- [ ] Port scan detection works (10 ports in 10 seconds)
- [ ] No duplicate alerts within 30 seconds
- [ ] State transitions are clear
- [ ] Output format matches specification
- [ ] No CRITICAL spam
- [ ] Local traffic ignored

## 🔧 TROUBLESHOOTING

**Problem:** Maven clean fails  
**Solution:** Files locked by IDE, use `mvn compile` instead

**Problem:** No interfaces found  
**Solution:** Run as administrator (packet capture requires privileges)

**Problem:** Too many alerts  
**Solution:** Check AlertSuppressor cooldown (30 sec default)

**Problem:** No alerts at all  
**Solution:** Verify interface index in Main.java (default: 4)

## 📝 FILES MODIFIED

1. `PacketSniffer.java` - Noise filtering, output format
2. `ThreatDetector.java` - Time-window detection (complete rewrite)
3. `SystemStateEngine.java` - CRITICAL threshold, output format
4. `IncidentCorrelator.java` - Output format
5. `Main.java` - Startup banner format

## 🎯 VERSION INFO

**Version:** 1.0 FINAL  
**Build Date:** 2026-02-03  
**Status:** ✅ STABLE & DEMO-READY  
**Java:** 8  
**Framework:** pcap4j
