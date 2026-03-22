
package org.utl.dsm.integradoraweb.model;

public class Prestamos {
    private int id_prestamo;
    private String id_rol;
    private String nombre;
    private String matricula;
    private String id_mobiliario;
    private String fecha_salida;
    private String fecha_devolucion_prevista;
    private String fecha_devolucion_real;
    private String estatus_prestamo;
    private String observaciones_salida;
    private String id_turno;
    private String correo;
    private String telefono;
    private String observaciones_entrada;

    public Prestamos() {
    }

    public Prestamos(String nombre, String id_mobiliario, String fecha_salida, String fecha_devolucion_prevista, String fecha_devolucion_real, String estatus_prestamo, String observaciones_salida, String id_turno, String correo, String telefono, String observaciones_entrada) {
        this.nombre = nombre;
        this.id_mobiliario = id_mobiliario;
        this.fecha_salida = fecha_salida;
        this.fecha_devolucion_prevista = fecha_devolucion_prevista;
        this.fecha_devolucion_real = fecha_devolucion_real;
        this.estatus_prestamo = estatus_prestamo;
        this.observaciones_salida = observaciones_salida;
        this.id_turno = id_turno;
        this.correo = correo;
        this.telefono = telefono;
        this.observaciones_entrada = observaciones_entrada;
    }
    
    

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getId_mobiliario() {
        return id_mobiliario;
    }

    public void setId_mobiliario(String id_mobiliario) {
        this.id_mobiliario = id_mobiliario;
    }

    public String getFecha_salida() {
        return fecha_salida;
    }

    public void setFecha_salida(String fecha_salida) {
        this.fecha_salida = fecha_salida;
    }

    public String getFecha_devolucion_prevista() {
        return fecha_devolucion_prevista;
    }

    public void setFecha_devolucion_prevista(String fecha_devolucion_prevista) {
        this.fecha_devolucion_prevista = fecha_devolucion_prevista;
    }

    public String getFecha_devolucion_real() {
        return fecha_devolucion_real;
    }

    public void setFecha_devolucion_real(String fecha_devolucion_real) {
        this.fecha_devolucion_real = fecha_devolucion_real;
    }

    public String getEstatus_prestamo() {
        return estatus_prestamo;
    }

    public void setEstatus_prestamo(String estatus_prestamo) {
        this.estatus_prestamo = estatus_prestamo;
    }

    public String getObservaciones_salida() {
        return observaciones_salida;
    }

    public void setObservaciones_salida(String observaciones_salida) {
        this.observaciones_salida = observaciones_salida;
    }

    public String getId_turno() {
        return id_turno;
    }

    public void setId_turno(String id_turno) {
        this.id_turno = id_turno;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getObservaciones_entrada() {
        return observaciones_entrada;
    }

    public void setObservaciones_entrada(String observaciones_entrada) {
        this.observaciones_entrada = observaciones_entrada;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public int getId_prestamo() {
        return id_prestamo;
    }

    public void setId_prestamo(int id_prestamo) {
        this.id_prestamo = id_prestamo;
    }

    public String getId_rol() {
        return id_rol;
    }

    public void setId_rol(String id_rol) {
        this.id_rol = id_rol;
    }
            
    
    
}
