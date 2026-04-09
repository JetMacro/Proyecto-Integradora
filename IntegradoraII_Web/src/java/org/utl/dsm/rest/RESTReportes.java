package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.utl.dsm.integradoraweb.controller.ControllerReporte;
import org.utl.dsm.integradoraweb.model.Reporte;

@Path("reporte")
public class RESTReportes {

    @GET
    @Path("getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        try {
            ControllerReporte cr = new ControllerReporte();
            List<Reporte> lista = cr.getAll();
            return Response.ok(new Gson().toJson(lista)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("getMateriales")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMateriales() {
        try {
            ControllerReporte cr = new ControllerReporte();
            List<Reporte> lista = cr.getMaterialesParaReporte();
            return Response.ok(new Gson().toJson(lista)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("insertar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertar(@FormParam("datos") String datos,
            @FormParam("idUsuario") int idU,
            @FormParam("idInventario") int idI,
            @FormParam("fotoB64") String fotoB64) {
        try {
            Gson gson = new Gson();
            Reporte r = gson.fromJson(datos, Reporte.class);

            // Guardamos la cadena Base64 directamente en el objeto.
            // Validamos que si no llega foto, se guarde una cadena vacía para evitar errores en BD.
            r.setFotoEvidencia(fotoB64 != null ? fotoB64 : "");

            ControllerReporte cr = new ControllerReporte();
            cr.insertar(r, idU, idI);

            return Response.ok("{\"res\":\"Éxito\"}").build();
        } catch (Exception e) {
            // Imprimimos el error en la consola del servidor para que puedas ver el detalle real
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @POST
    @Path("eliminar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@FormParam("idReporte") int idR) {
        try {
            ControllerReporte cr = new ControllerReporte();
            cr.eliminar(idR);
            return Response.ok("{\"res\":\"Reporte eliminado correctamente\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }

    @GET
    @Path("getFoto")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFoto(@QueryParam("idReporte") int idR) {
        try {
            ControllerReporte cr = new ControllerReporte();
            String foto = cr.getFotoById(idR);
            return Response.ok("{\"foto\":\"" + foto + "\"}").build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}").build();
        }
    }
}
