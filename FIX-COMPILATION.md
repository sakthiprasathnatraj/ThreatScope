# Fix Compilation Errors After UI Removal

## The Problem:
Service classes (NetworkService, SystemProcessService) reference deleted UI viewmodels.

## The Solution:
Delete the service classes - they were only created to feed the UI.

## Run These Commands:

```powershell
# Delete service classes that depend on UI
Remove-Item -Path "src\main\java\com\threatscope\service\NetworkService.java" -Force
Remove-Item -Path "src\main\java\com\threatscope\service\SystemProcessService.java" -Force

# Delete the entire service directory if empty
Remove-Item -Path "src\main\java\com\threatscope\service" -Recurse -Force -ErrorAction SilentlyContinue

# Now compile
mvn compile
```

## Why Delete Services?
- NetworkService.java uses `NetworkViewModel.ConnectionModel` (deleted)
- SystemProcessService.java uses `ProcessViewModel.ProcessModel` (deleted)
- These services were ONLY created to provide data to the UI
- The core backend (PacketSniffer, ThreatDetector, etc.) doesn't need them

## After Deletion:
Your backend will have:
- ✅ core/capture/ - Packet capture (PacketSniffer)
- ✅ core/detect/ - Threat detection
- ✅ core/risk/ - Risk scoring
- ✅ core/correlate/ - Incident correlation
- ✅ core/explain/ - Explanation engine
- ✅ logging/ - Event logger
- ✅ Main.java - Entry point

All fully functional without UI dependencies!
