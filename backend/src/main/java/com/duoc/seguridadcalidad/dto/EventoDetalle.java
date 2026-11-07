package com.duoc.seguridadcalidad.dto;

import java.time.LocalDateTime;

import com.duoc.seguridadcalidad.model.Evento;

/** Detalle completo de un evento. Solo para usuarios autenticados. */
public record EventoDetalle(
        Long id,
        String nombre,
        String categoria,
        String juego,
        String ciudad,
        String direccion,
        LocalDateTime fechaHora,
        String descripcion,
        String organizadores,
        String expositores,
        boolean estacionamiento,
        boolean locomocion,
        boolean comida,
        boolean alojamiento,
        boolean destacado,
        String imagen) {

    public static EventoDetalle desde(Evento e) {
        return new EventoDetalle(e.getId(), e.getNombre(), e.getCategoria(), e.getJuego(),
                e.getCiudad(), e.getDireccion(), e.getFechaHora(), e.getDescripcion(),
                e.getOrganizadores(), e.getExpositores(), e.isEstacionamiento(), e.isLocomocion(),
                e.isComida(), e.isAlojamiento(), e.isDestacado(), e.getImagen());
    }
}
