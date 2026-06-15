package com.sanosysalvos.coincidencias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.coincidencias.model.Coincidencia;
import com.sanosysalvos.coincidencias.service.CoincidenciaService;
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

@WebMvcTest(CoincidenciaController.class)
class CoincidenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoincidenciaService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Coincidencia coincidencia;

    @BeforeEach
    void setUp() {
        coincidencia = new Coincidencia();
        coincidencia.setId("1");
        coincidencia.setMascotaPerdidaId("mascota-1");
        coincidencia.setMascotaEncontradaId("mascota-2");
        coincidencia.setPorcentajeSimilitud(85.0);
        coincidencia.setEstado("pendiente");
        coincidencia.setObservaciones("Misma raza y color");
    }

    @Test
    void crear_deberiaRetornar200() throws Exception {
        when(service.crear(any(Coincidencia.class))).thenReturn(coincidencia);

        mockMvc.perform(post("/coincidencias")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(coincidencia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mascotaPerdidaId").value("mascota-1"));
    }

    @Test
    void listar_deberiaRetornar200ConLista() throws Exception {
        when(service.listar()).thenReturn(List.of(coincidencia));

        mockMvc.perform(get("/coincidencias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].mascotaPerdidaId").value("mascota-1"));
    }

    @Test
    void obtener_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.obtenerPorId("1")).thenReturn(Optional.of(coincidencia));

        mockMvc.perform(get("/coincidencias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mascotaPerdidaId").value("mascota-1"));
    }

    @Test
    void obtener_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(service.obtenerPorId("99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/coincidencias/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.actualizar(eq("1"), any(Coincidencia.class))).thenReturn(Optional.of(coincidencia));

        mockMvc.perform(put("/coincidencias/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(coincidencia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mascotaPerdidaId").value("mascota-1"));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(service.actualizar(eq("99"), any(Coincidencia.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/coincidencias/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(coincidencia)))
                .andExpect(status().isNotFound());
    }
}