package org.utl.dsm.rest;

import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.FormParam;
import java.util.List;

import org.utl.dsm.integradoraweb.controller.ControllerUsuarios;
import org.utl.dsm.integradoraweb.model.Usuarios;

/**
 *
 * @author rodod
 */
@Path("usuario")
public class RESTUsuarios {

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(Usuarios ul) {
        // Al imprimir esto en NetBeans, ahora NO debería salir null
        System.out.println("LLEGÓ A JAVA -> Matrícula: " + ul.getMatricula() + " | Pass: " + ul.getContrasenia());

        try {
            ControllerUsuarios ctrl = new ControllerUsuarios();
            // Usamos los getters correctos
            Usuarios u = ctrl.login(ul.getMatricula(), ul.getContrasenia());

            if (u == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"Credenciales incorrectas\"}").build();
            }

            return Response.ok(new Gson().toJson(u)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    // ==========================================
    // AQUI EMPIEZAN LOS MÉTODOS NUEVOS
    // ==========================================
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            ControllerUsuarios ctrl = new ControllerUsuarios();
            List<Usuarios> lista = ctrl.getAll();

            Gson gson = new Gson();
            String out = gson.toJson(lista);
            return Response.status(Response.Status.OK).entity(out).build();

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "Error al consultar los usuarios.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error.toString()).build();
        }
    }

    
}