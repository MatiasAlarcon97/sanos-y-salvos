package com.sanosysalvos.mascotas.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Data
@Document(collection = "mascotas")
public class Mascota {
    @Id
    private String id;
    private String nombre;
    private String raza;
    private String color;
    private String tamano;
    private String descripcion;
    private LocalDateTime fechaReporte;
    private String estado;
    private String foto;
    private String contacto;
}