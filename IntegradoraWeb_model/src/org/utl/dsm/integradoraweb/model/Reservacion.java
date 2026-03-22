/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.integradoraweb.model;

public class Reservacion {
    private int idReserva; 
    private int idUsuario;
    private int idSalon;
    private String fechaHora;
    private String estatus;
    private String matricula;
    private String nombreSalon;
    private String nombreEdificio;

    public Reservacion() {
    }

    public Reservacion(int idReserva, int idUsuario, int idSalon, String fechaHora, String estatus, String matricula, String nombreSalon, String nombreEdificio) {
        this.idReserva = idReserva;
        this.idUsuario = idUsuario;
        this.idSalon = idSalon;
        this.fechaHora = fechaHora;
        this.estatus = estatus;
        this.matricula = matricula;
        this.nombreSalon = nombreSalon;
        this.nombreEdificio = nombreEdificio;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdSalon() {
        return idSalon;
    }

    public void setIdSalon(int idSalon) {
        this.idSalon = idSalon;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
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

    @Override
    public String toString() {
        return "Reservacion{" + "idReserva=" + idReserva + ", estatus=" + estatus + ", matricula=" + matricula + ", nombreSalon=" + nombreSalon + ", nombreEdificio=" + nombreEdificio + ", fechaHora=" + fechaHora + '}';
    }
}
