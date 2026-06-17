# Frontend — Sanos y Salvos

Aplicación web desarrollada en React para reportar y buscar mascotas perdidas o encontradas.

## Tecnologías

- React 18
- React Router DOM — navegación entre páginas
- Axios — comunicación con el API Gateway
- Leaflet (vía CDN) — mapa interactivo para marcar ubicaciones

## Requisitos previos

- **Node.js** 18 o superior — descarga: https://nodejs.org
- Verificar instalación: `node --version`
- El backend (microservicios + API Gateway) debe estar corriendo para que la aplicación funcione con datos reales. Ver instrucciones en el README principal del proyecto.

## Instalación

```bash
cd frontend
npm install
```

Si al ejecutar `npm install` aparece un error de permisos en PowerShell (`la ejecución de scripts está deshabilitada en este sistema`), ejecuta una vez:
```bash
Set-ExecutionPolicy -Scope CurrentUser -ExecutionPolicy RemoteSigned
```
Confirma con `S` y vuelve a intentar `npm install`.

## Ejecución

```bash
npm start
```

La aplicación se abre automáticamente en `http://localhost:3000`.

## Scripts disponibles (`package.json`)

| Script | Comando | Descripción |
|---|---|---|
| Iniciar en desarrollo | `npm start` | Levanta el servidor de desarrollo con recarga automática |
| Compilar para producción | `npm run build` | Genera la versión optimizada en la carpeta `build/` |
| Ejecutar pruebas | `npm test` | Ejecuta las pruebas configuradas con Jest/React Testing Library |

## Cómo probar la aplicación

1. Asegúrate de tener el backend completo corriendo (los 3 microservicios + API Gateway en el puerto 8080).
2. Ejecuta `npm start` y abre `http://localhost:3000`.
3. **Probar el listado:** la página principal (`/`) debe mostrar las mascotas registradas, con estadísticas y filtros funcionales.
4. **Probar el reporte:** ve a `/reportar`, completa el formulario, marca un punto en el mapa y haz clic en "Guardar reporte". Debe redirigir a una pantalla de confirmación con un ID de reporte.
5. **Probar la búsqueda por ID:** copia ese ID y ve a `/detalle/:id` (o usa el botón "Ver coincidencias" de la pantalla de confirmación) para ver el detalle del reporte y sus coincidencias asociadas.
6. **Probar las coincidencias:** ve a `/coincidencias` y verifica que se listen las coincidencias detectadas automáticamente entre mascotas perdidas y encontradas.
7. **Probar el modo oscuro:** usa el botón ☾/☀ en el navbar para alternar entre modo claro y oscuro.

## Estructura del proyecto

```
frontend/
├── public/
│   └── index.html              # Carga Leaflet vía CDN
├── src/
│   ├── components/
│   │   ├── Navbar.js           # Barra de navegación y toggle de tema
│   │   └── MapaPicker.js       # Mapa interactivo (Leaflet)
│   ├── pages/
│   │   ├── MascotasPage.js     # Página principal (listado)
│   │   ├── ReportarPage.js     # Formulario de reporte con mapa
│   │   ├── CoincidenciasPage.js # Tabla de coincidencias
│   │   └── DetallePage.js      # Búsqueda de reporte por ID
│   ├── services/
│   │   ├── mascotaService.js          # Llamadas HTTP a /mascotas
│   │   ├── geolocalizacionService.js  # Llamadas HTTP a /geoloc
│   │   └── coincidenciaService.js     # Llamadas HTTP a /coincidencias
│   ├── App.js      # Rutas (React Router) y control de tema
│   ├── theme.css   # Variables CSS para modo claro/oscuro
│   └── index.js    # Punto de entrada de la aplicación
└── package.json    # Dependencias y scripts NPM
```

## Conexión con el backend

Todas las peticiones se realizan contra el API Gateway en `http://localhost:8080`, definido en cada archivo de `src/services/`. El Gateway enruta automáticamente cada petición al microservicio correspondiente (`/mascotas`, `/geoloc`, `/coincidencias`).

## Permisos del navegador

Para que el mapa detecte la ubicación actual automáticamente, el navegador debe tener permiso de geolocalización habilitado para `localhost:3000`. Si fue bloqueado previamente:

- **Chrome:** `chrome://settings/content/location`
- **Opera:** `opera://settings/content/location`

Busca `localhost:3000`, elimínalo de la lista de bloqueados y recarga la página.
