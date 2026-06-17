# ms-coincidencias

Microservicio responsable de registrar y gestionar las coincidencias detectadas entre mascotas perdidas y encontradas, junto con su porcentaje de similitud.

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
spring.application.name=ms-coincidencias
server.port=8083
spring.data.mongodb.uri=mongodb+srv://<usuario>:<password>@sanosysalvos.jabpcpa.mongodb.net/coincidencias_db?appName=sanosysalvos
```

## Instalación y ejecución

Desde la raíz del proyecto:

```bash
cd backend/ms-coincidencias
../../mvnw spring-boot:run
```

El servicio queda disponible en `http://localhost:8083`.

**Verificación de conexión:** en el log de inicio debe aparecer una línea con `srvHost=sanosysalvos.jabpcpa.mongodb.net`, confirmando que se conectó a MongoDB Atlas.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | /coincidencias | Listar todas las coincidencias |
| GET | /coincidencias/{id} | Obtener una coincidencia por ID |
| POST | /coincidencias | Crear una coincidencia (asigna fechaCoincidencia y fechaActualizacion automáticamente) |
| PUT | /coincidencias/{id} | Actualizar una coincidencia (por ejemplo, confirmarla) |

### Ejemplo de petición — Crear coincidencia

```bash
POST http://localhost:8083/coincidencias
Content-Type: application/json

{
    "mascotaPerdidaId": "69dee0a2b8341ab8b85c2fce",
    "mascotaEncontradaId": "69faa9714b521e33efcbc969",
    "porcentajeSimilitud": 100.0,
    "estado": "pendiente",
    "observaciones": "Misma raza. Mismo color. Mismo tamaño."
}
```

**Respuesta esperada (200 OK):**
```json
{
    "id": "69e6fab4345b6498722ea7f6",
    "mascotaPerdidaId": "69dee0a2b8341ab8b85c2fce",
    "mascotaEncontradaId": "69faa9714b521e33efcbc969",
    "porcentajeSimilitud": 100.0,
    "fechaCoincidencia": "2026-06-15T21:00:00.748",
    "estado": "pendiente",
    "observaciones": "Misma raza. Mismo color. Mismo tamaño.",
    "fechaActualizacion": "2026-06-15T21:00:00.748"
}
```

## Integración con otros componentes

Este microservicio recibe peticiones automáticas desde **ms-mascotas**: cada vez que se crea una mascota nueva, `ms-mascotas` calcula la similitud (raza, color, tamaño) contra las mascotas del estado opuesto y, si supera el 30%, envía un `POST /coincidencias` con el resultado. También puede usarse manualmente a través de Postman para pruebas.

## Cómo probar el microservicio

**Opción 1 — Postman:** importar la colección `Sanos_y_Salvos.postman_collection.json` (carpeta "Coincidencias"), y ejecutar las peticiones contra `http://localhost:8083` (directo) o `http://localhost:8080` (vía API Gateway).

**Opción 2 — Navegador:** abrir `http://localhost:8083/coincidencias` para verificar el listado (método GET).

**Opción 3 — Flujo completo:** crear dos mascotas con datos similares (misma raza, color y tamaño) en `ms-mascotas`, una con estado "perdida" y otra "encontrada", y verificar que la coincidencia se registre automáticamente en este microservicio.

## Pruebas unitarias

- `CoincidenciaServiceTest` — 6 pruebas (crear, listar, obtener por ID, actualizar)
- `CoincidenciaControllerTest` — 6 pruebas (endpoints HTTP, casos 200 y 404)

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
ms-coincidencias/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/sanosysalvos/coincidencias/
    │   │   ├── controller/CoincidenciaController.java
    │   │   ├── service/CoincidenciaService.java
    │   │   ├── repository/CoincidenciaRepository.java
    │   │   ├── model/Coincidencia.java
    │   │   └── CoincidenciasApplication.java
    │   └── resources/application.properties
    └── test/java/com/sanosysalvos/coincidencias/
        ├── service/CoincidenciaServiceTest.java
        └── controller/CoincidenciaControllerTest.java
```
