package org.utl.dsm.integradoraweb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    private Connection conn;

    public Connection open() {
        // Datos de autenticación (Variables de tu Railway)
        String user = "root";
        String password = "GIzvEZkDlYwpINLdXtxwzfmPdWWfRcDw";
        
        /**
         * CONFIGURACIÓN INTERNA PARA RAILWAY:
         * Host: mysql.railway.internal (MYSQLHOST)
         * Puerto: 3306 (MYSQLPORT interno)
         * BD: railway (MYSQLDATABASE)
         */
        String url = "jdbc:mysql://mysql.railway.internal:3306/railway"
                   + "?useUnicode=true"
                   + "&characterEncoding=utf-8"
                   + "&useSSL=false"
                   + "&allowPublicKeyRetrieval=true"
                   + "&serverTimezone=UTC";

        try {
            // Cargamos el driver de MySQL 8
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Intentamos la conexión a la red privada de Railway
            conn = DriverManager.getConnection(url, user, password);
            System.out.println(">>> CONEXIÓN EXITOSA A LA RED INTERNA DE RAILWAY <<<");
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se encontró el Driver de MySQL.");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.err.println("ERROR: Falló la conexión interna a la base de datos.");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
                System.out.println("Conexión cerrada correctamente.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}