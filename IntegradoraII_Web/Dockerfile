FROM tomcat:9.0-jdk17-openjdk-slim

# Railway usa el puerto 8080 por defecto
EXPOSE 8080

# Borramos lo que trae Tomcat para que no choque
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos tu archivo WAR. 
# IMPORTANTE: Asegúrate de que en NetBeans hiciste "Clean and Build" 
# para que exista la carpeta 'dist' con el archivo .war adentro.
COPY dist/*.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]