package org.utl.dsm.integradoraweb.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {

    private Connection conn;

    public Connection open() {
        // 1. Datos de acceso (los mismos que ya tienes)
        String user = "root";
        String password = "GIzvEZkDlYwpINLdXtxwzfmPdWWfRcDw";
        
        // 2. URL INTERNA: Cambiamos el host a 'mysql.railway.internal' 
        // y el puerto al estándar interno '3306'
        String url = "jdbc:mysql://mysql.railway.internal:3306/railway"
                   + "?useUnicode=true"
                   + "&characterEncoding=utf-8"
                   + "&useSSL=false"
                   + "&allowPublicKeyRetrieval=true"
                   + "&serverTimezone=UTC";

        try {
            // 3. Cargamos el driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // 4. Intentamos la conexión
            conn = DriverManager.getConnection(url, user, password);
            System.out.println(">>> CONEXIÓN EXITOSA A MYSQL (INTERNA) <<<");
            return conn;
            
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se encontró el conector de MySQL.");
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.err.println("ERROR: Falló la conexión a la base de datos.");
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
            }
        }
    }
}