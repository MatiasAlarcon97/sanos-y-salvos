package com.sanosysalvos.mascotas.service;

import com.sanosysalvos.mascotas.model.Mascota;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.sanosysalvos.mascotas.repository.MascotaRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoincidenciaClientService {

    private final RestTemplate restTemplate;
    private final MascotaRepository mascotaRepository;

    private static final String COINCIDENCIAS_URL = "http://localhost:8083/coincidencias";
    private static final String MASCOTAS_URL = "http://localhost:8081/mascotas";

    public void buscarYRegistrarCoincidencias(Mascota nuevaMascota) {
        String estadoOpuesto = nuevaMascota.getEstado().equals("perdida") ? "encontrada" : "perdida";

        List<Mascota> mascotasOpuestas = mascotaRepository.findByEstado(estadoOpuesto);

        for (Mascota mascotaOpuesta : mascotasOpuestas) {
            double similitud = calcularSimilitud(nuevaMascota, mascotaOpuesta);

            if (similitud >= 30) {
                String mascotaPerdidaId = nuevaMascota.getEstado().equals("perdida")
                        ? nuevaMascota.getId()
                        : mascotaOpuesta.getId();

                String mascotaEncontradaId = nuevaMascota.getEstado().equals("encontrada")
                        ? nuevaMascota.getId()
                        : mascotaOpuesta.getId();

                Map<String, Object> coincidencia = new HashMap<>();
                coincidencia.put("mascotaPerdidaId", mascotaPerdidaId);
                coincidencia.put("mascotaEncontradaId", mascotaEncontradaId);
                coincidencia.put("porcentajeSimilitud", similitud);
                coincidencia.put("estado", "pendiente");
                coincidencia.put("observaciones", generarObservaciones(nuevaMascota, mascotaOpuesta));

                try {
                    restTemplate.postForObject(COINCIDENCIAS_URL, coincidencia, Map.class);
                } catch (Exception e) {
                    System.err.println("Error al registrar coincidencia: " + e.getMessage());
                }
            }
        }
    }

    private double calcularSimilitud(Mascota m1, Mascota m2) {
        int puntos = 0;
        int total = 3;

        if (m1.getRaza() != null && m2.getRaza() != null &&
            m1.getRaza().equalsIgnoreCase(m2.getRaza())) {
            puntos++;
        }
        if (m1.getColor() != null && m2.getColor() != null &&
            m1.getColor().equalsIgnoreCase(m2.getColor())) {
            puntos++;
        }
        if (m1.getTamano() != null && m2.getTamano() != null &&
            m1.getTamano().equalsIgnoreCase(m2.getTamano())) {
            puntos++;
        }

        return (puntos * 100.0) / total;
    }

    private String generarObservaciones(Mascota m1, Mascota m2) {
        StringBuilder obs = new StringBuilder();

        if (m1.getRaza() != null && m1.getRaza().equalsIgnoreCase(m2.getRaza())) {
            obs.append("Misma raza. ");
        }
        if (m1.getColor() != null && m1.getColor().equalsIgnoreCase(m2.getColor())) {
            obs.append("Mismo color. ");
        }
        if (m1.getTamano() != null && m1.getTamano().equalsIgnoreCase(m2.getTamano())) {
            obs.append("Mismo tamaño. ");
        }

        return obs.toString().trim();
    }
}