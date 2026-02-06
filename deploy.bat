@echo off
echo ===============================
echo 🚀 Build & Deploy (Windows)
echo ===============================

REM Nettoyage et build
call mvn clean package
IF %ERRORLEVEL% NEQ 0 (
    echo ❌ Erreur Maven
    pause
    exit /b 1
)

REM Vérification des fichiers
IF NOT EXIST target\webapp-runner.jar (
    echo ❌ webapp-runner.jar introuvable
    pause
    exit /b 1
)

IF NOT EXIST target\my-framework-app.war (
    echo ❌ WAR introuvable
    pause
    exit /b 1
)

echo ✅ Build OK
echo ▶️ Lancement de l'application...

REM Lancer Tomcat embarqué
java -jar target\webapp-runner.jar target\my-framework-app.war

pause
