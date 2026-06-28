#!/bin/bash

# Ultimate Chat Translator - Build Script
# Installe les dependances manquantes (Arch/Debian) puis compile le JAR

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}"
echo "◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆"
echo "  Ultimate Chat Translator - Build"
echo "◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆"
echo -e "${NC}"

detect_distro() {
    if [ -f /etc/os-release ]; then
        . /etc/os-release
        DISTRO_ID="${ID:-unknown}"
        DISTRO_LIKE="${ID_LIKE:-}"
    else
        DISTRO_ID="unknown"
        DISTRO_LIKE=""
    fi
}

is_arch_based() {
    [[ "$DISTRO_ID" == "arch" || "$DISTRO_LIKE" == *"arch"* ]]
}

is_debian_based() {
    [[ "$DISTRO_ID" == "debian" || "$DISTRO_ID" == "ubuntu" || "$DISTRO_LIKE" == *"debian"* || "$DISTRO_LIKE" == *"ubuntu"* ]]
}

install_with_pacman() {
    local package="$1"
    if ! command -v sudo >/dev/null 2>&1; then
        echo -e "${RED}✗ sudo est requis pour installer ${package}.${NC}"
        exit 1
    fi
    echo -e "${YELLOW}→${NC} Installation de ${package} (pacman)..."
    sudo pacman -S --needed "$package"
}

install_with_apt() {
    local package="$1"
    if ! command -v sudo >/dev/null 2>&1; then
        echo -e "${RED}✗ sudo est requis pour installer ${package}.${NC}"
        exit 1
    fi
    echo -e "${YELLOW}→${NC} Mise a jour des depots APT..."
    sudo apt update
    echo -e "${YELLOW}→${NC} Installation de ${package} (apt)..."
    sudo apt install -y "$package"
}

version_ge() {
    # Retourne vrai si $1 >= $2
    [ "$(printf '%s\n' "$1" "$2" | sort -V | head -n1)" = "$2" ]
}

gradle_version() {
    "$1" -v 2>/dev/null | awk '/^Gradle / {print $2; exit}'
}

ensure_unzip() {
    if command -v unzip >/dev/null 2>&1; then
        return
    fi

    echo -e "${YELLOW}→${NC} unzip manquant, installation en cours..."
    detect_distro

    if is_arch_based; then
        install_with_pacman "unzip"
    elif is_debian_based; then
        install_with_apt "unzip"
    else
        echo -e "${RED}✗ Impossible d'installer unzip automatiquement sur ${DISTRO_ID}.${NC}"
        exit 1
    fi
}

ensure_gradle_compatible() {
    local min_required="8.11"
    local bundled_version="8.11"
    local gradle_cmd=""

    if command -v gradle >/dev/null 2>&1; then
        local system_version
        system_version=$(gradle_version "gradle")
        if [ -n "$system_version" ] && version_ge "$system_version" "$min_required"; then
            echo -e "${GREEN}✓ Gradle systeme${NC} compatible (${system_version})"
            GRADLE_CMD="gradle"
            return
        fi
        if [ -n "$system_version" ]; then
            echo -e "${YELLOW}→${NC} Gradle systeme trop ancien (${system_version}), minimum ${min_required}"
        else
            echo -e "${YELLOW}→${NC} Version Gradle systeme introuvable, fallback local"
        fi
    else
        echo -e "${YELLOW}→${NC} Gradle systeme absent, fallback local"
    fi

    ensure_unzip

    local gradle_dir=".gradle-bin/gradle-${bundled_version}"
    local gradle_zip=".gradle-bin/gradle-${bundled_version}-bin.zip"
    local gradle_url="https://services.gradle.org/distributions/gradle-${bundled_version}-bin.zip"
    local local_cmd="${gradle_dir}/bin/gradle"

    mkdir -p .gradle-bin

    if [ ! -x "$local_cmd" ]; then
        echo -e "${YELLOW}→${NC} Telechargement de Gradle ${bundled_version}..."
        if command -v curl >/dev/null 2>&1; then
            curl -fsSL "$gradle_url" -o "$gradle_zip"
        elif command -v wget >/dev/null 2>&1; then
            wget -qO "$gradle_zip" "$gradle_url"
        else
            echo -e "${RED}✗ curl ou wget requis pour telecharger Gradle.${NC}"
            exit 1
        fi

        echo -e "${YELLOW}→${NC} Extraction de Gradle ${bundled_version}..."
        unzip -qo "$gradle_zip" -d .gradle-bin
    fi

    local local_version
    local_version=$(gradle_version "$local_cmd")
    if [ -z "$local_version" ] || ! version_ge "$local_version" "$min_required"; then
        echo -e "${RED}✗ Gradle local invalide (${local_version:-inconnu}).${NC}"
        exit 1
    fi

    echo -e "${GREEN}✓ Gradle local${NC} pret (${local_version})"
    GRADLE_CMD="$local_cmd"
}

ensure_java_21() {
    if command -v java >/dev/null 2>&1; then
        local java_major
        java_major=$(java -version 2>&1 | awk -F '[\".]' '/version/ {print $2}')
        if [ "$java_major" = "21" ]; then
            echo -e "${GREEN}✓ Java 21${NC} deja installe"
            return
        fi
    fi

    echo -e "${YELLOW}→${NC} Java 21 manquant, installation en cours..."
    detect_distro

    if is_arch_based; then
        install_with_pacman "jdk21-openjdk"
    elif is_debian_based; then
        install_with_apt "openjdk-21-jdk"
    else
        echo -e "${RED}✗ Distribution non supportee automatiquement: ${DISTRO_ID}${NC}"
        echo "Installe Java 21 manuellement puis relance le script."
        exit 1
    fi
}

# Ensure Java 21 exists
echo -e "${YELLOW}→${NC} Verification de Java 21..."
ensure_java_21

# Set JAVA_HOME for common Linux locations (best effort)
JAVA_HOME_CANDIDATE=$(dirname "$(dirname "$(readlink -f "$(command -v java)")")")
if [ -d "$JAVA_HOME_CANDIDATE" ]; then
    export JAVA_HOME="$JAVA_HOME_CANDIDATE"
    export PATH="$JAVA_HOME/bin:$PATH"
fi

JAVA_VERSION=$(java -version 2>&1 | head -1)
echo -e "${GREEN}✓ Java:${NC} $JAVA_VERSION"

# Check build tool
echo -e "${YELLOW}→${NC} Verification de l'outil Gradle..."
if [ -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    BUILD_CMD="java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain"
    echo -e "${GREEN}✓ Gradle Wrapper${NC} detecte"
else
    echo -e "${YELLOW}→${NC} Gradle Wrapper absent, preparation d'un Gradle compatible"
    ensure_gradle_compatible
    echo -e "${YELLOW}→${NC} Generation du Gradle Wrapper (8.11)..."
    "$GRADLE_CMD" wrapper --gradle-version 8.11 --distribution-type bin || true
    if [ -f "gradle/wrapper/gradle-wrapper.jar" ]; then
        BUILD_CMD="java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain"
        echo -e "${GREEN}✓ Gradle Wrapper${NC} genere"
    else
        BUILD_CMD="$GRADLE_CMD"
        echo -e "${YELLOW}→${NC} Wrapper non genere, utilisation de Gradle compatible"
    fi
fi

# Clean option
if [ "$1" == "clean" ]; then
    echo -e "${YELLOW}→${NC} Nettoyage..."
    rm -rf build .gradle
    echo -e "${GREEN}✓ Nettoyé${NC}"
fi

# Build
echo -e "${YELLOW}→${NC} Compilation en cours..."
$BUILD_CMD build

# Final check
echo ""
JAR_PATH=$(find build/libs -maxdepth 1 -type f -name 'chat_language_translate-*.jar' ! -name '*-sources.jar' | sort -V | tail -1)
if [ -n "$JAR_PATH" ] && [ -f "$JAR_PATH" ]; then
    JAR_SIZE=$(ls -lh "$JAR_PATH" | awk '{print $5}')
    echo -e "${GREEN}◆ BUILD RÉUSSI!${NC}"
    echo ""
    echo "📦 JAR généré:"
    echo -e "   ${GREEN}$JAR_PATH${NC} ($JAR_SIZE)"
    echo ""
    echo "📋 Prochaines étapes:"
    echo "   1. Copie le JAR dans: .minecraft/mods/"
    echo "   2. Lance Minecraft avec Fabric 1.21.1"
    echo ""
else
    echo -e "${RED}✗ Erreur: JAR non généré!${NC}"
    exit 1
fi
