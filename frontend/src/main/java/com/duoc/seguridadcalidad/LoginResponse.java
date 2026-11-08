package com.duoc.seguridadcalidad;

import java.util.List;

/** Respuesta de POST /login del backend: token JWT, usuario y roles. */
public record LoginResponse(String token, String username, List<String> roles) {
}
