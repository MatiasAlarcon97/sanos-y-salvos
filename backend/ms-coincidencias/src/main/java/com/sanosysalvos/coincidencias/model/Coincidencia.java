package com.sanosysalvos.coincidencias.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "coincidencias")
public class Coincidencia {
    @Id
    private String id;
    private String mascotaPerdidaId;
    private String mascotaEncontradaId;
    private Double porcentajeSimilitud;
    private LocalDateTime fechaCoincidencia;
    private String estado;
    private String observaciones;
    private LocalDateTime fechaActualizacion;
}