# Usamos la imagen oficial de Tomcat 10.1 con Java 17
# Esta versión es la correcta para tu proyecto de Jakarta EE 9.1
FROM tomcat:10.0-jdk17

# Exponemos el puerto 8080 que es el que Railway busca por defecto
EXPOSE 8080

# Eliminamos las aplicaciones que trae Tomcat por defecto para que no causen conflictos
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos tu archivo WAR desde la carpeta dist de tu GitHub
# Se renombra a ROOT.war para que tu app sea la página principal
COPY dist/IntegradoraII_Web.war /usr/local/tomcat/webapps/ROOT.war

# Comando para iniciar el servidor
CMD ["catalina.sh", "run"]