# Usamos la imagen oficial de Tomcat 10.1 con Java 17
# Al no poner el parche específico (.28), Docker bajará la más reciente y estable
FROM tomcat:10.1-jdk17-slim

# Railway usa el puerto 8080
EXPOSE 8080

# Limpiar aplicaciones base para evitar conflictos
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos tu archivo WAR. 
# Asegúrate de que la ruta 'dist/IntegradoraII_Web.war' sea correcta en tu repo
COPY dist/IntegradoraII_Web.war /usr/local/tomcat/webapps/ROOT.war

CMD ["catalina.sh", "run"]