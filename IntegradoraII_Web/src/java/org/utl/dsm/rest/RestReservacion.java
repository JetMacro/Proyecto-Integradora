package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;
import org.utl.dsm.integradoraweb.controller.ControllerReservacion;
import org.utl.dsm.integradoraweb.model.Reservacion;
import org.utl.dsm.integradoraweb.model.Salones;

/**
 * Servicio REST para la gestión de reservaciones en entorno local.
 */
@Path("reservacion")
public class RestReservacion {

    /**
     * Obtiene el listado completo de reservaciones.
     * URL: http://localhost:8080/IntegradoraII_Web/api/reservacion/getAll
     */
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            ControllerReservacion cr = new ControllerReservacion();
            List<Reservacion> lista = cr.getAllReservaciones(); // Llama al método del controlador
            return Response.ok(new Gson().toJson(lista)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    /**
     * Inserta una nueva reservación.
     * Espera un parámetro "reservacion" en formato JSON (x-www-form-urlencoded).
     */
    @Path("insertar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response insertar(@FormParam("reservacion") String reservacionJson) {
        try {
            Gson gson = new Gson();
            Reservacion r = gson.fromJson(reservacionJson, Reservacion.class);
            ControllerReservacion cr = new ControllerReservacion();
            cr.insertarReservacion(r); //
            return Response.ok("{\"result\":\"OK\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    /**
     * Actualiza una reservación existente.
     */
    @Path("actualizar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response actualizar(@FormParam("reservacion") String reservacionJson) {
        try {
            Gson gson = new Gson();
            Reservacion r = gson.fromJson(reservacionJson, Reservacion.class);
            ControllerReservacion cr = new ControllerReservacion();
            int filas = cr.actualizarReservacion(r); //
            
            if (filas > 0) {
                return Response.ok("{\"result\":\"OK\"}").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("{\"error\":\"No se encontró el ID de la reserva\"}")
                               .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    /**
     * Elimina (baja lógica) una reservación por ID.
     */
    @Path("eliminar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response eliminar(@FormParam("idReserva") int id) {
        try {
            ControllerReservacion cr = new ControllerReservacion();
            cr.eliminarReservacion(id); //
            return Response.ok("{\"result\":\"OK\"}").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }

    /**
     * Obtiene el catálogo de salones y edificios disponibles.
     */
    @Path("getSalones")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalones() {
        try {
            ControllerReservacion cr = new ControllerReservacion();
            List<Salones> lista = cr.getAllSalones(); //
            return Response.ok(new Gson().toJson(lista)).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + e.getMessage() + "\"}")
                           .build();
        }
    }
}