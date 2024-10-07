package org.dam.models;

import java.time.LocalDate;

public class EventModel {
    private int id;
    private String nombre_evento;
    private String descripcion_evento;
    private int id_ubicacion;
    private int capacidad;
    private boolean estado;
    private double precio;
    private LocalDate fecha_evento;

    public EventModel(String nombre_evento, String descripcion_evento,
                      int id_ubicacion, int capacidad, boolean estado,
                      double precio, LocalDate fecha_evento) {
        this.nombre_evento = nombre_evento;
        this.descripcion_evento = descripcion_evento;
        this.id_ubicacion = id_ubicacion;
        this.capacidad = capacidad;
        this.estado = estado;
        this.precio = precio;
        this.fecha_evento = fecha_evento;
    }

    public int getId() {
        return id;
    }

    public String getNombre_evento() {
        return nombre_evento;
    }

    public String getDescripcion_evento() {
        return descripcion_evento;
    }

    public int getId_ubicacion() {
        return id_ubicacion;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public boolean isEstado() {
        return estado;
    }

    public double getPrecio() {
        return precio;
    }

    public LocalDate getFecha_evento() {
        return fecha_evento;
    }
}
