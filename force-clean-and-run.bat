@echo off
echo ========================================
echo ThreatScope - Force Clean and Run
echo ========================================
echo.

echo [Step 1/4] Stopping any running Java processes...
taskkill /F /IM java.exe /T 2>nul
timeout /t 2 /nobreak >nul
echo.

echo [Step 2/4] Forcefully deleting target directory...
if exist target (
    rmdir /s /q target
    echo Target directory deleted successfully!
) else (
    echo Target directory does not exist, skipping...
)
echo.

echo [Step 3/4] Compiling updated source code...
call mvn compile
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)
echo.

echo [Step 4/4] Running ThreatScope with updated dashboard...
call mvn javafx:run

pause
