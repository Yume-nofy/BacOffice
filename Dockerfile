# On part d'une image Tomcat 8 avec Java 8
FROM tomcat:8.5-jdk8-openjdk

# On supprime les applications par defaut de Tomcat pour plus de legerete
RUN rm -rf /usr/local/tomcat/webapps/*

# On copie ton fichier WAR genere par Maven dans le dossier webapps de Tomcat
# On le nomme ROOT.war pour qu'il soit accessible à la racine (/) sans le nom du projet
COPY target/my-framework-app.war /usr/local/tomcat/webapps/ROOT.war

# Port standard pour Render
EXPOSE 8080

CMD ["catalina.sh", "run"]