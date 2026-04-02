#!/bin/bash

# ============================================
# Build Script for Chat Language Translate
# ============================================
# Compile et génère le JAR avec les bonnes versions:
# - Java 21
# - Gradle 8.8

set -e

# Couleurs pour l'affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}================================${NC}"
echo -e "${YELLOW}Chat Language Translate - Build${NC}"
echo -e "${YELLOW}================================${NC}"
echo ""

# ============================================
# Vérifier Java 21
# ============================================
echo -e "${YELLOW}[1/4]${NC} Vérifiant Java 21..."

JAVA21_PATHS=(
    "/usr/lib/jvm/java-21-openjdk"
    "/usr/lib/jvm/jdk-21"
    "/usr/local/jdk-21"
)

JAVA_FOUND=0
for JAVA_PATH in "${JAVA21_PATHS[@]}"; do
    if [ -d "$JAVA_PATH" ]; then
        export JAVA_HOME="$JAVA_PATH"
        export PATH="$JAVA_HOME/bin:$PATH"
        JAVA_FOUND=1
        break
    fi
done

if [ $JAVA_FOUND -eq 0 ]; then
    echo -e "${RED}✗ Java 21 non trouvé!${NC}"
    echo ""
    echo "Installation requise:"
    echo "  Arch Linux:     sudo pacman -S jdk21-openjdk"
    echo "  Debian/Ubuntu:  sudo apt install openjdk-21-jdk"
    exit 1
fi

JAVA_VERSION=$(java -version 2>&1 | head -1)
echo -e "${GREEN}✓ Java trouvé:${NC} $JAVA_VERSION"
echo ""

# ============================================
# Vérifier Gradle Wrapper
# ============================================
echo -e "${YELLOW}[2/4]${NC} Vérifiant Gradle Wrapper..."

if [ ! -f "gradle/wrapper/gradle-wrapper.jar" ]; then
    echo -e "${RED}✗ Gradle wrapper non trouvé!${NC}"
    exit 1
fi

GRADLE_VERSION=$(java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain --version 2>&1 | grep "Gradle" | head -1)
echo -e "${GREEN}✓ Gradle trouvé:${NC} $GRADLE_VERSION"
echo ""

# ============================================
# Clean (optionnel)
# ============================================
if [ "$1" == "clean" ]; then
    echo -e "${YELLOW}[3/4]${NC} Nettoyage des anciens fichiers..."
    rm -rf build .gradle
    echo -e "${GREEN}✓ Nettoyage terminé${NC}"
    echo ""
fi

# ============================================
# Compilation
# ============================================
echo -e "${YELLOW}[3/4]${NC} Compilation en cours (cela peut prendre quelques minutes)..."
echo ""

java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain build

echo ""
echo -e "${YELLOW}[4/4]${NC} Récupération du JAR..."
echo ""

# ============================================
# Afficher le résultat
# ============================================
if [ -f "build/libs/chat_language_translate-1.0.0.jar" ]; then
    echo -e "${GREEN}✓ Build réussi!${NC}"
    echo ""
    echo "JAR généré:"
    echo -e "  ${GREEN}build/libs/chat_language_translate-1.0.0.jar${NC}"
    echo ""
    echo "Prochaines étapes:"
    echo "  1. Copie le JAR dans ton dossier mods Minecraft"
    echo "  2. Lance Minecraft avec Fabric 1.21.1"
    echo ""
else
    echo -e "${RED}✗ Erreur: JAR non trouvé après compilation${NC}"
    exit 1
fi
