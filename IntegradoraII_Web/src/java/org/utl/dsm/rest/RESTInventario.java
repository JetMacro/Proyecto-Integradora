/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.utl.dsm.integradoraweb.controller.ControllerInventario;
import org.utl.dsm.integradoraweb.model.Inventario;
import org.utl.dsm.integradoraweb.model.Edificios;
import org.utl.dsm.integradoraweb.model.Salones;

/**
 *
 * @author rodod
 */
@Path("inventario")
public class RESTInventario {

    private final Gson gson = new Gson();
    private final ControllerInventario controller = new ControllerInventario();

    // Obtener todas las reservaciones
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            List<Inventario> lista = controller.getAll();
            return Response.ok(gson.toJson(lista)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener reservaciones: " + e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("getByName")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getByName(@jakarta.ws.rs.QueryParam("nombre") String nombre) {
        try {
            // Validamos que el nombre no venga vacío
            if (nombre == null || nombre.trim().isEmpty()) {
                return Response.status(Response.Status.BAD_REQUEST)
                        .entity("{\"error\":\"El nombre del material es requerido\"}")
                        .build();
            }

            // Llamamos al método del controlador que creamos anteriormente
            List<Inventario> detalles = controller.getDetallePorTipo(nombre);

            // Retornamos la lista de detalles (salones, edificios, etc.)
            return Response.ok(gson.toJson(detalles)).build();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error al obtener detalles: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(String jsonInventario) {
        try {
            // 1. Convertimos el JSON recibido en un objeto de tipo Inventario
            Inventario inv = gson.fromJson(jsonInventario, Inventario.class);

            // 2. Llamamos al método del controlador que ejecuta el SP
            controller.registrarInventario(inv);

            // 3. Si todo sale bien, respondemos con éxito
            return Response.status(Response.Status.CREATED)
                    .entity("{\"result\":\"Registro de inventario exitoso\"}")
                    .build();

        } catch (java.sql.SQLException e) {
            // Aquí capturamos errores específicos de la base de datos 
            // (como el error del SP si el salón no coincide con el edificio)
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Error en base de datos: " + e.getMessage() + "\"}")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Error interno: " + e.getMessage() + "\"}")
                    .build();
        }
    }

    @POST
    @Path("update")
    public Response update(String data) {
        try {
            Inventario inv = gson.fromJson(data, Inventario.class);
            controller.actualizarInventario(inv);
            return Response.ok("{\"result\":\"OK\"}").build();
        } catch (Exception e) {
            e.printStackTrace(); // Esto DEBERÍA salir en la consola de NetBeans
            // Esto enviará el mensaje real al navegador (ej. "Connection refused" o "Column not found")
            return Response.status(500).entity("{\"error\":\"" + e.toString() + "\"}").build();
        }
    }

    // API para obtener todos los edificios
    @Path("getEdificios")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEdificios() {
        try {
            List<Edificios> lista = controller.getEdificios();
            return Response.ok(gson.toJson(lista)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @Path("getSalones")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalones(@QueryParam("idEdificio") int idEdificio) {
        try {
            // 1. Obtienes la lista de salones
            List<Salones> lista = controller.getSalones(idEdificio);

            // 2. Conviertes a JSON y LUEGO construyes la respuesta
            String json = new Gson().toJson(lista);

            return Response.ok(json).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @Path("delete")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(String data) {
        System.out.println(">>> JSON recibido: " + data);
        try {
            Gson gson = new Gson();
            Inventario inv = gson.fromJson(data, Inventario.class);

            // Si esto imprime 0, el problema es el nombre de la variable en Inventario.java
            System.out.println(">>> ID a eliminar: " + inv.getId_inventario());

            controller.eliminarInventario(inv);
            return Response.ok("{\"result\":\"OK\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
