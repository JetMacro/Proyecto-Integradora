package org.utl.dsm.integradoraweb.model;

public class Actividad {

    private String titulo;
    private String descripcion;
    private String fechaActividad;
    private String autor;

    public Actividad() {
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaActividad() {
        return fechaActividad;
    }

    public void setFechaActividad(String fechaActividad) {
        this.fechaActividad = fechaActividad;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

}
