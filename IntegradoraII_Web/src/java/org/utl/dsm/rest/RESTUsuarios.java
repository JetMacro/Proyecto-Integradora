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

        // --- INICIO DE LA CORRECCIÓN (Tu versión original) ---
        // Este "chismoso" imprimirá en la consola de NetBeans lo que llega desde tu página web
        System.out.println("LLEGÓ A JAVA -> Matrícula: " + ul.getMatricula() + " | Pass: " + ul.getContrasenia());
        // --- FIN DE LA CORRECCIÓN ---

        try {
            ControllerUsuarios ctrl = new ControllerUsuarios();
            Usuarios u = ctrl.login(ul.getMatricula(), ul.getContrasenia());

            if (u == null) {
                JsonObject error = new JsonObject();
                error.addProperty("error", "Credenciales inválidas o usuario inactivo.");
                return Response.status(Response.Status.UNAUTHORIZED).entity(error.toString()).build();
            }

            Gson gson = new Gson();
            String out = gson.toJson(u);
            return Response.status(Response.Status.OK).entity(out).build();

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "Error interno del servidor. Intente más tarde.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error.toString()).build();
        }
    }

    // ==========================================
    // MÉTODOS DE GESTIÓN DE USUARIOS
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

    @POST
    @Path("insertar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(String jsonString) { // Corrección del compañero: Recibir String crudo
        try {
            // Convertir el JSON a Objeto manualmente
            Gson gson = new Gson();
            Usuarios u = gson.fromJson(jsonString, Usuarios.class);

            ControllerUsuarios ctrl = new ControllerUsuarios();
            ctrl.insertar(u);

            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("mensaje", "Usuario registrado correctamente.");
            return Response.status(Response.Status.OK).entity(respuesta.toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            // Mantener detalle del error del compañero
            error.addProperty("error", "Error al registrar el usuario: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error.toString()).build();
        }
    }

    @POST
    @Path("modificar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response modificar(String jsonString) { // Corrección del compañero: Recibir String crudo
        try {
            Gson gson = new Gson();
            Usuarios u = gson.fromJson(jsonString, Usuarios.class);

            ControllerUsuarios ctrl = new ControllerUsuarios();
            ctrl.modificar(u);

            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("mensaje", "Usuario modificado correctamente.");
            return Response.status(Response.Status.OK).entity(respuesta.toString()).build();

        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "Error al modificar el usuario: " + e.getMessage());
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

    // ==========================================
    // MÉTODOS PARA RECUPERACIÓN DE CONTRASEÑA
    // ==========================================

    @POST
    @Path("solicitarRecuperacion")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response solicitarRecuperacion(Usuarios u) {
        try {
            ControllerUsuarios ctrl = new ControllerUsuarios();
            ctrl.enviarCorreoRecuperacion(u.getCorreo());
            
            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("mensaje", "Correo de recuperación enviado exitosamente.");
            return Response.status(Response.Status.OK).entity(respuesta.toString()).build();
            
        } catch (Exception e) {
            e.printStackTrace(); 
            // Conservado de TU versión para que puedas ver el error exacto en los logs de Railway
            return Response.status(500).entity("{\"error\":\"" + e.toString() + "\"}").build();
        }
    }

    @POST
    @Path("actualizarPassword")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizarPassword(Usuarios u) {
        try {
            ControllerUsuarios ctrl = new ControllerUsuarios();
            ctrl.actualizarPassword(u.getCorreo(), u.getContrasenia());
            
            JsonObject respuesta = new JsonObject();
            respuesta.addProperty("mensaje", "Contraseña actualizada correctamente.");
            return Response.status(Response.Status.OK).entity(respuesta.toString()).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject error = new JsonObject();
            error.addProperty("error", "Error al actualizar la contraseña en la base de datos.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(error.toString()).build();
        }
    }
}