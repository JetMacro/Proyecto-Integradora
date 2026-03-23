package org.utl.dsm.rest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.dsm.integradoraweb.controller.ControllerUsuarios;
import org.utl.dsm.integradoraweb.model.Usuarios;
import java.util.List;

@Path("usuario")
public class RESTUsuarios {

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(String jsonEntrada) {
        try {
            // 1. Recibimos un String y Gson se encarga de convertirlo, evitando el error 500 de Tomcat
            Gson gson = new Gson();
            Usuarios ul = gson.fromJson(jsonEntrada, Usuarios.class);

            System.out.println("LLEGÓ A JAVA -> Matrícula: " + ul.getMatricula() + " | Pass: " + ul.getContrasenia());

            // 2. Ejecutamos la base de datos
            ControllerUsuarios ctrl = new ControllerUsuarios();
            Usuarios u = ctrl.login(ul.getMatricula(), ul.getContrasenia());

            if (u == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("{\"error\":\"Credenciales incorrectas\"}").build();
            }

            // 3. Regresamos el resultado convertido a texto
            String jsonRespuesta = gson.toJson(u);
            return Response.status(Response.Status.OK).entity(jsonRespuesta).build();
            
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"Error interno: " + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            ControllerUsuarios ctrl = new ControllerUsuarios();
            List<Usuarios> lista = ctrl.getAll();

            Gson gson = new Gson();
            String jsonRespuesta = gson.toJson(lista);
            return Response.status(Response.Status.OK).entity(jsonRespuesta).build();

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
    public Response insertar(String jsonEntrada) {
        try {
            Gson gson = new Gson();
            Usuarios u = gson.fromJson(jsonEntrada, Usuarios.class);

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
    public Response modificar(String jsonEntrada) {
        try {
            Gson gson = new Gson();
            Usuarios u = gson.fromJson(jsonEntrada, Usuarios.class);

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