package com.sanosysalvos.geolocalizacion.repository;

import com.sanosysalvos.geolocalizacion.model.Geolocalizacion;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface GeolocalizacionRepository extends MongoRepository<Geolocalizacion, String> {
    Optional<Geolocalizacion> findByReporteId(String reporteId);
}