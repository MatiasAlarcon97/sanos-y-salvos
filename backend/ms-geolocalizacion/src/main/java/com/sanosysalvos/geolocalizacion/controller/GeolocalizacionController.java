package com.sanosysalvos.geolocalizacion.controller;

import com.sanosysalvos.geolocalizacion.model.Geolocalizacion;
import com.sanosysalvos.geolocalizacion.service.GeolocalizacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/geoloc")
@RequiredArgsConstructor
public class GeolocalizacionController {

    private final GeolocalizacionService service;

    @PostMapping
    public ResponseEntity<Geolocalizacion> registrar(@RequestBody Geolocalizacion geo) {
        return ResponseEntity.ok(service.registrar(geo));
    }

    @GetMapping
    public ResponseEntity<List<Geolocalizacion>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{reporteId}")
    public ResponseEntity<Geolocalizacion> obtener(@PathVariable String reporteId) {
        return service.obtenerPorReporteId(reporteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{reporteId}")
    public ResponseEntity<Geolocalizacion> actualizar(@PathVariable String reporteId, @RequestBody Geolocalizacion geo) {
        return service.actualizar(reporteId, geo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}