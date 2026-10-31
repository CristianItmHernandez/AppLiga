# Especificación Funcional y Criterios de Aceptación - V2 ("La liga Alfa Bravo")

## 1. Identidad Visual, Nombre e Icono del APK
- **Nombre de la App**: **"La liga Alfa Bravo"** visible en el cajón de aplicaciones de Android, pantalla de inicio y encabezados principales.
- **Icono del APK (`ic_launcher`)**: Icono adaptativo vectorial/alta resolución mostrando la **Copa de la UEFA Champions League** ("La Orejona") en tonos plateados/iluminados sobre un escudo circular oscuro (`#0B101E`) con destellos azul/dorado.

---

## 2. Barra Superior (TopAppBar) y Barra Inferior (BottomBar)
- **TopAppBar**:
  - **Esquina Superior Izquierda**: Botón con **símbolo de Historial** (`History / Clock-Refresh Icon`) que abre el modal de **Historial Completo de Rachas** de todos los equipos a lo largo de todas las jornadas finalizadas.
  - **Centro**: Título de la sección ("La liga Alfa Bravo").
  - **Esquina Superior Derecha**: Icono de **Ajustes** (`Settings Icon`) para Modo Día/Noche, fuentes y opciones globales.
- **BottomBar**: 4 pestañas principales y 1 botón central destacado (`+`):
  1. **Inicio (Posiciones, Rachas y Resumen)**
  2. **Partidos (Fase Regular y Eliminatorias)**
  3. **[+] Botón Central: Agregar Equipos**
  4. **Estadísticas (Tabla General)**
  5. **Historial (Vídeos de Goles)**

---

## 3. Módulo Central [+]: Catálogo Mundial e Histórico de Equipos
- **Categorías Oficiales y Continentales**:
  1. **UEFA Champions League** (Todos los participantes actuales + todos los clubes históricos que la han disputado).
  2. **UEFA Europa League** (Todos los participantes actuales + históricos).
  3. **Copa Libertadores** (Todos los participantes actuales + históricos de Sudamérica y México).
  4. **Liga Colombiana (Categoría Primera A / BetPlay)**.
  5. **Liga Mexicana (Liga MX / Expansión)**.
  6. **Resto del Mundo por Continentes**:
     - **América** (MLS, Brasileirão, Primera División Argentina, Uruguay, Chile, Perú, Ecuador, etc.).
     - **Europa** (Premier League, LaLiga, Serie A, Bundesliga, Ligue 1, Eredivisie, Portugal, ligas del Este y Nórdicas).
     - **Asia** (Saudi Pro League, J1 League Japón, K League Corea, AFC Champions League).
     - **África** (CAF Champions League: Egipto, Marruecos, Túnez, Sudáfrica, etc.).
- **Filtros y Buscador Instantáneo**: Pestañas por competición/continente + buscador rápido por nombre sin repetir equipos ya registrados en el torneo activo.
- **Creación Manual (Fallback)**: Si el usuario desea un club amateur o de barrio que no exista, mantiene la opción de *"Agregar equipo personalizado"* (Nombre + Imagen de escudo desde galería) y asignación obligatoria del **Nombre del Capitán**.

---

## 4. Apartado 1: Tabla de Posiciones y Sistema de Rachas ("Últimos 5" + Historial)
- **Podio Superior (Top 3)**: 1° (centro con corona), 2° (izquierda) y 3° (derecha) con escudo, nombre y puntos.
- **Tabla de Posiciones con Columna "Últimos 5"**:
  - Columnas: `Equipo`, `PJ`, `G`, `E`, `P`, `Pts` y **`Últimos 5`**.
  - **Diseño de los Indicadores de Racha** (determinados exclusivamente por los partidos con estado `COMPLETADO` en la sección de Jornadas):
    - **Victoria (Ganó)**: Círculo **Verde** (`#16A34A`) con un check blanco (`✓`) en su interior.
    - **Empate (Empató)**: Círculo **Gris** (`#64748B`) con un guion/línea blanca (`−`) en su interior.
    - **Derrota (Perdió)**: Círculo **Rojo** (`#EF4444`) con doble borde circular blanco y una equis blanca (`✕`) en su interior, resaltando el último resultado como en la imagen de referencia.
  - En la tabla principal se muestran máximo los **últimos 5 partidos jugados** de cada equipo (ordenados cronológicamente de izquierda a derecha, donde el más reciente está a la derecha con anillo resaltado).
- **Botón Superior Izquierdo (Historial Completo de Rachas)**:
  - Al tocar el botón de historial en la esquina superior izquierda, se abre una vista detallada donde se muestran **TODOS** los resultados históricos (`✓`, `−`, `✕`) de cada equipo desde la Jornada 1 hasta la última jugada, indicando el rival y marcador al tocar cualquier círculo.

---

## 5. Apartado 2: Gestión de Partidos ("Fase" y "Eliminatorias" Mejoradas)

### Sub-pestaña A: "Fase" (Jornadas Automáticas y Manuales)
- Mantiene las 2 opciones por jornada: **Automatizar Jornadas** (sorteo aleatorio con control de descanso justo para impares) y **Editar / Crear Partidos Uno Mismo** (eligiendo quién juega contra quién con validaciones de no duplicidad).
- Al completar todos los datos del acta (goles, goleadores, asistencias, atajadas de portero, faltas, tarjetas amarillas/rojas y MVP con puntaje decimal `9.9`), el partido pasa a `COMPLETADO`, actualiza los puntos (`3`, `1`, `0`) y agrega automáticamente la burbuja de racha (`Verde`, `Gris` o `Roja`) a ambos clubes.

### Sub-pestaña B: "Eliminatorias" (Mínimo 4 Equipos, Por Tabla o Manual, Ida y Vuelta)
- **Mínimo de Participantes Reducido a 4 Equipos**:
  - Permite iniciar las eliminatorias con un **mínimo de 4 equipos** (Semifinales directas -> Final), o bien **8 equipos** (Cuartos de Final), **16 equipos** (Octavos de Final) o un límite personalizado par definido por el usuario ($4, 8, 16, 32$).
- **2 Modos de Armado de Eliminatorias con Límite Definido por el Usuario**:
  1. **Opción A: Automático según Puesto en la Tabla de Inicio**:
     - El usuario ingresa el **límite de equipos que clasificarán** (mínimo 4, ej. Top 4, Top 8 o Top 16 de la tabla de posiciones del Apartado 1).
     - El sistema toma los primeros $N$ clubes de la tabla general y arma automáticamente los cruces en orden de mérito deportivo: $1^\circ \text{ vs } N^\circ$, $2^\circ \text{ vs } (N-1)^\circ$, $3^\circ \text{ vs } (N-2)^\circ$, etc.
  2. **Opción B: Añadir y Editar Manualmente los Partidos**:
     - El usuario define el límite/ronda inicial (desde mínimo 4 equipos) y elige manualmente qué equipo se enfrenta contra quién en cada llave, con validación para no repetir un mismo club en dos llaves distintas.
- **Partidos Ida y Vuelta + Global + Penales**:
  - Todas las llaves se juegan a **Ida y Vuelta**. Al confirmar ambos partidos, suma el marcador global y avanza al ganador a la siguiente ronda. Si empatan en el global al cerrar la Vuelta, abre el modal de **Penales** (`3-4`, `4-5`).

---

## 6. Nuevo Diseño Innovador y Moderno para Compartir Partido Finalizado
- Al compartir un partido completado (de jornada o eliminatoria), se genera un **Póster Deportivo de Alta Gama ($1080 \times 1350\text{ px}$)** con estética tipo broadcast UEFA / EA Sports FC:
  - **Cabecera Broadcast**: Fondo oscuro texturizado con degradado neón/dorado, logo/marca de agua de **"La liga Alfa Bravo"**, badge de la Jornada o Ronda Eliminatoria y estado `FINALIZADO`.
  - **Marcador Hero**: Escudos gigantes con resplandor dinámico (glow del color del club), nombres tipográficos estilizados y marcador central en fuente condensada gigante (incluyendo marcador global y penales si es eliminatoria).
  - **Tarjeta Destacada "MVP DEL PARTIDO"**: Banner dorado/esmeralda con insignia de estrella, nombre del jugador MVP y su calificación decimal (`9.9`) en un badge circular de alto impacto.
  - **Grid Bento de Estadísticas Simétrico (2 Columnas Ordenadas)**:
    - *Sección Goleadores*: Icono de balón + nombres de goleadores y cantidad de goles por lado.
    - *Sección Asistencias*: Icono de botín/pase + nombres de asistidores y cantidad.
    - *Sección Muralla (Porteros)*: Icono de guantes + nombre de cada portero y sus atajadas registradas.
    - *Barra Comparativa de Disciplina*: Faltas, tarjetas amarillas y rojas.

---

## 7. Apartado 3: Tabla General de Estadísticas
- Mantiene las 3 pestañas principales en orden: **Máximos Goleadores de la Liga**, **Máximos Asistidores de la Liga** y **Máximos Atajadores de la Liga**, más filtros de **Tarjetas Amarillas** y **Tarjetas Rojas**, ordenados de mayor a menor con el escudo pequeño del equipo.

---

## 8. Apartado 4: Historial de Goles (Selección Directa de Partido Finalizado)
- **Flujo Simplificado sin Cuestionario Manual**:
  - Al agregar un vídeo (por link de **Google Drive** o desde la **multimedia del teléfono**), ya **no** se llena un cuestionario libre escribiendo marcador ni ambos equipos a mano.
  - **Paso 1**: Se despliega una **lista seleccionable con todos los partidos finalizados (`COMPLETADOS`)** mostrando los escudos, nombres de los equipos que jugaron y cómo quedó el partido (ej. `[Escudo] Real Madrid 3 - 1 Chelsea [Escudo] - Jornada 2`).
  - **Paso 2**: Una vez elegido el partido finalizado de la lista, el usuario únicamente elige/escribe:
    1. **El Equipo** que anotó el gol (selector rápido entre los 2 clubes de ese partido).
    2. **El Nombre del Goleador** (con sugerencias automáticas de los goleadores registrados en el acta de ese partido o entrada rápida).
  - **Visualización**: Arriba del reproductor de vídeo se muestra automáticamente el emparejamiento del partido seleccionado, cuánto quedó el marcador y el equipo + nombre del goleador destacado.