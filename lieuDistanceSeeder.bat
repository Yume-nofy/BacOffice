@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo ========================================
echo      LIEU ET DISTANCE SEEDER
echo         (avec Maven)
echo ========================================
echo.

REM Vérifier que Maven est installé
where mvn >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERREUR] Maven n'est pas installé
    pause
    exit /b 1
)

:MENU
echo.
echo Choix d'execution :
echo 1. Insérer les lieux et distances
echo 2. Afficher uniquement les données
echo 3. Afficher les distances depuis un lieu
echo 4. Quitter
echo.

set /p choix="Votre choix (1-4) : "

if "%choix%"=="1" goto INSERT
if "%choix%"=="2" goto DISPLAY
if "%choix%"=="3" goto DISPLAY_FROM
if "%choix%"=="4" goto END

echo Choix invalide!
goto MENU

:INSERT
echo.
echo [1/3] Compilation...
call mvn clean compile
if %errorlevel% neq 0 (
    echo [ERREUR] Compilation echouee!
    pause
    goto MENU
)

echo.
echo [2/3] Execution du seeder...
call mvn exec:java -Dexec.mainClass="main.LieuDistanceMain"
echo.
echo [3/3] Termine!
pause
goto MENU

:DISPLAY
echo.
echo Affichage des donnees...
call mvn exec:java -Dexec.mainClass="main.LieuDistanceMain" -Dexec.args="--display"
pause
goto MENU

:DISPLAY_FROM
echo.
set /p code="Entrez le code du lieu (COL, NOV, IBI, LOK) : "
echo.
echo Affichage des distances depuis %code%...
call mvn exec:java -Dexec.mainClass="main.LieuDistanceMain" -Dexec.args="--display %code%"
pause
goto MENU

:END
echo.
echo Au revoir!
pause