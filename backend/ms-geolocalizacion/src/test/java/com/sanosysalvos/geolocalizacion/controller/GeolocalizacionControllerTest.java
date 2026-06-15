package com.sanosysalvos.geolocalizacion.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.geolocalizacion.model.Geolocalizacion;
import com.sanosysalvos.geolocalizacion.service.GeolocalizacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GeolocalizacionController.class)
class GeolocalizacionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeolocalizacionService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Geolocalizacion geolocalizacion;

    @BeforeEach
    void setUp() {
        geolocalizacion = new Geolocalizacion();
        geolocalizacion.setId("1");
        geolocalizacion.setReporteId("reporte-123");
        geolocalizacion.setLatitud(-33.4489);
        geolocalizacion.setLongitud(-70.6693);
        geolocalizacion.setDescripcion("Visto cerca del parque");
        geolocalizacion.setEstado("activo");
    }

    @Test
    void registrar_deberiaRetornar200() throws Exception {
        when(service.registrar(any(Geolocalizacion.class))).thenReturn(geolocalizacion);

        mockMvc.perform(post("/geoloc")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(geolocalizacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reporteId").value("reporte-123"));
    }

    @Test
    void listar_deberiaRetornar200ConLista() throws Exception {
        when(service.listar()).thenReturn(List.of(geolocalizacion));

        mockMvc.perform(get("/geoloc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].reporteId").value("reporte-123"));
    }

    @Test
    void obtener_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.obtenerPorReporteId("reporte-123")).thenReturn(Optional.of(geolocalizacion));

        mockMvc.perform(get("/geoloc/reporte-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reporteId").value("reporte-123"));
    }

    @Test
    void obtener_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(service.obtenerPorReporteId("inexistente")).thenReturn(Optional.empty());

        mockMvc.perform(get("/geoloc/inexistente"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.actualizar(eq("reporte-123"), any(Geolocalizacion.class))).thenReturn(Optional.of(geolocalizacion));

        mockMvc.perform(put("/geoloc/reporte-123")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(geolocalizacion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reporteId").value("reporte-123"));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(service.actualizar(eq("inexistente"), any(Geolocalizacion.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/geoloc/inexistente")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(geolocalizacion)))
                .andExpect(status().isNotFound());
    }
}