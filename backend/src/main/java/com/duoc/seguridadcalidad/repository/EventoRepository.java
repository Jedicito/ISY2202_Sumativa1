package com.duoc.seguridadcalidad.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import com.duoc.seguridadcalidad.model.Evento;

import jakarta.persistence.criteria.Predicate;

public interface EventoRepository extends JpaRepository<Evento, Long>, JpaSpecificationExecutor<Evento> {

    /**
     * Construye los filtros de búsqueda. Cada filtro es opcional y se aplica
     * solo si viene informado. Los valores se envían a la base de datos como
     * parámetros (Criteria API), nunca concatenados en el SQL.
     */
    static Specification<Evento> filtrar(String nombre, String categoria, String juego,
                                         String ciudad, LocalDate fecha) {
        return (root, query, cb) -> {
            List<Predicate> filtros = new ArrayList<>();
            if (StringUtils.hasText(nombre)) {
                filtros.add(cb.like(root.<String>get("nombre"), "%" + nombre.trim() + "%"));
            }
            if (StringUtils.hasText(categoria)) {
                filtros.add(cb.like(root.<String>get("categoria"), "%" + categoria.trim() + "%"));
            }
            if (StringUtils.hasText(juego)) {
                filtros.add(cb.like(root.<String>get("juego"), "%" + juego.trim() + "%"));
            }
            if (StringUtils.hasText(ciudad)) {
                filtros.add(cb.like(root.<String>get("ciudad"), "%" + ciudad.trim() + "%"));
            }
            if (fecha != null) {
                filtros.add(cb.between(root.<LocalDateTime>get("fechaHora"), fecha.atStartOfDay(), fecha.atTime(LocalTime.MAX)));
            }
            return cb.and(filtros.toArray(new Predicate[0]));
        };
    }
}
