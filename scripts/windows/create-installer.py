#!/usr/bin/env python3
"""
Script de Build Windows - Criar instalador MSI usando JPackage
"""

import os
import subprocess
import shutil
from pathlib import Path

# ============== CONFIGURAÇÕES ==============
SCRIPT_DIR = Path(__file__).parent
PROJECT_ROOT = SCRIPT_DIR.parent.parent

# Lê do gradle.properties
props = {}
gradle_props = PROJECT_ROOT / "gradle.properties"
if gradle_props.exists():
    for line in gradle_props.read_text(encoding='latin-1').splitlines():
        if '=' in line:
            key, value = line.split('=', 1)
            props[key.strip()] = value.strip()

APP_NAME = props.get('appName', 'plics-sw')
APP_VERSION = props.get('appVersion', '1.0.0')
APP_VENDOR = props.get('appVendor', 'Eliezer Dev')
APP_COPYRIGHT = props.get('appCopyright', 'Copyright 2025')
APP_DESCRIPTION = props.get('appDescription', 'Sistema de gestão')
APP_MAIN_CLASS = props.get('appMainClass', 'my_app.Main')
JAR_FILE = f"{APP_NAME}-{APP_VERSION}.jar"

FX_SDK_VERSION = "25.0.1"
FX_SDK_PATH = PROJECT_ROOT / "java_fx_modules" / f"windows-{FX_SDK_VERSION}"
FX_LIB_PATH = FX_SDK_PATH / "lib"
FX_BIN_PATH = FX_SDK_PATH / "bin"

BUILD_DIR = PROJECT_ROOT / "build"
DIST_DIR = PROJECT_ROOT / "dist"
RUNTIME_DIR = BUILD_DIR / "runtime"
INPUT_DIR = BUILD_DIR / "input_app"
DEPS_DIR = BUILD_DIR / "dependencies"

FX_MODULES = "javafx.controls,javafx.graphics"


def run_cmd(cmd, check=True):
    """Executa comando shell"""
    print(f"  -> {cmd}")
    result = subprocess.run(cmd, shell=True, capture_output=True, text=True, encoding='utf-8', errors='replace')
    if check and result.returncode != 0:
        print(f"ERRO: {result.stderr}")
        exit(1)
    return result


def check_requirements():
    """Verifica requisitos"""
    print("1. Verificando requisitos...")
    
    # Verifica jpackage
    result = subprocess.run("jpackage --version", shell=True, capture_output=True)
    if result.returncode != 0:
        print("ERRO: jpackage não encontrado")
        exit(1)
    
    # Verifica WiX
    result = subprocess.run("where light.exe", shell=True, capture_output=True)
    if result.returncode != 0:
        print("ERRO: WiX Toolset não encontrado")
        exit(1)
    
    print("   OK - Requisitos atendidos")


def prepare_dirs():
    """Prepara diretórios"""
    print("2. Preparando diretórios...")
    
    # Limpa e cria dirs
    for d in [DIST_DIR, INPUT_DIR]:
        if d.exists():
            shutil.rmtree(d)
        d.mkdir(parents=True, exist_ok=True)
    
    # Copia JAR
    jar_src = BUILD_DIR / "libs" / JAR_FILE
    if not jar_src.exists():
        print(f"ERRO: JAR não encontrado: {jar_src}")
        exit(1)
    shutil.copy(jar_src, INPUT_DIR / JAR_FILE)
    print(f"   Copiado: {JAR_FILE}")
    
    # Copia dependências
    if DEPS_DIR.exists():
        for jar in DEPS_DIR.glob("*.jar"):
            shutil.copy(jar, INPUT_DIR / jar.name)
        print(f"   Copiadas {len(list(DEPS_DIR.glob('*.jar')))} dependências")
    
    # Copia JavaFX binaries (DLLs)
    if FX_BIN_PATH.exists():
        bin_dir = INPUT_DIR / "bin"
        bin_dir.mkdir(exist_ok=True)
        for dll in FX_BIN_PATH.glob("*.dll"):
            shutil.copy(dll, bin_dir / dll.name)
        print(f"   Copiados {len(list(FX_BIN_PATH.glob('*.dll')))} DLLs JavaFX")
    
    # Copia JARs JavaFX
    fx_jars = ["javafx-controls", "javafx-graphics", "javafx-base"]
    for jar_name in fx_jars:
        for jar_file in FX_LIB_PATH.glob(f"{jar_name}*.jar"):
            shutil.copy(jar_file, INPUT_DIR / jar_file.name)
    print(f"   Copiados JARs JavaFX")


def create_jre_image():
    """Cria imagem JRE com JLink"""
    print("3. Criando runtime JRE com JLink...")
    
    if RUNTIME_DIR.exists():
        shutil.rmtree(RUNTIME_DIR)
    
    cmd = f'jlink --module-path "{FX_LIB_PATH}" --add-modules {FX_MODULES},java.sql,java.logging --output "{RUNTIME_DIR}" --strip-debug --compress=2 --no-header-files --no-man-pages'
    run_cmd(cmd)
    print(f"   OK - Runtime em: {RUNTIME_DIR}")


def create_msi():
    """Cria instalador MSI"""
    print("4. Criando instalador MSI...")
    
    app_icon = PROJECT_ROOT / "src/main/resources/assets/app_ico.ico"
    icon_arg = f'--icon "{app_icon}"' if app_icon.exists() else ""
    
    cmd = f'''jpackage --input "{INPUT_DIR}" --dest "{DIST_DIR}" --main-jar "{JAR_FILE}" --main-class {APP_MAIN_CLASS} --name {APP_NAME} --app-version {APP_VERSION} --vendor "{APP_VENDOR}" --copyright "{APP_COPYRIGHT}" --description "{APP_DESCRIPTION}" --type msi --module-path "{RUNTIME_DIR};{INPUT_DIR}" --add-modules {FX_MODULES},java.sql,java.logging {icon_arg} --win-menu --win-menu-group {APP_NAME} --win-shortcut --win-dir-chooser --win-per-user-install --java-options "-Djava.library.path=$APPDIR/bin" --java-options "--enable-native-access=javafx.graphics"'''
    
    run_cmd(cmd)
    print(f"   OK - Instalador em: {DIST_DIR / f'{APP_NAME}-{APP_VERSION}.msi'}")


def cleanup():
    """Limpa temporários"""
    print("5. Limpando temporários...")
    for d in [RUNTIME_DIR, INPUT_DIR]:
        if d.exists():
            shutil.rmtree(d)
    print("   OK")


def main():
    print("=" * 50)
    print("JPackage Build Script - Python")
    print("=" * 50)
    print()
    
    check_requirements()
    prepare_dirs()
    create_jre_image()
    create_msi()
    cleanup()
    
    print()
    print("=" * 50)
    print("SUCESSO! Instalador MSI criado.")
    print("=" * 50)


if __name__ == "__main__":
    main()
