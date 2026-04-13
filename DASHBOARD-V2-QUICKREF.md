# ThreatScope v2.0 - Dashboard V2 Quick Reference

## 🚀 Quick Start

### Run from IntelliJ
1. Open: `src/main/java/com/threatscope/ui/DashboardV2Launcher.java`
2. Right-click → **Run 'DashboardV2Launcher.main()'**
3. Window opens maximized on 1920×1080

### Run from Command Line
```bash
cd d:\Sakthi\Java\ThreatScope
mvn exec:java -Dexec.mainClass="com.threatscope.ui.DashboardV2Launcher"
```

---

## 📐 Layout at a Glance

```
┌──────────────────────────────────────────────┐
│ Header (70px): Logo | State Badge | User    │
├──────────────────────────────────────────────┤
│ ┌──────────────────┬──────────────┐         │
│ │ Live Monitoring  │ Risk Overview│ 45%     │
│ ├──────────────────┼──────────────┤         │
│ │ Timeline Table   │ Explanation  │ 55%     │
│ └──────────────────┴──────────────┘         │
│      65%                35%                  │
├──────────────────────────────────────────────┤
│ Control Bar (80px): Buttons | Expert Mode   │
└──────────────────────────────────────────────┘
```

---

## 🎨 Color Codes

| Element | Color | Hex |
|---------|-------|-----|
| Background | Very Dark | #0f0f0f |
| Panels | Dark | #1a1a1a |
| Cards | Medium Dark | #252525 |
| Accent | Cyan | #00d4ff |
| Success | Green | #4ade80 |
| Warning | Orange | #fb923c |
| Danger | Red | #f87171 |
| Info | Blue | #0078d4 |

---

## 📏 Component Sizes

| Component | Size |
|-----------|------|
| Header | 70px (fixed) |
| Control Bar | 80px (fixed) |
| Stat Cards | max 90px |
| Status Card | max 110px |
| Timeline Table | 300px |
| Risk Chart | max 200px |
| Explanation Scroll | max 300px |

---

## 🔧 Key Files

| File | Purpose |
|------|---------|
| `DashboardViewV2.java` | Main view (757 lines) |
| `DashboardControllerV2.java` | Controller logic (370+ lines) |
| `UiSecurityEvent.java` | Event model (enhanced) |
| `DashboardV2Launcher.java` | Test launcher |
| `DASHBOARD-V2-LAYOUT.md` | Full documentation |
| `DASHBOARD-V2-SUMMARY.md` | Implementation summary |
| `DASHBOARD-V2-CHECKLIST.md` | Testing checklist |

---

## ⚡ Quick Actions

### Start Monitoring
```java
// Click "Enable Monitoring" button
// Or programmatically:
controller.startMonitoring();
```

### Generate Test Event
```java
// Click "Generate Test Events" button
// Or programmatically:
backendBridge.addMockEvent();
```

### Toggle Expert Mode
```java
// Click "Expert Mode" toggle
// Changes color: gray → blue
```

---

## 🧪 Quick Test

1. **Launch** → Window opens maximized
2. **Check Layout** → No scrolling, all visible
3. **Click "Enable Monitoring"** → Button turns red
4. **Click "Generate Test Events"** → Events appear in table
5. **Click event in table** → Explanation panel updates
6. **Click explanation panel** → Expands/collapses
7. **Watch stats** → Update every second

---

## 📊 Data Sources

| Metric | Source Method |
|--------|---------------|
| System State | `getCurrentSystemState()` |
| Packets Analyzed | `getTotalPacketsAnalyzed()` |
| Packets/sec | `getCurrentPacketRate()` |
| Active Connections | `getActiveConnectionCount()` |
| Risk Score | `getCurrentRiskScore()` |
| Events | `getRecentSecurityEvents(100)` |

---

## 🎯 Success Indicators

✓ No vertical scrolling  
✓ All components visible  
✓ Timeline readable  
✓ Explanation expandable  
✓ Stats update in real-time  
✓ Professional appearance  
✓ Buttons functional  
✓ No errors in console  

---

## 🐛 Common Issues

### Window too small
- **Fix**: Ensure 1920×1080 resolution
- **Or**: Adjust grid percentages in code

### Stats not updating
- **Fix**: Check BackendBridge connection
- **Or**: Verify monitoring is started

### Table empty
- **Fix**: Generate test events
- **Or**: Check event data source

### Layout broken
- **Fix**: Check VBox.setVgrow settings
- **Or**: Verify max heights on components

---

## 📞 Support

- **Documentation**: See `DASHBOARD-V2-LAYOUT.md`
- **Checklist**: See `DASHBOARD-V2-CHECKLIST.md`
- **Summary**: See `DASHBOARD-V2-SUMMARY.md`
- **Diagram**: See `DASHBOARD-V2-DIAGRAM.txt`

---

**Version**: 2.0 Final  
**Date**: 2026-02-13  
**Status**: ✅ Ready for Testing
