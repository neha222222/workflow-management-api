#!/bin/bash

echo "=== Workforce Management API - Quick Start ==="
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 17 or higher first."
    echo "   Visit: https://www.oracle.com/java/technologies/downloads/"
    exit 1
fi

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "❌ Java 17 or higher is required. Current version: $JAVA_VERSION"
    echo "   Please upgrade your Java installation."
    exit 1
fi

echo "✅ Java version check passed"

# Make gradlew executable
chmod +x gradlew
echo "✅ Gradle wrapper made executable"

# Build the application
echo ""
echo "🔨 Building the application..."
./gradlew build

if [ $? -eq 0 ]; then
    echo "✅ Build successful!"
    echo ""
    echo "🚀 Starting the Workforce Management API..."
    echo "   The API will be available at: http://localhost:8080"
    echo "   Press Ctrl+C to stop the application"
    echo ""
    ./gradlew bootRun
else
    echo "❌ Build failed. Please check the error messages above."
    exit 1
fi