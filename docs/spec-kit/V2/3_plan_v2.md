# Plan Arquitectónico y Estructura Técnica - V2 ("La liga Alfa Bravo")

## 1. Stack Tecnológico y Recursos de Marca
- **Nombre e Iconografía**: Configuración en `AndroidManifest.xml` (`android:label="La liga Alfa Bravo"`) e iconos adaptativos en `res/mipmap-anydpi-v26/ic_launcher.xml` usando un vector/webp de la Copa de Champions.
- **Catálogo Masivo Eficiente**: Archivo `assets/world_clubs_catalog.json` pre-procesado e insertado en la tabla `catalog_clubs` de Room en el primer arranque mediante `RoomDatabase.Callback`, indexado por `competition` y `continent` para consumo cercano a $0\text{ MB}$ de RAM.
- **Motor Gráfico de Póster (`ShareMatchGraphicUseCase`)**: Dibujo estructurado en capas tipo Bento Grid usando `Canvas` nativo de alta precisión con gradientes `LinearGradient`, sombras de neón y tipografía condensada.

## 2. Estructura de Carpetas del Proyecto (V2)
```text
app/src/main/java/com/alfabravo/liga/
├── core/
│   └── settings/
│       ├── AppSettings.kt               // Modo Día/Noche, Fuente y ajustes extensibles
│       ├── SettingsDataStore.kt
│       └── AppTypographyCatalog.kt
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt
│   │   ├── dao/
│   │   │   ├── CatalogClubDao.kt        // NUEVO: Búsqueda indexada de clubes del mundo e históricos
│   │   │   ├── TeamDao.kt
│   │   │   ├── MatchDao.kt
│   │   │   ├── StatsDao.kt
│   │   │   └── VideoHistoryDao.kt
│   │   └── entity/
│   │       ├── CatalogClubEntity.kt     // NUEVO: UCL, UEL, Libertadores, COL, MEX, América, Europa, Asia, África
│   │       ├── TeamEntity.kt
│   │       ├── MatchEntity.kt
│   │       ├── MatchPlayerEventEntity.kt
│   │       ├── KnockoutTieEntity.kt
│   │       └── VideoHighlightEntity.kt  // ACTUALIZADO: Vinculado a matchId finalizado + scoringTeam + scorerName
│   └── repository/
│       └── TournamentRepository.kt
├── domain/
│   └── usecase/
│       ├── GetTeamStreaksUseCase.kt           // NUEVO: Calcula rachas (Últimos 5 y Completo) desde jornadas finalizadas
│       ├── GenerateAutoMatchdayUseCase.kt     // Sorteo automático par/impar con descanso justo
│       ├── UpsertManualMatchUseCase.kt        // Edición manual con validaciones
│       ├── GenerateKnockoutByStandingsUseCase.kt // NUEVO: Arma llaves (mín. 4 equipos) según puestos de tabla con límite N
│       ├── CompleteMatchUseCase.kt            // Cierra acta, actualiza tabla, rachas y global
│       ├── AdvanceKnockoutTieUseCase.kt       // Avance ida/vuelta y penales
│       └── ShareMatchGraphicUseCase.kt        // ACTUALIZADO: Póster moderno Bento Grid "La liga Alfa Bravo"
└── ui/
    ├── theme/
    │   └── AlfaBravoTheme.kt
    ├── components/
    │   └── StreakBadgesRow.kt           // NUEVO: Círculos Verde (✓), Gris (−) y Rojo con doble borde (✕)
    ├── standings/
    │   ├── StandingsScreen.kt           // Apartado 1: Podio + Tabla con "Últimos 5" + Botón superior izq. Historial
    │   └── FullStreakHistorySheet.kt    // NUEVO: Modal con todas las rachas históricas de cada equipo
    ├── matches/
    │   ├── MatchesTabsScreen.kt         // Apartado 2: Pestañas "Fase" y "Eliminatorias"
    │   ├── KnockoutSetupSheet.kt        // NUEVO: Selector de límite (mín. 4) por Puestos de Tabla o Manual
    │   ├── KnockoutBracketView.kt       // Bracket adaptable (Semis, Cuartos, Octavos) Ida/Vuelta + Penales
    │   ├── MatchDetailSheet.kt          // Acta completa del partido
    │   └── ModernShareableMatchPoster.kt// NUEVO: Diseño innovador para compartir partido finalizado
    ├── teams/
    │   └── AddTeamModalSheet.kt         // ACTUALIZADO: UCL, UEL, Libertadores, Liga COL, Liga MEX y Continentes
    ├── stats/
    │   └── LeagueStatsScreen.kt         // Apartado 3: Goleadores, Asistidores, Atajadores, Amarillas y Rojas
    └── videos/
        └── VideoHistoryScreen.kt        // ACTUALIZADO: Selector de partido finalizado + Equipo + Goleador
```