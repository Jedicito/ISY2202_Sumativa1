package com.duoc.seguridadcalidad;

import java.time.LocalDateTime;

/** Detalle completo de un evento, tal como lo entrega GET /eventos/{id} del backend. */
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
}
