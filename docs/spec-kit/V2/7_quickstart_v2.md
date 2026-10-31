# Quickstart y Smoke Test de Validación - V2 ("La liga Alfa Bravo")

## 1. Compilación e Instalación
```bash
./gradlew assembleDebug
./gradlew installDebug
```

## 2. Protocolo de Smoke Test V2

1. **Verificación de Nombre e Icono del APK**:
   - Comprobar en el lanzador de Android que la app se llama **"La liga Alfa Bravo"** y muestra el icono de la Copa de la Champions League.

2. **Prueba del Catálogo Mundial e Histórico (Botón Central `+`)**:
   - Abrir el selector de equipos y filtrar por **Liga Colombiana**, **Liga Mexicana**, **Champions League (Históricos)**, **Libertadores (Históricos)**, **Asia** y **África**.
   - Agregar al menos 6 equipos al torneo asignando sus capitanes.

3. **Prueba de Rachas ("Últimos 5" y Botón Superior Izquierdo de Historial)**:
   - En el Apartado 2 (Fase), completar varios partidos de jornadas con victorias, empates y derrotas.
   - Volver al Apartado 1: verificar en la tabla la columna **"Últimos 5"** con los círculos **Verde (`✓`)**, **Gris (`−`)** y **Rojo con doble borde (`✕`)**.
   - Tocar el botón con **símbolo de historial en la esquina superior izquierda** y verificar que muestra la racha histórica completa de todos los partidos jugados por cada club.

4. **Prueba de Eliminatorias desde Mínimo 4 Equipos (Por Tabla y Manual)**:
   - Ir a `Partidos -> Eliminatorias`.
   - Elegir **Organizar según el puesto de la tabla de inicio** y fijar el límite en **4 equipos**: verificar que el sistema toma al $1^\circ, 2^\circ, 3^\circ \text{ y } 4^\circ$ de la tabla y crea las llaves de Semifinal a **Ida y Vuelta** ($1^\circ \text{ vs } 4^\circ$ y $2^\circ \text{ vs } 3^\circ$).
   - Probar también la opción de **Añadir/Editar cruces manualmente** con mínimo 4 equipos.

5. **Prueba del Nuevo Diseño Moderno al Compartir Partido Finalizado**:
   - En cualquier partido `COMPLETADO`, pulsar el botón de compartir.
   - Verificar que la imagen exportada luce el nuevo diseño moderno tipo póster broadcast de **"La liga Alfa Bravo"** con escudos iluminados, tarjeta de MVP (`9.9`), goleadores, asistidores y atajadas de los porteros.

6. **Prueba del Nuevo Historial de Goles (Apartado 4)**:
   - Ir al Apartado 4 y pulsar "Subir Gol".
   - Seleccionar un **partido finalizado** de la lista desplegada, elegir el **equipo** que anotó, poner el **nombre del goleador** y adjuntar el link de Google Drive o vídeo del teléfono.
   - Verificar que encima del reproductor aparecen los equipos que jugaron, el resultado del partido y el goleador indicado.