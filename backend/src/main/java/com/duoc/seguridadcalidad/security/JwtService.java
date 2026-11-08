package com.duoc.seguridadcalidad.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

/**
 * Genera y valida los tokens JWT firmados con HMAC-SHA256.
 *
 * La clave de firma se genera de forma aleatoria (SecureRandom, 256 bits) cada
 * vez que arranca el backend y solo existe en memoria: no queda escrita en el
 * código, en archivos ni en variables de entorno. Al reiniciar el backend los
 * tokens anteriores dejan de ser válidos y se debe iniciar sesión de nuevo.
 */
@Service
public class JwtService {

    private final SecretKey clave = Jwts.SIG.HS256.key().build();
    private final long minutosExpiracion;

    public JwtService(@Value("${jwt.expiration-minutes}") long minutosExpiracion) {
        this.minutosExpiracion = minutosExpiracion;
    }

    public String generarToken(String username, List<String> roles) {
        Instant ahora = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(ahora.plus(minutosExpiracion, ChronoUnit.MINUTES)))
                .signWith(clave)
                .compact();
    }

    /** Verifica firma y expiración. Lanza JwtException si el token no es válido. */
    public Claims validar(String token) {
        return Jwts.parser()
                .verifyWith(clave)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
