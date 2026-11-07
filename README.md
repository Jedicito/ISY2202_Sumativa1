# Eventos Gamer

Aplicación web para reunir información de eventos gamer (torneos, convenciones,
LAN parties y lanzamientos). Proyecto de la asignatura **Seguridad y Calidad en
el Desarrollo de Software (ISY2202)**.

```
Navegador ──► Frontend (8080) ──► Backend (8081) ──► MySQL (3306)
              Spring Boot          Spring Boot
              Spring Security      Spring Security + JWT
              Thymeleaf            Spring Data JPA
```

| Carpeta     | Contenido |
|-------------|-----------|
| `frontend/` | Sitio web: Spring Boot, Spring Security y Thymeleaf |
| `backend/`  | API REST: Spring Boot, Spring Security, JWT, Spring Data JPA, MySQL |
| `db/`       | Script de creación e inserción de datos (`gamer_eventos.sql`) |
| `docs/`     | Pasos de ZAP Proxy y lista de URLs del backend para escanear |

---

## Requisitos (Windows 11)

- **Java 17 o superior** (por ejemplo Eclipse Temurin 17). Verifica en PowerShell: `java -version`
- **MySQL 8** o superior y **MySQL Workbench**.
- Conexión a internet la primera vez: el script `mvnw` descarga Maven y las
  dependencias automáticamente. No es necesario instalar Maven.

---

## 1. Base de datos

1. Abre **MySQL Workbench** y conéctate como `root`.
2. *File → Open SQL Script…* y elige `db/gamer_eventos.sql`.
3. Presiona el **rayo** (*Execute*). Al final verás 3 usuarios y 4 eventos.

El script crea la base `gamer_eventos`, las tablas `usuario` y `evento`, y un
usuario de MySQL solo para la aplicación (`gamer_app`, con permiso únicamente de
lectura). Se puede ejecutar de nuevo cuando quieras: deja los datos como al inicio.

## 2. Clave secreta para el JWT

En PowerShell, genera una clave aleatoria y cópiala:

```powershell
$b = New-Object byte[] 48; [Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($b); [Convert]::ToBase64String($b)
```

## 3. Levantar el backend (puerto 8081)

Abre una ventana de PowerShell:

```powershell
cd <ruta>\gamer-eventos\backend
$env:DB_PASSWORD = "GamerApp-Demo-2026"
$env:JWT_SECRET  = "<pega aquí la clave del paso 2>"
.\mvnw.cmd spring-boot:run
```

Cuando aparezca `Started BackendApplication`, abre <http://localhost:8081/eventos>:
debe mostrar los 4 eventos en formato JSON.

> Las variables `$env:` solo duran mientras esa ventana esté abierta. Si cierras
> PowerShell, vuelve a definirlas antes de ejecutar.

## 4. Levantar el frontend (puerto 8080)

Abre **otra** ventana de PowerShell (el backend debe seguir corriendo):

```powershell
cd <ruta>\gamer-eventos\frontend
.\mvnw.cmd spring-boot:run
```

Abre <http://localhost:8080>.

## Usuarios de prueba

| Usuario    | Contraseña      | Rol   |
|------------|-----------------|-------|
| `admin`    | `Admin-2026`    | ADMIN |
| `jugador1` | `Jugador1-2026` | USER  |
| `jugador2` | `Jugador2-2026` | USER  |

Son credenciales de demostración: no las uses en otros sistemas.

---

## Funcionalidades y rutas

### Frontend

| Ruta              | Acceso  | Descripción |
|-------------------|---------|-------------|
| `/`, `/home`      | Pública | Inicio: próximos eventos, destacados, avisos y acceso al login |
| `/buscar`         | Pública | Búsqueda por nombre, categoría, juego, ciudad y fecha |
| `/eventos/{id}`   | Privada | Detalle del evento (requiere iniciar sesión) |
| `/login`          | Pública | Inicio de sesión |

### Backend (API REST)

| Método y ruta        | Acceso            | Descripción |
|----------------------|-------------------|-------------|
| `POST /login`        | Pública           | Recibe `user` y `encryptedPass`; responde `Bearer <token JWT>` |
| `GET /eventos`       | Pública           | Lista y búsqueda (`nombre`, `categoria`, `juego`, `ciudad`, `fecha=AAAA-MM-DD`) |
| `GET /eventos/{id}`  | Privada (JWT)     | Detalle del evento. Sin token responde 401 |

## Prueba rápida

1. Entra a <http://localhost:8080>: se ven los eventos sin iniciar sesión.
2. Ve a **Buscar** y filtra, por ejemplo, por categoría *Torneo*.
3. Haz clic en **Ver detalle**: te lleva al login.
4. Ingresa con `jugador1`: vuelves directo al detalle del evento.
5. **Cerrar sesión**: vuelves al login con el mensaje de sesión cerrada.

---

## Imágenes

Las imágenes están en `frontend/src/main/resources/static/img/`. Para usar las
tuyas, reemplaza los archivos **con el mismo nombre** (no hay que tocar código):

| Archivo                                  | Uso                  | Tamaño sugerido |
|------------------------------------------|----------------------|-----------------|
| `evento-1.jpg` … `evento-4.jpg`          | Imagen de cada evento | 800 × 450 (16:9) |
| `auspiciador-1.jpg`, `auspiciador-2.jpg` | Avisos del inicio    | 728 × 90        |

Recomendación: menos de 300 KB por imagen. Reinicia el frontend después de cambiarlas.

---

## Problemas comunes

| Mensaje | Solución |
|---------|----------|
| `Could not resolve placeholder 'DB_PASSWORD'` o `'JWT_SECRET'` | Faltan las variables `$env:` en esa ventana de PowerShell (paso 3). |
| `JWT_SECRET debe tener al menos 32 caracteres` | Usa la clave generada en el paso 2. |
| `Access denied for user 'gamer_app'@'localhost'` | Vuelve a ejecutar el script SQL y revisa que `DB_PASSWORD` coincida con la del script. |
| `Port 8080 was already in use` | Otro programa usa el puerto (por ejemplo ZAP). Ver `docs/ZAP.md`. |
| `Schema-validation: wrong column type` | Avísame el mensaje completo (diferencia entre la tabla y el código). |
| `JAVA_HOME not found` | Instala Java 17 y verifica `java -version`. |
| Error 500 en el frontend | Revisa que el backend esté corriendo en la otra ventana. |
