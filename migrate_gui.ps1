$src = "d:\HeThongDuocAnKhang\src\main\java"
$oldGui = "$src\hethongnhathuocduocankhang\gui"
$newGui = "$src\client\gui"

Write-Host "Moving GUI files..."
Get-ChildItem -Path $oldGui | Where-Object { 
    $_.Name -notmatch "^DangNhapGUI" -and 
    $_.Name -notmatch "^GiaoDienChinhGUI" -and 
    $_.Name -notmatch "^AboutGUI" -and 
    $_.Name -notmatch "^HuongDanSuDungGUI" 
} | Move-Item -Destination $newGui -Force

Write-Host "Moving menu, theme, icon directories..."
if (Test-Path "$src\hethongnhathuocduocankhang\menu") {
    Move-Item -Path "$src\hethongnhathuocduocankhang\menu" -Destination "$src\client\menu" -Force
}
if (Test-Path "$src\hethongnhathuocduocankhang\theme") {
    Move-Item -Path "$src\hethongnhathuocduocankhang\theme" -Destination "$src\client\theme" -Force
}
if (Test-Path "$src\hethongnhathuocduocankhang\icon") {
    Move-Item -Path "$src\hethongnhathuocduocankhang\icon" -Destination "$src\client\icon" -Force
}

Write-Host "Replacing package and import statements in all files..."
$files = Get-ChildItem -Path $src -Include *.java, *.form -Recurse

foreach ($file in $files) {
    $content = Get-Content -Path $file.FullName -Raw
    $original = $content
    
    $content = $content -replace 'hethongnhathuocduocankhang\.gui', 'client.gui'
    $content = $content -replace 'hethongnhathuocduocankhang\.menu', 'client.menu'
    $content = $content -replace 'hethongnhathuocduocankhang\.theme', 'client.theme'
    $content = $content -replace 'hethongnhathuocduocankhang\.icon', 'client.icon'
    
    $content = $content -replace 'hethongnhathuocduocankhang/icon', 'client/icon'
    $content = $content -replace 'hethongnhathuocduocankhang/theme', 'client/theme'

    if ($content -cne $original) {
        Set-Content -Path $file.FullName -Value $content -NoNewline
    }
}
Write-Host "Done!"
