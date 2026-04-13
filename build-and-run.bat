@echo off
REM ThreatScope Build and Run Script
REM Navigate to project root and build

echo ========================================
echo ThreatScope - Build and Run
echo ========================================
echo.

cd /d "d:\Sakthi\Java\ThreatScope"

echo [1/3] Cleaning previous build...
call mvn clean

echo.
echo [2/3] Compiling project...
call mvn compile

echo.
echo [3/3] Running ThreatScope...
echo.
call mvn exec:java -Dexec.mainClass="com.threatscope.Main"

pause
