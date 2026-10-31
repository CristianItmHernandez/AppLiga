# Modelo de Datos (Room SQLite Schema)

## Diagrama de Tablas y Entidades

### 1. `teams` (`TeamEntity`)
Almacena los equipos elegidos del catálogo o creados manualmente.
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | Identificador único del equipo |
| `name` | ` TEXT` | `UNIQUE NOT NULL` | Nombre del equipo (no se repite) |
| `leagueOrigin` | `TEXT` | `NOT NULL` | `UCL`, `UEL`, `LIBERTADORES`, `CUSTOM` |
| `crestUri` | `TEXT` | `NOT NULL` | Recurso drawable o URI de imagen local |
| `captainName` | `TEXT` | `NOT NULL` | Nombre del capitán del equipo |
| `byeCount` | `INTEGER` | `NOT NULL DEFAULT 0` | Veces que ha descansado (control de impares) |

### 2. `matches` (`MatchEntity`)
Representa tanto partidos de jornada regular como partidos individuales (Ida o Vuelta) de eliminatorias.
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | ID del partido |
| `stage` | `TEXT` | `NOT NULL` | `REGULAR` o `KNOCKOUT` |
| `matchdayOrRound` | `INTEGER` | `NOT NULL` | N° de Jornada (1..N) o Ronda (8=Octavos, 4=Cuartos, 2=Semi, 1=Final) |
| `knockoutTieId` | `INTEGER` | `NULLABLE` | FK hacia `knockout_ties.id` si es eliminatoria |
| `legNumber` | `INTEGER` | `NOT NULL DEFAULT 1` | `1` (Ida / Único), `2` (Vuelta) |
| `homeTeamId` | `INTEGER` | `NOT NULL REFERENCES teams(id)` | Equipo Local |
| `awayTeamId` | `INTEGER` | `NULLABLE REFERENCES teams(id)` | Equipo Visitante (`NULL` si el local descansa) |
| `isBye` | `INTEGER` | `NOT NULL DEFAULT 0` | `1` si es registro de descanso en jornada impar |
| `status` | `TEXT` | `NOT NULL DEFAULT 'PENDING'` | `PENDING` o `COMPLETED` |
| `homeGoals` | `INTEGER` | `NOT NULL DEFAULT 0` | Goles local |
| `awayGoals` | `INTEGER` | `NOT NULL DEFAULT 0` | Goles visitante |
| `homeFouls` | `INTEGER` | `NOT NULL DEFAULT 0` | Faltas cometidas por local |
| `awayFouls` | `INTEGER` | `NOT NULL DEFAULT 0` | Faltas cometidas por visitante |
| `mvpPlayerName` | `TEXT` | `NULLABLE` | Nombre del MVP del partido |
| `mvpRating` | `REAL` | `NULLABLE` | Puntuación en decimales (ej. `9.9`, `9.2`) |

### 3. `match_player_events` (`MatchPlayerEventEntity`)
Registra goles, asistencias, atajadas de portero, tarjetas amarillas y rojas por jugador en cada partido.
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | ID del registro |
| `matchId` | `INTEGER` | `NOT NULL REFERENCES matches(id) ON DELETE CASCADE` | Partido asociado |
| `teamId` | `INTEGER` | `NOT NULL REFERENCES teams(id)` | Equipo al que pertenece el jugador |
| `playerName` | `TEXT` | `NOT NULL` | Nombre del jugador o portero |
| `eventType` | `TEXT` | `NOT NULL` | `GOAL`, `ASSIST`, `SAVE`, `YELLOW_CARD`, `RED_CARD` |
| `count` | `INTEGER` | `NOT NULL DEFAULT 1` | Cantidad lograda en ese partido |

### 4. `knockout_ties` (`KnockoutTieEntity`)
Controla las llaves de eliminatoria (Ida y Vuelta, Global y Penales).
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | ID de la llave |
| `round` | `INTEGER` | `NOT NULL` | `8` (Octavos), `4` (Cuartos), `2` (Semis), `1` (Final) |
| `slotIndex` | `INTEGER` | `NOT NULL` | Posición en el bracket (`0..7` en octavos, `0..3` en cuartos, etc.) |
| `teamAId` | `INTEGER` | `NULLABLE REFERENCES teams(id)` | Primer equipo de la serie |
| `teamBId` | `INTEGER` | `NULLABLE REFERENCES teams(id)` | Segundo equipo de la serie |
| `teamAPenalties` | `INTEGER` | `NULLABLE` | Goles en penales Equipo A (si hubo empate global) |
| `teamBPenalties` | `INTEGER` | `NULLABLE` | Goles en penales Equipo B (si hubo empate global) |
| `winnerTeamId` | `INTEGER` | `NULLABLE REFERENCES teams(id)` | Ganador promovido a la siguiente ronda |

### 5. `video_highlights` (`VideoHighlightEntity`)
Almacena el historial histórico de vídeos de goles.
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | ID del vídeo |
| `homeTeamName` | `TEXT` | `NOT NULL` | Nombre equipo local |
| `awayTeamName` | `TEXT` | `NOT NULL` | Nombre equipo visitante |
| `scoreSummary` | `TEXT` | `NOT NULL` | Marcador final (ej. `3 - 1`) |
| `scorersList` | `TEXT` | `NOT NULL` | Nombres de los goleadores del partido |
| `videoSourceType`| `TEXT` | `NOT NULL` | `GOOGLE_DRIVE` o `LOCAL_URI` |
| `videoUrlOrUri` | `TEXT` | `NOT NULL` | URL transformada de Drive o URI local persistente |
| `createdAt` | `INTEGER` | `NOT NULL` | Timestamp de subida |