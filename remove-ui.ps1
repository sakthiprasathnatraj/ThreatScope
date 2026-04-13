# ============================================
# ThreatScope UI Removal Script
# ============================================
# This script removes ONLY the UI layer while preserving all backend logic.
# Backend components (packet capture, threat detection, risk scoring) remain intact.

Write-Host "========================================" -ForegroundColor Cyan
Write-Host " ThreatScope UI Removal Script" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Set base directory
$baseDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $baseDir

Write-Host "[1/6] Removing UI Java package..." -ForegroundColor Yellow
$uiPackagePath = "src\main\java\com\threatscope\ui"
if (Test-Path $uiPackagePath) {
    Remove-Item -Path $uiPackagePath -Recurse -Force
    Write-Host "  ✓ Deleted: $uiPackagePath" -ForegroundColor Green
} else {
    Write-Host "  ℹ Already removed: $uiPackagePath" -ForegroundColor Gray
}

Write-Host "[2/6] Removing FXML files..." -ForegroundColor Yellow
$fxmlPath = "src\main\resources\fxml"
if (Test-Path $fxmlPath) {
    Remove-Item -Path $fxmlPath -Recurse -Force
    Write-Host "  ✓ Deleted: $fxmlPath" -ForegroundColor Green
} else {
    Write-Host "  ℹ Already removed: $fxmlPath" -ForegroundColor Gray
}

Write-Host "[3/6] Removing CSS files..." -ForegroundColor Yellow
$cssPath = "src\main\resources\css"
if (Test-Path $cssPath) {
    Remove-Item -Path $cssPath -Recurse -Force
    Write-Host "  ✓ Deleted: $cssPath" -ForegroundColor Green
} else {
    Write-Host "  ℹ Already removed: $cssPath" -ForegroundColor Gray
}

Write-Host "[4/6] Removing UI documentation files..." -ForegroundColor Yellow
$uiDocs = @(
    "VISUAL-GUIDE.md",
    "VISUAL-REFERENCE.md",
    "ENHANCED-UI-ACTIVE.md",
    "QUICK-TEST-GUIDE.md",
    "UI-UPGRADE-GUIDE.md",
    "README-ENHANCEMENTS.md",
    "EXECUTION-COMPLETE.md",
    "IMPLEMENTATION-CHECKLIST.md",
    "DELIVERABLES.md"
)

foreach ($doc in $uiDocs) {
    if (Test-Path $doc) {
        Remove-Item -Path $doc -Force
        Write-Host "  ✓ Deleted: $doc" -ForegroundColor Green
    }
}

Write-Host "[5/6] Cleaning compiled UI classes..." -ForegroundColor Yellow
$targetUiPath = "target\classes\com\threatscope\ui"
if (Test-Path $targetUiPath) {
    Remove-Item -Path $targetUiPath -Recurse -Force
    Write-Host "  ✓ Deleted: $targetUiPath" -ForegroundColor Green
}

$targetFxmlPath = "target\classes\fxml"
if (Test-Path $targetFxmlPath) {
    Remove-Item -Path $targetFxmlPath -Recurse -Force
    Write-Host "  ✓ Deleted: $targetFxmlPath" -ForegroundColor Green
}

$targetCssPath = "target\classes\css"
if (Test-Path $targetCssPath) {
    Remove-Item -Path $targetCssPath -Recurse -Force
    Write-Host "  ✓ Deleted: $targetCssPath" -ForegroundColor Green
}

Write-Host "[6/6] Verifying backend integrity..." -ForegroundColor Yellow
$backendPaths = @(
    "src\main\java\com\threatscope\core",
    "src\main\java\com\threatscope\service",
    "src\main\java\com\threatscope\logging",
    "src\main\java\com\threatscope\Main.java"
)

$allBackendIntact = $true
foreach ($path in $backendPaths) {
    if (Test-Path $path) {
        Write-Host "  ✓ Backend intact: $path" -ForegroundColor Green
    } else {
        Write-Host "  ✗ Missing backend: $path" -ForegroundColor Red
        $allBackendIntact = $false
    }
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
if ($allBackendIntact) {
    Write-Host " UI Removal Complete!" -ForegroundColor Green
    Write-Host " Backend is intact and ready to run." -ForegroundColor Green
} else {
    Write-Host " WARNING: Backend integrity check failed!" -ForegroundColor Red
}
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Next steps:" -ForegroundColor Yellow
Write-Host "  1. Update pom.xml to remove JavaFX dependencies"
Write-Host "  2. Run: mvn clean compile"
Write-Host "  3. Run backend: mvn exec:java -Dexec.mainClass=com.threatscope.Main"
Write-Host ""
