@echo off
chcp 65001 >nul
title Build ^& Deploy (Windows)

echo ===================================
echo    Build ^& Deploy (Windows)
echo ===================================

call mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERREUR] Le build Maven a echoue avec le code %ERRORLEVEL%
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo  Build OK
echo  Lancement de l'application...

call java --add-opens java.base/java.time=ALL-UNNAMED -jar "target/dependency/webapp-runner.jar" "target/my-framework-app.war"

if %ERRORLEVEL% NEQ 0 (
    echo [ERREUR] Echec du lancement Java.
)

pause