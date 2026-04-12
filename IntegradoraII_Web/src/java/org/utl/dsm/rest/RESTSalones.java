package org.utl.dsm.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.utl.dsm.integradoraweb.controller.ControllerSalones;
import org.utl.dsm.integradoraweb.model.Salones;

@Path("salones")
public class RESTSalones {
    
    private final Gson gson = new Gson();
    private final ControllerSalones controller = new ControllerSalones();

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            List<Salones> lista = controller.getAll();
            return Response.ok(gson.toJson(lista)).build();
        } catch (Exception e) {
            e.printStackTrace(); 
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener salones: " + e.getMessage())
                    .build();
        }
    }
    
    @Path("insertar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insertar(String salonJson) {
        try {
            Salones s = new Gson().fromJson(salonJson, Salones.class);
            String mensaje = new ControllerSalones().insertarSalon(s);
            
            return Response.ok("{\"mensaje\":\"" + mensaje + "\"}").build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"Error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @Path("actualizar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response actualizar(String salonJson) {
        try {
            Salones s = new Gson().fromJson(salonJson, Salones.class);
            String mensaje = new ControllerSalones().actualizarSalon(s);
            
            return Response.ok("{\"mensaje\":\"" + mensaje + "\"}").build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"Error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @Path("eliminar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response eliminar(String request) {
        try {
            // CORRECCIÓN: Parseo manual seguro desde el String JSON usando Gson
            JsonObject json = new Gson().fromJson(request, JsonObject.class);
            int idSalon = json.get("idSalon").getAsInt();
            
            String mensaje = new ControllerSalones().eliminarSalon(idSalon);
            return Response.ok("{\"mensaje\":\"" + mensaje + "\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"mensaje\":\"Error: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("detalle/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerDetalle(@PathParam("id") int idSalon) {
        try {
            List<Salones> detalle = controller.obtenerDetalleSalon(idSalon);
            return Response.ok(gson.toJson(detalle)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"mensaje\":\"Error al obtener detalle: " + e.getMessage() + "\"}")
                    .build();
        }
    }
}