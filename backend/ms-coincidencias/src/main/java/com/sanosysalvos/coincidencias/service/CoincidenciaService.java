package com.sanosysalvos.coincidencias.service;

import com.sanosysalvos.coincidencias.model.Coincidencia;
import com.sanosysalvos.coincidencias.repository.CoincidenciaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CoincidenciaService {

    private final CoincidenciaRepository repository;

    public Coincidencia crear(Coincidencia coincidencia) {
        coincidencia.setFechaCoincidencia(LocalDateTime.now());
        coincidencia.setFechaActualizacion(LocalDateTime.now());
        return repository.save(coincidencia);
    }

    public List<Coincidencia> listar() {
        return repository.findAll();
    }

    public Optional<Coincidencia> obtenerPorId(String id) {
        return repository.findById(id);
    }

    public Optional<Coincidencia> actualizar(String id, Coincidencia datos) {
        return repository.findById(id).map(c -> {
            c.setPorcentajeSimilitud(datos.getPorcentajeSimilitud());
            c.setEstado(datos.getEstado());
            c.setObservaciones(datos.getObservaciones());
            c.setFechaActualizacion(LocalDateTime.now());
            return repository.save(c);
        });
    }
}