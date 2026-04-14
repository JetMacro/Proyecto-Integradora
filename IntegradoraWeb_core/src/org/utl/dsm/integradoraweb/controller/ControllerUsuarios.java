package org.utl.dsm.integradoraweb.controller;

import org.utl.dsm.integradoraweb.db.ConexionMySQL;
import org.utl.dsm.integradoraweb.model.Usuarios;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

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

                //agregado token
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

        try (CallableStatement cstmt = conn.prepareCall(query); ResultSet rs = cstmt.executeQuery()) {
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

    // ==========================================
    // Metodos para Recuperacion y Cambio de Contrasena
    // ==========================================
    public void actualizarPassword(String correo, String nuevaContrasenia) throws Exception {
        String query = "{CALL sp_ActualizarPassword(?, ?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        try (CallableStatement cstmt = conn.prepareCall(query)) {
            cstmt.setString(1, correo);
            cstmt.setString(2, nuevaContrasenia);
            cstmt.executeUpdate();
        } finally {
            conn.close();
        }
    }

    public void enviarCorreoRecuperacion(String correoDestino) throws Exception {
        final String correoOrigen = "yoqzan25@gmail.com";
        final String contraseniaApp = "ujrroquepkrtmsib";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(correoOrigen, contraseniaApp);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoOrigen));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
            message.setSubject("Recuperacion de Contrasena - Gestion UTL");

            String enlace = "https://proyecto-integradora-production-f92d.up.railway.app/index.html?correo=" + correoDestino + "&reset=true";

            String html = "<div style='background-color: #f4f7f9; padding: 40px 20px; font-family: \"Segoe UI\", Tahoma, Geneva, Verdana, sans-serif;'>"
    + "<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 12px rgba(0,0,0,0.08); border: 1px solid #e1e8ed;'>"
    
    // --- CABECERA ---
    + "<div style='background-color: #2a2155; padding: 30px; text-align: center;'>"
    + "<h1 style='color: #ffffff; margin: 0; font-size: 24px; font-weight: 700; letter-spacing: 1px;'>Gestión UTL</h1>"
    + "</div>"
    
    // --- CUERPO ---
    + "<div style='padding: 40px; text-align: center;'>"
    // Icono decorativo (Llave)
    + "<div style='margin-bottom: 25px;'>"
    + "<img src='https://cdn-icons-png.flaticon.com/512/6195/6195696.png' width='70' alt='Seguridad' style='display: block; margin: 0 auto;'>"
    + "</div>"
    
    + "<h2 style='color: #1a1a1a; margin-top: 0; font-size: 22px;'>Restablecer Contraseña</h2>"
    + "<p style='font-size: 16px; color: #555; line-height: 1.6; margin-bottom: 10px;'>"
    + "Hola, recibimos una solicitud para restablecer la contraseña de tu cuenta académica."
    + "</p>"
    + "<p style='font-size: 16px; color: #555; margin-bottom: 35px;'>"
    + "Haz clic en el siguiente botón para crear una nueva clave de acceso:"
    + "</p>"
    
    // --- BOTÓN ---
    + "<a href='" + enlace + "' style='background-color: #2a2155; color: #ffffff; padding: 16px 32px; text-decoration: none; border-radius: 12px; font-weight: bold; font-size: 16px; display: inline-block; box-shadow: 0 4px 10px rgba(42, 33, 85, 0.25);'>"
    + "Crear Nueva Contraseña"
    + "</a>"
    
    // --- NOTA DE PIE ---
    + "<div style='margin-top: 40px; padding-top: 25px; border-top: 1px solid #eeeeee;'>"
    + "<p style='font-size: 13px; color: #999; line-height: 1.4; margin: 0;'>"
    + "Si tú no solicitaste este cambio, puedes ignorar este correo de forma segura. El enlace tiene un tiempo de expiración limitado."
    + "</p>"
    + "</div>"
    + "</div>"
    
    // --- FOOTER EXTERNO ---
    + "<div style='padding: 20px; text-align: center;'>"
    + "<p style='font-size: 12px; color: #adb5bd; margin: 0;'>&copy; 2026 Universidad Tecnológica de León<br>Departamento de Sistemas</p>"
    + "</div>"
    
    + "</div>"
    + "</div>";

            message.setContent(html, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new Exception("Error al conectar con Gmail: " + e.getMessage());
        }
    }
}