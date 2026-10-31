# Plan de Trabajo por Fases - V2 ("La liga Alfa Bravo")

## Fase 1: Rebranding ("La liga Alfa Bravo"), Icono de Copa Champions y Catálogo Mundial

* [ ] **Tarea 1.1**: Actualizar `AndroidManifest.xml`, `strings.xml` y configuración de Gradle con el nombre oficial **"La liga Alfa Bravo"** y generar el Adaptive Icon (`ic_launcher`) con la imagen de la Copa de la Champions League.

* [ ] **Tarea 1.2**: Crear `CatalogClubEntity`, `CatalogClubDao` y el archivo semilla `world_clubs_catalog.json` con **TODOS** los equipos actuales e históricos de UEFA Champions League, Europa League, Copa Libertadores, **Liga Colombiana**, **Liga Mexicana** y clubes de **América, Europa, Asia y África**.

* [ ] **Tarea 1.3**: Actualizar `AddTeamModalSheet` (Botón Central `+`) con pestañas por liga/continente, buscador instantáneo paginado, creación manual de respaldo y registro de capitán.

## Fase 2: Sistema de Rachas ("Últimos 5" + Historial Completo) en Apartado 1

* [ ] **Tarea 2.1**: Implementar `GetTeamStreaksUseCase` calculando victorias, empates y derrotas a partir de los partidos finalizados (`COMPLETED`) de las jornadas regulares.

* [ ] **Tarea 2.2**: Crear el componente visual `StreakBadgesRow` fiel a la imagen de referencia: círculo verde con check (`✓`) para victoria, círculo gris con guion (`−`) para empate y círculo rojo con doble anillo blanco y equis (`✕`) para derrota, integrando la columna **"Últimos 5"** en la tabla de posiciones del Apartado 1.

* [ ] **Tarea 2.3**: Añadir el botón con **símbolo de historial en la esquina superior izquierda** del `TopAppBar` en el Apartado 1 y construir `FullStreakHistorySheet` para visualizar todas las rachas completas de la temporada por equipo.

## Fase 3: Eliminatorias Mejoradas (Mínimo 4 Equipos, Por Tabla o Manual) y Póster Moderno

* [ ] **Tarea 3.1**: Actualizar el módulo de **Eliminatorias** para permitir un **mínimo de 4 equipos** con partidos de **Ida y Vuelta**, agregando `KnockoutSetupSheet` donde el usuario define el límite de equipos participantes y elige entre: (A) Organizarlos automáticamente según el puesto de la tabla de inicio ($1^\circ \text{ vs } N^\circ$), o (B) Añadir/editar manualmente los partidos.

* [ ] **Tarea 3.2**: Rediseñar `ModernShareableMatchPoster` y `ShareMatchGraphicUseCase` con una estética moderna, ordenada e innovadora (estilo broadcast deportivo en Bento Grid con branding de **"La liga Alfa Bravo"**, marcador hero, tarjeta destacada de MVP con puntaje decimal, goleadores, asistidores y atajadas de porteros).

## Fase 4: Nuevo Historial de Goles por Selección de Partido y Validación Final

* [ ] **Tarea 4.1**: Actualizar `VideoHighlightEntity` y `VideoHistoryScreen` (Apartado 4) eliminando el cuestionario antiguo: implementar el selector de **partidos finalizados** (mostrando la lista de equipos que jugaron y el marcador), donde el usuario solo elige el **equipo**, ingresa/selecciona el **nombre del goleador** y sube el link de Google Drive o vídeo local.

* [ ] **Tarea 4.2**: Ejecutar el Smoke Test completo de `7_quickstart.md` verificando el consumo eficiente de RAM y el cambio en caliente entre Modo Día, Modo Noche y tipografías.