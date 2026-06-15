package com.sanosysalvos.coincidencias.service;

import com.sanosysalvos.coincidencias.model.Coincidencia;
import com.sanosysalvos.coincidencias.repository.CoincidenciaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoincidenciaServiceTest {

    @Mock
    private CoincidenciaRepository repository;

    @InjectMocks
    private CoincidenciaService service;

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
    void crear_deberiaAsignarFechasYGuardar() {
        when(repository.save(any(Coincidencia.class))).thenReturn(coincidencia);

        Coincidencia resultado = service.crear(coincidencia);

        assertNotNull(resultado);
        assertNotNull(coincidencia.getFechaCoincidencia());
        assertNotNull(coincidencia.getFechaActualizacion());
        verify(repository, times(1)).save(coincidencia);
    }

    @Test
    void listar_deberiaRetornarTodasLasCoincidencias() {
        when(repository.findAll()).thenReturn(List.of(coincidencia));

        List<Coincidencia> resultado = service.listar();

        assertEquals(1, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarCoincidencia() {
        when(repository.findById("1")).thenReturn(Optional.of(coincidencia));

        Optional<Coincidencia> resultado = service.obtenerPorId("1");

        assertTrue(resultado.isPresent());
        assertEquals("mascota-1", resultado.get().getMascotaPerdidaId());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarVacio() {
        when(repository.findById("99")).thenReturn(Optional.empty());

        Optional<Coincidencia> resultado = service.obtenerPorId("99");

        assertFalse(resultado.isPresent());
    }

    @Test
    void actualizar_cuandoExiste_deberiaActualizarDatos() {
        Coincidencia datosNuevos = new Coincidencia();
        datosNuevos.setPorcentajeSimilitud(95.0);
        datosNuevos.setEstado("confirmada");
        datosNuevos.setObservaciones("Confirmado por el usuario");

        when(repository.findById("1")).thenReturn(Optional.of(coincidencia));
        when(repository.save(any(Coincidencia.class))).thenReturn(coincidencia);

        Optional<Coincidencia> resultado = service.actualizar("1", datosNuevos);

        assertTrue(resultado.isPresent());
        assertEquals("confirmada", resultado.get().getEstado());
        assertEquals(95.0, resultado.get().getPorcentajeSimilitud());
        verify(repository, times(1)).save(coincidencia);
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarVacio() {
        Coincidencia datosNuevos = new Coincidencia();
        when(repository.findById("99")).thenReturn(Optional.empty());

        Optional<Coincidencia> resultado = service.actualizar("99", datosNuevos);

        assertFalse(resultado.isPresent());
        verify(repository, never()).save(any());
    }
}