#!/bin/bash

# Define que o script deve sair em caso de erro
set -e

# --- Application Configuration ---
GRADLE_PROPERTIES_FILE="gradle.properties"

if [ ! -f "$GRADLE_PROPERTIES_FILE" ]; then
    echo "🚨 ERRO: gradle.properties não encontrado."
    exit 1
fi

# Função para ler propriedade ignorando comentários
get_property() {
    grep -a "^$1=" "$GRADLE_PROPERTIES_FILE" | head -n 1 | cut -d'=' -f2- | tr -d '\r'
}

APP_NAME=$(get_property "appName")
APP_VERSION=$(get_property "appVersion")
APP_VENDOR=$(get_property "appVendor")
APP_COPYRIGHT=$(get_property "appCopyright")
APP_DESCRIPTION=$(get_property "appDescription")
APP_MAIN_CLASS=$(get_property "appMainClass")

if [ -z "$APP_NAME" ] || [ -z "$APP_VERSION" ] || [ -z "$APP_MAIN_CLASS" ]; then
    echo "🚨 ERRO: Alguma propriedade não foi lida corretamente do gradle.properties"
    echo "APP_NAME=$APP_NAME"
    echo "APP_VERSION=$APP_VERSION"
    echo "APP_MAIN_CLASS=$APP_MAIN_CLASS"
    exit 1
fi

JAR_FILE="${APP_NAME}-${APP_VERSION}.jar"

# Módulos JavaFX
FX_MODULES="javafx.controls,javafx.graphics"
JAVAFX_SDK_VERSION="25.0.1"
FX_SDK_PATH="java_fx_modules/linux-${JAVAFX_SDK_VERSION}"
FX_LIB_PATH="${FX_SDK_PATH}/lib"
FX_BIN_PATH="${FX_SDK_PATH}/lib"
APP_ICON="src/main/resources/assets/app_ico.png"

# Módulos JavaFX a incluir
FX_JARS=("javafx-controls" "javafx-graphics")

# Pastas de trabalho
BUILD_DIR="build"
DIST_DIR="dist"
RUNTIME_DIR="${BUILD_DIR}/runtime"
INPUT_DIR="${BUILD_DIR}/input_app"

echo "### 📦 JPackage Build Script para Linux (Otimizado) ###"
echo

# --- 1. Requirements Check ---
echo "1. Checando 'jpackage' e 'java'..."
if ! command -v jpackage &> /dev/null || ! command -v java &> /dev/null; then
    echo "🚨 ERRO: 'jpackage' ou 'java' não encontrados."
    exit 1
fi

echo "Requisitos básicos atendidos."
echo

# --- 2. Cleanup and Preparation ---
echo "2. Limpando diretórios temporários..."
rm -rf "$DIST_DIR"
rm -rf "$INPUT_DIR"
rm -rf "$RUNTIME_DIR"

mkdir -p "$INPUT_DIR" "$DIST_DIR"

echo "   Copiando JAR principal..."
if [ -f "build/libs/${JAR_FILE}" ]; then
    cp "build/libs/${JAR_FILE}" "$INPUT_DIR/"
else
    echo "🚨 ERRO: JAR não encontrado em build/libs/${JAR_FILE}"
    exit 1
fi

# Copia dependências do Gradle
echo "   Copiando dependências do Gradle..."
if [ -d "build/dependencies" ]; then
    cp build/dependencies/*.jar "$INPUT_DIR/"
fi

# Copia apenas os JARs do JavaFX necessários
echo "   Copiando JARs do JavaFX necessários..."
for jar in "${FX_JARS[@]}"; do
    cp "${FX_LIB_PATH}/${jar}-${JAVAFX_SDK_VERSION}-linux.jar" "$INPUT_DIR/" 2>/dev/null || true
    cp "${FX_LIB_PATH}/${jar}.jar" "$INPUT_DIR/" 2>/dev/null || true
done

# Copia javafx.base se necessário
cp "${FX_LIB_PATH}/javafx.base.jar" "$INPUT_DIR/" 2>/dev/null || true
cp "${FX_LIB_PATH}/javafx-base-${JAVAFX_SDK_VERSION}-linux.jar" "$INPUT_DIR/" 2>/dev/null || true

echo ""

# --- 3. JLink: Create Runtime Image (JRE) ---
echo "3. Criando imagem de runtime customizada (JRE) com JLink..."
jlink \
    --module-path "$FX_LIB_PATH" \
    --add-modules $FX_MODULES \
    --output "$RUNTIME_DIR" \
    --strip-debug \
    --compress=2 \
    --no-header-files \
    --no-man-pages

echo "   Runtime image criada em: ${RUNTIME_DIR}"
echo

# --- 4. JPackage: Create Installer ---
echo "4. Criando instalador Linux (.deb)..."
jpackage \
    --input "$INPUT_DIR" \
    --dest "$DIST_DIR" \
    --main-jar "${JAR_FILE}" \
    --main-class "$APP_MAIN_CLASS" \
    --name "$APP_NAME" \
    --app-version "$APP_VERSION" \
    --vendor "$APP_VENDOR" \
    --copyright "$APP_COPYRIGHT" \
    --description "$APP_DESCRIPTION" \
    --type deb \
    --runtime-image "$RUNTIME_DIR" \
    --icon "$APP_ICON" \
    --linux-menu-group "Utility;Utilities;Tool;Tools" \
    --linux-shortcut \
    --linux-app-category "Utility" \
    --java-options "--enable-native-access=javafx.graphics" \
    --java-options "-Dprism.verbose=true"

echo
echo "✅ Instalador criado com sucesso!"
echo "O arquivo do instalador está em: ${DIST_DIR}"
echo

# --- 5. Final Cleanup ---
echo "5. Limpando diretórios de build temporários..."
rm -rf "$BUILD_DIR"
