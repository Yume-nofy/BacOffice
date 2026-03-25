@echo off
echo ================================
echo   Switching JAVA_HOME to JDK 17
echo ================================

set "JAVA_HOME=C:\Program Files\Java\jdk-17"
set "PATH=%JAVA_HOME%\bin;%PATH%"

echo JAVA_HOME is now: %JAVA_HOME%
echo.

echo ================================
echo   Starting Apache Tomcat
echo ================================

cd /d "C:\Program Files\apache-tomcat-10.1.28\bin"

startup.bat

cd /d "%~dp0"
