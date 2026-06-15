package com.sanosysalvos.mascotas.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanosysalvos.mascotas.model.Mascota;
import com.sanosysalvos.mascotas.service.MascotaService;
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

@WebMvcTest(MascotaController.class)
class MascotaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MascotaService service;

    @Autowired
    private ObjectMapper objectMapper;

    private Mascota mascota;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        mascota = new Mascota();
        mascota.setId("1");
        mascota.setNombre("Firulais");
        mascota.setRaza("Labrador");
        mascota.setColor("Amarillo");
        mascota.setTamano("Grande");
        mascota.setEstado("perdida");
        mascota.setContacto("912345678");
    }

    @Test
    void crear_deberiaRetornar200() throws Exception {
        when(service.crear(any(Mascota.class))).thenReturn(mascota);

        mockMvc.perform(post("/mascotas")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mascota)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Firulais"));
    }

    @Test
    void listar_deberiaRetornar200ConLista() throws Exception {
        when(service.listar()).thenReturn(List.of(mascota));

        mockMvc.perform(get("/mascotas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Firulais"));
    }

    @Test
    void obtener_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.obtenerPorId("1")).thenReturn(Optional.of(mascota));

        mockMvc.perform(get("/mascotas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Firulais"));
    }

    @Test
    void obtener_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(service.obtenerPorId("99")).thenReturn(Optional.empty());

        mockMvc.perform(get("/mascotas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizar_cuandoExiste_deberiaRetornar200() throws Exception {
        when(service.actualizar(eq("1"), any(Mascota.class))).thenReturn(Optional.of(mascota));

        mockMvc.perform(put("/mascotas/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mascota)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Firulais"));
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornar404() throws Exception {
        when(service.actualizar(eq("99"), any(Mascota.class))).thenReturn(Optional.empty());

        mockMvc.perform(put("/mascotas/99")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mascota)))
                .andExpect(status().isNotFound());
    }
}