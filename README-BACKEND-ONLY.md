# ThreatScope - Backend Only (UI Removed)

## Overview
This is the **backend-only** version of ThreatScope. All UI components have been removed while preserving the complete backend functionality.

## What Was Removed ❌

### UI Components Deleted:
- ✗ `src/main/java/com/threatscope/ui/` - Entire UI package
  - Controllers (Dashboard, Incidents, Network, Processes, Settings)
  - ViewModels (Dashboard, Incident, Network, Process)
  - UI Models (SecurityIncident, SecurityState, SystemRiskState, TimelineEntry)
  - Timeline components
  - Event bus
  - MainApp (JavaFX application entry point)

### Resources Deleted:
- ✗ `src/main/resources/fxml/` - All FXML layout files
- ✗ `src/main/resources/css/` - All CSS stylesheets

### Dependencies Removed:
- ✗ JavaFX Controls (org.openjfx:javafx-controls)
- ✗ JavaFX FXML (org.openjfx:javafx-fxml)
- ✗ JavaFX Maven Plugin

### Documentation Removed:
- ✗ VISUAL-GUIDE.md
- ✗ VISUAL-REFERENCE.md
- ✗ ENHANCED-UI-ACTIVE.md
- ✗ QUICK-TEST-GUIDE.md
- ✗ UI-UPGRADE-GUIDE.md
- ✗ README-ENHANCEMENTS.md
- ✗ EXECUTION-COMPLETE.md
- ✗ IMPLEMENTATION-CHECKLIST.md
- ✗ DELIVERABLES.md

---

## What Was Preserved ✅

### Core Backend (100% Intact):
- ✓ `src/main/java/com/threatscope/core/` - All backend logic
  - **capture/** - Packet capture engine (PacketSniffer, NetworkInterfaceScanner)
  - **detect/** - Threat detection (ThreatDetector, TrafficStats)
  - **correlate/** - Incident correlation (IncidentCorrelator)
  - **explain/** - Explanation engine (ExplanationEngine)
  - **risk/** - Risk scoring (RiskScoreEngine, ConfidenceEvaluator, ContextClassifier)
  - **model/** - Data models (SecurityEvent)

### Services:
- ✓ `src/main/java/com/threatscope/service/`
  - NetworkService
  - SystemProcessService

### Logging:
- ✓ `src/main/java/com/threatscope/logging/`
  - EventLogger

### Entry Point:
- ✓ `src/main/java/com/threatscope/Main.java` - Backend entry point

### Dependencies (Preserved):
- ✓ Pcap4J Core (packet capture)
- ✓ Pcap4J Packet Factory
- ✓ SLF4J Logging API
- ✓ Logback Classic

---

## Running the Backend

### Prerequisites
1. **Java 8+** installed
2. **Maven** installed
3. **WinPcap** or **Npcap** installed (for packet capture on Windows)
4. **Administrator privileges** (required for packet capture)

### Build the Backend
```bash
mvn clean compile
```

### Run the Backend
```bash
# Method 1: Using Maven
mvn exec:java -Dexec.mainClass="com.threatscope.Main"

# Method 2: Using Java directly (after building)
java -cp target/classes;%MAVEN_REPO%/* com.threatscope.Main
```

### Expected Output
```
====================================
 ThreatScope starting...
 Phase 3: Live Packet Capture
====================================

[*] Available Network Interfaces:
[0] Intel(R) Wi-Fi 6 AX201 160MHz
[1] Realtek PCIe GbE Family Controller
...

[*] Starting packet capture...

[PACKET] TCP 192.168.1.100:54321 -> 142.250.185.46:443
[PACKET] UDP 192.168.1.100:53 -> 8.8.8.8:53
...
```

---

## Backend Architecture

```
ThreatScope Backend
│
├── Main.java (Entry Point)
│   └── Initializes PacketSniffer
│
├── core/
│   ├── capture/
│   │   ├── PacketSniffer ────────► Captures network packets
│   │   ├── NetworkInterfaceScanner ► Lists available interfaces
│   │   └── InterfaceLister ──────► Interface utilities
│   │
│   ├── detect/
│   │   ├── ThreatDetector ───────► Analyzes packets for threats
│   │   └── TrafficStats ─────────► Tracks traffic statistics
│   │
│   ├── correlate/
│   │   └── IncidentCorrelator ───► Correlates security events
│   │
│   ├── explain/
│   │   └── ExplanationEngine ────► Generates explanations
│   │
│   ├── risk/
│   │   ├── RiskScoreEngine ──────► Calculates risk scores
│   │   ├── ConfidenceEvaluator ──► Evaluates confidence levels
│   │   └── ContextClassifier ────► Classifies security context
│   │
│   └── model/
│       └── SecurityEvent ────────► Event data model
│
├── service/
│   ├── NetworkService ───────────► Network monitoring service
│   └── SystemProcessService ─────► Process monitoring service
│
└── logging/
    └── EventLogger ──────────────► Event logging
```

---

## Customization

### Change Network Interface
Edit `Main.java` line 19:
```java
int interfaceIndex = 0;  // Change to your active interface
```

### Add Custom Logic
The backend is fully functional. You can:
- Add new threat detection rules in `ThreatDetector`
- Customize risk scoring in `RiskScoreEngine`
- Extend packet analysis in `PacketSniffer`
- Add new services in `service/` package

---

## Re-adding UI (Future)

If you want to add a new UI later:
1. Create a new `ui/` package
2. Add UI framework dependencies (JavaFX, Swing, web framework, etc.)
3. Connect to existing backend services
4. Backend APIs remain unchanged and ready to integrate

---

## Notes

- **No UI = Headless Mode**: The application runs in console/terminal mode
- **Backend Fully Functional**: All packet capture, threat detection, and risk scoring work as before
- **Clean Separation**: Backend never depended on UI, so removal is safe
- **Production Ready**: Backend can be deployed as a service/daemon

---

## Troubleshooting

### "No suitable device found"
- Ensure WinPcap/Npcap is installed
- Run with administrator privileges
- Check interface index in `Main.java`

### Compilation Errors
- Run `mvn clean` to clear old UI classes
- Verify JavaFX dependencies are removed from `pom.xml`

### No Output
- Check that the correct network interface is selected
- Verify network traffic is flowing on that interface

---

**Backend Status**: ✅ Fully Operational  
**UI Status**: ❌ Removed  
**Last Updated**: 2026-01-25
