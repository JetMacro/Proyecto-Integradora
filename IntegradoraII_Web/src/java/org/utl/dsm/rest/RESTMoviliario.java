package org.utl.dsm.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.dsm.integradoraweb.model.DashBoard;
import org.utl.dsm.integradoraweb.controller.ControllerDashboardAdmin;

@Path("dashboard")
public class RESTMoviliario {

    private final Gson gson = new Gson();
    private ControllerDashboardAdmin cm;

    public RESTMoviliario() {
        try {
            cm = new ControllerDashboardAdmin();
        } catch (Exception e) {
            throw new RuntimeException("No se pudo inicializar ControllerDashboardAdmin", e);
        }
    }

    @GET
    @Path("resumen")
    @Produces(MediaType.APPLICATION_JSON)
    public Response contarTotal() {
        System.out.println("Entrando a contarTotal Mobiliario...");
        
        try {
            DashBoard resumen = cm.obtenerDashboardCompleto();
            
            String jsonRespuesta = gson.toJson(resumen);
            
            return Response.ok(jsonRespuesta).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            
            JsonObject error = new JsonObject();
            error.addProperty("result", "error");
            error.addProperty("message", "Error al consultar inventario: " + e.getMessage());
            
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity(error.toString())
                           .build();
        }
    }
}