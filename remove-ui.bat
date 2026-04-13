@echo off
REM ============================================
REM ThreatScope UI Removal Script (Batch)
REM ============================================

echo ========================================
echo  ThreatScope UI Removal Script
echo ========================================
echo.

echo [1/6] Removing UI Java package...
if exist "src\main\java\com\threatscope\ui" (
    rmdir /s /q "src\main\java\com\threatscope\ui"
    echo   [OK] Deleted: src\main\java\com\threatscope\ui
) else (
    echo   [INFO] Already removed: src\main\java\com\threatscope\ui
)

echo [2/6] Removing FXML files...
if exist "src\main\resources\fxml" (
    rmdir /s /q "src\main\resources\fxml"
    echo   [OK] Deleted: src\main\resources\fxml
) else (
    echo   [INFO] Already removed: src\main\resources\fxml
)

echo [3/6] Removing CSS files...
if exist "src\main\resources\css" (
    rmdir /s /q "src\main\resources\css"
    echo   [OK] Deleted: src\main\resources\css
) else (
    echo   [INFO] Already removed: src\main\resources\css
)

echo [4/6] Removing UI documentation files...
if exist "VISUAL-GUIDE.md" del /q "VISUAL-GUIDE.md"
if exist "VISUAL-REFERENCE.md" del /q "VISUAL-REFERENCE.md"
if exist "ENHANCED-UI-ACTIVE.md" del /q "ENHANCED-UI-ACTIVE.md"
if exist "QUICK-TEST-GUIDE.md" del /q "QUICK-TEST-GUIDE.md"
if exist "UI-UPGRADE-GUIDE.md" del /q "UI-UPGRADE-GUIDE.md"
if exist "README-ENHANCEMENTS.md" del /q "README-ENHANCEMENTS.md"
if exist "EXECUTION-COMPLETE.md" del /q "EXECUTION-COMPLETE.md"
if exist "IMPLEMENTATION-CHECKLIST.md" del /q "IMPLEMENTATION-CHECKLIST.md"
if exist "DELIVERABLES.md" del /q "DELIVERABLES.md"
if exist "ERROR-FIXES.md" del /q "ERROR-FIXES.md"
echo   [OK] Deleted UI documentation files

echo [5/6] Cleaning compiled UI classes...
if exist "target\classes\com\threatscope\ui" (
    rmdir /s /q "target\classes\com\threatscope\ui"
    echo   [OK] Deleted: target\classes\com\threatscope\ui
)
if exist "target\classes\fxml" (
    rmdir /s /q "target\classes\fxml"
    echo   [OK] Deleted: target\classes\fxml
)
if exist "target\classes\css" (
    rmdir /s /q "target\classes\css"
    echo   [OK] Deleted: target\classes\css
)

echo [6/6] Verifying backend integrity...
if exist "src\main\java\com\threatscope\core" (
    echo   [OK] Backend intact: src\main\java\com\threatscope\core
) else (
    echo   [ERROR] Missing backend: src\main\java\com\threatscope\core
)
if exist "src\main\java\com\threatscope\service" (
    echo   [OK] Backend intact: src\main\java\com\threatscope\service
) else (
    echo   [ERROR] Missing backend: src\main\java\com\threatscope\service
)
if exist "src\main\java\com\threatscope\Main.java" (
    echo   [OK] Backend intact: src\main\java\com\threatscope\Main.java
) else (
    echo   [ERROR] Missing backend: src\main\java\com\threatscope\Main.java
)

echo.
echo ========================================
echo  UI Removal Complete!
echo  Backend is intact and ready to run.
echo ========================================
echo.
echo Next steps:
echo   1. Run: mvn clean compile
echo   2. Run backend: mvn exec:java -Dexec.mainClass=com.threatscope.Main
echo.
pause
