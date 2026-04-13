# Quick fix for PacketSniffer.java syntax error

$file = "src\main\java\com\threatscope\core\capture\PacketSniffer.java"
$content = Get-Content $file -Raw
$content = $content -replace '\\\"--------------------------------------------------\\\"', '"--------------------------------------------------"'
Set-Content $file $content -NoNewline

Write-Host "Fixed PacketSniffer.java syntax error" -ForegroundColor Green

# Now compile
mvn compile
