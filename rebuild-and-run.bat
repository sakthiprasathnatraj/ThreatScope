@echo off
echo ========================================
echo ThreatScope - Clean Build and Run
echo ========================================
echo.

echo [Step 1/3] Cleaning old compiled files...
call mvn clean
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Clean failed!
    pause
    exit /b 1
)
echo.

echo [Step 2/3] Compiling updated source code...
call mvn compile
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Compilation failed!
    pause
    exit /b 1
)
echo.

echo [Step 3/3] Running ThreatScope with updated UI...
call mvn javafx:run

pause
