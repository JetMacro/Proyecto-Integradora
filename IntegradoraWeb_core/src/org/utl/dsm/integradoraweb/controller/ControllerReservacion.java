package org.utl.dsm.integradoraweb.controller;

import org.utl.dsm.integradoraweb.db.ConexionMySQL;
import java.sql.*;
import java.util.*;
import org.utl.dsm.integradoraweb.model.Reservacion;
import org.utl.dsm.integradoraweb.model.Salones;

public class ControllerReservacion {

    public int actualizarReservacion(Reservacion r) throws Exception {
        String sql = "{CALL sp_actualizar_reservacion(?, ?, ?, ?, ?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);
        cstmt.setInt(1, r.getIdReserva());
        cstmt.setInt(2, r.getIdUsuario());
        cstmt.setInt(3, r.getIdSalon());
        cstmt.setString(4, r.getFechaHora());
        cstmt.setString(5, r.getEstatus());
        int filasAfectadas = cstmt.executeUpdate();
        cstmt.close();
        conn.close();
        connMySQL.close();
        return filasAfectadas;
    }

    public void insertarReservacion(Reservacion r) throws Exception {
        String sql = "{CALL sp_insertar_reservacion(?, ?, ?, ?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);
        cstmt.setInt(1, r.getIdUsuario());
        cstmt.setInt(2, r.getIdSalon());
        cstmt.setString(3, r.getFechaHora());
        cstmt.setString(4, r.getEstatus());
        cstmt.executeUpdate();
        cstmt.close();
        conn.close();
        connMySQL.close();
    }

    public List<Reservacion> getAllReservaciones() throws Exception {
        String sql = "SELECT * FROM v_reservaciones_detalle";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Reservacion> lista = new ArrayList<>();
        while (rs.next()) {
            Reservacion r = new Reservacion();
            r.setIdReserva(rs.getInt("id_reserva"));
            r.setFechaHora(rs.getString("fecha_hora"));
            r.setEstatus(rs.getString("estatus"));
            r.setMatricula(rs.getString("matricula"));
            r.setNombreSalon(rs.getString("nombre_salon"));
            r.setNombreEdificio(rs.getString("nombre_edificio"));
            lista.add(r);
        }
        rs.close(); 
        pstmt.close(); 
        conn.close();
        connMySQL.close();
        return lista;
    }
    
    public void eliminarReservacion(int id) throws Exception {
        String sql = "{CALL sp_eliminar_reservacion(?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);
        cstmt.setInt(1, id);
        cstmt.executeUpdate();
        cstmt.close();
        conn.close();
        connMySQL.close();
    }
    
    public List<Salones> getAllSalones() throws Exception {
        String sql = "SELECT * FROM v_salones_edificios";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();
        List<Salones> lista = new ArrayList<>();
        while (rs.next()) {
            Salones s = new Salones();
            s.setIdSalon(rs.getInt("id_salon"));
            s.setNombre(rs.getString("nombre_salon"));
            s.setEdificio(rs.getString("nombre_edificio"));
            lista.add(s);
        }
        rs.close(); 
        pstmt.close(); 
        conn.close();
        connMySQL.close();
        return lista;
    }
}