package com.example.lab6.model;

import java.util.Date;

public class Tarea {

    private String id;
    private String titulo;
    private String descripcion;
    private Date fechaLimite;
    private boolean estado;
    private String uidUsuario;

    public Tarea() {
    }

    public Tarea(String id, String titulo, String descripcion, Date fechaLimite, boolean estado, String uidUsuario) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaLimite = fechaLimite;
        this.estado = estado;
        this.uidUsuario = uidUsuario;
    }

    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getDescripcion() { return descripcion; }
    public Date getFechaLimite() { return fechaLimite; }
    public boolean isEstado() { return estado; }
    public String getUidUsuario() { return uidUsuario; }

    public void setId(String id) { this.id = id; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setFechaLimite(Date fechaLimite) { this.fechaLimite = fechaLimite; }
    public void setEstado(boolean estado) { this.estado = estado; }
    public void setUidUsuario(String uidUsuario) { this.uidUsuario = uidUsuario; }
}
