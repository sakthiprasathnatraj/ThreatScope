@echo off
echo ========================================
echo  Complete UI Cleanup
echo ========================================
echo.

echo [1/3] Removing UI-dependent service files...
if exist "src\main\java\com\threatscope\service\NetworkService.java" (
    del /q "src\main\java\com\threatscope\service\NetworkService.java"
    echo   [OK] Deleted NetworkService.java
)
if exist "src\main\java\com\threatscope\service\SystemProcessService.java" (
    del /q "src\main\java\com\threatscope\service\SystemProcessService.java"
    echo   [OK] Deleted SystemProcessService.java
)
if exist "src\main\java\com\threatscope\service" (
    rmdir /s /q "src\main\java\com\threatscope\service"
    echo   [OK] Deleted service directory
)

echo [2/3] Cleaning compiled classes...
if exist "target" (
    rmdir /s /q "target"
    echo   [OK] Deleted target directory
)

echo [3/3] Cleanup complete!
echo.
echo ========================================
echo  Ready to compile!
echo ========================================
echo.
echo Next step: mvn clean compile
echo.
pause
