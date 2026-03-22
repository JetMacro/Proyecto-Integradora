package org.utl.dsm.integradoraweb.model;

public class Reporte {

    private int id_reporte;
    private String matricula;
    private String codigo_mueble;
    private String tipo_material;
    private String nombre_salon;
    private String tipo_dano;
    private String descripcion;
    private String prioridad;
    private String fecha_reporte;
    private String estatus;
    private String fotoEvidencia;

    public Reporte() {
    }

    public int getId_reporte() {
        return id_reporte;
    }

    public void setId_reporte(int id_reporte) {
        this.id_reporte = id_reporte;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCodigo_mueble() {
        return codigo_mueble;
    }

    public void setCodigo_mueble(String codigo_mueble) {
        this.codigo_mueble = codigo_mueble;
    }

    public String getTipo_material() {
        return tipo_material;
    }

    public void setTipo_material(String tipo_material) {
        this.tipo_material = tipo_material;
    }

    public String getNombre_salon() {
        return nombre_salon;
    }

    public void setNombre_salon(String nombre_salon) {
        this.nombre_salon = nombre_salon;
    }

    public String getTipo_dano() {
        return tipo_dano;
    }

    public void setTipo_dano(String tipo_dano) {
        this.tipo_dano = tipo_dano;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getFecha_reporte() {
        return fecha_reporte;
    }

    public void setFecha_reporte(String fecha_reporte) {
        this.fecha_reporte = fecha_reporte;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getFotoEvidencia() {
        return fotoEvidencia;
    }

    public void setFotoEvidencia(String fotoEvidencia) {
        this.fotoEvidencia = fotoEvidencia;
    }
    
    
}
