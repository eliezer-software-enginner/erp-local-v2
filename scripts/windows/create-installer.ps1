# build.ps1 - Script de Build Windows (PowerShell)
$ErrorActionPreference = "Stop"

## --- Application Configuration ---
$APP_NAME = "MyApp"
$APP_VERSION = "1.2"
$APP_VENDOR = "YOUR NAME OR BUSINESS NAME"
$APP_COPYRIGHT = "Copyright 2025"
$APP_DESCRIPTION = "YOUR APP DESCRIPTION HERE"
$APP_MAIN_CLASS = "my_app.App"
$JAR_FILE = "my_app-$APP_VERSION-jar-with-dependencies.jar"
$FX_MODULES = "javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web"
$JAVAFX_SDK_VERSION = "25.0.1"
# OBS: O caminho usa '\' no Windows
$FX_SDK_PATH = "java_fx_modules\windows-$JAVAFX_SDK_VERSION\lib"
$APP_ICON = "src\main\resources\assets\app_ico.ico"

# Pastas de trabalho
$BUILD_DIR = "build"
$DIST_DIR = "dist"
$RUNTIME_DIR = "$BUILD_DIR\runtime"
$INPUT_DIR = "$BUILD_DIR\input_app"


Write-Host "### 📦 JPackage Build Script para Windows (JavaFX/JRE Embutido) ###"
Write-Host ""

# ----------------------------------------------------------------------------------
## 1. Requirements Check (Simplificado)
Write-Host "1. Checando 'jpackage' e WiX Toolset..."

# Checa se JPackage existe no PATH
if (-not (Get-Command jpackage -ErrorAction SilentlyContinue)) {
    Write-Error "🚨 ERRO: 'jpackage' não encontrado. Verifique a instalação do seu JDK e o PATH."
    exit 1
}

# Checa se o WiX Toolset está acessível (necessário para criar o MSI)
if (-not (Get-Command light.exe -ErrorAction SilentlyContinue)) {
    Write-Error "🚨 ERRO: WiX Toolset não encontrado. Instale-o para criar o instalador MSI."
    exit 1
}

Write-Host "Requisitos básicos atendidos."
Write-Host ""

# ----------------------------------------------------------------------------------
## 2. Cleanup and Preparation (Simplificado)
Write-Host "2. Limpando diretórios temporários e de saída..."
Remove-Item -Path $BUILD_DIR, $DIST_DIR -Recurse -Force -ErrorAction SilentlyContinue
New-Item -Path $INPUT_DIR, $DIST_DIR -ItemType Directory | Out-Null

# Cópia do JAR (Principal)
Write-Host "   Copiando JAR principal para o diretório de entrada..."
Copy-Item "target\$JAR_FILE" "$INPUT_DIR\"

# Cópia das bibliotecas nativas e JARs (Necessário para a correção do UnsatisfiedLinkError)
# Copia o CONTEÚDO de 'lib' do SDK para o diretório de entrada do JPackage.
Write-Host "   Copiando bibliotecas nativas do JavaFX para a entrada do JPackage..."
Copy-Item "$FX_SDK_PATH\*" "$INPUT_DIR\" -Recurse -Force
Write-Host ""


# ----------------------------------------------------------------------------------
## 3. JLink: Create Runtime Image (JRE)
Write-Host "3. Criando imagem de runtime customizada (JRE) com JLink..."
jlink `
    --module-path "$FX_SDK_PATH" `
    --add-modules $FX_MODULES `
    --output $RUNTIME_DIR `
    --strip-debug `
    --compress=2 `
    --no-header-files `
    --no-man-pages

Write-Host "   Runtime image criada em: $RUNTIME_DIR"
Write-Host ""

# ----------------------------------------------------------------------------------
## 4. JPackage: Create MSI Installer (Single Step)
Write-Host "4. Criando instalador Windows (.msi) com o JRE customizado..."

# OBS: No Windows, as DLLs do JavaFX precisam estar na pasta 'bin' do aplicativo
# Por isso, o Djava.library.path aponta para o bin, e a cópia de libs no passo 2 garante que jpackage as encontre.
jpackage `
    --input $INPUT_DIR `
    --dest $DIST_DIR `
    --main-jar $JAR_FILE `
    --main-class $APP_MAIN_CLASS `
    --name $APP_NAME `
    --app-version $APP_VERSION `
    --vendor "$APP_VENDOR" `
    --copyright "$APP_COPYRIGHT" `
    --description "$APP_DESCRIPTION" `
    --type msi `
    --runtime-image $RUNTIME_DIR `
    --icon "$APP_ICON" `
    --win-menu `
    --win-menu-group "$APP_NAME" `
    --win-shortcut `
    --win-dir-chooser `
    --win-per-user-install `
    --java-options "-Djava.library.path=\$APPDIR\bin" `
    --java-options "--enable-native-access=javafx.graphics" `
    --java-options "-Dprism.verbose=true"

Write-Host ""
Write-Host "✅ Instalador MSI criado com sucesso!"
Write-Host "O arquivo do instalador está em: $DIST_DIR"
Write-Host ""

# ----------------------------------------------------------------------------------
## 5. Final Cleanup
Write-Host "5. Limpando diretórios de build temporários..."
Remove-Item -Path $BUILD_DIR -Recurse -Force