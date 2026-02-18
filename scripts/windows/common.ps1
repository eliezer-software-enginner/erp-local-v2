# common.ps1 - Funções comuns para os scripts de build Windows
# Deve ser incluído APÓS config.ps1

function Write-Step($message) {
    Write-Host "   $message"
}

function Test-Requirements {
    param([string]$ToolName, [string]$ErrorMsg)
    
    if (-not (Get-Command $ToolName -ErrorAction SilentlyContinue)) {
        Write-Error "🚨 ERRO: $ErrorMsg"
        exit 1
    }
}

function Clear-BuildDirectory {
    Write-Host "5. Limpando diretórios de build temporários..."
    if (Test-Path "$env:BUILD_DIR\runtime") { Remove-Item -Path "$env:BUILD_DIR\runtime" -Recurse -Force -ErrorAction SilentlyContinue }
    if (Test-Path "$env:BUILD_DIR\input_app") { Remove-Item -Path "$env:BUILD_DIR\input_app" -Recurse -Force -ErrorAction SilentlyContinue }
}

function Copy-MainJar {
    Write-Step "Copiando JAR principal..."
    $jarPath = "$env:BUILD_DIR\libs\${env:JAR_FILE}"
    if (-not (Test-Path $jarPath)) {
        Write-Error "🚨 ERRO: JAR não encontrado em ${jarPath}"
        exit 1
    }
    Copy-Item $jarPath "$env:INPUT_DIR\"
}

function Copy-Dependencies {
    Write-Step "Copiando dependências do Gradle..."
    $depsDir = "$env:BUILD_DIR\dependencies"
    if (Test-Path $depsDir) {
        Copy-Item "$depsDir\*.jar" "$env:INPUT_DIR\" -ErrorAction SilentlyContinue
    }
}

function Copy-FxBinaries {
    # Copia bibliotecas nativas (DLLs) para a pasta bin
    Write-Step "Copiando bibliotecas nativas do JavaFX (DLLs)..."
    if (Test-Path $env:FX_BIN_PATH) {
        New-Item -Path "$env:INPUT_DIR\bin" -ItemType Directory -Force | Out-Null
        Copy-Item "$env:FX_BIN_PATH\*" "$env:INPUT_DIR\bin\" -Recurse -Force
    } else {
        Write-Warning "⚠️ JavaFX bin path não encontrado: $env:FX_BIN_PATH"
    }
    
    # Copia os JARs necessários do JavaFX
    Write-Step "Copiando JARs do JavaFX necessários..."
    if (Test-Path $env:FX_LIB_PATH) {
        $fxJarsList = @("javafx-controls", "javafx-graphics", "javafx-base")
        foreach ($jar in $fxJarsList) {
            Copy-Item "$env:FX_LIB_PATH\${jar}-${env:JAVAFX_SDK_VERSION}-win.jar" "$env:INPUT_DIR\" -ErrorAction SilentlyContinue
            Copy-Item "$env:FX_LIB_PATH\${jar}.jar" "$env:INPUT_DIR\" -ErrorAction SilentlyContinue
        }
    }
}

function New-JreImage {
    Write-Host "3. Criando imagem de runtime customizada (JRE) com JLink..."
    $modules = "$env:FX_MODULES,java.sql,java.logging"
    jlink `
        --module-path "$env:FX_LIB_PATH" `
        --add-modules $modules `
        --output $env:RUNTIME_DIR `
        --strip-debug `
        --compress=2 `
        --no-header-files `
        --no-man-pages
    
    Write-Step "Runtime image criada em: $env:RUNTIME_DIR"
    Write-Host ""
}

function Write-Title($message) {
    Write-Host ""
    Write-Host $message
    Write-Host ""
}

function Write-Success($message) {
    Write-Host ""
    Write-Host "✅ $message"
}
