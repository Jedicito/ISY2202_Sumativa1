package com.duoc.seguridadcalidad;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final CustomAuthenticationProvider authProvider;

    public WebSecurityConfig(CustomAuthenticationProvider authProvider) {
        this.authProvider = authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Después del login solo se vuelve a páginas de detalle de eventos;
        // cualquier otra petición pendiente (ej. scripts del navegador) lleva al inicio.
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setRequestMatcher(new AntPathRequestMatcher("/eventos/**", "GET"));

        http
            .authenticationManager(new ProviderManager(authProvider))
            .authorizeHttpRequests((requests) -> requests
                // Páginas públicas: inicio y búsqueda
                .requestMatchers("/", "/home", "/buscar").permitAll()
                // Recursos estáticos (estilos e imágenes)
                .requestMatchers("/css/**", "/img/**").permitAll()
                .requestMatchers("/error").permitAll()
                // Todo lo demás es privado (ej. detalle de evento)
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .failureHandler(this::loginFallido)
                .permitAll()
            )
            .logout((logout) -> logout.permitAll())
            .requestCache((cache) -> cache.requestCache(requestCache))
            .headers((headers) -> headers.contentSecurityPolicy((csp) -> csp.policyDirectives(
                "default-src 'self'; form-action 'self'; frame-ancestors 'none'; base-uri 'self'; object-src 'none'")));

        return http.build();
    }

    /** Login fallido: vuelve al formulario con el motivo, sin mostrar errores técnicos. */
    private void loginFallido(HttpServletRequest request, HttpServletResponse response,
                              AuthenticationException exception) throws IOException {
        String motivo = exception instanceof AuthenticationServiceException ? "servicio" : "error";
        response.sendRedirect(request.getContextPath() + "/login?" + motivo);
    }
}
