#!/bin/bash

# Chat Language Translate - Build Script
# Simple compilation script to generate the JAR file

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

# Find Java 21
echo -e "${YELLOW}→${NC} Cherchant Java 21..."
JAVA_FOUND=0
for JAVA_PATH in "/usr/lib/jvm/java-21-openjdk" "/usr/lib/jvm/jdk-21" "/usr/local/jdk-21"; do
    if [ -d "$JAVA_PATH" ]; then
        export JAVA_HOME="$JAVA_PATH"
        export PATH="$JAVA_HOME/bin:$PATH"
        JAVA_FOUND=1
        break
    fi
done

if [ $JAVA_FOUND -eq 0 ]; then
    echo -e "${RED}✗ Erreur: Java 21 non trouvé!${NC}"
    echo "Installation:"
    echo "  Arch Linux:     sudo pacman -S jdk21-openjdk"
    echo "  Debian/Ubuntu:  sudo apt install openjdk-21-jdk"
    exit 1
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
java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain build 2>&1 | tail -10

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
