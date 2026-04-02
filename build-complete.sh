#!/bin/bash

# Build script for Chat Language Translate

set -e

export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export PATH=$JAVA_HOME/bin:$PATH

echo "================================"
echo "Building Chat Language Translate"
echo "================================"
echo ""
echo "Java version:"
java -version
echo ""
echo "Gradle version:"
java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain --version
echo ""
echo "Starting build..."
echo ""

java -classpath gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain build

echo ""
echo "Build completed successfully!"
echo ""
echo "Output JAR: build/libs/chat_language_translate-1.0.0.jar"
