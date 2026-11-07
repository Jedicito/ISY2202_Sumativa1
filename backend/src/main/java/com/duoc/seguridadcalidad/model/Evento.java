package com.duoc.seguridadcalidad.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "evento")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(nullable = false, length = 40)
    private String categoria;

    @Column(nullable = false, length = 80)
    private String juego;

    @Column(nullable = false, length = 60)
    private String ciudad;

    @Column(nullable = false, length = 150)
    private String direccion;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false, length = 1000)
    private String descripcion;

    @Column(nullable = false, length = 200)
    private String organizadores;

    @Column(nullable = false, length = 300)
    private String expositores;

    @Column(nullable = false)
    private boolean estacionamiento;

    @Column(nullable = false)
    private boolean locomocion;

    @Column(nullable = false)
    private boolean comida;

    @Column(nullable = false)
    private boolean alojamiento;

    @Column(nullable = false)
    private boolean destacado;

    @Column(nullable = false, length = 100)
    private String imagen;

    protected Evento() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getJuego() {
        return juego;
    }

    public String getCiudad() {
        return ciudad;
    }

    public String getDireccion() {
        return direccion;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getOrganizadores() {
        return organizadores;
    }

    public String getExpositores() {
        return expositores;
    }

    public boolean isEstacionamiento() {
        return estacionamiento;
    }

    public boolean isLocomocion() {
        return locomocion;
    }

    public boolean isComida() {
        return comida;
    }

    public boolean isAlojamiento() {
        return alojamiento;
    }

    public boolean isDestacado() {
        return destacado;
    }

    public String getImagen() {
        return imagen;
    }
}
