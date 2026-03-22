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
import org.utl.dsm.integradoraweb.controller.ControllerPrestamos;
import org.utl.dsm.integradoraweb.model.Prestamos;

/**
 *
 * @author rodod
 */
@Path("prestamos")
public class RESTPrestamos {

    private final Gson gson = new Gson();
    private final ControllerPrestamos controller = new ControllerPrestamos();

    // Obtener todas las reservaciones
    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            List<Prestamos> lista = controller.getAll();
            return Response.ok(gson.toJson(lista)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error al obtener reservaciones: " + e.getMessage())
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
            Prestamos inv = gson.fromJson(data, Prestamos.class);

            // Si esto imprime 0, el problema es el nombre de la variable en Inventario.java
            System.out.println(">>> ID a eliminar: " + inv.getId_prestamo());

            controller.eliminarPrestamo(inv);
            return Response.ok("{\"result\":\"OK\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(500).entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("update")
    public Response update(String data) {
        try {
            Prestamos inv = gson.fromJson(data, Prestamos.class);
            controller.actualizarPrestamo(inv);
            return Response.ok("{\"result\":\"OK\"}").build();
        } catch (Exception e) {
            e.printStackTrace(); // Esto DEBERÍA salir en la consola de NetBeans
            // Esto enviará el mensaje real al navegador (ej. "Connection refused" o "Column not found")
            return Response.status(500).entity("{\"error\":\"" + e.toString() + "\"}").build();
        }
    }

    @Path("insert")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response insert(String json) {
        Gson gson = new Gson();
        try {
            // Log para ver qué está llegando exactamente desde el navegador
            System.out.println("JSON Recibido: " + json);

            Prestamos p = gson.fromJson(json, Prestamos.class);

            ControllerPrestamos cp = new ControllerPrestamos();
            cp.insertarPrestamo(p);

            return Response.ok("{\"result\":\"OK\"}").build();
        } catch (Exception e) {
            e.printStackTrace(); // Esto imprimirá el error real en la consola negra de tu IDE
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
