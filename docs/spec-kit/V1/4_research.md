# Decisiones Técnicas y Alternativas Evaluadas

## 1. Creación Dual de Partidos: Automática vs Manual Validada
- **Requerimiento**: Permitir en el Apartado 2 tanto el sorteo automático por jornadas como la creación/edición manual eligiendo qué equipo se enfrenta a cuál, previniendo inconsistencias.
- **Implementación**: `GenerateMatchdayUseCase` genera jornadas aleatorias con rotación de descansos y persiste partidos/descanso en una transacción. `UpsertManualMatchUseCase` crea o reasigna cruces regulares:
  - **Modo Automático (`GenerateMatchdayUseCase`)**: Aplica el contador de equidad `byeCount` cuando el total es impar; nadie repite descanso hasta que todos hayan descansado una vez, y sortea el resto de los cruces.
  - El modo manual valida que la jornada sea positiva, los equipos existan y sean distintos, y que ninguno esté ya asignado a otro cruce de la jornada. Los partidos completados no se pueden reasignar.

## 2. Configuraciones de tema y fuente (fuera de alcance de esta entrega)
- **Problema**: Cambiar el tema o la fuente suele requerir reiniciar la `Activity` si se hace con XML tradicional, consumiendo memoria y causando parpadeos. Además, se requiere dejar el código preparado para agregar más ajustes fácilmente.
- **Estado**: La idea se conserva como alternativa futura; no forma parte de `2_spec.md` ni tiene tareas de aceptación en `8_tasks.md`. No se implementa ni se considera requisito para cerrar las cuatro fases funcionales actuales.
- **Propuesta si se incorpora a una especificación posterior**:
  - Usar **Jetpack DataStore** exponiendo un único `StateFlow<AppSettings>`.
  - En `HogRanaTheme`, conmutar entre `darkColorScheme()` (estética negra/verde original) y `lightColorScheme()` (fondo blanco hueso `#F8F9FA`, tarjetas `#FFFFFF` con bordes sutiles y acento verde esmeralda `#1B8A4B` de alto contraste).
  - Para la fuente, mapear el enum `AppFontFamily` (`SANS_SERIF`, `SPORTS_CONDENSED`, `SERIF`, `MONOSPACE`) a un objeto `Typography` de Material 3 en memoria.
  - **Patrón para añadir nuevas configuraciones en el futuro**: Solo se agrega la propiedad en `data class AppSettings`, su `Preferences.Key` en `SettingsDataStore` y un ítem composable `SettingRowItem` en `SettingsModalSheet.kt`.

## 3. Generación de Imagen y Reproductor de Vídeo Eficientes
- **Imagen Compartida**: Renderizado bajo demanda en un `Bitmap` temporal; incluye escudos, marcador, global/penales cuando aplica, eventos y MVP. El `Bitmap` se libera después de escribir el PNG en caché y abrir el selector nativo.
- **Vídeos Drive y Locales**: Los enlaces HTTPS de archivos de Drive se transforman a `uc?export=download&id=...`; el archivo debe ser público para el dispositivo que reproduce. Una instancia de `ExoPlayer` se libera al detener o retirar la pantalla.