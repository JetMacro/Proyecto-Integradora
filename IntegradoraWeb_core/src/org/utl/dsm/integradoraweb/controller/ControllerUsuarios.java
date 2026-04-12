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
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "465");

// ESTO FUERZA LA CONEXIÓN SSL PARA EVITAR EL TIMEOUT
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

// TIEMPOS DE ESPERA PARA EVITAR QUE EL SERVIDOR SE CUELGUE
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");          // 10 segundos

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                // USA TU CONTRASEÑA DE APLICACIÓN AQUÍ
                return new PasswordAuthentication("tu_correo@gmail.com", "tu_clave_de_16_letras");
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correoOrigen));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(correoDestino));
            message.setSubject("Recuperacion de Contrasena - Gestion UTL");

            String enlace = "https://proyecto-integradora-production-f92d.up.railway.app/index.html?correo=" + correoDestino + "&reset=true";

            String html = "<div style='font-family: Arial, sans-serif; padding: 20px; max-width: 600px; margin: 0 auto; border: 1px solid #ddd; border-radius: 8px;'>"
                    + "<h2 style='color: #2a2155; text-align: center;'>Gestion UTL</h2>"
                    + "<p style='font-size: 16px; color: #333;'>Hemos recibido una solicitud para restablecer la contrasena de tu cuenta.</p>"
                    + "<p style='font-size: 16px; color: #333;'>Haz clic en el boton de abajo para crear una nueva contrasena:</p>"
                    + "<div style='text-align: center; margin: 30px 0;'>"
                    + "<a href='" + enlace + "' style='background-color: #2a2155; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold; display: inline-block;'>Restablecer mi Contrasena</a>"
                    + "</div>"
                    + "<p style='font-size: 14px; color: #777;'>Si tu no solicitaste este cambio, puedes ignorar este correo de forma segura.</p>"
                    + "</div>";

            message.setContent(html, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new Exception("Error al conectar con Gmail: " + e.getMessage());
        }
    }
}
