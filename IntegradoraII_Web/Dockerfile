# Usamos Tomcat 10.1 con una versión de Java 17 actualizada (0.12) 
# Esto corrige el error de "CgroupInfo" que sale en tu log
FROM tomcat:10.1.28-jdk17-openjdk-slim

# Railway usa el puerto 8080
EXPOSE 8080

# Limpiar aplicaciones base
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos tu archivo WAR como ROOT
COPY dist/IntegradoraII_Web.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]