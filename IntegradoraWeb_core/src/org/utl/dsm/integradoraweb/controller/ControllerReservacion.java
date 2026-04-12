package org.utl.dsm.integradoraweb.controller;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.integradoraweb.db.ConexionMySQL;
import org.utl.dsm.integradoraweb.model.Reservacion;
import org.utl.dsm.integradoraweb.model.Salones;

public class ControllerReservacion {

    /**
     * Obtiene todas las reservaciones utilizando la vista de detalle.
     */
    public List<Reservacion> getAllReservaciones() {
        List<Reservacion> lista = new ArrayList<>();
        String query = "SELECT * FROM v_reservaciones_detalle";
        
        try {
            ConexionMySQL connMySQL = new ConexionMySQL();
            Connection conn = connMySQL.open();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Reservacion r = new Reservacion();
                r.setIdReserva(rs.getInt("id_reserva"));
                r.setIdUsuario(rs.getInt("id_usuario"));
                r.setIdSalon(rs.getInt("id_salon"));
                r.setFechaHora(rs.getString("fecha_hora"));
                r.setEstatus(rs.getString("estatus"));
                r.setMatricula(rs.getString("matricula"));
                r.setNombreSalon(rs.getString("nombre_salon"));
                r.setNombreEdificio(rs.getString("nombre_edificio"));
                lista.add(r);
            }
            rs.close();
            pstmt.close();
            connMySQL.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Inserta una reservación llamando al procedimiento almacenado.
     */
    public void insertarReservacion(Reservacion r) throws Exception {
        String query = "{CALL sp_insertar_reservacion(?, ?, ?, ?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        try (CallableStatement cstmt = conn.prepareCall(query)) {
            cstmt.setInt(1, r.getIdUsuario());
            cstmt.setInt(2, r.getIdSalon());
            cstmt.setString(3, r.getFechaHora());
            cstmt.setString(4, r.getEstatus());
            cstmt.executeUpdate();
        } finally {
            conn.close();
        }
    }

    /**
     * Actualiza los datos de una reservación.
     * Retorna el número de filas afectadas para confirmación en el REST.
     */
    public int actualizarReservacion(Reservacion r) throws Exception {
        int filas = 0;
        String query = "{CALL sp_actualizar_reservacion(?, ?, ?, ?, ?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        try (CallableStatement cstmt = conn.prepareCall(query)) {
            cstmt.setInt(1, r.getIdReserva());
            cstmt.setInt(2, r.getIdUsuario());
            cstmt.setInt(3, r.getIdSalon());
            cstmt.setString(4, r.getFechaHora());
            cstmt.setString(5, r.getEstatus());
            filas = cstmt.executeUpdate();
        } finally {
            conn.close();
        }
        return filas;
    }

    /**
     * Elimina una reservación (cambio de estatus) llamando al procedimiento.
     */
    public void eliminarReservacion(int idReserva) throws Exception {
        String query = "{CALL sp_eliminar_reservacion(?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        
        try (CallableStatement cstmt = conn.prepareCall(query)) {
            cstmt.setInt(1, idReserva);
            cstmt.executeUpdate();
        } finally {
            conn.close();
        }
    }

    /**
     * Obtiene el catálogo de salones y edificios desde la vista correspondiente.
     */
    public List<Salones> getAllSalones() {
        List<Salones> lista = new ArrayList<>();
        String query = "SELECT * FROM v_salones_edificios";
        
        try {
            ConexionMySQL connMySQL = new ConexionMySQL();
            Connection conn = connMySQL.open();
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Salones s = new Salones();
                s.setIdSalon(rs.getInt("id_salon"));
                s.setNombre(rs.getString("nombre_salon"));
                s.setEdificio(rs.getString("nombre_edificio"));
                lista.add(s);
            }
            rs.close();
            pstmt.close();
            connMySQL.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
}