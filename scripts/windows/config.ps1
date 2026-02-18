# config.ps1 - Configurações da aplicação (deve ser incluído primeiro)
$script:PROJECT_ROOT = Split-Path -Parent (Split-Path -Parent $PSScriptRoot)

# Lê configurações do gradle.properties
$props = @{}
Get-Content "$script:PROJECT_ROOT\gradle.properties" | ForEach-Object {
    if ($_ -match '^([^=]+)=(.*)$') {
        $props[$matches[1].Trim()] = $matches[2].Trim()
    }
}

$env:APP_NAME = $props["appName"]
$env:APP_VERSION = $props["appVersion"]
$env:APP_VENDOR = $props["appVendor"]
$env:APP_COPYRIGHT = $props["appCopyright"]
$env:APP_DESCRIPTION = $props["appDescription"]
$env:APP_MAIN_CLASS = $props["appMainClass"]
$env:JAR_FILE = "$env:APP_NAME-$env:APP_VERSION.jar"

# Módulos JavaFX
$env:FX_MODULES = "javafx.controls,javafx.graphics"
$env:JAVAFX_SDK_VERSION = "25.0.1"
$env:FX_SDK_PATH = "$script:PROJECT_ROOT\java_fx_modules\windows-${env:JAVAFX_SDK_VERSION}"
$env:FX_LIB_PATH = "$env:FX_SDK_PATH\lib"
$env:FX_BIN_PATH = "$env:FX_SDK_PATH\bin"
$env:APP_ICON = "$script:PROJECT_ROOT\src\main\resources\assets\app_ico.ico"

# Módulos JavaFX a incluir
$env:FX_JARS = "javafx-controls,javafx-graphics"

# Pastas de trabalho
$env:BUILD_DIR = "$script:PROJECT_ROOT\build"
$env:DIST_DIR = "$script:PROJECT_ROOT\dist"
$env:RUNTIME_DIR = "$env:BUILD_DIR\runtime"
$env:INPUT_DIR = "$env:BUILD_DIR\input_app"
