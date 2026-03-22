package org.utl.dsm.integradoraweb.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionMySQL {

    Connection conn;

    public Connection open() {
        // Datos actualizados con tu URL de Railway
        String user = "root";
        String password = "GIzvEZkDlYwpINLdXtxwzfmPdWWfRcDw";
        String url = "jdbc:mysql://centerbeam.proxy.rlwy.net:18338/railway"
                   + "?useSSL=false&useUnicode=true&characterEncoding=utf-8";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(url, user, password);
            return conn;
        } catch (Exception e) {
            e.printStackTrace(); // Es mejor imprimir el error para saber qué falló
            throw new RuntimeException(e);
        }
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Excepcion controlada.");
            }
        }
    }
}