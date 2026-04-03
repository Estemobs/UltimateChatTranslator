#!/bin/bash

# Chat Language Translate - Build Script
# Installe les dependances manquantes (Arch/Debian) puis compile le JAR

set -e

# Colors
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo -e "${YELLOW}"
echo "◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆◆"
echo "  Chat Language Translate - Build"
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

# Check Gradle Wrapper
echo -e "${YELLOW}→${NC} Cherchant Gradle Wrapper..."
if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo -e "${RED}✗ Erreur: Gradle wrapper non trouvé!${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Gradle Wrapper${NC} trouvé (8.8)"

# Clean option
if [ "$1" == "clean" ]; then
    echo -e "${YELLOW}→${NC} Nettoyage..."
    rm -rf build .gradle
    echo -e "${GREEN}✓ Nettoyé${NC}"
fi

# Build
echo -e "${YELLOW}→${NC} Compilation en cours..."
java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain build

# Final check
echo ""
if [ -f "build/libs/chat_language_translate-1.0.0.jar" ]; then
    JAR_SIZE=$(ls -lh "build/libs/chat_language_translate-1.0.0.jar" | awk '{print $5}')
    echo -e "${GREEN}◆ BUILD RÉUSSI!${NC}"
    echo ""
    echo "📦 JAR généré:"
    echo -e "   ${GREEN}build/libs/chat_language_translate-1.0.0.jar${NC} ($JAR_SIZE)"
    echo ""
    echo "📋 Prochaines étapes:"
    echo "   1. Copie le JAR dans: .minecraft/mods/"
    echo "   2. Lance Minecraft avec Fabric 1.21.1"
    echo ""
else
    echo -e "${RED}✗ Erreur: JAR non généré!${NC}"
    exit 1
fi
