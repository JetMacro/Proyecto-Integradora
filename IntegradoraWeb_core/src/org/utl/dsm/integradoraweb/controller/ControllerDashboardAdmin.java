package org.utl.dsm.integradoraweb.controller;

import org.utl.dsm.integradoraweb.db.ConexionMySQL;
import org.utl.dsm.integradoraweb.model.DashBoard;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.integradoraweb.model.Actividad;

public class ControllerDashboardAdmin {

    public DashBoard obtenerDashboardCompleto() throws SQLException {
        DashBoard resumen = new DashBoard();

        ConexionMySQL connMySQL = new ConexionMySQL();
        Connection conn = connMySQL.open();

        try {
            //  1: Total de Inventario
            String sqlInventario = "{CALL spTotalInventario()}";
            CallableStatement cstmtInventario = conn.prepareCall(sqlInventario);
            ResultSet rsInventario = cstmtInventario.executeQuery();

            if (rsInventario.next()) {
                resumen.setTotalMobiliario(rsInventario.getInt("totalMobiliario"));
            }
            rsInventario.close();
            cstmtInventario.close();

            // -2 otal de Usuarios 
            String sqlUsuarios = "{CALL spTotalUsuarios()}";
            CallableStatement cstmtUsuarios = conn.prepareCall(sqlUsuarios);
            ResultSet rsUsuarios = cstmtUsuarios.executeQuery();

            if (rsUsuarios.next()) {
                resumen.setTotalUsuarios(rsUsuarios.getInt("totalUsuarios"));
            }
            rsUsuarios.close();
            cstmtUsuarios.close();

            // -- consulta 3: ContarHistorial
            String sqlReportes = "{CALL spTotalReportes()}";
            CallableStatement cstmtReportes = conn.prepareCall(sqlReportes);
            ResultSet rsReportes = cstmtReportes.executeQuery();

            if (rsReportes.next()) { 
                resumen.setReporteSemanal(rsReportes.getInt("reportesSemanal"));
            }
            rsReportes.close(); 
            cstmtReportes.close(); 

            
            
            
            // consulta 4: Contar Reservaciones
              String sqlReservaciones = "{CALL spContarReservacionesActivas()}";
            CallableStatement cstmtReservaciones = conn.prepareCall(sqlReservaciones);
            ResultSet rsReservaciones = cstmtReservaciones.executeQuery();

            if (rsReservaciones.next()) { 
                resumen.setActivas(rsReservaciones.getInt("Activas"));
            }
           rsReservaciones.close(); 
            cstmtReservaciones.close(); 
            
            // --- CONSULTA 5: Actividad Reciente ---
            String sqlActividad = "{CALL spActividadReciente()}";
            CallableStatement cstmtActividad = conn.prepareCall(sqlActividad);
            ResultSet rsActividad = cstmtActividad.executeQuery();

            List<Actividad> listaActividades = new ArrayList<>();

            while (rsActividad.next()) { 
                Actividad act = new Actividad(); 
                act.setTitulo(rsActividad.getString("titulo"));
                act.setDescripcion(rsActividad.getString("descripcion"));
                act.setFechaActividad(rsActividad.getString("fecha_actividad"));
                act.setAutor(rsActividad.getString("autor"));
                
                listaActividades.add(act); 
            }
            
            resumen.setActividades(listaActividades);

            rsActividad.close(); 
            cstmtActividad.close();
            
            
           
        } finally {
            conn.close();
        }

        return resumen;
    }
}
