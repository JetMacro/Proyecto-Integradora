package org.utl.dsm.integradoraweb.model;

public class Salones {
    
    private int idSalon;
    private int idEdificio;
    private String nombre;
    private String tipo;
    private int capacidad;
    private String edificio;
    private String estatus;
    private String ubicacionEdificio;
    private int pisosEdificio;
    private String divisionAcademica;
    private String tipoMobiliario;
    private String tipoSalon;
    private int totalMobiliario;
    private int disponibles;
    private int enUso;
    
    public Salones() {
    }
    
    public Salones(int idSalon, int idEdificio, String nombre, String tipo, int capacidad, String edificio, String estatus, String ubicacionEdificio, int pisosEdificio, String divisionAcademica, String tipoMobiliario, int totalMobiliario, int disponibles, int enUso) {
        this.idSalon = idSalon;
        this.idEdificio = idEdificio;
        this.nombre = nombre;
        this.tipo = tipo;
        this.capacidad = capacidad;
        this.edificio = edificio;
        this.estatus = estatus;
        this.ubicacionEdificio = ubicacionEdificio;
        this.pisosEdificio = pisosEdificio;
        this.divisionAcademica = divisionAcademica;
        this.tipoMobiliario = tipoMobiliario;
        this.totalMobiliario = totalMobiliario;
        this.disponibles = disponibles;
        this.enUso = enUso;
    }
    
    public int getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(int idSalon) {
        this.idSalon = idSalon;
    }

    public int getIdEdificio() {
        return idEdificio;
    }

    public void setIdEdificio(int idEdificio) {
        this.idEdificio = idEdificio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getUbicacionEdificio() {
        return ubicacionEdificio;
    }

    public void setUbicacionEdificio(String ubicacionEdificio) {
        this.ubicacionEdificio = ubicacionEdificio;
    }

    public int getPisosEdificio() {
        return pisosEdificio;
    }

    public void setPisosEdificio(int pisosEdificio) {
        this.pisosEdificio = pisosEdificio;
    }

    public String getDivisionAcademica() {
        return divisionAcademica;
    }

    public void setDivisionAcademica(String divisionAcademica) {
        this.divisionAcademica = divisionAcademica;
    }

    public String getTipoMobiliario() {
        return tipoMobiliario;
    }

    public void setTipoMobiliario(String tipoMobiliario) {
        this.tipoMobiliario = tipoMobiliario;
    }

    public int getTotalMobiliario() {
        return totalMobiliario;
    }

    public void setTotalMobiliario(int totalMobiliario) {
        this.totalMobiliario = totalMobiliario;
    }

    public int getDisponibles() {
        return disponibles;
    }

    public void setDisponibles(int disponibles) {
        this.disponibles = disponibles;
    }

    public int getEnUso() {
        return enUso;
    }

    public void setEnUso(int enUso) {
        this.enUso = enUso;
    }

    public String getTipoSalon() {
        return tipoSalon;
    }

    public void setTipoSalon(String tipoSalon) {
        this.tipoSalon = tipoSalon;
    }
    
    
}