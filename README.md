# Sanos y Salvos

Plataforma de microservicios para gestión de mascotas perdidas y encontradas. Permite reportar mascotas, registrar ubicaciones y detectar coincidencias automáticamente entre mascotas perdidas y encontradas.

## Tecnologías

| Capa | Tecnología |
|---|---|
| Backend | Java 21 + Spring Boot 3.5 |
| Frontend | React 18 |
| Base de datos | MongoDB 8 |
| Gateway | Spring Cloud Gateway |

## Requisitos previos

### Java JDK 21
- Descarga: https://adoptium.net
- Selecciona JDK 21 - LTS para Windows
- Verificar instalación: `java -version`

### MongoDB
- Descarga: https://www.mongodb.com/try/download/community
- Selecciona Windows, versión 8.x, paquete MSI
- Instala con todas las opciones por defecto
- Verificar que esté corriendo: `Get-Service -Name MongoDB`

### Node.js
- Versión recomendada: 18 o superior
- Descarga: https://nodejs.org
- Verificar instalación: `node --version`

### Maven
El proyecto usa Maven Wrapper (`mvnw`) incluido en el repositorio, no es necesario instalarlo por separado.

## Estructura del proyecto
sanos-y-salvos/

├── backend/

│   ├── ms-mascotas/          # Microservicio de mascotas (puerto 8081)

│   ├── ms-geolocalizacion/   # Microservicio de geolocalización (puerto 8082)

│   └── ms-coincidencias/     # Microservicio de coincidencias (puerto 8083)

├── api-gateway/              # API Gateway (puerto 8080)

├── frontend/                 # Aplicación React (puerto 3000)

└── pom.xml                   # POM raíz del proyecto

## Puertos

| Servicio | Puerto |
|---|---|
| Frontend React | 3000 |
| API Gateway | 8080 |
| ms-mascotas | 8081 |
| ms-geolocalizacion | 8082 |
| ms-coincidencias | 8083 |
| MongoDB | 27017 |

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/MatiasAlarcon97/sanos-y-salvos.git
cd sanos-y-salvos
```

### 2. Iniciar MongoDB
Verifica que MongoDB esté corriendo:

```bash
Get-Service -Name MongoDB
```

Si el estado no es `Running`, inícialo:

```bash
Start-Service -Name MongoDB
```

### 3. Iniciar los microservicios del backend
Abre una terminal por cada microservicio:

```bash
# ms-mascotas
cd backend/ms-mascotas
../../mvnw spring-boot:run

# ms-geolocalizacion
cd backend/ms-geolocalizacion
../../mvnw spring-boot:run

# ms-coincidencias
cd backend/ms-coincidencias
../../mvnw spring-boot:run
```

### 4. Iniciar el API Gateway

```bash
cd api-gateway
../mvnw spring-boot:run
```

### 5. Iniciar el frontend

```bash
cd frontend
npm install
npm start
```

La aplicación estará disponible en `http://localhost:3000`.

## Endpoints disponibles

Todos los endpoints son accesibles a través del API Gateway en `http://localhost:8080`.

### Mascotas
| Método | Ruta | Descripción |
|---|---|---|
| GET | /mascotas | Listar todas las mascotas |
| GET | /mascotas/{id} | Obtener una mascota por ID |
| POST | /mascotas | Crear una mascota |
| PUT | /mascotas/{id} | Actualizar una mascota |

### Geolocalización
| Método | Ruta | Descripción |
|---|---|---|
| GET | /geoloc | Listar todas las geolocalizaciones |
| GET | /geoloc/{reporteId} | Obtener geolocalización por reporte |
| POST | /geoloc | Registrar una geolocalización |
| PUT | /geoloc/{reporteId} | Actualizar una geolocalización |

### Coincidencias
| Método | Ruta | Descripción |
|---|---|---|
| GET | /coincidencias | Listar todas las coincidencias |
| GET | /coincidencias/{id} | Obtener una coincidencia por ID |
| POST | /coincidencias | Crear una coincidencia |
| PUT | /coincidencias/{id} | Actualizar una coincidencia |

## Páginas del frontend

| Ruta | Descripción |
|---|---|
| / | Lista de mascotas con filtros y buscador |
| /reportar | Formulario para reportar una mascota con mapa interactivo |
| /coincidencias | Tabla de coincidencias entre mascotas |

## Permisos del navegador
Para que el mapa detecte tu ubicación automáticamente, permite el acceso a la ubicación cuando el navegador lo solicite. Si lo bloqueaste, puedes resetearlo desde:

- **Opera:** `opera://settings/content/location`
- **Chrome:** `chrome://settings/content/location`

Busca `localhost:3000`, elimínalo de la lista de bloqueados y recarga la página.
