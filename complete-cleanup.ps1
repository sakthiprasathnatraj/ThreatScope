# Complete UI Cleanup Script

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " Complete UI Cleanup" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Delete service files (they use UI viewmodels)
Write-Host "[1/3] Removing UI-dependent service files..." -ForegroundColor Yellow
if (Test-Path "src\main\java\com\threatscope\service\NetworkService.java") {
    Remove-Item "src\main\java\com\threatscope\service\NetworkService.java" -Force
    Write-Host "  ✓ Deleted NetworkService.java" -ForegroundColor Green
}
if (Test-Path "src\main\java\com\threatscope\service\SystemProcessService.java") {
    Remove-Item "src\main\java\com\threatscope\service\SystemProcessService.java" -Force
    Write-Host "  ✓ Deleted SystemProcessService.java" -ForegroundColor Green
}
if (Test-Path "src\main\java\com\threatscope\service") {
    Remove-Item "src\main\java\com\threatscope\service" -Recurse -Force
    Write-Host "  ✓ Deleted service directory" -ForegroundColor Green
}

# Clean target directory
Write-Host "[2/3] Cleaning compiled classes..." -ForegroundColor Yellow
if (Test-Path "target") {
    Remove-Item "target" -Recurse -Force -ErrorAction SilentlyContinue
    Write-Host "  ✓ Deleted target directory" -ForegroundColor Green
}

# Verify no UI imports remain
Write-Host "[3/3] Verifying backend is clean..." -ForegroundColor Yellow
$uiImports = Select-String -Path "src\main\java\com\threatscope\core\**\*.java" -Pattern "import.*\.ui\." -ErrorAction SilentlyContinue
$javafxImports = Select-String -Path "src\main\java\com\threatscope\core\**\*.java" -Pattern "import javafx" -ErrorAction SilentlyContinue

if ($uiImports -or $javafxImports) {
    Write-Host "  ⚠ Warning: Found UI imports in core files" -ForegroundColor Red
    $uiImports | ForEach-Object { Write-Host "    - $($_.Filename):$($_.LineNumber)" -ForegroundColor Red }
    $javafxImports | ForEach-Object { Write-Host "    - $($_.Filename):$($_.LineNumber)" -ForegroundColor Red }
} else {
    Write-Host "  ✓ No UI imports found in backend" -ForegroundColor Green
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
Write-Host " Cleanup Complete!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next step: mvn clean compile" -ForegroundColor Yellow
Write-Host ""
