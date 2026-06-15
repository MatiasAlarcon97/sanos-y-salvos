package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.model.Mascota;
import com.sanosysalvos.mascotas.repository.MascotaRepository;
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
class MascotaServiceTest {

    @Mock
    private MascotaRepository repository;

    @Mock
    private CoincidenciaClientService coincidenciaClientService;

    @InjectMocks
    private MascotaService service;

    private Mascota mascota;

    @BeforeEach
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
    void crear_deberiaAsignarFechaReporteYGuardar() {
        when(repository.save(any(Mascota.class))).thenReturn(mascota);

        Mascota resultado = service.crear(mascota);

        assertNotNull(resultado);
        assertNotNull(mascota.getFechaReporte());
        verify(repository, times(1)).save(mascota);
        verify(coincidenciaClientService, times(1)).buscarYRegistrarCoincidencias(mascota);
    }

    @Test
    void listar_deberiaRetornarTodasLasMascotas() {
        when(repository.findAll()).thenReturn(List.of(mascota));

        List<Mascota> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals("Firulais", resultado.get(0).getNombre());
        verify(repository, times(1)).findAll();
    }

    @Test
    void obtenerPorId_cuandoExiste_deberiaRetornarMascota() {
        when(repository.findById("1")).thenReturn(Optional.of(mascota));

        Optional<Mascota> resultado = service.obtenerPorId("1");

        assertTrue(resultado.isPresent());
        assertEquals("Firulais", resultado.get().getNombre());
    }

    @Test
    void obtenerPorId_cuandoNoExiste_deberiaRetornarVacio() {
        when(repository.findById("99")).thenReturn(Optional.empty());

        Optional<Mascota> resultado = service.obtenerPorId("99");

        assertFalse(resultado.isPresent());
    }

    @Test
    void actualizar_cuandoExiste_deberiaActualizarDatos() {
        Mascota datosNuevos = new Mascota();
        datosNuevos.setNombre("Max");
        datosNuevos.setRaza("Golden Retriever");
        datosNuevos.setColor("Dorado");
        datosNuevos.setTamano("Grande");
        datosNuevos.setEstado("encontrada");
        datosNuevos.setContacto("987654321");

        when(repository.findById("1")).thenReturn(Optional.of(mascota));
        when(repository.save(any(Mascota.class))).thenReturn(mascota);

        Optional<Mascota> resultado = service.actualizar("1", datosNuevos);

        assertTrue(resultado.isPresent());
        assertEquals("Max", resultado.get().getNombre());
        assertEquals("encontrada", resultado.get().getEstado());
        verify(repository, times(1)).save(mascota);
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarVacio() {
        Mascota datosNuevos = new Mascota();
        when(repository.findById("99")).thenReturn(Optional.empty());

        Optional<Mascota> resultado = service.actualizar("99", datosNuevos);

        assertFalse(resultado.isPresent());
        verify(repository, never()).save(any());
    }
}