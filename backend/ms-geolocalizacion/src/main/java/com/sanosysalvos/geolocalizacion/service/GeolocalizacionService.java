package com.sanosysalvos.geolocalizacion.service;

import com.sanosysalvos.geolocalizacion.model.Geolocalizacion;
import com.sanosysalvos.geolocalizacion.repository.GeolocalizacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GeolocalizacionService {

    private final GeolocalizacionRepository repository;

    public Geolocalizacion registrar(Geolocalizacion geo) {
        geo.setTimestamp(LocalDateTime.now());
        geo.setEstado("activo");
        return repository.save(geo);
    }

    public List<Geolocalizacion> listar() {
        return repository.findAll();
    }

    public Optional<Geolocalizacion> obtenerPorReporteId(String reporteId) {
        return repository.findByReporteId(reporteId);
    }

    public Optional<Geolocalizacion> actualizar(String reporteId, Geolocalizacion datos) {
        return repository.findByReporteId(reporteId).map(g -> {
            g.setLatitud(datos.getLatitud());
            g.setLongitud(datos.getLongitud());
            g.setDescripcion(datos.getDescripcion());
            g.setEstado(datos.getEstado());
            return repository.save(g);
        });
    }
}