package com.duoc.seguridadcalidad;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Autentica al usuario contra la capa backend (POST /login) y guarda el token
 * JWT en la sesión del usuario (TokenStore).
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    private final TokenStore tokenStore;
    private final String backendUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public CustomAuthenticationProvider(TokenStore tokenStore, @Value("${backend.url}") String backendUrl) {
        this.tokenStore = tokenStore;
        this.backendUrl = backendUrl;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();
        final String ip = origen(authentication);

        final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("user", name);
        requestBody.add("encryptedPass", password);

        final LoginResponse respuesta;
        try {
            respuesta = restTemplate.postForObject(backendUrl + "/login", requestBody, LoginResponse.class);
        } catch (HttpClientErrorException e) {
            // 401 u otro error 4xx: credenciales rechazadas por el backend.
            log.warn("Login fallido: usuario={}, origen={}", limpiar(name), ip);
            throw new BadCredentialsException("Usuario o contraseña incorrectos");
        } catch (RestClientException e) {
            // El backend no respondió o respondió con error 5xx.
            log.error("Login no disponible: el backend no respondió ({})", e.getClass().getSimpleName());
            throw new AuthenticationServiceException("Servicio de autenticación no disponible");
        }

        if (respuesta == null || respuesta.token() == null) {
            throw new AuthenticationServiceException("Respuesta de autenticación inválida");
        }

        tokenStore.setToken(respuesta.token());

        // Roles reales del usuario, entregados por el backend.
        List<SimpleGrantedAuthority> authorities = respuesta.roles() == null ? List.of()
                : respuesta.roles().stream().map(SimpleGrantedAuthority::new).toList();

        log.info("Login exitoso: usuario={}, origen={}", limpiar(name), ip);

        // La contraseña no se conserva en la sesión.
        return UsernamePasswordAuthenticationToken.authenticated(respuesta.username(), null, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private static String origen(Authentication authentication) {
        return authentication.getDetails() instanceof WebAuthenticationDetails detalles
                ? detalles.getRemoteAddress() : "desconocido";
    }

    /** Evita que un nombre de usuario con saltos de línea falsifique entradas del log. */
    private static String limpiar(String texto) {
        String limpio = texto == null ? "" : texto.replaceAll("[\\r\\n\\t]", "_");
        return limpio.length() > 50 ? limpio.substring(0, 50) : limpio;
    }
}
