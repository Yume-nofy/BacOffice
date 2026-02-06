@echo off
title Build & Deploy (Windows)
echo ===============================
echo   Build ^& Deploy (Windows)
echo ===============================

call mvn clean package

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERREUR] Le build Maven a echoue.
    pause
    exit /b %ERRORLEVEL%
)

echo.
echo  Build OK
echo  Lancement de l'application...

call java -jar target\dependency\webapp-runner.jar target\my-framework-app.war

pause