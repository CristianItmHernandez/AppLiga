# Quickstart y Smoke Test

## 1. Requisitos

- JDK 17 y Android SDK con API 35 y Build Tools disponibles.
- Windows: usa `gradlew.bat`; macOS/Linux: `./gradlew`.
- Para probar instalación y vídeo: emulador o dispositivo Android conectado.

## 2. Compilar, ejecutar lint e instalar

Desde PowerShell, en la raíz del proyecto:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

`assembleDebug` incluye `lintDebug`. El APK se genera en `app\build\outputs\apk\debug\app-debug.apk`.

## 3. Smoke test funcional

1. Registra dos equipos del catálogo con capitán y uno personalizado con nombre, capitán y escudo.
2. Genera una jornada, abre y completa un acta; confirma que Inicio y Estadísticas reflejen el resultado.
3. Genera una jornada con número impar de equipos y confirma que el descanso se muestre en Partidos e Inicio.
4. Crea y reasigna un cruce manual; intenta repetir un equipo en la jornada y confirma que la validación lo impide.
5. Con 16 equipos, inicia el bracket; completa ida y vuelta, verifica el global, los penales si empatan y la ronda siguiente.
6. Comparte un partido completado y verifica escudos, marcador, eventos y MVP en el PNG.
7. Guarda un enlace público de Drive y un vídeo local, reprodúcelos y cambia de pantalla para revisar el ciclo de vida de ExoPlayer.

La compilación/lint se valida con el wrapper. El perfilado cuantitativo de RAM debe ejecutarse en Android Studio Profiler sobre dispositivo/emulador y registrarse aparte; no se sustituye por compilar.
