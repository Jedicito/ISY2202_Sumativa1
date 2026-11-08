package com.duoc.seguridadcalidad;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Guarda el token JWT del usuario. Con @SessionScope existe una instancia por
 * sesión: cada usuario tiene su propio token y se descarta al cerrar sesión.
 */
@Component
@SessionScope
public class TokenStore implements Serializable {

    private static final long serialVersionUID = 1L;

    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
