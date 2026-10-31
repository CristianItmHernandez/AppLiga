# Plan Arquitectónico y Estructura Técnica (CÓMO Construir)

## 1. Stack Tecnológico Ligero
- **UI Framework**: Jetpack Compose (Material 3) con tema oscuro personalizado (`#121212` / `#1E1E1E` y acentos verde neón `#2ECC71` y dorado `#F1C40F`).
- **Base de Datos**: Android Room (SQLite) 2.6+ con soporte de `Flow` reactivo.
- **Carga de Imágenes**: Coil-Compose (`io.coil-kt:coil-compose`) solicita imágenes al tamaño de sus contenedores Compose y limita la caché de memoria al 15% del heap y la caché de disco al 2%. Se conserva ARGB para escudos con transparencia; forzar `RGB_565` globalmente degradaría los fondos transparentes.
- **Reproductor de Vídeo**: Jetpack Media3 (`androidx.media3:media3-exoplayer` + `media3-ui`), instanciado como singleton compartido para no duplicar decodificadores de hardware.
- **Inyección de Dependencias**: Contenedor manual ligero (`AppContainer`) o Hilt para mantener bajo el overhead de arranque.

## 2. Estructura de Carpetas del Proyecto
```text
app/src/main/java/com/hograna/liga/
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt
│   │   ├── dao/
│   │   │   ├── TeamDao.kt
│   │   │   ├── MatchDao.kt
│   │   │   ├── StatsDao.kt
│   │   │   └── VideoHistoryDao.kt
│   │   └── entity/
│   │       ├── TeamEntity.kt
│   │       ├── MatchEntity.kt
│   │       ├── MatchPlayerEventEntity.kt
│   │       ├── KnockoutTieEntity.kt
│   │       └── VideoHighlightEntity.kt
│   ├── catalog/
│   │   └── PredefinedTeamsCatalog.kt   // UCL, Europa League, Libertadores
│   └── repository/
│       └── TournamentRepository.kt
├── domain/
│   ├── model/
│   └── usecase/
│       ├── GenerateMatchdayUseCase.kt  // Algoritmo par/impar con cola de descanso
│       ├── CompleteMatchUseCase.kt     // Actualiza puntos, goles, MVP y bracket
│       ├── AdvanceKnockoutTieUseCase.kt// Calcula global, pide penales y promueve ganador
│       └── ShareMatchGraphicUseCase.kt // Renderiza Compose/Canvas a PNG en caché
└── ui/
    ├── navigation/
    │   └── MainScaffoldWithFab.kt      // BottomBar 4 pestañas + Botón Central (+)
    ├── standings/
    │   └── StandingsScreen.kt          // Apartado 1: Podio Top 3 + Tabla + Jornada
    ├── matches/
    │   ├── MatchesTabsScreen.kt        // Apartado 2: Pestañas "Fase" y "Eliminatorias"
    │   ├── MatchDetailSheet.kt         // Formulario MVP, goles, atajadas, tarjetas
    │   ├── KnockoutBracketView.kt      // Árbol visual Octavos -> Final + Modal Penales
    │   └── ShareableMatchCard.kt       // Plantilla visual para exportar imagen
    ├── teams/
    │   └── AddTeamModalSheet.kt        // Botón Central: Catálogo 3 ligas, buscador, capitán
    ├── stats/
    │   └── HogRanaStatsScreen.kt       // Apartado 3: Goleadores, Asistidores, Atajadores, Tarjetas
    └── videos/
        └── VideoHistoryScreen.kt       // Apartado 4: ExoPlayer Drive/Local + Cabecera de partido