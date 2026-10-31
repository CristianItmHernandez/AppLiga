# Modelo de Datos V2 (Room SQLite Schema + DataStore)

## A. Esquema Relacional Room (SQLite)

### 1. `catalog_clubs` (`CatalogClubEntity`) - NUEVO EN V2
Almacena el catálogo predefinido completo (históricos de UCL, UEL, Libertadores, Liga Colombiana, Liga Mexicana y clubes de América, Europa, Asia y África).
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | ID del club en catálogo |
| `name` | `TEXT` | `UNIQUE NOT NULL` | Nombre oficial del club |
| `country` | `TEXT` | `NOT NULL` | País de origen (ej. Colombia, México, España, Japón, Egipto) |
| `continent` | `TEXT` | `NOT NULL` | `AMERICA`, `EUROPE`, `ASIA`, `AFRICA` |
| `tags` | `TEXT` | `NOT NULL` | Etiquetas: `UCL`, `UEL`, `LIBERTADORES`, `LIGA_COL`, `LIGA_MEX`, `HISTORIC` |
| `crestReference` | `TEXT` | `NOT NULL` | Identificador de escudo o drawable/URI |

### 2. `teams` (`TeamEntity`)
Equipos participantes activos en el torneo actual de "La liga Alfa Bravo".
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | ID único del equipo en el torneo |
| `name` | `TEXT` | `UNIQUE NOT NULL` | Nombre del equipo (no se repite) |
| `leagueOrigin` | `TEXT` | `NOT NULL` | `UCL`, `UEL`, `LIBERTADORES`, `LIGA_COL`, `LIGA_MEX`, `AMERICA`, `EUROPE`, `ASIA`, `AFRICA`, `CUSTOM` |
| `crestUri` | `TEXT` | `NOT NULL` | Recurso o URI persistente del escudo |
| `captainName` | `TEXT` | `NOT NULL` | Nombre del capitán |
| `byeCount` | `INTEGER` | `NOT NULL DEFAULT 0` | Contador de descansos acumulados en jornadas impares |

### 3. `matches` (`MatchEntity`)
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | ID del partido |
| `stage` | `TEXT` | `NOT NULL` | `REGULAR` (Jornadas) o `KNOCKOUT` (Eliminatorias) |
| `matchdayOrRound` | `INTEGER` | `NOT NULL` | N° de Jornada ($1 \dots N$) o Ronda ($16, 8, 4, 2, 1$) |
| `creationMode` | `TEXT` | `NOT NULL DEFAULT 'AUTO'` | `AUTO`, `MANUAL`, `STANDINGS_SEEDED` |
| `knockoutTieId` | `INTEGER` | `NULLABLE` | FK hacia `knockout_ties.id` |
| `legNumber` | `INTEGER` | `NOT NULL DEFAULT 1` | `1` (Ida / Único), `2` (Vuelta) |
| `homeTeamId` | `INTEGER` | `NOT NULL REFERENCES teams(id)` | Equipo Local |
| `awayTeamId` | `INTEGER` | `NULLABLE REFERENCES teams(id)` | Equipo Visitante (`NULL` si descansa) |
| `isBye` | `INTEGER` | `NOT NULL DEFAULT 0` | `1` si descansa en esta jornada |
| `status` | `TEXT` | `NOT NULL DEFAULT 'PENDING'` | `PENDING` o `COMPLETED` |
| `homeGoals` | `INTEGER` | `NOT NULL DEFAULT 0` | Goles local |
| `awayGoals` | `INTEGER` | `NOT NULL DEFAULT 0` | Goles visitante |
| `homeFouls` | `INTEGER` | `NOT NULL DEFAULT 0` | Faltas local |
| `awayFouls` | `INTEGER` | `NOT NULL DEFAULT 0` | Faltas visitante |
| `mvpPlayerName` | `TEXT` | `NULLABLE` | Nombre del jugador MVP |
| `mvpRating` | `REAL` | `NULLABLE` | Puntuación decimal (`9.9`, `9.2`) |

### 4. `match_player_events` (`MatchPlayerEventEntity`)
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | ID del evento |
| `matchId` | `INTEGER` | `NOT NULL REFERENCES matches(id) ON DELETE CASCADE` | Partido asociado |
| `teamId` | `INTEGER` | `NOT NULL REFERENCES teams(id)` | Equipo del jugador |
| `playerName` | `TEXT` | `NOT NULL` | Nombre del jugador o portero |
| `eventType` | `TEXT` | `NOT NULL` | `GOAL`, `ASSIST`, `SAVE`, `YELLOW_CARD`, `RED_CARD` |
| `count` | `INTEGER` | `NOT NULL DEFAULT 1` | Cantidad registrada en el partido |

### 5. `knockout_ties` (`KnockoutTieEntity`)
Soporta eliminatorias desde mínimo 4 equipos (Semifinales `round = 2`), Cuartos (`round = 4`), Octavos (`round = 8`), etc.
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | ID de la serie eliminatoria |
| `round` | `INTEGER` | `NOT NULL` | Cantidad de llaves en esa fase: `8` (Octavos), `4` (Cuartos), `2` (Semis - 4 equipos), `1` (Final) |
| `slotIndex` | `INTEGER` | `NOT NULL` | Posición en el bracket ($0 \dots \text{round}-1$) |
| `teamAId` | `INTEGER` | `NULLABLE REFERENCES teams(id)` | Equipo A (Mejor clasificado si es por tabla) |
| `teamBId` | `INTEGER` | `NULLABLE REFERENCES teams(id)` | Equipo B |
| `teamAPenalties` | `INTEGER` | `NULLABLE` | Penales anotados por Equipo A |
| `teamBPenalties` | `INTEGER` | `NULLABLE` | Penales anotados por Equipo B |
| `winnerTeamId` | `INTEGER` | `NULLABLE REFERENCES teams(id)` | Ganador promovido a la siguiente ronda |

### 6. `video_highlights` (`VideoHighlightEntity`) - ACTUALIZADO EN V2
Vinculado directamente a un partido finalizado seleccionado de la lista.
| Columna | Tipo | Restricciones | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | `INTEGER` | `PRIMARY KEY AUTOINCREMENT` | ID del vídeo |
| `matchId` | `INTEGER` | `NOT NULL REFERENCES matches(id) ON DELETE CASCADE` | Partido finalizado seleccionado |
| `scoringTeamId` | `INTEGER` | `NOT NULL REFERENCES teams(id)` | Equipo al que pertenece el goleador del vídeo |
| `scorerName` | `TEXT` | `NOT NULL` | Nombre del goleador ingresado/seleccionado |
| `videoSourceType`| `TEXT` | `NOT NULL` | `GOOGLE_DRIVE` o `LOCAL_URI` |
| `videoUrlOrUri` | `TEXT` | `NOT NULL` | URL directa de Drive o URI local persistente |
| `createdAt` | `INTEGER` | `NOT NULL` | Timestamp de creación |