# Contratos de Interfaz, Consultas DAO y Casos de Uso

## 1. Consultas SQL Reactivas (DAOs)

### Contrato 1: Tabla de Posiciones Automática (`StandingsDao.getStandings()`)
Calcula `PJ`, `G`, `E`, `P`, `GF`, `GC` y `Pts` directamente de los partidos con `stage = 'REGULAR'` y `status = 'COMPLETED'`:
```sql
SELECT 
    t.id AS teamId,
    t.name AS teamName,
    t.crestUri AS crestUri,
    t.captainName AS captainName,
    COUNT(m.id) AS played,
    SUM(CASE 
        WHEN (m.homeTeamId = t.id AND m.homeGoals > m.awayGoals) OR 
             (m.awayTeamId = t.id AND m.awayGoals > m.homeGoals) THEN 1 ELSE 0 END) AS won,
    SUM(CASE 
        WHEN m.homeGoals = m.awayGoals THEN 1 ELSE 0 END) AS drawn,
    SUM(CASE 
        WHEN (m.homeTeamId = t.id AND m.homeGoals < m.awayGoals) OR 
             (m.awayTeamId = t.id AND m.awayGoals < m.homeGoals) THEN 1 ELSE 0 END) AS lost,
    SUM(CASE 
        WHEN (m.homeTeamId = t.id AND m.homeGoals > m.awayGoals) OR 
             (m.awayTeamId = t.id AND m.awayGoals > m.homeGoals) THEN 3
        WHEN m.homeGoals = m.awayGoals THEN 1 
        ELSE 0 END) AS points,
    SUM(CASE
        WHEN m.homeTeamId = t.id THEN m.homeGoals
        WHEN m.awayTeamId = t.id THEN m.awayGoals
        ELSE 0 END) AS goalsFor,
    SUM(CASE
        WHEN m.homeTeamId = t.id THEN m.awayGoals
        WHEN m.awayTeamId = t.id THEN m.homeGoals
        ELSE 0 END) AS goalsAgainst
FROM teams t
LEFT JOIN matches m ON (m.homeTeamId = t.id OR m.awayTeamId = t.id) 
    AND m.stage = 'REGULAR' AND m.status = 'COMPLETED' AND m.isBye = 0
GROUP BY t.id
ORDER BY points DESC,
         (goalsFor - goalsAgainst) DESC,
         goalsFor DESC,
         t.name ASC;
```

## 2. Reglas de escritura

| Operación | Validación | Persistencia |
|-----------|------------|--------------|
| Completar acta | Goles/faltas no negativos; eventos con cantidad positiva y equipo participante; jugador MVP y calificación `1.0–10.0` se guardan juntos. | Partido y reemplazo de eventos en una transacción Room. |
| Crear/reasignar cruce manual | Jornada positiva, equipos existentes y distintos, sin repetir equipo en la jornada; un partido completado no se reasigna. | Inserta o actualiza un partido regular. |
| Generar jornada | Al menos dos equipos; una jornada existente no se genera de nuevo. Con cantidad impar, descansa un equipo con el menor `byeCount`. | Partidos y aumento de `byeCount` en una transacción Room. |
| Inicializar bracket | Exactamente 16 equipos registrados y distintos; no sobrescribe un bracket existente. | Crea las 15 llaves y los 16 partidos de ida/vuelta de octavos en una transacción. |
| Avanzar llave | Dos partidos completados; un global empatado requiere penales no empatados. | Actualiza ganador, tanda y partidos de la ronda siguiente en transacción. |
| Guardar vídeo | Metadatos obligatorios; URL HTTPS de Drive reconocida o URI local seleccionada. | Almacena el tipo de fuente y la URL/URI en `video_highlights`. |

## 3. Contratos reactivos de lectura

- `StandingsDao.getStandings()` publica posiciones y puntos de partidos regulares completados; los descansos no suman partidos jugados.
- `StatsDao` publica goles, asistencias, atajadas y tarjetas acumulados desde partidos completados.
- `MatchDao` publica partidos regulares y de eliminatorias; los detalles del acta se observan por `matchId`.
- `KnockoutTieDao` publica el bracket ordenado por ronda y posición.
- `VideoHistoryDao` publica el historial del más reciente al más antiguo.

Room es la fuente de verdad: sus `Flow` mantienen sincronizadas las pantallas con cada escritura confirmada.