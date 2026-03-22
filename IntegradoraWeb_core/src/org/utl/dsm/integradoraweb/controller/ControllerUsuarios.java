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
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setString(1, usuario);
        cstmt.setString(2, contrasenia);

        ResultSet rs = cstmt.executeQuery();
        Usuarios u = null;

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
                // nuevos agregados token
                u.setToken(rs.getString("token")); 
                u.setLast_used_token(rs.getString("last_used_token"));
            }
        }

        rs.close();
        cstmt.close();
        conn.close();

        return u;
    }

   
    public void insertar(Usuarios u) throws Exception {
        String query = "{CALL sp_InsertarUsuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        try (CallableStatement cstmt = conn.prepareCall(query)) {
            cstmt.setString(1, u.getNombre());
            cstmt.setString(2, u.getApellido_paterno());
            cstmt.setString(3, u.getApellido_materno());
            cstmt.setString(4, u.getCorreo());
            cstmt.setString(5, u.getTelefono());
            cstmt.setDate(6, java.sql.Date.valueOf(u.getFecha_nacimiento())); 
            cstmt.setString(7, u.getDireccion());
            cstmt.setString(8, u.getMatricula());
            cstmt.setString(9, u.getContrasenia());
            cstmt.setString(10, u.getId_rol());
            cstmt.setString(11, u.getId_turno());
            
            cstmt.executeUpdate();
        } finally {
            conn.close();
        }
    }

    // ---------------------------------------------------------------
    // 3. NUEVO MÉTODO: MODIFICAR
    // ---------------------------------------------------------------
    public void modificar(Usuarios u) throws Exception {
        String query = "{CALL sp_ModificarUsuario(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        try (CallableStatement cstmt = conn.prepareCall(query)) {
            cstmt.setInt(1, u.getId_usuario());
            cstmt.setString(2, u.getNombre());
            cstmt.setString(3, u.getApellido_paterno());
            cstmt.setString(4, u.getApellido_materno());
            cstmt.setString(5, u.getCorreo());
            cstmt.setString(6, u.getTelefono());
            cstmt.setDate(7, java.sql.Date.valueOf(u.getFecha_nacimiento()));
            cstmt.setString(8, u.getDireccion());
            cstmt.setString(9, u.getId_rol());
            cstmt.setString(10, u.getId_turno());
            cstmt.setString(11, u.getId_estatus());
            
            cstmt.executeUpdate();
        } finally {
            conn.close();
        }
    }

    public List<Usuarios> getAll() throws Exception {
        String query = "{CALL sp_ConsultarUsuarios()}";
        List<Usuarios> listaUsuarios = new ArrayList<>();
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        try (CallableStatement cstmt = conn.prepareCall(query);
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
        } finally {
            conn.close();
        }
        return listaUsuarios;
    }

    public void eliminar(int idUsuario) throws Exception {
        String query = "{CALL sp_EliminarUsuario(?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        try (CallableStatement cstmt = conn.prepareCall(query)) {
            cstmt.setInt(1, idUsuario);
            cstmt.executeUpdate();
        } finally {
            conn.close();
        }
    }
}