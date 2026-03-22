package org.utl.dsm.integradoraweb.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import org.utl.dsm.integradoraweb.db.ConexionMySQL;
import org.utl.dsm.integradoraweb.model.Prestamos;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ControllerPrestamos {

    public List<Prestamos> getAll() throws Exception {
        List<Prestamos> lista = new ArrayList<>();
        String query = "SELECT * FROM db_gam.view_prestamos_inventario;";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection con = connMySQL.open();

        PreparedStatement pstmt = con.prepareStatement(query);
        ResultSet rs = pstmt.executeQuery();

        while (rs.next()) {
            Prestamos i = new Prestamos();
            i.setId_prestamo(rs.getInt("id_prestamo"));
            i.setId_rol(rs.getString("id_rol"));
            i.setMatricula(rs.getString("matricula"));
            i.setId_mobiliario(rs.getString("id_mobiliario"));
            i.setFecha_salida(rs.getString("fecha_salida"));
            i.setFecha_devolucion_prevista(rs.getString("fecha_devolucion_prevista"));
            i.setFecha_devolucion_real(rs.getString("fecha_devolucion_real"));
            i.setEstatus_prestamo(rs.getString("estatus_prestamo"));
            i.setObservaciones_salida(rs.getString("observaciones_salida"));
            i.setId_turno(rs.getString("id_turno"));
            i.setNombre(rs.getString("nombre"));
            i.setCorreo(rs.getString("correo"));
            i.setTelefono(rs.getString("telefono"));
            i.setObservaciones_entrada(rs.getString("observaciones_entrada"));
            lista.add(i);
        }

        rs.close();
        pstmt.close();
        con.close();

        return lista;
    }

    public void eliminarPrestamo(Prestamos inv) throws Exception {
        String sql = "{call sp_eliminar_Prestamo(?)}";
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection con = null;
        try {
            con = connMySQL.open();
            java.sql.CallableStatement cstmt = con.prepareCall(sql);
            cstmt.setInt(1, inv.getId_prestamo());
            cstmt.executeUpdate();
            cstmt.close();
        } finally {
            if (con != null) {
                con.close(); // ESTO ES VITAL
            }
        }
    }

    public void actualizarPrestamo(Prestamos inv) throws Exception {
        // 1. Definir la consulta para invocar el Stored Procedure
        String sql = "{CALL sp_actualizar_estatus_prestamo(?,?,?)}";

        // 2. Conectar a la base de datos
        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection con = connMySQL.open();

        // 3. Preparar la llamada
        java.sql.CallableStatement cstmt = con.prepareCall(sql);

        // 4. Establecer los parámetros (asegúrate que los nombres coincidan con tu Model)
        cstmt.setInt(1, inv.getId_prestamo());
        cstmt.setString(2, inv.getEstatus_prestamo()); // "Disponible" o "En uso"
        cstmt.setString(3, inv.getObservaciones_entrada());

        // 5. Ejecutar
        cstmt.executeUpdate();

        // 6. Cerrar conexiones
        cstmt.close();
        con.close();
    }

    public void insertarPrestamo(Prestamos p) throws Exception {
        // 1. Consultas para validar existencia
        String sqlCheckUsuario = "SELECT id_usuario FROM tbl_usuarios WHERE matricula = ?";
        String sqlCheckInv = "SELECT id_inventario FROM tbl_inventario WHERE id_mobiliario = ?";
        String sqlInsert = "{CALL sp_insertar_prestamo(?,?,?,?)}";

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection con = connMySQL.open();

        try {
            // --- VALIDACIÓN 1: USUARIO ---
            PreparedStatement pstmtU = con.prepareStatement(sqlCheckUsuario);
            pstmtU.setString(1, p.getMatricula());
            ResultSet rsU = pstmtU.executeQuery();
            if (!rsU.next()) {
                throw new Exception("La matrícula '" + p.getMatricula() + "' no está registrada.");
            }

            // --- VALIDACIÓN 2: MOBILIARIO ---
            PreparedStatement pstmtI = con.prepareStatement(sqlCheckInv);
            pstmtI.setString(1, p.getId_mobiliario());
            ResultSet rsI = pstmtI.executeQuery();
            if (!rsI.next()) {
                throw new Exception("El mobiliario con ID '" + p.getId_mobiliario() + "' no existe.");
            }

            // --- 2. EJECUCIÓN DEL PROCEDIMIENTO SI TODO ESTÁ BIEN ---
            java.sql.CallableStatement cstmt = con.prepareCall(sqlInsert);
            cstmt.setString(1, p.getMatricula());
            cstmt.setString(2, p.getId_mobiliario());

            // Limpiamos el formato de fecha de HTML (YYYY-MM-DDTHH:MM -> YYYY-MM-DD HH:MM:SS)
            String fechaLimpia = p.getFecha_devolucion_prevista().replace("T", " ");
            cstmt.setString(3, fechaLimpia);

            cstmt.setString(4, p.getObservaciones_salida());

            cstmt.executeUpdate();

            cstmt.close();
            pstmtU.close();
            pstmtI.close();
        } finally {
            // El bloque finally asegura que la conexión se cierre aunque ocurra un error
            con.close();
            connMySQL.close();
        }
    }

}
