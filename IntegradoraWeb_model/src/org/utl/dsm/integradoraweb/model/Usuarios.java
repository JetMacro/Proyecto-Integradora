package org.utl.dsm.integradoraweb.model;

public class Usuarios {

    // Datos de tbl_usuarios
    private int id_usuario;
    private String matricula;
    private String id_rol;
    private String id_turno;
    private String id_estatus;
    private String contrasenia;

    // Datos de tbl_personas
    private int id_persona;
    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private String correo;
    private String telefono;
    private String fecha_nacimiento;
    private String direccion;

    // Nuevos agregados mau
    private String token;
    private String last_token;
    private String last_date_token;
    private String last_used_token;

    public Usuarios() {
    }

    // Constructor completo
    public Usuarios(int id_usuario, String matricula, String id_rol, String id_turno, String id_estatus, String contrasenia, int id_persona, String nombre, String apellido_paterno, String apellido_materno, String correo, String telefono, String fecha_nacimiento, String direccion) {
        this.id_usuario = id_usuario;
        this.matricula = matricula;
        this.id_rol = id_rol;
        this.id_turno = id_turno;
        this.id_estatus = id_estatus;
        this.contrasenia = contrasenia;
        this.id_persona = id_persona;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.correo = correo;
        this.telefono = telefono;
        this.fecha_nacimiento = fecha_nacimiento;
        this.direccion = direccion;
    }

    // Getters y Setters
    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getId_rol() {
        return id_rol;
    }

    public void setId_rol(String id_rol) {
        this.id_rol = id_rol;
    }

    public String getId_turno() {
        return id_turno;
    }

    public void setId_turno(String id_turno) {
        this.id_turno = id_turno;
    }

    public String getId_estatus() {
        return id_estatus;
    }

    public void setId_estatus(String id_estatus) {
        this.id_estatus = id_estatus;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public int getId_persona() {
        return id_persona;
    }

    public void setId_persona(int id_persona) {
        this.id_persona = id_persona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
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

    public String getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(String fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    
    // agragados Token

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLast_token() {
        return last_token;
    }

    public void setLast_token(String last_token) {
        this.last_token = last_token;
    }

    public String getLast_date_token() {
        return last_date_token;
    }

    public void setLast_date_token(String last_date_token) {
        this.last_date_token = last_date_token;
    }

    public String getLast_used_token() {
        return last_used_token;
    }

    public void setLast_used_token(String last_used_token) {
        this.last_used_token = last_used_token;
    }
    
    
}
