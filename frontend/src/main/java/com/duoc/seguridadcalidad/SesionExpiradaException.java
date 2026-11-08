package com.duoc.seguridadcalidad;

/** El backend rechazó el token (expiró o el backend se reinició). */
public class SesionExpiradaException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SesionExpiradaException() {
        super("Sesión expirada");
    }
}
