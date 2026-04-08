package org.utl.dsm.integradoraweb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    private Connection conn;

    public Connection open() {
        // Datos fijos de tu nueva base de datos en Railway
        String user = "root";
        String password = "GIzvEZkDlYwpINLdXtxwzfmPdWWfRcDw";
        
        // URL usando la red interna de Railway (más rápida y estable)
        String url = "jdbc:mysql://mysql.railway.internal:3306/railway"
                   + "?useSSL=false"
                   + "&allowPublicKeyRetrieval=true"
                   + "&serverTimezone=UTC";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("ERROR BD: " + e.getMessage());
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