/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.utl.dsm.integradoraweb.model;

/**
 *
 * @author rodod
 */
public class Edificios {
    // Edificio.java

    private int id_edificio;
    private String nombre;

    public Edificios() {
    }

    public Edificios(int id_edificio, String nombre) {
        this.id_edificio = id_edificio;
        this.nombre = nombre;
    }

    // Getters y Setters
    public int getId_edificio() {
        return id_edificio;
    }

    public void setId_edificio(int id_edificio) {
        this.id_edificio = id_edificio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
