# Sanos y Salvos

Plataforma de microservicios para gestión de mascotas perdidas y encontradas. Permite reportar mascotas, registrar ubicaciones y detectar coincidencias automáticamente entre mascotas perdidas y encontradas.

## Tecnologías

| Capa | Tecnología |
|---|---|
| Backend | Java 21 + Spring Boot 3.5 |
| Frontend | React 18 |
| Base de datos | MongoDB Atlas (en la nube) |
| Gateway | Spring Cloud Gateway |

## Requisitos previos

### Java JDK 21 (no JRE)

Este es el requisito que con más frecuencia genera problemas al ejecutar el proyecto, por lo que se detalla en profundidad.

**Diferencia entre JDK y JRE:** el JRE solo permite *ejecutar* programas Java ya compilados. El JDK incluye además el compilador (`javac`), necesario para construir el proyecto con Maven. Si solo tienes un JRE instalado, Maven fallará con el error:
```
No compiler is provided in this environment. Perhaps you are running on a JRE rather than a JDK?
```

**Pasos para instalar el JDK 21:**
1. Ve a https://adoptium.net
2. Selecciona la pestaña **JDK 21 - LTS**
3. Descarga el instalador para **Windows** (.msi)
4. Ejecuta el instalador dejando todas las opciones por defecto, asegurándote de que la opción **"Set JAVA_HOME variable"** esté activada

**Verificar la instalación:**
```bash
java -version
```
Debe mostrar algo similar a:
```
openjdk version "21.0.x"
OpenJDK Runtime Environment Temurin-21.0.x+x-LTS
```

**Si `java -version` muestra una versión distinta a la 21 (por ejemplo, una versión 1.8 de Oracle o una versión más nueva como la 25):**

Esto ocurre cuando el equipo ya tenía otra versión de Java instalada previamente, y esa versión tiene prioridad en la variable de entorno `PATH` del sistema operativo. Para solucionarlo:

1. Verifica la ruta de instalación del JDK 21:
   ```bash
   Get-ChildItem "C:\Program Files\Eclipse Adoptium"
   ```
2. Abre **Variables de entorno** (búscalo en el menú inicio de Windows) → **Variables de entorno...**
3. En **Variables del sistema**, crea o edita `JAVA_HOME` con el valor de la ruta encontrada en el paso 1, por ejemplo:
   ```
   C:\Program Files\Eclipse Adoptium\jdk-21.0.x.x-hotspot
   ```
4. En **Variables del sistema**, edita la variable **Path** y agrega al inicio de la lista:
   ```
   %JAVA_HOME%\bin
   ```
5. Revisa también el **Path de variables de usuario** (sección superior de la ventana) — si existe alguna entrada que apunte a otra instalación de Java (por ejemplo `AppData\Local\Programs\Eclipse Adoptium\jdk-25...` o `Common Files\Oracle\Java\javapath`), **elimínala** o muévela al final de la lista. El Path de usuario se evalúa antes que el del sistema, por lo que una entrada ahí puede seguir forzando la versión incorrecta aunque `JAVA_HOME` esté bien configurado.
6. **Cierra completamente VS Code** (no solo la terminal) y vuelve a abrirlo — los cambios de variables de entorno no se aplican a una sesión ya abierta.
7. Verifica de nuevo con `java -version`.

### MongoDB Atlas (base de datos en la nube)

El proyecto usa **MongoDB Atlas** en lugar de una instalación local de MongoDB. Esto permite que todos los integrantes del equipo trabajen sobre los **mismos datos**, sin importar desde qué computador ejecuten el proyecto.

No es necesario instalar nada: la conexión ya está configurada en los archivos `application.properties` de cada microservicio, apuntando al clúster compartido `sanosysalvos.jabpcpa.mongodb.net`.

Si necesitas crear tu propio clúster (por ejemplo, para un proyecto derivado de este), sigue estos pasos generales:
1. Crea una cuenta en https://cloud.mongodb.com
2. Crea un clúster gratuito (M0)
3. En **Database Access**, crea un usuario con contraseña
4. En **Network Access**, agrega `0.0.0.0/0` para permitir conexiones desde cualquier equipo del grupo
5. En **Connect → Drivers**, selecciona **Java** y copia el *connection string*, reemplazando `<db_password>` por la contraseña real

### Node.js
- Versión recomendada: 18 o superior
- Descarga: https://nodejs.org
- Verificar instalación: `node --version`

**Si al ejecutar `npm install` aparece un error de seguridad/permisos en PowerShell** (`la ejecución de scripts está deshabilitada en este sistema`), ejecuta una vez:
```bash
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
```
Confirma con `S` cuando lo solicite, y vuelve a intentar `npm install`.

### Maven
El proyecto usa Maven Wrapper (`mvnw`) incluido en el repositorio, no es necesario instalarlo por separado.

## Estructura del proyecto

```
sanos-y-salvos/
├── backend/
│   ├── ms-mascotas/          # Microservicio de mascotas (puerto 8081)
│   ├── ms-geolocalizacion/   # Microservicio de geolocalización (puerto 8082)
│   └── ms-coincidencias/     # Microservicio de coincidencias (puerto 8083)
├── api-gateway/              # API Gateway (puerto 8080)
├── frontend/                 # Aplicación React (puerto 3000)
└── pom.xml                   # POM raíz del proyecto
```

## Puertos

| Servicio | Puerto |
|---|---|
| Frontend React | 3000 |
| API Gateway | 8080 |
| ms-mascotas | 8081 |
| ms-geolocalizacion | 8082 |
| ms-coincidencias | 8083 |
| MongoDB Atlas | 27017 (gestionado en la nube) |

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/MatiasAlarcon97/sanos-y-salvos.git
cd sanos-y-salvos
```

### 2. Verificar conexión a MongoDB Atlas
No requiere instalación. Solo asegúrate de tener conexión a internet, ya que los microservicios se conectan al clúster en la nube al iniciar. La URI de conexión ya viene configurada en cada `application.properties`.

### 3. Iniciar los microservicios del backend
Abre una terminal por cada microservicio, desde la raíz del proyecto:

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

**Importante:** verifica en los logs de inicio que cada microservicio muestre `srvHost=sanosysalvos.jabpcpa.mongodb.net` (conexión a Atlas) y no `hosts=[localhost:27017]` (conexión local incorrecta). Si ves esto último, revisa que el archivo `application.properties` correspondiente tenga la URI de Atlas guardada correctamente, y ejecuta `../../mvnw clean` antes de volver a correr el servicio.

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

## Flujo de integración entre microservicios

Al reportar una mascota desde el frontend, ocurren dos llamadas encadenadas:

1. El frontend envía `POST /mascotas` → se crea el documento en `ms-mascotas` y se obtiene su `id`.
2. Si se marcó una ubicación en el mapa, el frontend envía `POST /geoloc` usando ese `id` como `reporteId`, registrando la ubicación en `ms-geolocalizacion`.
3. Internamente, `ms-mascotas` también dispara una búsqueda de coincidencias contra `ms-coincidencias`, comparando la nueva mascota con las del estado opuesto (perdida ↔ encontrada) y registrando el resultado si hay similitud.

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
| /detalle/:id | Búsqueda de un reporte por ID y visualización de sus coincidencias |

## Permisos del navegador
Para que el mapa detecte tu ubicación automáticamente, permite el acceso a la ubicación cuando el navegador lo solicite. Si lo bloqueaste, puedes resetearlo desde:

- **Opera:** `opera://settings/content/location`
- **Chrome:** `chrome://settings/content/location`

Busca `localhost:3000`, elimínalo de la lista de bloqueados y recarga la página.

## Pruebas unitarias y cobertura

Cada microservicio incluye pruebas unitarias (JUnit 5 + Mockito) y reportes de cobertura generados con JaCoCo.

Para ejecutar las pruebas y generar el reporte de un microservicio:

```bash
cd backend/ms-mascotas
../../mvnw test
start target/site/jacoco/index.html
```

Repite el mismo proceso reemplazando la carpeta por `ms-geolocalizacion` o `ms-coincidencias`. El detalle completo de las pruebas implementadas y las métricas de cobertura se encuentra en el documento `pruebas_unitarias.pdf` de la documentación del proyecto.