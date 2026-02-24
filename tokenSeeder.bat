@echo off
chcp 65001 >nul
echo ========================================
echo      TOKEN SEEDER - BAC OFFICE
echo ========================================
echo.

REM Compilation et execution avec Maven en une seule commande
echo Compilation et execution en cours...
echo.

call mvn clean compile exec:java -Dexec.mainClass="main.TokenMain"

if %errorlevel% equ 0 (
    echo.
    echo  Seeder execute avec succes!
) else (
    echo.
    echo  Erreur lors de l'execution du seeder
)

echo.
pause