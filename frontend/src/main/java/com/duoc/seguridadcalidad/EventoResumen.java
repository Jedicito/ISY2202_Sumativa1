package com.duoc.seguridadcalidad;

import java.time.LocalDateTime;

/** Datos públicos de un evento, tal como los entrega GET /eventos del backend. */
public record EventoResumen(
        Long id,
        String nombre,
        String categoria,
        String juego,
        String ciudad,
        LocalDateTime fechaHora,
        boolean destacado,
        String imagen) {
}
