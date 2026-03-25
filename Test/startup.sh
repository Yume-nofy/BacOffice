#!/bin/bash
echo "========================================"
echo "   Switching JAVA_HOME to JDK 17"
echo "========================================"

export JAVA_HOME=/usr/lib/jvm/jdk-17-oracle-x64

export PATH=$JAVA_HOME/bin:$PATH

echo "JAVA_HOME is now: $JAVA_HOME"
echo

echo "========================================"
echo "   Starting Apache Tomcat"
echo "========================================"

CATALINA_HOME=/home/yume/apache-tomcat-10.1.34
cd "$CATALINA_HOME/bin"

./startup.sh

cd -
echo "Tomcat started. Access: http://localhost:8080"
echo "Logs: $CATALINA_HOME/logs/catalina.out"

