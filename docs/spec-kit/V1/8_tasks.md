# Plan de Trabajo por Fases

## Fase 1: Cimientos, Catálogo de Equipos y Base de Datos Room
- [x] **Tarea 1.1**: Configurar Android Kotlin con Jetpack Compose, Room, Coil y Media3; `assembleDebug` incluye lint.
- [x] **Tarea 1.2**: Implementar las cinco entidades, DAOs y `AppDatabase`.
- [x] **Tarea 1.3**: Crear el catálogo de UEFA Champions League, Europa League y Copa Libertadores; Coil carga los escudos al tamaño de sus contenedores y aplica caché.
- [x] **Tarea 1.4**: Construir `MainScaffoldWithFab`, las cuatro secciones y el registro de catálogo/personalizado con capitán y escudo persistente.

## Fase 2: Apartado 1 y Motor de Jornadas
- [x] **Tarea 2.1**: Implementar tabla SQL reactiva, podio, tabla de posiciones y resumen de la jornada activa.
- [x] **Tarea 2.2**: Implementar `GenerateMatchdayUseCase`, confirmación, mínimo de dos equipos, sorteo y rotación transaccional de descansos.
- [x] **Tarea 2.3**: Construir tarjetas por jornada con estados, edición/eliminación y cruces manuales validados.

## Fase 3: Actas, Eliminatorias y Compartir
- [x] **Tarea 3.1**: Implementar actas con goles, jugadores, asistencias, atajadas, faltas, tarjetas y MVP; editar conserva eventos y equipos correctos.
- [x] **Tarea 3.2**: Implementar bracket de octavos a final con clasificación, sorteo o asignación manual; ida/vuelta, global, penales y avance.
- [x] **Tarea 3.3**: Implementar PNG compartible con escudos, marcador, global/penales, eventos y MVP.

## Fase 4: Estadísticas e Historial de Vídeos
- [x] **Tarea 4.1**: Implementar estadísticas de goleadores, asistidores, atajadores y disciplina.
- [x] **Tarea 4.2**: Implementar historial, Drive, vídeos locales y reproductor Media3 administrado por ciclo de vida.
- [x] **Tarea 4.3a**: Ejecutar `.\gradlew.bat assembleDebug`, que compila y ejecuta `lintDebug`.
- [ ] **Tarea 4.3b**: Perfilado de RAM en Android Studio Profiler sobre dispositivo/emulador. Requiere una sesión visual de ejecución que no está disponible en este entorno.

## Criterio de cierre

Las cuatro fases funcionales están implementadas. La entrega de Spec-Kit queda con una verificación externa pendiente: registrar una captura/medición de RAM en Android Studio Profiler. No se marca como realizada sin medición.
