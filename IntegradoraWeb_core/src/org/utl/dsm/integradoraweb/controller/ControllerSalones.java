package org.utl.dsm.integradoraweb.controller;

// CORRECCIÓN: Se cambió de com.mysql... a java.sql.CallableStatement
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.integradoraweb.db.ConexionMySQL;
import org.utl.dsm.integradoraweb.model.Salones;

public class ControllerSalones {
    
    public List<Salones> getAll() throws Exception {
        List<Salones> lista = new ArrayList<>();
        
        String query = "SELECT * FROM view_salones_tabla;";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection con = connMySQL.open();
        
        PreparedStatement pstmt = con.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Salones s = new Salones();
            s.setIdSalon(rs.getInt("id_salon")); 
            s.setIdEdificio(rs.getInt("id_edificio"));
            s.setNombre(rs.getString("nombre"));
            s.setTipo(rs.getString("tipo"));
            s.setCapacidad(rs.getInt("capacidad"));
            s.setEdificio(rs.getString("edificio"));
            s.setEstatus(rs.getString("estatus"));
            
            lista.add(s);
        }
        
        rs.close();
        pstmt.close();
        con.close();
        
        return lista;
    }
    
    public String insertarSalon(Salones s) throws Exception {
        String sql = "{CALL sp_insertar_salon(?, ?, ?, ?, ?)}";
        String resultado = "";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);
        
        cstmt.setInt(1, s.getIdEdificio());        
        cstmt.setString(2, s.getTipo());            
        cstmt.setString(3, s.getNombre());          
        cstmt.setInt(4, s.getCapacidad());          
        cstmt.setString(5, s.getEstatus());        
        
        boolean hasResults = cstmt.execute();
        
        if (hasResults) {
            ResultSet rs = cstmt.getResultSet();
            if (rs.next()) {
                resultado = rs.getString("mensaje");
                int idGenerado = rs.getInt("id_salon");
                System.out.println("ID generado: " + idGenerado);
            }
            rs.close();
        }
        
        cstmt.close();
        conn.close();
        
        return resultado;
    }
    
   public String actualizarSalon(Salones s) throws Exception {
        String sql = "{CALL sp_actualizar_salon(?, ?, ?, ?, ?, ?)}";
        String resultado = "";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);
        
        cstmt.setInt(1, s.getIdSalon());             
        cstmt.setInt(2, s.getIdEdificio());           
        cstmt.setString(3, s.getTipo());              
        cstmt.setString(4, s.getNombre());            
        cstmt.setInt(5, s.getCapacidad());            
        cstmt.setString(6, s.getEstatus());           
        
        boolean hasResults = cstmt.execute();
        
        if (hasResults) {
            ResultSet rs = cstmt.getResultSet();
            if (rs.next()) {
                resultado = rs.getString("mensaje");
                int idActualizado = rs.getInt("id_salon");
                System.out.println("ID actualizado: " + idActualizado);
            }
            rs.close();
        }
        
        cstmt.close();
        conn.close();
        
        return resultado;
    }
   
   public String eliminarSalon(int idSalon) throws Exception {
        String sql = "{CALL sp_eliminar_salon(?)}";
        String resultado = "";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);
        
        cstmt.setInt(1, idSalon);
        
        boolean hasResults = cstmt.execute();
        
        if (hasResults) {
            ResultSet rs = cstmt.getResultSet();
            if (rs.next()) {
                resultado = rs.getString("mensaje");
            }
            rs.close();
        }
        
        cstmt.close();
        conn.close();
        
        return resultado;
    }
   
   public List<Salones> obtenerDetalleSalon(int idSalon) throws Exception {
        List<Salones> lista = new ArrayList<>();
        
        String sql = "SELECT * FROM view_detalle_salon_completo WHERE id_salon = ?";
        
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idSalon);
        
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Salones s = new Salones();
            
            s.setIdSalon(rs.getInt("id_salon"));
            s.setNombre(rs.getString("nombre_salon"));
            s.setTipo(rs.getString("tipo_salon"));
            s.setCapacidad(rs.getInt("capacidad"));
            s.setEstatus(rs.getString("estatus_salon"));
            s.setIdEdificio(rs.getInt("id_edificio"));
            s.setEdificio(rs.getString("nombre_edificio"));
            s.setUbicacionEdificio(rs.getString("ubicacion_edificio"));
            s.setPisosEdificio(rs.getInt("pisos_edificio"));
            s.setDivisionAcademica(rs.getString("division_academica"));
            s.setTipoMobiliario(rs.getString("tipo_mobiliario"));
            s.setTotalMobiliario(rs.getInt("total_mobiliario"));
            s.setDisponibles(rs.getInt("disponibles"));
            s.setEnUso(rs.getInt("en_uso"));
            
            lista.add(s);
        }
        
        rs.close();
        pstmt.close();
        conn.close();
        
        return lista;
    }
}