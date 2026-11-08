package com.duoc.seguridadcalidad.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.duoc.seguridadcalidad.dto.LoginResponse;
import com.duoc.seguridadcalidad.security.JwtService;

import jakarta.servlet.http.HttpServletRequest;

/**
 * API de login. Recibe los parámetros "user" y "encryptedPass" (los nombres
 * que usa el frontend de la guía) y responde el token JWT con el usuario y sus roles.
 */
@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam("user") String user,
                                   @RequestParam("encryptedPass") String password,
                                   HttpServletRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(user, password));
            List<String> roles = auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();
            String token = jwtService.generarToken(auth.getName(), roles);
            log.info("Login exitoso: usuario={}, origen={}", limpiar(auth.getName()), request.getRemoteAddr());
            return ResponseEntity.ok(new LoginResponse(token, auth.getName(), roles));
        } catch (AuthenticationException e) {
            // Se registra el intento fallido sin la contraseña.
            log.warn("Login fallido: usuario={}, origen={}", limpiar(user), request.getRemoteAddr());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Credenciales inválidas"));
        }
    }

    /** Evita que un nombre de usuario con saltos de línea falsifique entradas del log. */
    private static String limpiar(String texto) {
        String limpio = texto == null ? "" : texto.replaceAll("[\\r\\n\\t]", "_");
        return limpio.length() > 50 ? limpio.substring(0, 50) : limpio;
    }
}
