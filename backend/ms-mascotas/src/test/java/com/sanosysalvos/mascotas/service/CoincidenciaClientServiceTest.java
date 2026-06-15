package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.model.Mascota;
import com.sanosysalvos.mascotas.repository.MascotaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoincidenciaClientServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private MascotaRepository mascotaRepository;

    @InjectMocks
    private CoincidenciaClientService service;

    private Mascota mascotaPerdida;
    private Mascota mascotaEncontrada;

    @BeforeEach
    void setUp() {
        mascotaPerdida = new Mascota();
        mascotaPerdida.setId("1");
        mascotaPerdida.setNombre("Firulais");
        mascotaPerdida.setRaza("Labrador");
        mascotaPerdida.setColor("Amarillo");
        mascotaPerdida.setTamano("Grande");
        mascotaPerdida.setEstado("perdida");

        mascotaEncontrada = new Mascota();
        mascotaEncontrada.setId("2");
        mascotaEncontrada.setNombre("Max");
        mascotaEncontrada.setRaza("Labrador");
        mascotaEncontrada.setColor("Amarillo");
        mascotaEncontrada.setTamano("Grande");
        mascotaEncontrada.setEstado("encontrada");
    }

    @Test
    void buscarYRegistrarCoincidencias_conCoincidenciaTotal_deberiaRegistrar100Porciento() {
        when(mascotaRepository.findByEstado("encontrada")).thenReturn(List.of(mascotaEncontrada));
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(Map.of());

        service.buscarYRegistrarCoincidencias(mascotaPerdida);

        verify(restTemplate, times(1)).postForObject(anyString(), any(), eq(Map.class));
    }

    @Test
    void buscarYRegistrarCoincidencias_sinSimilitud_noDeberiaRegistrar() {
        Mascota mascotaDistinta = new Mascota();
        mascotaDistinta.setId("3");
        mascotaDistinta.setNombre("Rocky");
        mascotaDistinta.setRaza("Chihuahua");
        mascotaDistinta.setColor("Negro");
        mascotaDistinta.setTamano("Pequeño");
        mascotaDistinta.setEstado("encontrada");

        when(mascotaRepository.findByEstado("encontrada")).thenReturn(List.of(mascotaDistinta));

        service.buscarYRegistrarCoincidencias(mascotaPerdida);

        verify(restTemplate, never()).postForObject(anyString(), any(), eq(Map.class));
    }

    @Test
    void buscarYRegistrarCoincidencias_buscaEstadoOpuestoCorrectamente() {
        when(mascotaRepository.findByEstado("perdida")).thenReturn(List.of(mascotaPerdida));
        when(restTemplate.postForObject(anyString(), any(), eq(Map.class))).thenReturn(Map.of());

        service.buscarYRegistrarCoincidencias(mascotaEncontrada);

        verify(mascotaRepository, times(1)).findByEstado("perdida");
    }

    @Test
    void buscarYRegistrarCoincidencias_listaVacia_noDeberiaRegistrarNada() {
        when(mascotaRepository.findByEstado("encontrada")).thenReturn(List.of());

        service.buscarYRegistrarCoincidencias(mascotaPerdida);

        verify(restTemplate, never()).postForObject(anyString(), any(), eq(Map.class));
    }
}