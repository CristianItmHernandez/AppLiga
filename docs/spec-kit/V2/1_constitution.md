# Constitución del Proyecto: La liga Alfa Bravo (Android Kotlin - V2)

## 1. Principios Innegociables de Ingeniería
- **Identidad del Producto**: El nombre oficial de la aplicación en el `AndroidManifest.xml` y recursos `strings.xml` es **"La liga Alfa Bravo"**, acompañada de un Adaptive Icon (`ic_launcher`) con el trofeo estilo UEFA Champions League sobre fondo oscuro premium.
- **Lenguaje y Estándar**: Kotlin 2.0+ con Coroutines y `StateFlow` para reactividad sin fugas de memoria.
- **Eficiencia de RAM (Zero-Bloat)**: 
  - Uso estricto de `LazyColumn` y `LazyRow` con `key` únicas para reciclaje eficiente de nodos en UI.
  - **Catálogo Mundial e Histórico Indexado**: Prohibido cargar los cientos de equipos históricos y continentales en listas de RAM al iniciar. El catálogo completo (Champions, Europa League, Libertadores + Históricos, Liga Colombiana, Liga Mexicana, América, Europa, Asia y África) se almacena en un archivo semilla comprimido (`assets/world_clubs_catalog.json`) sincronizado a una tabla SQLite de catálogo con índices FTS/LIKE (`LIMIT 50` por página).
  - Prohibido cargar imágenes en resolución original; **Coil** debe redimensionar cada escudo al tamaño exacto del contenedor (`size(64.dp)` o `size(24.dp)`) con caché en disco y memoria limitada al 15% del heap disponible.
  - Liberación obligatoria de `ExoPlayer` (Media3) en el ciclo de vida `onStop` / `DisposableEffect`.
- **Arquitectura MVI / MVVM Limpia (3 Capas)**:
  - `ui`: Jetpack Compose puro, consumiendo un `AppTheme` dinámico gobernado por preferencias de usuario (Tema Claro/Oscuro y Familia Tipográfica).
  - `domain`: Casos de uso puros en Kotlin (generador de sorteos, calculador de rachas `W/D/L`, emparejador de eliminatorias desde mínimo 4 equipos por puestos de tabla $1^\circ \text{ vs } N^\circ$ o manual, validador de emparejamientos).
  - `data`: Room SQLite local (Single Source of Truth para el torneo) + **Jetpack DataStore Preferences** (para configuraciones globales ligeras y extensibles).
- **Sistema de Configuración Abierto/Cerrado (OCP)**: La gestión de ajustes (`AppSettings`) está centralizada en un único modelo reactivo para permitir añadir nuevas opciones en el código añadiendo una sola clave en `PreferenceKeys` y una fila en `SettingsScreen`.
- **Un Solo Comando de Construcción**: `./gradlew assembleDebug` compila, ejecuta lint y genera el APK de **"La liga Alfa Bravo"** listo para instalar.