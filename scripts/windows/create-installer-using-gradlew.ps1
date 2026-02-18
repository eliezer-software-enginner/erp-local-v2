# create-installer-using-gradlew.ps1 - Script de Build Windows (MSI Installer)
. "$PSScriptRoot\config.ps1"
. "$PSScriptRoot\common.ps1"

Write-Title "### 📦 JPackage Build Script para Windows (MSI Installer) ###"

# --- 1. Requirements Check ---
Write-Host "1. Checando 'jpackage' e 'java'..."
Test-Requirements -ToolName "jpackage" -ErrorMsg "'jpackage' não encontrado. Verifique a instalação do seu JDK e o PATH."
Test-Requirements -ToolName "light.exe" -ErrorMsg "WiX Toolset não encontrado. Instale-o para criar o instalador MSI."
Write-Step "Requisitos básicos atendidos."

# --- 2. Preparation (copia arquivos antes de limpar diretórios) ---
Write-Host "2. Preparando diretórios e copiando arquivos..."
New-Item -Path $env:INPUT_DIR -ItemType Directory -Force | Out-Null
New-Item -Path $env:DIST_DIR -ItemType Directory -Force | Out-Null
Copy-MainJar
Copy-Dependencies
Copy-FxBinaries

# --- 3. JLink: Create Runtime Image (JRE) ---
New-JreImage

# --- 4. JPackage: Create MSI Installer ---
Write-Host "4. Criando instalador Windows (.msi) com o JRE customizado..."

jpackage `
    --input $env:INPUT_DIR `
    --dest $env:DIST_DIR `
    --main-jar $env:JAR_FILE `
    --main-class $env:APP_MAIN_CLASS `
    --name $env:APP_NAME `
    --app-version $env:APP_VERSION `
    --vendor $env:APP_VENDOR `
    --copyright $env:APP_COPYRIGHT `
    --description $env:APP_DESCRIPTION `
    --type msi `
    --module-path "$env:RUNTIME_DIR;$env:INPUT_DIR" `
    --add-modules "$env:FX_MODULES,java.sql,java.logging" `
    --icon $env:APP_ICON `
    --win-menu `
    --win-menu-group $env:APP_NAME `
    --win-shortcut `
    --win-dir-chooser `
    --win-per-user-install `
    --java-options "-Djava.library.path=`$APPDIR/bin" `
    --java-options "--enable-native-access=javafx.graphics" `
    --java-options "-Dprism.verbose=true"

Write-Success "Instalador MSI criado com sucesso!"
Write-Host "O arquivo do instalador está em: $env:DIST_DIR"

# --- 5. Final Cleanup ---
Clear-BuildDirectory
