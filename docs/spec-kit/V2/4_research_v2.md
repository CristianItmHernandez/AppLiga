# Decisiones Técnicas y Alternativas Evaluadas - V2

## 1. Cálculo de Rachas ("Últimos 5" y Vista Completa) sin Penalizar RAM
- **Requerimiento**: Mostrar en la tabla de posiciones los últimos 5 resultados con iconos idénticos a la referencia visual (verde con check `✓`, gris de empate `−`, rojo con doble anillo y `✕`) y un botón en la esquina superior izquierda con icono de historial para ver todas las rachas completas, calculadas a partir de los partidos finalizados en la sección de jornadas.
- **Decisión**:
  - Consultar los partidos con `stage = 'REGULAR'`, `status = 'COMPLETED'` e `isBye = 0` ordenados por `matchdayOrRound ASC, id ASC`.
  - Para cada equipo, mapear cada partido a un enum `StreakResult { WIN, DRAW, LOSS }`.
  - En `StandingsScreen`, tomar `results.takeLast(5)` y renderizarlos en un `Row` compacto; al último elemento de la derecha se le dibuja el borde doble resaltado si corresponde.
  - Al tocar el botón de historial en el `TopAppBar` (esquina superior izquierda), abrir `FullStreakHistorySheet` mostrando la línea de tiempo completa (`LazyRow` horizontal por equipo) desde la Jornada 1.

## 2. Eliminatorias Flexibles (Mínimo 4 Equipos: Por Puesto de Tabla o Manual)
- **Requerimiento**: Permitir mínimo 4 equipos en eliminatorias (con partidos de ida y vuelta) y dar dos opciones: organizarlos automáticamente según el puesto de la tabla de inicio (donde el usuario define el límite de equipos que jugarán) o añadirlos manualmente.
- **Decisión**:
  - El usuario define el límite $N$ de clasificados ($N \ge 4$ y par, ej. $4, 6, 8, 16$).
  - **Modo Por Tabla (`BY_STANDINGS`)**: Se consulta la tabla de posiciones actual (`StandingsDao.getStandings()`) y se toman los primeros $N$ equipos (`take(N)`). Se emparejan el mejor contra el peor clasificado dentro del corte: para $i \in [0, N/2 - 1]$, la llave $i$ enfrenta a `standings[i]` (Ida de visitante, Vuelta de local por mérito deportivo) contra `standings[N - 1 - i]`.
  - **Modo Manual (`MANUAL`)**: Se crean las $N/2$ llaves vacías de la ronda correspondiente y el usuario elige libremente los clubes de cada llave validando que ningún equipo se repita.

## 3. Catálogo Mundial e Histórico sin Saturar el APK ni la Memoria
- **Requerimiento**: Incluir Liga Colombiana, Liga Mexicana, todos los clubes actuales e históricos de Champions League, Europa League y Copa Libertadores, además de clubes de América, Europa, Asia y África.
- **Decisión**:
  - Estructurar una base de datos semilla en JSON (`world_clubs_catalog.json`) con campos: `name`, `shortName`, `country`, `continent` (`AMERICA`, `EUROPE`, `ASIA`, `AFRICA`), `competitions` (tags separados por coma: `UCL_HISTORIC`, `UEL_HISTORIC`, `LIBERTADORES_HISTORIC`, `LIGA_COL`, `LIGA_MEX`, `DOMESTIC`) y un generador/cargador de escudos vectoriales o URLs con caché persistente.
  - La búsqueda se ejecuta en SQLite (`SELECT * FROM catalog_clubs WHERE competitionTag LIKE :filter AND name LIKE :search LIMIT 50`), consumiendo menos de $2\text{ MB}$ de RAM.

## 4. Rediseño del Historial de Goles (Selector de Partido Finalizado)
- **Requerimiento**: Eliminar el cuestionario manual; elegir directamente un partido finalizado de una lista y solo indicar el nombre del equipo y el nombre del goleador junto al vídeo.
- **Decisión**:
  - `VideoHighlightEntity` ahora guarda `matchId` (FK hacia `matches.id`), `scoringTeamId` y `scorerPlayerName`.
  - El formulario de subida consulta `MatchDao.getCompletedMatchesWithTeams()` para mostrar un selector visual con los partidos ya jugados. Al seleccionar uno, los dos equipos del partido aparecen como chips seleccionables y los goleadores registrados en el acta de ese partido aparecen como sugerencias de un toque (permitiendo también escribir el nombre si se desea).