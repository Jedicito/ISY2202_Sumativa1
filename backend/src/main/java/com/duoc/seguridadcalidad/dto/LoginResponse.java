package com.duoc.seguridadcalidad.dto;

import java.util.List;

/** Respuesta de un login exitoso: token JWT, usuario y roles. */
public record LoginResponse(String token, String username, List<String> roles) {
}
