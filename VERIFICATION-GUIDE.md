# ThreatScope - Quick Verification Guide

## ✅ Pre-Flight Checklist

Before running, verify these files exist:

### 1. Logback Configuration
```
src/main/resources/logback.xml
```
**Purpose**: Suppresses pcap4j debug logs

### 2. Modified Java Files
- ✅ `src/main/java/com/threatscope/Main.java`
- ✅ `src/main/java/com/threatscope/core/capture/PacketSniffer.java`
- ✅ `src/main/java/com/threatscope/core/detect/ThreatDetector.java`
- ✅ `src/main/java/com/threatscope/core/correlate/IncidentCorrelator.java`
- ✅ `src/main/java/com/threatscope/core/risk/SystemStateEngine.java`

### 3. Unchanged Files (working as designed)
- ✅ `src/main/java/com/threatscope/core/detect/AlertSuppressor.java`

---

## 🚀 How to Run

### Option 1: Using IntelliJ IDEA
1. Open project in IntelliJ
2. Navigate to `src/main/java/com/threatscope/Main.java`
3. Right-click → Run 'Main.main()'
4. **IMPORTANT**: Run as Administrator (required for packet capture)

### Option 2: Using Maven Command Line
```bash
cd d:\Sakthi\Java\ThreatScope
mvn clean compile
mvn exec:java -Dexec.mainClass="com.threatscope.Main"
```

### Option 3: Using Batch Script
```bash
cd d:\Sakthi\Java\ThreatScope
build-and-run.bat
```

---

## 🔍 What to Look For

### ✅ GOOD - Clean Startup
```
╔════════════════════════════════════════════════════════════╗
║                    ThreatScope v1.0                        ║
║          Live Packet Capture + Threat Detection            ║
╚════════════════════════════════════════════════════════════╝

📋 Available Network Interfaces:
[0] Intel(R) Wi-Fi 6 AX201 160MHz
[1] Realtek PCIe GbE Family Controller
...

🚀 Starting packet capture...
✅ Monitoring: Intel(R) Wi-Fi 6 AX201 160MHz
📡 Listening for threats...
```

### ❌ BAD - Debug Spam (Should NOT appear)
```
Ipv4Packet - Total Length is 0...
org.pcap4j.packet.IpV4Packet - ...
DEBUG org.pcap4j...
```

### ✅ GOOD - Single Alert Per Attack
```
╔════════════════════════════════════════════════════════════╗
║              🔵 SECURITY EVENT DETECTED                    ║
╠════════════════════════════════════════════════════════════╣
║ Source IP    : 192.168.1.100                               ║
║ Type         : PORT_SCAN                                   ║
║ Description  : Multiple destination ports accessed (15)    ║
║ Severity     : MEDIUM                                      ║
║ Risk Score   : 65                                          ║
║ Confidence   : MEDIUM                                      ║
╚════════════════════════════════════════════════════════════╝

🧠 SYSTEM STATE TRANSITION: SAFE → WARNING
```

### ❌ BAD - Repeated Alerts (Should NOT happen within 30 seconds)
```
🔵 SECURITY EVENT
Source IP   : 192.168.1.100
...

🔵 SECURITY EVENT
Source IP   : 192.168.1.100
...

🔵 SECURITY EVENT
Source IP   : 192.168.1.100
...
```

---

## 🧪 Testing Scenarios

### Test 1: Clean Startup
**Expected**: No debug logs, clean banner, interface list

### Test 2: No Traffic
**Expected**: Silent operation, no spam

### Test 3: Normal Traffic
**Expected**: Silent operation (local traffic filtered)

### Test 4: Port Scan Detection
**Trigger**: External IP accessing 10+ unique ports
**Expected**: 
- ONE alert appears
- System state transitions to WARNING or OBSERVE
- No duplicate alerts for 30 seconds

### Test 5: Repeated Port Scan (within 30 seconds)
**Trigger**: Same IP scans again immediately
**Expected**: NO new alert (suppressed)

### Test 6: Repeated Port Scan (after 30 seconds)
**Trigger**: Same IP scans again after cooldown
**Expected**: New alert appears (cooldown expired)

---

## 🐛 Troubleshooting

### Problem: "No network interfaces found"
**Solution**: 
- Run as Administrator
- Install WinPcap or Npcap
- Check network adapters are enabled

### Problem: "Invalid interface index: 4"
**Solution**:
- Check available interfaces in startup output
- Update `Main.java` line 31 with correct index
- Example: Change `PacketSniffer.startSniffing(4);` to `PacketSniffer.startSniffing(0);`

### Problem: Still seeing pcap4j debug logs
**Solution**:
1. Verify `logback.xml` exists in `src/main/resources/`
2. Run `mvn clean compile` to rebuild
3. Check Maven output for logback dependency

### Problem: Alerts still repeating
**Solution**:
1. Check `AlertSuppressor.java` exists
2. Verify `ThreatDetector.java` calls `AlertSuppressor.shouldSuppress()`
3. Check cooldown period (default: 30 seconds)

### Problem: No alerts appearing at all
**Solution**:
1. Verify external traffic is reaching the interface
2. Check PORT_SCAN_THRESHOLD (default: 10 ports)
3. Ensure source IP is not in local IP range (192.168.x.x, 10.x.x.x, etc.)

---

## 📝 Configuration

### Change Network Interface
Edit `Main.java` line 31:
```java
PacketSniffer.startSniffing(0);  // Change index here
```

### Change Alert Cooldown
Edit `AlertSuppressor.java`:
```java
private static final long COOLDOWN_MS = 30_000;  // milliseconds
```

### Change Port Scan Threshold
Edit `ThreatDetector.java`:
```java
private static final int PORT_SCAN_THRESHOLD = 10;  // number of ports
```

---

## ✅ Success Criteria

Your fixes are working correctly if:

1. ✅ Clean startup with no debug logs
2. ✅ Professional-looking output with box characters
3. ✅ ONE alert per attack scenario
4. ✅ No repeated alerts within cooldown window
5. ✅ System state transitions are clean (no spam)
6. ✅ Application runs stably without crashes

---

## 📚 Additional Documentation

- Full fix details: `ALERT-SYSTEM-FIXES.md`
- Architecture overview: See "Architecture Overview" section in ALERT-SYSTEM-FIXES.md
- Original requirements: See top of this conversation

---

## 🎯 Next Steps

1. Run the application using one of the methods above
2. Verify clean startup
3. Monitor for a few minutes
4. Trigger a port scan (if possible)
5. Verify alert behavior

**If everything looks good, you're done! 🎉**
