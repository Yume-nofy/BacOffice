#!/bin/bash

# Configuration de l'encodage UTF-8
export LANG=en_US.UTF-8
export LC_ALL=en_US.UTF-8

echo "========================================"
echo "      TOKEN SEEDER - BAC OFFICE"
echo "========================================"
echo ""

# Compilation et execution avec Maven en une seule commande
echo "Compilation et execution en cours..."
echo ""

# Exécution de Maven
mvn clean compile exec:java -Dexec.mainClass="main.TokenMain"

# Vérification du code de retour
if [ $? -eq 0 ]; then
    echo ""
    echo " Seeder execute avec succes!"
else
    echo ""
    echo " Erreur lors de l'execution du seeder"
fi

echo ""
read -p "Appuyez sur Entrée pour continuer..."