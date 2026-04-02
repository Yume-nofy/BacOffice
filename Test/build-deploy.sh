#!/bin/bash
set -e

# ==========================================================
# === CONFIGURATION PERSONNALISABLE ========================
# ==========================================================
export JAVA_HOME=/usr/lib/jvm/jdk-17-oracle-x64

export PATH=$JAVA_HOME/bin:$PATH
export CATALINA_HOME=/home/yume/apache-tomcat-10.1.34
PROJECT_DIR=$(pwd)
BUILD_DIR="$PROJECT_DIR/build"
LIB="$PROJECT_DIR/lib/jakarta.servlet-api-5.0.0.jar:$PROJECT_DIR/lib/framework.jar"

# Create lib dir if missing
mkdir -p lib

# ==========================================================
# === NETTOYAGE DES ANCIENS BUILD ==========================
# ==========================================================
echo
echo "=== Nettoyage du dossier build ==="
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR"

# ==========================================================
# === COMPILATION DU PROJET ================================
# ==========================================================
echo
echo "=== Compilation du projet ==="
PROJECT_CLASSES="$BUILD_DIR/backoffice-classes"
mkdir -p "$PROJECT_CLASSES"

SRC_FILES=$(find "$PROJECT_DIR/src/main/java" -name "*.java")

if [ -z "$SRC_FILES" ]; then
    echo "Aucun fichier source trouvé dans $PROJECT_DIR/src/main/java"
    exit 1
fi

javac -encoding UTF-8 -parameters -cp "$LIB" -d "$PROJECT_CLASSES" $SRC_FILES
if [ $? -ne 0 ]; then
    echo "Erreur de compilation"
    exit 1
fi

echo "Compilation terminée"

# ==========================================================
# === CREATION DU WAR ======================================
# ==========================================================
echo
echo "=== Préparation de la structure WAR ==="
PROJECT_BUILD="$BUILD_DIR/war"
mkdir -p "$PROJECT_BUILD/WEB-INF/lib" "$PROJECT_BUILD/WEB-INF/classes"

cp -r "$PROJECT_DIR/src/webapp/"* "$PROJECT_BUILD/" 2>/dev/null || true
cp "$PROJECT_DIR/lib/framework.jar" "$PROJECT_BUILD/WEB-INF/lib/" 2>/dev/null || true
cp -r "$PROJECT_CLASSES"/* "$PROJECT_BUILD/WEB-INF/classes/" 2>/dev/null || true

echo
echo "=== Création du fichier WAR ==="
cd "$PROJECT_BUILD"
jar cf "$BUILD_DIR/backoffice.war" *
cd "$PROJECT_DIR"

echo "WAR généré : $BUILD_DIR/backoffice.war"

# ==========================================================
# === DEPLOIEMENT SUR TOMCAT ===============================
# ==========================================================
echo
echo "=== Déploiement sur Tomcat ==="
cp "$BUILD_DIR/backoffice.war" "$CATALINA_HOME/webapps/" || echo "Échec copie WAR (Tomcat absent?)"

echo "WAR copié dans : $CATALINA_HOME/webapps"

# ==========================================================
# === FIN DU BUILD =========================================
# ==========================================================
echo
echo "Compilation et déploiement terminés avec succès !"
echo "Adaptez JAVA_HOME/CATALINA_HOME si nécessaire."
echo "Run: $CATALINA_HOME/bin/startup.sh"

