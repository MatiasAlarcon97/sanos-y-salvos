# ms-geolocalizacion

Microservicio responsable de registrar y consultar las coordenadas geográficas (latitud/longitud) asociadas a un reporte de mascota.

## Tecnologías

- Java 21
- Spring Boot 3.5
- Spring Data MongoDB
- Lombok
- JUnit 5 + Mockito (pruebas unitarias)
- JaCoCo (cobertura de pruebas)

## Requisitos previos

- **JDK 21** (no JRE) — ver instrucciones detalladas de instalación y configuración en el README principal del proyecto.
- Conexión a internet (la base de datos es MongoDB Atlas, en la nube).
- Verificar instalación de Java: `java -version` debe mostrar `openjdk version "21...`.

## Configuración

El archivo `src/main/resources/application.properties` define:

```properties
spring.application.name=ms-geolocalizacion
server.port=8082
spring.data.mongodb.uri=mongodb+srv://<usuario>:<password>@sanosysalvos.jabpcpa.mongodb.net/geolocalizacion_db?appName=sanosysalvos
```

## Instalación y ejecución

Desde la raíz del proyecto:

```bash
cd backend/ms-geolocalizacion
../../mvnw spring-boot:run
```

El servicio queda disponible en `http://localhost:8082`.

**Verificación de conexión:** en el log de inicio debe aparecer una línea con `srvHost=sanosysalvos.jabpcpa.mongodb.net`, confirmando que se conectó a MongoDB Atlas.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | /geoloc | Listar todas las geolocalizaciones |
| GET | /geoloc/{reporteId} | Obtener la geolocalización de un reporte específico |
| POST | /geoloc | Registrar una geolocalización (asigna timestamp y estado "activo" automáticamente) |
| PUT | /geoloc/{reporteId} | Actualizar una geolocalización existente |

### Ejemplo de petición — Registrar geolocalización

```bash
POST http://localhost:8082/geoloc
Content-Type: application/json

{
    "reporteId": "69dee0a2b8341ab8b85c2fce",
    "latitud": -33.4489,
    "longitud": -70.6693,
    "descripcion": "Visto cerca del parque"
}
```

**Respuesta esperada (200 OK):**
```json
{
    "id": "6a01a1b2c3d4e5f6a7b8c9d0",
    "reporteId": "69dee0a2b8341ab8b85c2fce",
    "latitud": -33.4489,
    "longitud": -70.6693,
    "timestamp": "2026-06-15T20:55:12.123",
    "descripcion": "Visto cerca del parque",
    "estado": "activo"
}
```

## Integración con otros componentes

Este microservicio es invocado automáticamente por el **frontend** inmediatamente después de crear una mascota: una vez que `ms-mascotas` retorna el `id` del nuevo reporte, el frontend usa ese mismo valor como `reporteId` para registrar la ubicación marcada en el mapa interactivo.

## Cómo probar el microservicio

**Opción 1 — Postman:** importar la colección `Sanos_y_Salvos.postman_collection.json` (carpeta "Geolocalización"), y ejecutar las peticiones contra `http://localhost:8082` (directo) o `http://localhost:8080` (vía API Gateway).

**Opción 2 — Navegador:** abrir `http://localhost:8082/geoloc` para verificar el listado (método GET).

## Pruebas unitarias

- `GeolocalizacionServiceTest` — 6 pruebas (registrar, listar, obtener por reporteId, actualizar)
- `GeolocalizacionControllerTest` — 6 pruebas (endpoints HTTP, casos 200 y 404)

**Ejecutar las pruebas:**
```bash
../../mvnw test
```

**Generar y ver el reporte de cobertura (JaCoCo):**
```bash
start target/site/jacoco/index.html
```

Cobertura actual: **94%** de instrucciones cubiertas (ver detalle en `pruebas_unitarias.pdf`).

## Estructura del proyecto

```
ms-geolocalizacion/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/sanosysalvos/geolocalizacion/
    │   │   ├── controller/GeolocalizacionController.java
    │   │   ├── service/GeolocalizacionService.java
    │   │   ├── repository/GeolocalizacionRepository.java
    │   │   ├── model/Geolocalizacion.java
    │   │   └── GeolocalizacionApplication.java
    │   └── resources/application.properties
    └── test/java/com/sanosysalvos/geolocalizacion/
        ├── service/GeolocalizacionServiceTest.java
        └── controller/GeolocalizacionControllerTest.java
```
