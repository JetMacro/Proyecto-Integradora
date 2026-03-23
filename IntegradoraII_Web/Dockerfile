# Cambiamos a Tomcat 10 para soportar Jakarta EE 9.1
FROM tomcat:10.1-jdk17-openjdk-slim

# Exponer el puerto de Railway
EXPOSE 8080

# Limpiar aplicaciones base
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar tu archivo WAR como ROOT para que cargue al entrar al link
COPY dist/IntegradoraII_Web.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]