package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;
import org.utl.dsm.integradoraweb.controller.ControllerReservacion;
import org.utl.dsm.integradoraweb.model.Reservacion;
import org.utl.dsm.integradoraweb.model.Salones;

@Path("reservacion")
public class RestReservacion {

    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            List<Reservacion> lista = new ControllerReservacion().getAllReservaciones();
            return Response.ok(new Gson().toJson(lista)).build();
        } catch (Exception e) {
            return Response.ok("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @Path("actualizar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response actualizar(@FormParam("reservacion") String reservacionJson) {
        try {
            Gson gson = new Gson();
            Reservacion r = gson.fromJson(reservacionJson, Reservacion.class);
            ControllerReservacion cr = new ControllerReservacion();
            int filas = cr.actualizarReservacion(r);
            if (filas > 0) return Response.ok("{\"result\":\"OK\"}").build();
            else return Response.ok("{\"error\":\"No se encontró el ID " + r.getIdReserva() + "\"}").build();
        } catch (Exception e) {
            e.printStackTrace(); 
            String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
            return Response.status(Response.Status.OK)
                           .entity("{\"error\":\"" + errorMsg.replace("\"", "'") + "\"}")
                           .build();
        }
    }

    @Path("insertar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response insertar(@FormParam("reservacion") String reservacionJson) {
        try {
            Reservacion r = new Gson().fromJson(reservacionJson, Reservacion.class);
            new ControllerReservacion().insertarReservacion(r);
            return Response.ok("{\"result\":\"OK\"}").build();
        } catch (Exception e) {
            return Response.ok("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
    
    @Path("eliminar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response eliminar(@FormParam("idReserva") int id) {
        try {
            new ControllerReservacion().eliminarReservacion(id);
            return Response.ok("{\"result\":\"OK\"}").build();
        } catch (Exception e) {
            return Response.ok("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
    
    @Path("getSalones")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSalones() {
        try {
            List<Salones> lista = new ControllerReservacion().getAllSalones();
            return Response.ok(new Gson().toJson(lista)).build();
        } catch (Exception e) {
            String errorMsg = e.getMessage() != null ? e.getMessage() : e.toString();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("{\"error\":\"" + errorMsg.replace("\"", "'") + "\"}")
                           .build();
        }
    }
}