package org.utl.dsm.integradoraweb.model;

public class Inventario {

    private String nombre;
    private int id_inventario;
    private int id_salon;
    private String id_mobiliario;
    private int id_edificio;
    private String id_ubicacion;
    private String id_tipo_mobi;
    private String id_categoria_mobi;
    private String id_estatus;
    private String id_estatus_actual;
    private String observaciones;
    private String material;
    private String categoria;
    private String estatus;
    private String nombreSalon;
    private String nombreEdificio;
    private String ubicacion;
    private int capacidad;
    private int total;
    private int disponibles;
    private int uso;
    private double pct_disponibles;

    ;

    public Inventario() {
    }

    public Inventario(int id_inventario, int id_salon, String id_mobiliario, int id_edificio, String id_ubicacion, String id_tipo_mobi, String id_categoria_mobi, String id_estatus, String id_estatus_actual, String observaciones) {
        this.id_inventario = id_inventario;
        this.id_salon = id_salon;
        this.id_mobiliario = id_mobiliario;
        this.id_edificio = id_edificio;
        this.id_ubicacion = id_ubicacion;
        this.id_tipo_mobi = id_tipo_mobi;
        this.id_categoria_mobi = id_categoria_mobi;
        this.id_estatus = id_estatus;
        this.id_estatus_actual = id_estatus_actual;
        this.observaciones = observaciones;
    }

    public int getId_inventario() {
        return id_inventario;
    }

    public void setId_inventario(int id_inventario) {
        this.id_inventario = id_inventario;
    }

    public int getId_salon() {
        return id_salon;
    }

    public void setId_salon(int id_salon) {
        this.id_salon = id_salon;
    }

    public String getId_mobiliario() {
        return id_mobiliario;
    }

    public void setId_mobiliario(String id_mobiliario) {
        this.id_mobiliario = id_mobiliario;
    }

    public int getId_edificio() {
        return id_edificio;
    }

    public void setId_edificio(int id_edificio) {
        this.id_edificio = id_edificio;
    }

    public String getId_ubicacion() {
        return id_ubicacion;
    }

    public void setId_ubicacion(String id_ubicacion) {
        this.id_ubicacion = id_ubicacion;
    }

    public String getId_tipo_mobi() {
        return id_tipo_mobi;
    }

    public void setId_tipo_mobi(String id_tipo_mobi) {
        this.id_tipo_mobi = id_tipo_mobi;
    }

    public String getId_categoria_mobi() {
        return id_categoria_mobi;
    }

    public void setId_categoria_mobi(String id_categoria_mobi) {
        this.id_categoria_mobi = id_categoria_mobi;
    }

    public String getId_estatus() {
        return id_estatus;
    }

    public void setId_estatus(String id_estatus) {
        this.id_estatus = id_estatus;
    }

    public String getId_estatus_actual() {
        return id_estatus_actual;
    }

    public void setId_estatus_actual(String id_estatus_actual) {
        this.id_estatus_actual = id_estatus_actual;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public int getUso() {
        return uso;
    }

    public void setUso(int uso) {
        this.uso = uso;
    }

    public double getPct_disponibles() {
        return pct_disponibles;
    }

    public void setPct_disponibles(double pct_disponibles) {
        this.pct_disponibles = pct_disponibles;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getNombreSalon() {
        return nombreSalon;
    }

    public void setNombreSalon(String nombreSalon) {
        this.nombreSalon = nombreSalon;
    }

    public String getNombreEdificio() {
        return nombreEdificio;
    }

    public void setNombreEdificio(String nombreEdificio) {
        this.nombreEdificio = nombreEdificio;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
}
