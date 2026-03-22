package org.utl.dsm.integradoraweb.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.utl.dsm.integradoraweb.db.ConexionMySQL;
import org.utl.dsm.integradoraweb.model.Inventario;
import org.utl.dsm.integradoraweb.model.Edificios;
import org.utl.dsm.integradoraweb.model.Salones;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerInventario {

    public List<Inventario> getAll() throws Exception {
        List<Inventario> lista = new ArrayList<>();
        String query = "SELECT * FROM db_gam.view_inventario_general;";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection con = connMySQL.open();

        PreparedStatement pstmt = con.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Inventario i = new Inventario();

            i.setMaterial(rs.getString("material"));
            i.setCategoria(rs.getString("categoria"));
            i.setTotal(rs.getInt("total"));
            i.setDisponibles(rs.getInt("disponibles"));
            i.setUso(rs.getInt("uso"));
            i.setPct_disponibles(rs.getDouble("pct_disponibles"));
            lista.add(i);
        }

        rs.close();
        pstmt.close();
        con.close();

        return lista;
    }

    public List<Inventario> getDetallePorTipo(String tipoMobi) throws Exception {
        List<Inventario> lista = new ArrayList<>();
        // Llamada al procedimiento almacenado usando la sintaxis de escape de JDBC
        String query = "{CALL sp_getDetalleInventarioPorTipo(?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection con = connMySQL.open();

        // Usamos CallableStatement para procedimientos almacenados
        java.sql.CallableStatement cstmt = con.prepareCall(query);

        // Establecemos el parámetro de entrada
        cstmt.setString(1, tipoMobi);

        ResultSet rs = cstmt.executeQuery();

        while (rs.next()) {
            Inventario i = new Inventario();

            // Mapeo de campos básicos del inventario
            i.setId_inventario(rs.getInt("id_inventario"));
            i.setNombre(rs.getString("id_mobiliario")); // O el campo que uses para el nombre
            i.setMaterial(rs.getString("id_tipo_mobi")); // O el campo que uses para el nombre
            i.setCategoria(rs.getString("id_categoria_mobi"));
            i.setObservaciones(rs.getString("observaciones"));
            i.setEstatus(rs.getString("id_estatus_actual"));

            // Mapeo de campos de las tablas unidas (Salones y Edificios)
            // Nota: Asegúrate de tener estos atributos en tu modelo Inventario o usa setters genéricos
            i.setNombreSalon(rs.getString("nombre_salon"));
            i.setNombreEdificio(rs.getString("nombre_edificio"));
            i.setUbicacion(rs.getString("ubicacion"));
            i.setCapacidad(rs.getInt("capacidad_personas"));

            lista.add(i);
        }

        rs.close();
        cstmt.close();
        con.close();

        return lista;
    }

    public void registrarInventario(Inventario i) throws Exception {
        // La consulta usa la sintaxis de escape de JDBC para procedimientos almacenados
        // Tenemos 9 parámetros según el SP creado anteriormente
        String query = "{CALL sp_registrar_inventario(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection con = connMySQL.open();

        // Usamos CallableStatement para ejecutar el procedimiento
        java.sql.CallableStatement cstmt = con.prepareCall(query);

        // Establecemos los parámetros de entrada (IN)
        // Nota: Asegúrate de que los nombres de los getters coincidan con tu clase Model
        cstmt.setInt(1, i.getId_salon());
        cstmt.setString(2, i.getId_mobiliario());
        cstmt.setInt(3, i.getId_edificio());
        cstmt.setString(4, i.getUbicacion());       // Debe ser 'Edificio' o 'Administracion'
        cstmt.setString(5, i.getMaterial());        // id_tipo_mobi
        cstmt.setString(6, i.getCategoria());       // id_categoria_mobi
        cstmt.setString(7, i.getEstatus());         // id_estatus (Activo, Inactivo, etc.)
        cstmt.setString(8, i.getId_estatus_actual());  // id_estatus_actual (Disponible, En uso)
        cstmt.setString(9, i.getObservaciones());

        // Ejecutamos el procedimiento
        cstmt.execute();

        // Cerramos recursos
        cstmt.close();
        con.close();
    }

    public void actualizarInventario(Inventario inv) throws Exception {
        // 1. Definir la consulta para invocar el Stored Procedure
        String sql = "{call sp_actualizar_Inventario(?, ?)}";

        // 2. Conectar a la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection con = connMySQL.open();

        // 3. Preparar la llamada
        java.sql.CallableStatement cstmt = con.prepareCall(sql);

        // 4. Establecer los parámetros (asegúrate que los nombres coincidan con tu Model)
        cstmt.setInt(1, inv.getId_inventario());
        cstmt.setString(2, inv.getId_estatus_actual()); // "Disponible" o "En uso"

        // 5. Ejecutar
        cstmt.executeUpdate();

        // 6. Cerrar conexiones
        cstmt.close();
        con.close();
    }
    
    
    public List<Edificios> getEdificios() throws Exception {
        List<Edificios> lista = new ArrayList<>();
        
        String query ="{call sp_getEdificiosActivos()}";
       
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection con = connMySQL.open();

        PreparedStatement pstmt = con.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Edificios i = new Edificios();
            i.setId_edificio(rs.getInt("id_edificio"));
            i.setNombre(rs.getString("nombre")); 
            lista.add(i);
        }
        
        rs.close();
        pstmt.close();
        con.close();
        return lista;
    }

    public List<Salones> getSalones(int idEdificio) throws Exception {
        List<Salones> lista = new ArrayList<>();
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection con = connMySQL.open();
        
        String query = "{call sp_getSalonesPorEdificio(?)}";
        java.sql.CallableStatement cstmt = con.prepareCall(query);
        
        cstmt.setInt(1, idEdificio);
        ResultSet rs = cstmt.executeQuery();
        
        while (rs.next()) {
            Salones i = new Salones();
            i.setIdSalon(rs.getInt("id_salon"));
            i.setNombre(rs.getString("nombre"));
            i.setTipoSalon(rs.getString("id_tipo_salon"));
            lista.add(i);
        }
        
        rs.close();
        cstmt.close();
        con.close();
        return lista;
    }
    
    public void eliminarInventario(Inventario inv) throws Exception {
    String sql = "{call sp_eliminar_Inventario(?)}";
    ConexionMySQL connMySQL = new ConexionMySQL();
    Connection con = null;
    try {
        con = connMySQL.open();
        java.sql.CallableStatement cstmt = con.prepareCall(sql);
        cstmt.setInt(1, inv.getId_inventario());
        cstmt.executeUpdate();
        cstmt.close();
    } finally {
        if (con != null) con.close(); // ESTO ES VITAL
    }
}

}
