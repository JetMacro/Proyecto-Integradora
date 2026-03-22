package org.utl.dsm.integradoraweb.model;

import java.util.List;

public class DashBoard {
    
    private int totalMobiliario;
    private int totalUsuarios; 
    private int reporteSemanal;
    private int activas;
    private List<Actividad> actividades;
    
    public DashBoard() {
    }

    public DashBoard(int totalMobiliario, int totalUsuarios, int reporteSemanal, int activas, List<Actividad> actividades) {
        this.totalMobiliario = totalMobiliario;
        this.totalUsuarios = totalUsuarios;
        this.reporteSemanal = reporteSemanal;
        this.activas = activas;
        this.actividades = actividades;
    }

    public int getTotalMobiliario() {
        return totalMobiliario;
    }

    public void setTotalMobiliario(int totalMobiliario) {
        this.totalMobiliario = totalMobiliario;
    }

    public int getTotalUsuarios() {
        return totalUsuarios;
    }

    public void setTotalUsuarios(int totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public int getReporteSemanal() {
        return reporteSemanal;
    }

    public void setReporteSemanal(int reporteSemanal) {
        this.reporteSemanal = reporteSemanal;
    }

    public int getActivas() {
        return activas;
    }

    public void setActivas(int activas) {
        this.activas = activas;
    }
    
    public List<Actividad> getActividades() {
        return actividades;
    }

    public void setActividades(List<Actividad> actividades) {
        this.actividades = actividades;
    }
}