package com.sanosysalvos.geolocalizacion.service;

import com.sanosysalvos.geolocalizacion.model.Geolocalizacion;
import com.sanosysalvos.geolocalizacion.repository.GeolocalizacionRepository;
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
class GeolocalizacionServiceTest {

    @Mock
    private GeolocalizacionRepository repository;

    @InjectMocks
    private GeolocalizacionService service;

    private Geolocalizacion geolocalizacion;

    @BeforeEach
    void setUp() {
        geolocalizacion = new Geolocalizacion();
        geolocalizacion.setId("1");
        geolocalizacion.setReporteId("reporte-123");
        geolocalizacion.setLatitud(-33.4489);
        geolocalizacion.setLongitud(-70.6693);
        geolocalizacion.setDescripcion("Visto cerca del parque");
    }

    @Test
    void registrar_deberiaAsignarTimestampYEstadoActivo() {
        when(repository.save(any(Geolocalizacion.class))).thenReturn(geolocalizacion);

        Geolocalizacion resultado = service.registrar(geolocalizacion);

        assertNotNull(resultado);
        assertNotNull(geolocalizacion.getTimestamp());
        assertEquals("activo", geolocalizacion.getEstado());
        verify(repository, times(1)).save(geolocalizacion);
    }

    @Test
    void listar_deberiaRetornarTodasLasGeolocalizaciones() {
        when(repository.findAll()).thenReturn(List.of(geolocalizacion));

        List<Geolocalizacion> resultado = service.listar();

        assertEquals(1, resultado.size());
        verify(repository, times(1)).findAll();
    }

    @Test
    void obtenerPorReporteId_cuandoExiste_deberiaRetornarGeolocalizacion() {
        when(repository.findByReporteId("reporte-123")).thenReturn(Optional.of(geolocalizacion));

        Optional<Geolocalizacion> resultado = service.obtenerPorReporteId("reporte-123");

        assertTrue(resultado.isPresent());
        assertEquals("reporte-123", resultado.get().getReporteId());
    }

    @Test
    void obtenerPorReporteId_cuandoNoExiste_deberiaRetornarVacio() {
        when(repository.findByReporteId("inexistente")).thenReturn(Optional.empty());

        Optional<Geolocalizacion> resultado = service.obtenerPorReporteId("inexistente");

        assertFalse(resultado.isPresent());
    }

    @Test
    void actualizar_cuandoExiste_deberiaActualizarDatos() {
        Geolocalizacion datosNuevos = new Geolocalizacion();
        datosNuevos.setLatitud(-33.5);
        datosNuevos.setLongitud(-70.7);
        datosNuevos.setDescripcion("Nueva ubicación");
        datosNuevos.setEstado("inactivo");

        when(repository.findByReporteId("reporte-123")).thenReturn(Optional.of(geolocalizacion));
        when(repository.save(any(Geolocalizacion.class))).thenReturn(geolocalizacion);

        Optional<Geolocalizacion> resultado = service.actualizar("reporte-123", datosNuevos);

        assertTrue(resultado.isPresent());
        assertEquals(-33.5, resultado.get().getLatitud());
        assertEquals("inactivo", resultado.get().getEstado());
        verify(repository, times(1)).save(geolocalizacion);
    }

    @Test
    void actualizar_cuandoNoExiste_deberiaRetornarVacio() {
        Geolocalizacion datosNuevos = new Geolocalizacion();
        when(repository.findByReporteId("inexistente")).thenReturn(Optional.empty());

        Optional<Geolocalizacion> resultado = service.actualizar("inexistente", datosNuevos);

        assertFalse(resultado.isPresent());
        verify(repository, never()).save(any());
    }
}