package com.sanosysalvos.mascotas.repository;

import com.sanosysalvos.mascotas.model.Mascota;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MascotaRepository extends MongoRepository<Mascota, String> {
    List<Mascota> findByEstado(String estado);
    List<Mascota> findByRaza(String raza);
}