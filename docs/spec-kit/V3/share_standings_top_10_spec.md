# Especificación y Plan: Compartir Imagen de la Tabla de Posiciones (Top 10)

## 1. QUÉ Construir (Especificación Funcional)
- **Botón de Compartir en la Página de Inicio (Apartado 1)**:
  - En el encabezado de la Tabla de Posiciones del inicio (`StandingsScreen`), añadir un botón de acción rápida con el icono de **Compartir / WhatsApp** (`Share Icon`).
- **Contenido Exacto de la Imagen Exportada**:
  - La imagen generada debe replicar fielmente la estética de la página de inicio de **"La liga Alfa Bravo"**, incluyendo:
    1. **Podio Superior (Top 3)**: Tarjeta superior con el 1° (centro con corona), 2° y 3° lugar con sus escudos, nombres y puntos.
    2. **Tabla de Posiciones (Limitada a los Primeros 10 Equipos)**:
       - Si el torneo tiene más de 10 equipos, la imagen **solo renderizará los primeros 10 puestos** (`take(10)`) según el orden actual de la tabla. Si tiene 10 o menos, los mostrará todos.
       - **Columnas**: Posición (`1..10`), `Equipo` (**Logo/Escudo** + Nombre del club), `PJ`, `G`, `E`, `P`, **`Pts`** (resaltados en verde) y **`Últimos 5` (Rachas)**.
       - **Rachas idénticas al inicio**: Círculos **Verdes (`✓`)** para victoria, **Grises (`−`)** para empate y **Rojos con doble borde (`✕`)** para derrota.
- **Flujo de Compartir**:
  - Al pulsar el botón, se genera la imagen en alta definición ($1080 \times 1440\text{ px}$) y se abre el selector nativo de Android (`Intent.ACTION_SEND`) para enviarla directamente por WhatsApp, Telegram o redes sociales.

---

## 2. CÓMO Construirlo (Arquitectura y Eficiencia de RAM)

### A. Caso de Uso (`ShareStandingsGraphicUseCase.kt`)
- Ubicación: `domain/usecase/ShareStandingsGraphicUseCase.kt`
- **Entrada**:
  - `standingsList: List<TeamStandingRow>` (recortada automáticamente con `.take(10)`).
  - `streaksMap: Map<Long, List<StreakBadgeType>>` (los últimos 5 resultados de cada uno de esos 10 equipos).
- **Procesamiento Off-Screen (Zero-Bloat)**:
  1. Se ejecuta en `Dispatchers.Default`.
  2. Precarga los bitmaps de los escudos de los primeros 10 equipos mediante **Coil** (`ImageLoader`) escalados a $64 \times 64\text{ px}$ para no saturar la memoria RAM.
  3. Renderiza el componente `ShareableStandingsCard` o dibuja sobre un `Bitmap` temporal (`Bitmap.createBitmap(1080, 1440, Bitmap.Config.ARGB_8888)`).
  4. Guarda el archivo en `context.cacheDir/shared_standings_top10.png` (sobrescribiendo el anterior para no acumular basura en almacenamiento) y lanza el `Intent` con `FileProvider`.
  5. Llama a `bitmap.recycle()` inmediatamente después de comprimir el PNG.

### B. Componente Visual (`ShareableStandingsCard.kt`)
- Ubicación: `ui/standings/ShareableStandingsCard.kt`
- **Estructura Visual**:
  - **Header**: Icono de la Copa de Champions + título **"La liga Alfa Bravo - Tabla Oficial (Top 10)"**.
  - **Podio Top 3**: Mismo diseño visual de la pantalla principal.
  - **Filas 1 a 10**: Fondo oscuro (`#121212` / `#1E1E1E`), divisores sutiles, escudo circular del equipo, nombre truncado limpiamente a 1 línea, estadísticas (`PJ`, `G`, `E`, `P`), puntos en verde neón (`#2ECC71`) y la fila `StreakBadgesRow` con los últimos 5 partidos.

---

## 3. Contrato de Datos (`StandingsShareContract`)
```kotlin
data class StandingsSharePayload(
    val tournamentTitle: String = "La liga Alfa Bravo",
    val topThreePodium: List<StandingWithStreakUiModel>, // take(3)
    val topTenTable: List<StandingWithStreakUiModel>     // take(10) estricto
)

data class StandingWithStreakUiModel(
    val position: Int,
    val teamId: Long,
    val teamName: String,
    val crestUri: String,
    val played: Int,
    val won: Int,
    val drawn: Int,
    val lost: Int,
    val points: Int,
    val lastFiveStreak: List<StreakBadgeType> // WIN (✓), DRAW (−), LOSS (✕)
)
```

---

## 4. Tareas de Implementación (Checklist para el Agente)
- [ ] **Tarea S.1**: Crear `ShareableStandingsCard.kt` en `ui/standings/` replicando el diseño exacto de la página de inicio (Podio Top 3 + Tabla con logo, estadísticas, puntos y rachas de los últimos 5 partidos) restringido a `.take(10)`.
- [ ] **Tarea S.2**: Implementar `ShareStandingsGraphicUseCase.kt` en `domain/usecase/` que tome los primeros 10 equipos de la tabla actual, resuelva sus escudos con Coil en tamaño reducido, exporte el PNG a `cacheDir` y lance el `Intent.ACTION_SEND` con `FileProvider`.
- [ ] **Tarea S.3**: Añadir el botón de **Compartir Tabla** en la cabecera de `StandingsScreen.kt` (Apartado 1) conectado al `ViewModel` y verificar que la imagen compartida muestre únicamente el Top 10 con sus logos, puntos y rachas.