package com.duoc.seguridadcalidad.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.seguridadcalidad.dto.EventoDetalle;
import com.duoc.seguridadcalidad.dto.EventoResumen;
import com.duoc.seguridadcalidad.repository.EventoRepository;

@RestController
@RequestMapping("/eventos")
public class EventoController {

    private final EventoRepository eventoRepository;

    public EventoController(EventoRepository eventoRepository) {
        this.eventoRepository = eventoRepository;
    }

    /** [Pública] Lista y búsqueda de eventos. Todos los filtros son opcionales. */
    @GetMapping
    public List<EventoResumen> buscar(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String juego,
            @RequestParam(required = false) String ciudad,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return eventoRepository
                .findAll(EventoRepository.filtrar(nombre, categoria, juego, ciudad, fecha),
                        Sort.by("fechaHora"))
                .stream()
                .map(EventoResumen::desde)
                .toList();
    }

    /** [Privada] Detalle de un evento. Requiere token JWT. */
    @GetMapping("/{id}")
    public ResponseEntity<EventoDetalle> detalle(@PathVariable Long id) {
        return eventoRepository.findById(id)
                .map(EventoDetalle::desde)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
