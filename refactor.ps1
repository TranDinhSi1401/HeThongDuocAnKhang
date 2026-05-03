$sourceDir = "d:\HeThongDuocAnKhang\src\main\java\hethongnhathuocduocankhang"
$targetDir = "d:\HeThongDuocAnKhang\src\main\java\client"

# Tạo thư mục nếu chưa có
if (!(Test-Path "$targetDir\gui")) { New-Item -ItemType Directory -Path "$targetDir\gui" | Out-Null }

# Copy đè nội dung
Copy-Item -Path "$sourceDir\gui\*" -Destination "$targetDir\gui\" -Recurse -Force
Copy-Item -Path "$sourceDir\menu" -Destination "$targetDir" -Recurse -Force
Copy-Item -Path "$sourceDir\icon" -Destination "$targetDir" -Recurse -Force
Copy-Item -Path "$sourceDir\theme" -Destination "$targetDir" -Recurse -Force
Copy-Item -Path "$sourceDir\util" -Destination "$targetDir" -Recurse -Force
Copy-Item -Path "$sourceDir\bus" -Destination "$targetDir" -Recurse -Force
Copy-Item -Path "$sourceDir\connectDB" -Destination "$targetDir" -Recurse -Force
Copy-Item -Path "$sourceDir\HeThongNhaThuocDuocAnKhang.java" -Destination "$targetDir\ClientApplication.java" -Force

# Lấy tất cả file java trong thư mục client (trừ thư mục socket nếu có code riêng)
$files = Get-ChildItem -Path $targetDir -Recurse -Filter *.java

foreach ($file in $files) {
    $content = Get-Content -Encoding UTF8 $file.FullName -Raw
    
    # Đổi package
    $content = $content -replace "package hethongnhathuocduocankhang\.gui", "package client.gui"
    $content = $content -replace "package hethongnhathuocduocankhang\.menu", "package client.menu"
    $content = $content -replace "package hethongnhathuocduocankhang\.theme", "package client.theme"
    $content = $content -replace "package hethongnhathuocduocankhang\.util", "package client.util"
    $content = $content -replace "package hethongnhathuocduocankhang\.bus", "package client.bus"
    $content = $content -replace "package hethongnhathuocduocankhang\.icon", "package client.icon"
    $content = $content -replace "package hethongnhathuocduocankhang\.connectDB", "package client.connectDB"
    $content = $content -replace "package hethongnhathuocduocankhang;", "package client;"
    $content = $content -replace "import hethongnhathuocduocankhang;", "import client;"

    # Đổi import
    $content = $content -replace "import hethongnhathuocduocankhang\.gui", "import client.gui"
    $content = $content -replace "import hethongnhathuocduocankhang\.menu", "import client.menu"
    $content = $content -replace "import hethongnhathuocduocankhang\.theme", "import client.theme"
    $content = $content -replace "import hethongnhathuocduocankhang\.util", "import client.util"
    $content = $content -replace "import hethongnhathuocduocankhang\.bus", "import client.bus"
    $content = $content -replace "import hethongnhathuocduocankhang\.icon", "import client.icon"
    $content = $content -replace "import hethongnhathuocduocankhang\.connectDB", "import client.connectDB"
    $content = $content -replace "import hethongnhathuocduocankhang\.entity", "import server.entity"
    $content = $content -replace "import hethongnhathuocduocankhang\.dao", "import server.dao"
    
    # Đổi một số class
    $content = $content -replace "hethongnhathuocduocankhang/icon", "client/icon"
    $content = $content -replace "hethongnhathuocduocankhang\.theme", "client.theme"
    $content = $content -replace "class HeThongNhaThuocDuocAnKhang", "class ClientApplication"

    Set-Content -Path $file.FullName -Value $content -Encoding UTF8
}

Write-Host "Done copying and refactoring to client"
