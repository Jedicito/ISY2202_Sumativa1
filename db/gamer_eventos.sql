-- =====================================================================
-- Eventos Gamer - Script de creación e inserción de datos
-- Asignatura: Seguridad y Calidad en el Desarrollo de Software (ISY2202)
--
-- Ejecutar en MySQL Workbench conectado como root (o un usuario con
-- permisos de administración): abrir este archivo y presionar el rayo
-- (Execute). El script se puede ejecutar varias veces: borra y vuelve a
-- crear la base de datos.
-- =====================================================================

SET NAMES utf8mb4;

-- ---------------------------------------------------------------------
-- 1. Base de datos
-- ---------------------------------------------------------------------
DROP DATABASE IF EXISTS gamer_eventos;
CREATE DATABASE gamer_eventos
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;

USE gamer_eventos;

-- ---------------------------------------------------------------------
-- 2. Usuario de MySQL exclusivo para la aplicación (mínimo privilegio)
--    La aplicación solo lee datos, por lo que solo recibe SELECT sobre
--    esta base. No se usa root desde la aplicación.
--    Contraseña de DEMO: cámbiala si quieres, pero debe coincidir con la
--    variable de entorno DB_PASSWORD del backend.
-- ---------------------------------------------------------------------
DROP USER IF EXISTS 'gamer_app'@'localhost';
CREATE USER 'gamer_app'@'localhost' IDENTIFIED BY 'GamerApp-Demo-2026';
GRANT SELECT ON gamer_eventos.* TO 'gamer_app'@'localhost';
FLUSH PRIVILEGES;

-- ---------------------------------------------------------------------
-- 3. Tablas
-- ---------------------------------------------------------------------
CREATE TABLE usuario (
  id        BIGINT       NOT NULL AUTO_INCREMENT,
  username  VARCHAR(50)  NOT NULL,
  password  VARCHAR(100) NOT NULL,   -- hash BCrypt, nunca texto plano
  rol       VARCHAR(20)  NOT NULL,   -- USER o ADMIN
  PRIMARY KEY (id),
  UNIQUE KEY uk_usuario_username (username)
) ENGINE = InnoDB;

CREATE TABLE evento (
  id               BIGINT        NOT NULL AUTO_INCREMENT,
  nombre           VARCHAR(120)  NOT NULL,
  categoria        VARCHAR(40)   NOT NULL,
  juego            VARCHAR(80)   NOT NULL,
  ciudad           VARCHAR(60)   NOT NULL,
  direccion        VARCHAR(150)  NOT NULL,
  fecha_hora       DATETIME      NOT NULL,
  descripcion      VARCHAR(1000) NOT NULL,
  organizadores    VARCHAR(200)  NOT NULL,
  expositores      VARCHAR(300)  NOT NULL,
  estacionamiento  BOOLEAN       NOT NULL DEFAULT FALSE,
  locomocion       BOOLEAN       NOT NULL DEFAULT FALSE,
  comida           BOOLEAN       NOT NULL DEFAULT FALSE,
  alojamiento      BOOLEAN       NOT NULL DEFAULT FALSE,
  destacado        BOOLEAN       NOT NULL DEFAULT FALSE,
  imagen           VARCHAR(100)  NOT NULL,
  PRIMARY KEY (id)
) ENGINE = InnoDB;

-- ---------------------------------------------------------------------
-- 4. Usuarios (contraseñas cifradas con BCrypt, factor 10)
--    Credenciales de DEMO (solo para pruebas):
--      admin    / Admin-2026     (rol ADMIN)
--      jugador1 / Jugador1-2026  (rol USER)
--      jugador2 / Jugador2-2026  (rol USER)
-- ---------------------------------------------------------------------
INSERT INTO usuario (username, password, rol) VALUES
  ('admin',    '$2a$10$4VrHTtOAKAJiwBQpFJ7zhOdDJBx.yPdMkEVkCAURlPSuK5zBoDTYW', 'ADMIN'),
  ('jugador1', '$2a$10$7A3dNfFomqnVUC3GRR.fuu8lDtXlQfXGR/FUoWQFSGmZVLs1g.YgS', 'USER'),
  ('jugador2', '$2a$10$BpcpR4Q4di/GsxRi3QXsAORzKa.bzAq145X72yqnKgYAu4j37AkSO', 'USER');

-- ---------------------------------------------------------------------
-- 5. Eventos (datos ficticios)
-- ---------------------------------------------------------------------
INSERT INTO evento (nombre, categoria, juego, ciudad, direccion, fecha_hora,
                    descripcion, organizadores, expositores,
                    estacionamiento, locomocion, comida, alojamiento,
                    destacado, imagen) VALUES
(
  'Copa Pixel Santiago', 'Torneo', 'League of Legends', 'Santiago',
  'Av. Libertador Bernardo O''Higgins 1450, Santiago Centro',
  '2026-12-12 15:00:00',
  'Torneo abierto 5 vs 5 con fase de grupos y eliminación directa. Inscripción por equipos, transmisión en vivo de la final y premios para los tres primeros lugares.',
  'Club de Esports Santiago',
  'Equipo de casters Voz Gamer; charla sobre el camino al juego competitivo',
  TRUE, TRUE, TRUE, FALSE,
  TRUE, 'evento-1.jpg'
),
(
  'Retro Fest Valparaíso', 'Convención', 'Juegos retro (multiplataforma)', 'Valparaíso',
  'Calle Blanco 980, Valparaíso',
  '2027-01-16 11:00:00',
  'Convención dedicada a consolas y juegos clásicos: zona de juego libre, exhibición de coleccionistas, feria de compra y venta, y competencias de puntaje en máquinas arcade.',
  'Asociación Retro Gamers del Puerto',
  'Coleccionistas invitados; taller de restauración de consolas',
  FALSE, TRUE, TRUE, TRUE,
  FALSE, 'evento-2.jpg'
),
(
  'LAN Party Sur', 'LAN Party', 'Counter-Strike 2', 'Concepción',
  'Av. Pedro de Valdivia 1200, Concepción',
  '2027-02-06 10:00:00',
  'Jornada de 24 horas de juego en red local. Cada participante trae su equipo; la organización entrega red cableada, energía y puestos. Incluye torneo relámpago nocturno.',
  'Colectivo LAN Biobío',
  'Charla de armado de PC y periféricos; muestra de equipos de la comunidad',
  TRUE, FALSE, TRUE, FALSE,
  TRUE, 'evento-3.jpg'
),
(
  'Noche de Lanzamiento Indie', 'Lanzamiento', 'Proyecto Cóndor', 'La Serena',
  'Av. Francisco de Aguirre 350, La Serena',
  '2027-03-13 19:00:00',
  'Presentación del juego independiente Proyecto Cóndor por su equipo desarrollador. Demo jugable, conversación con los creadores y sorteo de copias entre los asistentes.',
  'Estudio Cumbre Juegos',
  'Equipo desarrollador de Proyecto Cóndor; músico de la banda sonora',
  TRUE, TRUE, FALSE, FALSE,
  FALSE, 'evento-4.jpg'
);

-- ---------------------------------------------------------------------
-- 6. Verificación rápida
-- ---------------------------------------------------------------------
SELECT id, username, rol FROM usuario;
SELECT id, nombre, categoria, ciudad, fecha_hora, destacado FROM evento;
