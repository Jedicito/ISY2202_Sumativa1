package com.duoc.seguridadcalidad.dto;

import java.time.LocalDateTime;

import com.duoc.seguridadcalidad.model.Evento;

/** Datos públicos de un evento, usados en el inicio y en la búsqueda. */
public record EventoResumen(
        Long id,
        String nombre,
        String categoria,
        String juego,
        String ciudad,
        LocalDateTime fechaHora,
        boolean destacado,
        String imagen) {

    public static EventoResumen desde(Evento e) {
        return new EventoResumen(e.getId(), e.getNombre(), e.getCategoria(), e.getJuego(),
                e.getCiudad(), e.getFechaHora(), e.isDestacado(), e.getImagen());
    }
}
