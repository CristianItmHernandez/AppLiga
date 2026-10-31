# Contratos de Interfaz, Consultas SQL y Casos de Uso - V2

## 1. Contrato de Rachas por Equipo (`GetTeamStreaksUseCase`)
Obtiene los resultados de cada equipo en partidos de jornada (`stage = 'REGULAR'` y `status = 'COMPLETED'`):
```kotlin
enum class StreakBadgeType {
    WIN,  // Círculo Verde (#16A34A) con check blanco (✓)
    DRAW, // Círculo Gris (#64748B) con guion blanco (−)
    LOSS  // Círculo Rojo (#EF4444) con doble anillo blanco y equis (✕)
}

data class TeamStreakItem(
    val matchId: Long,
    val matchday: Int,
    val opponentName: String,
    val scoreText: String,
    val badgeType: StreakBadgeType
)

data class TeamStreakSummary(
    val teamId: Long,
    val teamName: String,
    val teamCrestUri: String,
    val lastFive: List<TeamStreakItem>, // Se muestra en la columna "Últimos 5" de la tabla
    val fullHistory: List<TeamStreakItem> // Se muestra en el botón de Historial (esquina superior izquierda)
)
```

---

## 2. Contrato de Eliminatorias Flexibles (`SetupKnockoutPhaseUseCase`)
Permite configurar las eliminatorias a partir de un **mínimo de 4 equipos** en modo Ida y Vuelta:
```kotlin
enum class KnockoutSeedMode {
    BY_STANDINGS_ORDER, // Ordena según puesto de la tabla de inicio hasta el límite N
    MANUAL_PAIRING      // El usuario elige manualmente los cruces
}

suspend fun setupKnockoutBracket(
    teamLimit: Int, // Mínimo 4 equipos (ej. 4 = Semifinales, 8 = Cuartos, 16 = Octavos)
    mode: KnockoutSeedMode,
    manualPairs: List<Pair<Long, Long>> = emptyList()
): Result<Unit>
```
- **Validación**: `require(teamLimit >= 4 && teamLimit % 2 == 0)` y `registeredTeamsCount >= teamLimit`.
- **Comportamiento `BY_STANDINGS_ORDER`**:
  1. Obtiene la lista ordenada de `StandingsDao.getStandings().take(teamLimit)`.
  2. Empareja el puesto $1^\circ$ vs puesto $N^\circ$, $2^\circ$ vs $(N-1)^\circ$, etc.
  3. Crea para cada serie la `KnockoutTieEntity` y automáticamente sus 2 partidos en `matches` (`legNumber = 1` Ida y `legNumber = 2` Vuelta).

---

## 3. Contrato de Subida de Vídeo de Gol (`AddGoalHighlightUseCase`)
Simplificado para elegir de una lista de partidos finalizados:
```kotlin
data class CompletedMatchOption(
    val matchId: Long,
    val stageAndRoundLabel: String, // Ej: "Jornada 3" o "Semifinal - Vuelta"
    val homeTeamId: Long,
    val homeTeamName: String,
    val homeCrestUri: String,
    val awayTeamId: Long,
    val awayTeamName: String,
    val awayCrestUri: String,
    val finalScore: String,         // Ej: "2 - 1"
    val recordedScorers: List<String> // Sugerencias tomadas del acta del partido
)

suspend fun addGoalHighlight(
    selectedMatchId: Long,
    scoringTeamId: Long,
    scorerName: String,
    videoSourceType: VideoSourceType,
    rawUrlOrUri: String
): Result<Unit>
```

---

## 4. Contrato del Póster Moderno para Compartir (`ShareMatchGraphicUseCase`)
Recibe un `MatchFullSummary` y renderiza un `Bitmap` de $1080 \times 1350\text{ px}$ con diseño broadcast moderno:
- Cabecera con branding **"La liga Alfa Bravo"** y trofeo.
- Escudos con efecto glow, marcador gigante y etiqueta de global/penales si aplica.
- Tarjeta destacada dorada para el **MVP del Partido** y su puntuación (`9.9`).
- Bloques Bento simétricos para **Goleadores**, **Asistidores** y **Atajadas de Porteros**.