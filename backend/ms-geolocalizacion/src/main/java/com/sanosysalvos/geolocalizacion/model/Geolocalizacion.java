package com.sanosysalvos.geolocalizacion.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "geolocalizaciones")
public class Geolocalizacion {
    @Id
    private String id;
    private String reporteId;
    private Double latitud;
    private Double longitud;
    private LocalDateTime timestamp;
    private String descripcion;
    private String estado;
}