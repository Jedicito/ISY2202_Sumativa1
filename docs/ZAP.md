# Análisis con ZAP Proxy

Guía para instalar ZAP y escanear el frontend y el backend en Windows 11.
Guarda una **captura de pantalla de cada paso marcado con 📸**: son la evidencia
del informe.

> Los nombres de los menús pueden variar un poco según la versión de ZAP.

---

## 1. Instalación

1. Descarga el instalador **Windows (64) Installer** desde <https://www.zaproxy.org/download/>
   (versión 2.17.0, unos 244 MB). 📸 página de descarga.
2. ZAP necesita **Java 17 o superior**, el mismo que usas para el proyecto.
3. Ejecuta el instalador. En la carpeta de destino puedes elegir, por ejemplo,
   `D:\ZAP`. 📸 pantalla de la carpeta de instalación.
4. Abre ZAP. Si pregunta por guardar la sesión, elige no guardarla (o guárdala
   en una carpeta de `D:`). 📸 ZAP abierto.

## 2. Configuración: puerto del proxy

ZAP usa por defecto el puerto **8080**, el mismo del frontend. Para evitar el
choque, cambia el puerto de ZAP a **8090**:

- *Tools → Options → Network → Local Servers/Proxies* → puerto `8090` → *OK*. 📸

Si abres ZAP con el frontend ya corriendo, ZAP avisará que el puerto 8080 está
ocupado y te dejará elegir otro: escribe `8090`.

## 3. Preparación

1. Levanta el **backend** y el **frontend** como dice el `README.md`.
2. Comprueba en el navegador que <http://localhost:8080> y
   <http://localhost:8081/eventos> responden.

## 4. Escaneo del frontend (http://localhost:8080)

1. En la pestaña **Quick Start** elige **Automated Scan**.
2. *URL to attack*: `http://localhost:8080`
3. Deja marcado **Use traditional spider** y desmarca *Use ajax spider*.
4. Presiona **Attack**. 📸 configuración antes de atacar.
5. Espera a que terminen el *Spider* y el *Active Scan* (barra de progreso
   abajo, puede tardar varios minutos). 📸 progreso.
6. Revisa la pestaña **Alerts** (abajo a la izquierda). 📸 árbol de alertas.
7. Haz clic en cada alerta y toma captura del detalle (descripción, URL,
   solución). 📸 una por alerta.

## 5. Escaneo del backend (http://localhost:8081)

ZAP no descubre solo las rutas de una API REST, por eso se le entrega una lista.

1. Menú **Import → Import a File Containing URLs** y elige
   `docs/zap-urls-backend.txt`. 📸
2. En el panel **Sites** (izquierda) aparecerá `http://localhost:8081`.
3. Clic derecho sobre `http://localhost:8081` → **Attack → Active Scan** →
   **Start Scan**. 📸
4. Espera a que termine y revisa **Alerts**. 📸 árbol de alertas y detalle de cada una.

## 6. Exportar los reportes

Genera **un reporte por capa**:

1. Menú **Report → Generate Report**.
2. *Template*: **Traditional HTML Report**.
3. En la pestaña *Scope*, en **Sites**, marca solo `http://localhost:8080`
   (frontend). Nombre del archivo: `zap-frontend-v1.html`. *Generate Report*. 📸
4. Repite marcando solo `http://localhost:8081` (backend). Nombre:
   `zap-backend-v1.html`.

**Envíame los dos archivos HTML** para analizar los hallazgos y preparar las correcciones.

---

## Segundo escaneo (después de las correcciones)

Se repiten los pasos 3 a 6 con la versión corregida. Para no mezclar resultados,
empieza con una sesión nueva: *File → New Session*. Los reportes se llaman
`zap-frontend-v2.html` y `zap-backend-v2.html`.
