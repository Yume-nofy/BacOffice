@echo off
setlocal enabledelayedexpansion

echo ===============================================
echo Deploiement vers Tomcat
echo ===============================================

:: Configuration des chemins
set "TOMCAT_WEBAPPS=C:\xampp\tomcat\webapps"
set "APP_NAME=test_framework"
set "SOURCE_DIR=."
set "WAR_FILE=%APP_NAME%.war"

:: Verification de l'existence du dossier Tomcat
if not exist "%TOMCAT_WEBAPPS%" (
    echo Erreur: Dossier Tomcat introuvable: %TOMCAT_WEBAPPS%
    echo Veuillez verifier le chemin de Tomcat.
    pause
    exit /b 1
)

:: Verification de la presence de Java (necessaire pour jar)
java -version >nul 2>&1
if errorlevel 1 (
    echo Erreur: Java n'est pas installe ou n'est pas dans le PATH
    echo Le JDK est necessaire pour creer le fichier WAR
    pause
    exit /b 1
)

:: Suppression des anciens fichiers
echo Suppression des anciens fichiers...
if exist "%WAR_FILE%" (
    echo Suppression de l'ancien WAR local...
    del "%WAR_FILE%"
)

if exist "%TOMCAT_WEBAPPS%\%APP_NAME%" (
    echo Suppression de l'ancienne application deployee...
    rmdir /s /q "%TOMCAT_WEBAPPS%\%APP_NAME%"
)

if exist "%TOMCAT_WEBAPPS%\%APP_NAME%.war" (
    echo Suppression de l'ancien WAR de Tomcat...
    del "%TOMCAT_WEBAPPS%\%APP_NAME%.war"
)

:: Arrêt de Tomcat (optionnel - decommentez si necessaire)
:: echo Arret de Tomcat...
:: call "%TOMCAT_HOME%\bin\shutdown.bat"
:: timeout /t 5

:: Creation du fichier WAR
echo Creation du fichier WAR...
cd "%SOURCE_DIR%"

:: Verification de la structure web standard
if not exist "WEB-INF" (
    echo Attention: Dossier WEB-INF non trouve dans %SOURCE_DIR%
    echo Le WAR pourrait ne pas etre structure correctement.
)

:: Creation du WAR avec la commande jar
jar -cvf "%WAR_FILE%" * >nul

if errorlevel 1 (
    echo Erreur lors de la creation du WAR!
    pause
    exit /b 1
)

echo Fichier WAR cree avec succes: %WAR_FILE%

:: Copie du WAR vers Tomcat
echo Deploiement du WAR vers Tomcat...
copy "%WAR_FILE%" "%TOMCAT_WEBAPPS%\" >nul

if errorlevel 1 (
    echo Erreur lors du deploiement du WAR!
    pause
    exit /b 1
)

:: Nettoyage du WAR local (optionnel)
echo Nettoyage du WAR local...
del "%WAR_FILE%"

echo ===============================================
echo Deploiement termine avec succes!
echo Fichier WAR deploye: %APP_NAME%.war
echo Application disponible sur: http://localhost:8080/%APP_NAME%/
echo ===============================================

:: Redemarrage de Tomcat (optionnel)
echo.
echo Note: Tomcat va automatiquement decompacter le WAR et deployer l'application.
echo Si Tomcat est arrete, demarrez-le manuellement.

endlocal
pause