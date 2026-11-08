package com.duoc.seguridadcalidad;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class HomeController {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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

    /**
     * [Pública] Búsqueda por nombre, categoría, juego, ciudad y fecha.
     * Los filtros aplicados se muestran como texto (no dentro de atributos HTML).
     */
    @GetMapping("/buscar")
    public String buscar(@RequestParam(required = false) String nombre,
                         @RequestParam(required = false) String categoria,
                         @RequestParam(required = false) String juego,
                         @RequestParam(required = false) String ciudad,
                         @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
                         Model model) {
        List<String> filtros = new ArrayList<>();
        agregarFiltro(filtros, "Nombre", nombre);
        agregarFiltro(filtros, "Categoría", categoria);
        agregarFiltro(filtros, "Juego", juego);
        agregarFiltro(filtros, "Ciudad", ciudad);
        if (fecha != null) {
            filtros.add("Fecha: " + fecha.format(FORMATO_FECHA));
        }

        model.addAttribute("eventos", eventoService.buscar(nombre, categoria, juego, ciudad, fecha));
        model.addAttribute("filtros", filtros);
        return "buscar";
    }

    /** [Privada] Detalle del evento. Solo usuarios autenticados. */
    @GetMapping("/eventos/{id}")
    public String detalle(@PathVariable Long id, Model model,
                          HttpServletRequest request, HttpServletResponse response) {
        try {
            model.addAttribute("evento", eventoService.detalle(id));
            return "evento";
        } catch (SesionExpiradaException e) {
            // El token ya no es válido: se cierra la sesión y se pide iniciar sesión de nuevo.
            new SecurityContextLogoutHandler().logout(request, response,
                    SecurityContextHolder.getContext().getAuthentication());
            return "redirect:/login?expirada";
        }
    }

    private static void agregarFiltro(List<String> filtros, String etiqueta, String valor) {
        if (StringUtils.hasText(valor)) {
            filtros.add(etiqueta + ": " + valor.trim());
        }
    }
}
