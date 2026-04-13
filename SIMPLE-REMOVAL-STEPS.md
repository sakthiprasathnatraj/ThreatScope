# SIMPLE UI REMOVAL INSTRUCTIONS

## Copy and paste these commands into PowerShell:

```powershell
# Navigate to project directory
cd D:\Sakthi\Java\ThreatScope

# Remove UI Java package (20+ files)
Remove-Item -Path "src\main\java\com\threatscope\ui" -Recurse -Force

# Remove FXML resources (11 files)
Remove-Item -Path "src\main\resources\fxml" -Recurse -Force

# Remove CSS resources (2 files)
Remove-Item -Path "src\main\resources\css" -Recurse -Force

# Remove UI documentation files
Remove-Item -Path "VISUAL-GUIDE.md" -Force -ErrorAction SilentlyContinue
Remove-Item -Path "VISUAL-REFERENCE.md" -Force -ErrorAction SilentlyContinue
Remove-Item -Path "ENHANCED-UI-ACTIVE.md" -Force -ErrorAction SilentlyContinue
Remove-Item -Path "QUICK-TEST-GUIDE.md" -Force -ErrorAction SilentlyContinue
Remove-Item -Path "UI-UPGRADE-GUIDE.md" -Force -ErrorAction SilentlyContinue
Remove-Item -Path "UI-UPGRADE-README.md" -Force -ErrorAction SilentlyContinue
Remove-Item -Path "README-ENHANCEMENTS.md" -Force -ErrorAction SilentlyContinue
Remove-Item -Path "EXECUTION-COMPLETE.md" -Force -ErrorAction SilentlyContinue
Remove-Item -Path "IMPLEMENTATION-CHECKLIST.md" -Force -ErrorAction SilentlyContinue
Remove-Item -Path "DELIVERABLES.md" -Force -ErrorAction SilentlyContinue
Remove-Item -Path "ERROR-FIXES.md" -Force -ErrorAction SilentlyContinue
Remove-Item -Path "QUICK-START.md" -Force -ErrorAction SilentlyContinue

# Clean Maven build
mvn clean

# Done!
Write-Host "UI Removal Complete!" -ForegroundColor Green
Write-Host "Now run: mvn compile" -ForegroundColor Yellow
```

## That's it! Just copy all the commands above and paste into PowerShell.

---

## Verification (after running commands):

```powershell
# These should return FALSE (deleted):
Test-Path "src\main\java\com\threatscope\ui"
Test-Path "src\main\resources\fxml"
Test-Path "src\main\resources\css"

# These should return TRUE (preserved):
Test-Path "src\main\java\com\threatscope\core"
Test-Path "src\main\java\com\threatscope\Main.java"
```

---

## After removal, rebuild and test:

```bash
# Rebuild
mvn clean compile

# Run backend
mvn exec:java -Dexec.mainClass="com.threatscope.Main"
```
