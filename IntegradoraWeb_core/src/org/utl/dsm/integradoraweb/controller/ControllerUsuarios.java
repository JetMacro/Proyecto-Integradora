package org.utl.dsm.integradoraweb.controller;

import org.utl.dsm.integradoraweb.db.ConexionMySQL;
import org.utl.dsm.integradoraweb.model.Usuarios;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerUsuarios {

    public Usuarios login(String usuario, String contrasenia) throws SQLException {
        String sql = "{CALL sp_login_usuario(?, ?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Usuarios u = null;

        // Uso de try-with-resources para asegurar que la conexión se cierre siempre
        try (Connection conn = connMySQL.open();
             CallableStatement cstmt = conn.prepareCall(sql)) {

            cstmt.setString(1, usuario);
            cstmt.setString(2, contrasenia);

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    int success = rs.getInt("success");

                    if (success == 1) {
                        u = new Usuarios();
                        u.setId_usuario(rs.getInt("id_usuario"));
                        u.setMatricula(rs.getString("matricula"));
                        u.setId_rol(rs.getString("id_rol"));
                        u.setId_turno(rs.getString("id_turno"));
                        u.setNombre(rs.getString("nombre"));
                        u.setApellido_paterno(rs.getString("apellido_paterno"));
                        u.setApellido_materno(rs.getString("apellido_materno"));
                        
                        // Sincronizado con las nuevas columnas de tu SQL
                        u.setToken(rs.getString("token")); 
                        u.setLast_used_token(rs.getString("last_used_token"));
                        
                        System.out.println("Login exitoso en Java para: " + u.getMatricula());
                    } else {
                        System.out.println("Login fallido: " + rs.getString("message"));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error en el login (Controller): " + e.getMessage());
            throw e; // Lanza el error para que el REST lo atrape y envíe el 500 con detalle
        }
        return u;
    }

    // Asegúrate de que los métodos getAll, insertar, etc., llamen a los nombres 
    // exactos de tus procedimientos (sp_ConsultarUsuarios, etc.)
    public List<Usuarios> getAll() throws Exception {
        // Tu SP se llama sp_ConsultarUsuarios según tu script
        String query = "{CALL sp_ConsultarUsuarios()}"; 
        List<Usuarios> listaUsuarios = new ArrayList<>();
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        try (Connection conn = connMySQL.open();
             CallableStatement cstmt = conn.prepareCall(query);
             ResultSet rs = cstmt.executeQuery()) {
            
            while (rs.next()) {
                Usuarios u = new Usuarios();
                u.setId_usuario(rs.getInt("id_usuario"));
                u.setMatricula(rs.getString("matricula"));
                u.setId_rol(rs.getString("id_rol"));
                u.setId_turno(rs.getString("id_turno"));
                u.setId_estatus(rs.getString("id_estatus"));
                u.setId_persona(rs.getInt("id_persona"));
                u.setNombre(rs.getString("nombre"));
                u.setApellido_paterno(rs.getString("apellido_paterno"));
                u.setApellido_materno(rs.getString("apellido_materno"));
                u.setCorreo(rs.getString("correo"));
                u.setTelefono(rs.getString("telefono"));
                u.setFecha_nacimiento(rs.getString("fecha_nacimiento"));
                u.setDireccion(rs.getString("direccion"));
                listaUsuarios.add(u);
            }
        }
        return listaUsuarios;
    }
}