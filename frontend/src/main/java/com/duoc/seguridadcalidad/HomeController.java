package com.duoc.seguridadcalidad;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    /** Categorías disponibles en el filtro de búsqueda. */
    private static final List<String> CATEGORIAS =
            List.of("Torneo", "Convención", "LAN Party", "Lanzamiento");

    private final EventoService eventoService;

    public HomeController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    /** [Pública] Inicio: próximos eventos, destacados, avisos y acceso al login. */
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<EventoResumen> eventos = eventoService.buscar(null, null, null, null, null);
        LocalDateTime ahora = LocalDateTime.now();
        model.addAttribute("proximos", eventos.stream()
                .filter(e -> e.fechaHora().isAfter(ahora))
                .toList());
        model.addAttribute("destacados", eventos.stream()
                .filter(EventoResumen::destacado)
                .toList());
        return "home";
    }

    /** [Pública] Búsqueda por nombre, categoría, juego, ciudad y fecha. */
    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String nombre,
                         @RequestParam(required = false) String categoria,
                         @RequestParam(required = false) String juego,
                         @RequestParam(required = false) String ciudad,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                         Model model) {
        model.addAttribute("eventos", eventoService.buscar(nombre, categoria, juego, ciudad, fecha));
        model.addAttribute("categorias", CATEGORIAS);
        model.addAttribute("nombre", nombre);
        model.addAttribute("categoria", categoria);
        model.addAttribute("juego", juego);
        model.addAttribute("ciudad", ciudad);
        model.addAttribute("fecha", fecha);
        return "buscar";
    }

    /** [Privada] Detalle del evento. Solo usuarios autenticados. */
    @GetMapping("/eventos/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("evento", eventoService.detalle(id));
        return "evento";
    }
}
