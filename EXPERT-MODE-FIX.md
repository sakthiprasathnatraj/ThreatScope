# ThreatScope - Expert Mode Raw Activity Fix

## ✅ Changes Made

### 1. **Added Raw Activity Table to DashboardViewV2**
   - Created a new `rawActivitySection` that displays detailed packet information
   - Table includes: Time, Source IP, Dest IP, Src Port, Dst Port, Protocol, Size, Threat, Confidence
   - Section is hidden by default and appears when Expert Mode is toggled

### 2. **Updated DashboardControllerV2**
   - Expert Mode toggle now shows/hides the Raw Activity section
   - Raw Activity table is populated with the same event data as the timeline
   - Visual feedback: button changes color when Expert Mode is active

### 3. **Enhanced UiSecurityEvent Model**
   - Added new fields for raw packet data:
     - `destinationIp` - Destination IP address
     - `sourcePort` - Source port number
     - `destinationPort` - Destination port number
     - `protocol` - Network protocol (TCP/UDP)
     - `packetSize` - Packet size in bytes

### 4. **Updated BackendBridge** (Needs Manual Fix)
   - The `createMockEvent()` method needs to be updated to include the new fields

## ⚠️ Manual Fix Required

Due to line ending issues, you need to manually update ONE file:

### File: `src/main/java/com/threatscope/ui/service/BackendBridge.java`

**Find this method (around line 236):**
```java
public UiSecurityEvent createMockEvent() {
    return new UiSecurityEvent(
            System.currentTimeMillis(),
            "198.20.69.42",
            "PORT_SCAN",
            "BENIGN_NOISE",
            25,
            "MEDIUM",
            "An external computer attempted to connect to 12 different services. " +
                    "This appears to be automated internet scanning, which is very common.",
            "No action needed. We are monitoring the situation.");
}
```

**Replace it with:**
```java
public UiSecurityEvent createMockEvent() {
    return new UiSecurityEvent(
            System.currentTimeMillis(),
            "198.20.69.42",
            "192.168.1.1",  // destination IP
            (int) (Math.random() * 60000) + 1024,  // source port (1024-65535)
            (int) (Math.random() * 1000) + 80,  // destination port (80-1080)
            "TCP",  // protocol
            (int) (Math.random() * 1400) + 60,  // packet size (60-1460 bytes)
            "PORT_SCAN",
            "BENIGN_NOISE",
            25,
            "MEDIUM",
            "An external computer attempted to connect to 12 different services. " +
                    "This appears to be automated internet scanning, which is very common.",
            "No action needed. We are monitoring the situation.");
}
```

## 🚀 How to Test

1. **Make the manual fix above** in `BackendBridge.java`

2. **Clean and rebuild:**
   ```bash
   force-clean-and-run.bat
   ```

3. **Test Expert Mode:**
   - Login (admin / admin123)
   - Click "Enable Monitoring"
   - Click "Generate Test Events"
   - Click "Expert Mode" toggle
   - **Raw Activity table should appear** with detailed packet information
   - Toggle Expert Mode off - table should disappear

## 📊 What You'll See in Expert Mode

### Raw Activity Table Columns:
| Column | Description |
|--------|-------------|
| Time | Timestamp of the packet |
| Source IP | Source IP address |
| Dest IP | Destination IP address |
| Src Port | Source port number |
| Dst Port | Destination port number |
| Protocol | Network protocol (TCP/UDP) |
| Size | Packet size in bytes |
| Threat | Threat type detected |
| Confidence | Detection confidence % |

## 🎯 Features

✅ **Scrollable Dashboard** - All content visible with proper scrolling
✅ **Live Monitoring Stats** - Real-time packet analysis
✅ **Security Event Timeline** - High-level event summary
✅ **Raw Activity Table** - Detailed packet-level data (Expert Mode)
✅ **Risk Overview** - Progress bar and distribution chart
✅ **Event Explanations** - User-friendly descriptions

## 📝 Summary

The dashboard now has a complete Expert Mode feature that reveals detailed packet information in a Raw Activity table. This gives advanced users access to low-level network data while keeping the interface clean and simple for regular users.

---

**Status:** ✅ READY (after manual fix)
**Date:** 2026-02-13
**Version:** ThreatScope v2.0 - Expert Mode Complete
