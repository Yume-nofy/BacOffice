@echo off
title Build & Deploy (Windows)
echo ===============================
echo   Build ^& Deploy (Windows)
echo ===============================

:: Build Maven
:: On utilise 'call' car mvn est souvent un fichier .cmd ou .bat
call mvn clean package

:: Vérification de l'erreur (équivalent de set -e)
if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERREUR] Le build Maven a echoue.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo  Build OK
echo  Lancement de l'application...

:: Lancer l'application
java -jar target\dependency\webapp-runner.jar target\my-framework-app.war

pause