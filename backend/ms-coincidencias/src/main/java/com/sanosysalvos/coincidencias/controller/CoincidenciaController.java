package com.sanosysalvos.coincidencias.controller;

import com.sanosysalvos.coincidencias.model.Coincidencia;
import com.sanosysalvos.coincidencias.service.CoincidenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/coincidencias")
@RequiredArgsConstructor
public class CoincidenciaController {

    private final CoincidenciaService service;

    @PostMapping
    public ResponseEntity<Coincidencia> crear(@RequestBody Coincidencia coincidencia) {
        return ResponseEntity.ok(service.crear(coincidencia));
    }

    @GetMapping
    public ResponseEntity<List<Coincidencia>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Coincidencia> obtener(@PathVariable String id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coincidencia> actualizar(@PathVariable String id, @RequestBody Coincidencia coincidencia) {
        return service.actualizar(id, coincidencia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}