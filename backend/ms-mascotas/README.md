# ms-mascotas

Microservicio responsable de la gestión de mascotas perdidas y encontradas. Permite crear, listar, consultar y actualizar reportes, y dispara automáticamente la búsqueda de coincidencias al crear una nueva mascota.

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
spring.application.name=ms-mascotas
server.port=8081
spring.data.mongodb.uri=mongodb+srv://<usuario>:<password>@sanosysalvos.jabpcpa.mongodb.net/mascotas_db?appName=sanosysalvos
```

## Instalación y ejecución

Desde la raíz del proyecto:

```bash
cd backend/ms-mascotas
../../mvnw spring-boot:run
```

El servicio queda disponible en `http://localhost:8081`.

**Verificación de conexión:** en el log de inicio debe aparecer una línea con `srvHost=sanosysalvos.jabpcpa.mongodb.net`, confirmando que se conectó a MongoDB Atlas (no a una base local).

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| GET | /mascotas | Listar todas las mascotas |
| GET | /mascotas/{id} | Obtener una mascota por ID |
| POST | /mascotas | Crear una mascota (asigna fechaReporte y busca coincidencias automáticamente) |
| PUT | /mascotas/{id} | Actualizar una mascota |

### Ejemplo de petición — Crear mascota

```bash
POST http://localhost:8081/mascotas
Content-Type: application/json

{
    "nombre": "Firulais",
    "raza": "Labrador",
    "color": "Amarillo",
    "tamano": "Grande",
    "descripcion": "Perro amigable con collar azul",
    "estado": "perdida",
    "contacto": "912345678"
}
```

**Respuesta esperada (200 OK):**
```json
{
    "id": "69dee0a2b8341ab8b85c2fce",
    "nombre": "Firulais",
    "raza": "Labrador",
    "color": "Amarillo",
    "tamano": "Grande",
    "descripcion": "Perro amigable con collar azul",
    "fechaReporte": "2026-06-15T20:49:38.685",
    "estado": "perdida",
    "foto": null,
    "contacto": "912345678"
}
```

## Cómo probar el microservicio

**Opción 1 — Postman:** importar la colección `Sanos_y_Salvos.postman_collection.json` (carpeta "Mascotas") incluida en la documentación del proyecto, y ejecutar las peticiones contra `http://localhost:8081` (directo) o `http://localhost:8080` (vía API Gateway).

**Opción 2 — Navegador:** abrir `http://localhost:8081/mascotas` para verificar el listado (método GET).

## Pruebas unitarias

El proyecto incluye pruebas unitarias para las capas `Service` y `Controller`:

- `MascotaServiceTest` — 6 pruebas (crear, listar, obtener por ID, actualizar)
- `CoincidenciaClientServiceTest` — 4 pruebas (lógica de cálculo de similitud)
- `MascotaControllerTest` — 6 pruebas (endpoints HTTP, casos 200 y 404)

**Ejecutar las pruebas:**
```bash
../../mvnw test
```

**Generar y ver el reporte de cobertura (JaCoCo):**
```bash
start target/site/jacoco/index.html
```

Cobertura actual: **96%** de instrucciones cubiertas (ver detalle en `pruebas_unitarias.pdf`).

## Estructura del proyecto

```
ms-mascotas/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/sanosysalvos/mascotas/
    │   │   ├── controller/MascotaController.java
    │   │   ├── service/MascotaService.java
    │   │   ├── service/CoincidenciaClientService.java
    │   │   ├── repository/MascotaRepository.java
    │   │   ├── model/Mascota.java
    │   │   └── MascotasApplication.java
    │   └── resources/application.properties
    └── test/java/com/sanosysalvos/mascotas/
        ├── service/MascotaServiceTest.java
        ├── service/CoincidenciaClientServiceTest.java
        └── controller/MascotaControllerTest.java
```
