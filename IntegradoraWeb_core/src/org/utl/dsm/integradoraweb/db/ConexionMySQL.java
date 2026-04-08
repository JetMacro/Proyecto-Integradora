package org.utl.dsm.integradoraweb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    private Connection conn;

    public Connection open() {
        // Leemos las variables directamente desde Railway
        String host = System.getenv("MYSQLHOST");
        String port = System.getenv("MYSQLPORT");
        String dbName = System.getenv("MYSQLDATABASE");
        String user = System.getenv("MYSQLUSER");
        String password = System.getenv("MYSQLPASSWORD");

        // Construimos la URL dinámicamente
        // Si estás en la misma red de Railway, usará mysql.railway.internal:3306 automáticamente
        String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName 
                   + "?useSSL=false"
                   + "&allowPublicKeyRetrieval=true"
                   + "&serverTimezone=UTC";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            // Esto ayudará a ver el error real en los Logs de Railway
            throw new RuntimeException("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}