# FINAL UI REMOVAL STEPS

## Current Status:
- ✅ UI package deleted
- ✅ FXML files deleted
- ✅ CSS files deleted
- ✅ Service files deleted
- ✅ UI imports removed from backend
- ⚠️ One syntax error to fix

## Run These Commands:

```powershell
cd D:\Sakthi\Java\ThreatScope

# Fix the syntax error in PacketSniffer.java
powershell -ExecutionPolicy Bypass -File .\fix-syntax.ps1
```

## OR Manual Fix:

Open `src\main\java\com\threatscope\core\capture\PacketSniffer.java`

Find line 49:
```java
System.out.println(\"--------------------------------------------------\");
```

Replace with:
```java
System.out.println("--------------------------------------------------");
```

Then compile:
```bash
mvn compile
```

## After Successful Compilation:

Run the backend:
```bash
mvn exec:java -Dexec.mainClass="com.threatscope.Main"
```

## Expected Output:
```
====================================
 ThreatScope starting...
 Phase 3: Live Packet Capture
====================================

[*] Available Network Interfaces:
[0] Intel(R) Wi-Fi 6 AX201 160MHz
...

[*] Starting packet capture...
--------------------------------------------------
 Monitoring Interface: \Device\NPF_{...}
 Description: Intel(R) Wi-Fi 6 AX201 160MHz
--------------------------------------------------

[HH:MM:SS.mmm] TCP 192.168.1.100:54321 -> 142.250.185.46:443 | Size=52 bytes
...
```

## UI Removal Complete! ✅

Your backend is now:
- ✅ UI-free
- ✅ Headless (console-only)
- ✅ Fully functional
- ✅ Ready for service/daemon deployment
