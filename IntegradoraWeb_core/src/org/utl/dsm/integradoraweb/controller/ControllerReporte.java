package org.utl.dsm.integradoraweb.controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.integradoraweb.db.ConexionMySQL;
import org.utl.dsm.integradoraweb.model.Reporte;

public class ControllerReporte {

    public List<Reporte> getAll() throws Exception {
        String sql = "SELECT * FROM view_reportes_recientes";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        List<Reporte> lista = new ArrayList<>();
        while (rs.next()) {
            Reporte r = new Reporte();
            r.setId_reporte(rs.getInt("id_reporte"));
            r.setMatricula(rs.getString("matricula"));
            r.setCodigo_mueble(rs.getString("codigo_mueble"));
            r.setTipo_material(rs.getString("tipo_material"));
            r.setNombre_salon(rs.getString("nombre_salon"));
            r.setTipo_dano(rs.getString("tipo_dano"));
            r.setDescripcion(rs.getString("descripcion"));
            r.setPrioridad(rs.getString("prioridad"));
            r.setFecha_reporte(rs.getString("fecha_reporte"));
            r.setEstatus(rs.getString("estatus"));
            r.setFotoEvidencia(rs.getString("foto_evidencia"));
            lista.add(r);
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return lista;
    }

    public List<Reporte> getMaterialesParaReporte() throws Exception {
        List<Reporte> lista = new ArrayList<>();
        String sql = "SELECT id_inventario, id_mobiliario, id_tipo_mobi FROM tbl_inventario WHERE id_estatus = 'Activo'";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Reporte r = new Reporte();
            // Aquí usamos el id_inventario para que el combo box sepa qué ID mandar al insertar
            r.setId_reporte(rs.getInt("id_inventario"));
            r.setCodigo_mueble(rs.getString("id_mobiliario"));
            r.setTipo_material(rs.getString("id_tipo_mobi"));
            lista.add(r);
        }
        rs.close();
        pstmt.close();
        connMySQL.close();
        return lista;
    }

    public void insertar(Reporte r, int idUsuario, int idInventario) throws Exception {
        String sql = "{call sp_insertar_reporte_mobiliario(?,?,?,?,?,?,?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        CallableStatement cstmt = conn.prepareCall(sql);

        cstmt.setInt(1, idUsuario);
        cstmt.setInt(2, idInventario);
        cstmt.setString(3, r.getTipo_dano());
        cstmt.setString(4, r.getDescripcion());
        cstmt.setString(5, r.getPrioridad());
        cstmt.setString(6, r.getEstatus());
        cstmt.setString(7, r.getFotoEvidencia());

        cstmt.execute();

        cstmt.close();
        conn.close();
        connMySQL.close();
    }

    public void eliminar(int idReporte) throws Exception {
        String sql = "UPDATE tbl_reportes_mobiliario SET id_estatus_reporte = 'Baja Definitiva' WHERE id_reporte = ?";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, idReporte);

        pstmt.executeUpdate();

        pstmt.close();
        conn.close();
        connMySQL.close();
    }
}
