@echo off
echo ========================================
echo ThreatScope UI - Clean Build and Run
echo ========================================
echo.

cd /d d:\Sakthi\Java\ThreatScope

echo [1/3] Cleaning previous build...
call mvn clean
if errorlevel 1 (
    echo ERROR: Clean failed!
    pause
    exit /b 1
)

echo.
echo [2/3] Compiling project...
call mvn compile
if errorlevel 1 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)

echo.
echo [3/3] Running JavaFX application...
call mvn javafx:run

pause
