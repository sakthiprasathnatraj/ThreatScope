# FINAL UI CLEANUP - Remove All UI Dependencies

## Problem:
Service files (NetworkService.java, SystemProcessService.java) still exist and contain UI imports.

## Solution - Choose ONE method:

---

## METHOD 1: Run Batch File (Easiest)
```cmd
.\complete-cleanup.bat
```

---

## METHOD 2: Manual Commands (Copy-Paste All)
```powershell
# Delete service files
Remove-Item -Path "src\main\java\com\threatscope\service\NetworkService.java" -Force -ErrorAction SilentlyContinue

Remove-Item -Path "src\main\java\com\threatscope\service\SystemProcessService.java" -Force -ErrorAction SilentlyContinue

Remove-Item -Path "src\main\java\com\threatscope\service" -Recurse -Force -ErrorAction SilentlyContinue

# Delete target directory
Remove-Item -Path "target" -Recurse -Force -ErrorAction SilentlyContinue

# Now compile
mvn clean compile
```

---

## METHOD 3: One-Line Command
```cmd
cd D:\Sakthi\Java\ThreatScope && rmdir /s /q "src\main\java\com\threatscope\service" && rmdir /s /q "target" && mvn clean compile
```

---

## What Gets Deleted:

### Service Files (UI-Dependent):
```
src/main/java/com/threatscope/service/
├── NetworkService.java          ❌ DELETE (uses NetworkViewModel)
└── SystemProcessService.java    ❌ DELETE (uses ProcessViewModel)
```

These files:
- Import `com.threatscope.ui.viewmodel.*`
- Import `javafx.collections.*`
- Were ONLY created to feed data to the UI
- Are NOT needed for backend operation

---

## What Remains (Backend):

```
src/main/java/com/threatscope/
├── Main.java                    ✅ KEEP (entry point)
├── core/
│   ├── capture/                 ✅ KEEP (packet capture)
│   ├── detect/                  ✅ KEEP (threat detection)
│   ├── correlate/               ✅ KEEP (incident correlation)
│   ├── explain/                 ✅ KEEP (explanation engine)
│   ├── risk/                    ✅ KEEP (risk scoring)
│   └── model/                   ✅ KEEP (data models)
└── logging/                     ✅ KEEP (event logger)
```

---

## After Cleanup:

### Verify Deletion:
```powershell
# Should return FALSE:
Test-Path "src\main\java\com\threatscope\service"

# Should return TRUE:
Test-Path "src\main\java\com\threatscope\core"
```

### Compile:
```bash
mvn clean compile
```

### Expected Result:
```
[INFO] BUILD SUCCESS
[INFO] Total time: X.XXX s
```

### Run Backend:
```bash
mvn exec:java -Dexec.mainClass="com.threatscope.Main"
```

---

## Why Delete Service Files?

1. **UI Dependencies**: They import UI viewmodels that no longer exist
2. **JavaFX Dependencies**: They use JavaFX collections (removed from pom.xml)
3. **Not Needed**: Backend works without them
4. **Clean Separation**: Backend should not depend on UI layer

---

## Quick Fix:

**Just run this:**
```cmd
.\complete-cleanup.bat
```

Then:
```bash
mvn clean compile
```

**Done!** ✅
