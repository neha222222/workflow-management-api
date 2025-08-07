@echo off
title Workforce Management API - Quick Start

echo === Workforce Management API - Quick Start ===
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ Java is not installed. Please install Java 17 or higher first.
    echo    Visit: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

echo ✅ Java is installed

REM Build the application
echo.
echo 🔨 Building the application...
gradlew.bat build

if %errorlevel% equ 0 (
    echo ✅ Build successful!
    echo.
    echo 🚀 Starting the Workforce Management API...
    echo    The API will be available at: http://localhost:8080
    echo    Press Ctrl+C to stop the application
    echo.
    gradlew.bat bootRun
) else (
    echo ❌ Build failed. Please check the error messages above.
    pause
    exit /b 1
)

pause