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
        System.out.println("LLEGÓ A JAVA -> Matrícula: " + ul.getMatricula() + " | Pass: " + ul.getContrasenia());

        try {
            ControllerUsuarios ctrl = new ControllerUsuarios();
            Usuarios u = ctrl.login(ul.getMatricula(), ul.getContrasenia());

            if (u == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"Credenciales incorrectas\"}").build();
            }

            // Devolvemos el objeto "u" directamente. Jersey lo convierte a JSON automáticamente.
            return Response.ok(u).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            ControllerUsuarios ctrl = new ControllerUsuarios();
            List<Usuarios> lista = ctrl.getAll();

            // Devolvemos la lista directamente, sin usar Gson
            return Response.status(Response.Status.OK).entity(lista).build();

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "Error al consultar los usuarios.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error.toString()).build();
        }
    }

    @POST
    @Path("insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(Usuarios u) {
        try {
            ControllerUsuarios ctrl = new ControllerUsuarios();
            ctrl.insertar(u);

            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("mensaje", "Usuario registrado correctamente.");
            return Response.status(Response.Status.OK).entity(respuesta.toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "Error al registrar el usuario.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error.toString()).build();
        }
    }

    @POST
    @Path("modificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modificar(Usuarios u) {
        try {
            ControllerUsuarios ctrl = new ControllerUsuarios();
            ctrl.modificar(u);

            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("mensaje", "Usuario modificado correctamente.");
            return Response.status(Response.Status.OK).entity(respuesta.toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "Error al modificar el usuario.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error.toString()).build();
        }
    }

    @POST
    @Path("eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@FormParam("id_usuario") int idUsuario) {
        try {
            ControllerUsuarios ctrl = new ControllerUsuarios();
            ctrl.eliminar(idUsuario);

            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("mensaje", "Usuario eliminado correctamente.");
            return Response.status(Response.Status.OK).entity(respuesta.toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "Error al eliminar el usuario.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error.toString()).build();
        }
    }
}