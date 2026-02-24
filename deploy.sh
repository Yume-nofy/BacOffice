#!/bin/bash

echo "==============================="
echo "🚀 Build & Deploy (Linux/Mac)"
echo "==============================="

# Stop en cas d’erreur
set -e

# Build Maven
mvn clean package

echo "✅ Build OK"
echo "▶️ Lancement de l'application..."

# Lancer Tomcat embarque avec l'autorisation d'acces aux modules java.time
java --add-opens java.base/java.time=ALL-UNNAMED -jar target/dependency/webapp-runner.jar target/my-framework-app.war