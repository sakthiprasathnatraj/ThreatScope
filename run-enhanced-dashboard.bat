@echo off
echo ========================================
echo ThreatScope v2.0 - Enhanced Dashboard
echo ========================================
echo.

echo [1/3] Compiling project...
call mvn compile
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: Compilation failed!
    echo Please check the error messages above.
    pause
    exit /b 1
)

echo.
echo [2/3] Compilation successful!
echo.

echo [3/3] Launching ThreatScope Enhanced Dashboard...
echo.
echo Login credentials:
echo   Username: admin
echo   Password: admin123
echo.

call mvn javafx:run

pause
