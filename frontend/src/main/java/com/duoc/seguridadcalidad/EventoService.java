package com.duoc.seguridadcalidad;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

/** Llamadas a la capa backend. */
@Service
public class EventoService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final String backendUrl;
    private final TokenStore tokenStore;

    public EventoService(@Value("${backend.url}") String backendUrl, TokenStore tokenStore) {
        this.backendUrl = backendUrl;
        this.tokenStore = tokenStore;
    }

    /** API pública: lista o busca eventos. Los filtros vacíos se omiten. */
    public List<EventoResumen> buscar(String nombre, String categoria, String juego,
                                      String ciudad, LocalDate fecha) {
        UriComponentsBuilder uri = UriComponentsBuilder.fromUriString(backendUrl).path("/eventos");
        agregar(uri, "nombre", nombre);
        agregar(uri, "categoria", categoria);
        agregar(uri, "juego", juego);
        agregar(uri, "ciudad", ciudad);
        if (fecha != null) {
            uri.queryParam("fecha", fecha.toString());
        }
        EventoResumen[] eventos = restTemplate.getForObject(uri.encode().build().toUri(), EventoResumen[].class);
        return eventos == null ? List.of() : Arrays.asList(eventos);
    }

    /** API privada: detalle de un evento, enviando el token JWT del login. */
    public EventoDetalle detalle(Long id) {
        String url = backendUrl + "/eventos/" + id;
        String token = tokenStore.getToken();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Void> entity = new HttpEntity<Void>(headers);

        ResponseEntity<EventoDetalle> response =
                restTemplate.exchange(url, HttpMethod.GET, entity, EventoDetalle.class);
        return response.getBody();
    }

    private static void agregar(UriComponentsBuilder uri, String nombre, String valor) {
        if (StringUtils.hasText(valor)) {
            uri.queryParam(nombre, valor.trim());
        }
    }
}
