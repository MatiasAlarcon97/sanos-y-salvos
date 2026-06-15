package com.sanosysalvos.coincidencias.repository;

import com.sanosysalvos.coincidencias.model.Coincidencia;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CoincidenciaRepository extends MongoRepository<Coincidencia, String> {
    List<Coincidencia> findByEstado(String estado);
    List<Coincidencia> findByMascotaPerdidaId(String mascotaPerdidaId);
}