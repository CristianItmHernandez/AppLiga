# Constitución del Proyecto: Liga Hog Rana (Android Kotlin)

## 1. Principios Innegociables de Ingeniería
- **Lenguaje y Estándar**: Kotlin 2.0+ con Coroutines y `StateFlow` para reactividad sin fugas de memoria.
- **Eficiencia de RAM (Zero-Bloat)**: 
  - Uso estricto de `LazyColumn` y `LazyRow` con `key` únicas para reciclaje eficiente de nodos en UI.
  - Prohibido cargar imágenes en resolución original; **Coil** debe redimensionar cada escudo al tamaño exacto del contenedor (`size(64.dp)` o `size(32.dp)`) con caché en disco y memoria limitada al 15% del heap disponible.
  - Liberación obligatoria de `ExoPlayer` (Media3) en el ciclo de vida `onStop` / `DisposableEffect`.
- **Arquitectura MVI / MVVM Limpia (3 Capas)**:
  - `ui`: Jetpack Compose puro, sin lógica de negocio ni referencias a `Context` fuera de efectos controlados.
  - `domain`: Casos de uso puros en Kotlin (generador de sorteos, motor de cálculo de tabla, reglas de eliminatorias).
  - `data`: Room SQLite local (Single Source of Truth) con transacciones `@Transaction` atómicas.
- **Persistencia Offline-First**: Toda la información de torneos, estadísticas, escudos personalizados (URI persistente) y enlaces de vídeo reside en SQLite vía Room. Cero recálculos en memoria principal; las tablas y rankings se resuelven mediante consultas SQL indexadas.
- **Un Solo Comando de Construcción**: `./gradlew assembleDebug` compila, ejecuta lint y genera el APK listo para pruebas.