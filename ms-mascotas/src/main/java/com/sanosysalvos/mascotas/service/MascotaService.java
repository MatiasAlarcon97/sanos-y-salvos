package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.model.Mascota;
import com.sanosysalvos.mascotas.repository.MascotaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MascotaService {

    private final MascotaRepository repository;

    public Mascota crear(Mascota mascota) {
        mascota.setFechaReporte(LocalDateTime.now());
        return repository.save(mascota);
    }

    public List<Mascota> listar() {
        return repository.findAll();
    }

    public Optional<Mascota> obtenerPorId(String id) {
        return repository.findById(id);
    }

    public Optional<Mascota> actualizar(String id, Mascota datos) {
        return repository.findById(id).map(m -> {
            m.setNombre(datos.getNombre());
            m.setRaza(datos.getRaza());
            m.setColor(datos.getColor());
            m.setTamano(datos.getTamano());
            m.setDescripcion(datos.getDescripcion());
            m.setEstado(datos.getEstado());
            m.setFoto(datos.getFoto());
            m.setContacto(datos.getContacto());
            return repository.save(m);
        });
    }
}