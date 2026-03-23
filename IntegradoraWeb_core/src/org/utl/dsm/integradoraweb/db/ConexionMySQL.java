package org.utl.dsm.integradoraweb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    private Connection conn;

    public Connection open() {
        // Datos de autenticación de tu instancia de Railway
        String user = "root";
        String password = "GIzvEZkDlYwpINLdXtxwzfmPdWWfRcDw";
        
        /**
         * CONFIGURACIÓN PARA RAILWAY:
         * 1. allowPublicKeyRetrieval=true: Permite que el driver de MySQL obtenga la llave del servidor.
         * 2. useSSL=false: Desactiva SSL para evitar errores de certificados en el entorno de pruebas.
         * 3. serverTimezone=UTC: Evita errores de desfase de horario entre el servidor de Railway y la BD.
         */
        String url = "jdbc:mysql://centerbeam.proxy.rlwy.net:18338/railway"
                   + "?useUnicode=true"
                   + "&characterEncoding=utf-8"
                   + "&useSSL=false"
                   + "&allowPublicKeyRetrieval=true"
                   + "&serverTimezone=UTC";

        try {
            // Cargamos el driver de MySQL 8
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a Railway MySQL");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el Driver de MySQL.");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.err.println("Error: No se pudo conectar a la base de datos.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexión cerrada.");
            } catch (SQLException e) {
                e.printStackTrace();
                System.out.println("Excepción al cerrar la conexión.");
            }
        }
    }
}