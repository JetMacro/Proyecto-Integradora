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
                        
                        //u.setToken(rs.getString("token")); 
                        //u.setLast_used_token(rs.getString("last_used_token"));
                        System.out.println("Login exitoso en Java para: " + u.getMatricula());
                    }
                }
            }
        }
        return u;
    }

    public List<Usuarios> getAll() throws Exception {
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

    public void insertar(Usuarios u) throws Exception {
        String query = "{CALL sp_insertar_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        try (Connection conn = connMySQL.open();
             CallableStatement cstmt = conn.prepareCall(query)) {
            
            cstmt.setString(1, u.getMatricula());
            cstmt.setString(2, u.getContrasenia());
            cstmt.setString(3, u.getId_rol());
            cstmt.setString(4, u.getId_turno());
            cstmt.setString(5, u.getNombre());
            cstmt.setString(6, u.getApellido_paterno());
            cstmt.setString(7, u.getApellido_materno());
            cstmt.setString(8, u.getCorreo());
            cstmt.setString(9, u.getTelefono());
            cstmt.setString(10, u.getFecha_nacimiento());
            cstmt.setString(11, u.getDireccion());
            cstmt.setString(12, u.getId_estatus());
            
            cstmt.executeUpdate();
        }
    }

    public void modificar(Usuarios u) throws Exception {
        String query = "{CALL sp_modificar_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        try (Connection conn = connMySQL.open();
             CallableStatement cstmt = conn.prepareCall(query)) {
            
            cstmt.setInt(1, u.getId_usuario());
            cstmt.setInt(2, u.getId_persona());
            cstmt.setString(3, u.getMatricula());
            cstmt.setString(4, u.getContrasenia());
            cstmt.setString(5, u.getId_rol());
            cstmt.setString(6, u.getId_turno());
            cstmt.setString(7, u.getNombre());
            cstmt.setString(8, u.getApellido_paterno());
            cstmt.setString(9, u.getApellido_materno());
            cstmt.setString(10, u.getCorreo());
            cstmt.setString(11, u.getTelefono());
            cstmt.setString(12, u.getFecha_nacimiento());
            cstmt.setString(13, u.getDireccion());
            cstmt.setString(14, u.getId_estatus());
            
            cstmt.executeUpdate();
        }
    }

    public void eliminar(int idUsuario) throws Exception {
        String query = "{CALL sp_eliminar_usuario(?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        
        try (Connection conn = connMySQL.open();
             CallableStatement cstmt = conn.prepareCall(query)) {
            
            cstmt.setInt(1, idUsuario);
            cstmt.executeUpdate();
        }
    }
}