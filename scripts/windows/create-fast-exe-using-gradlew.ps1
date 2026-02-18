# create-fast-exe-using-gradlew.ps1 - Script de Build Windows rápido (exe simples)
. "$PSScriptRoot\config.ps1"
. "$PSScriptRoot\common.ps1"

Write-Title "### 📦 JPackage Build Script para Windows (EXE rápido) ###"

# --- 1. Requirements Check ---
Write-Host "1. Checando 'jpackage' e 'java'..."
Test-Requirements -ToolName "jpackage" -ErrorMsg "'jpackage' não encontrado. Verifique a instalação do seu JDK e o PATH."
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

# --- 4. JPackage: Create EXE (app-image) ---
Write-Host "4. Criando executável Windows (.exe)..."

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
    --type app-image `
    --runtime-image $env:RUNTIME_DIR `
    --icon $env:APP_ICON `
    --java-options "-Djava.library.path=`$APPDIR/bin" `
    --java-options "--enable-native-access=javafx.graphics" `
    --java-options "-Dprism.verbose=true"

Write-Success "Executável criado com sucesso!"
Write-Host "O diretório com o exe está em: $env:DIST_DIR\$env:APP_NAME"

# --- 5. Final Cleanup ---
Clear-BuildDirectory
