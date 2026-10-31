# Especificación Funcional y Criterios de Aceptación (QUÉ Construir)

## Visión General
Aplicación nativa Android de gestión integral de torneos de fútbol ("Liga Hog Rana") con estética oscura moderna, barra de navegación inferior de 4 secciones y un botón de acción central (FAB integrado) para el registro de clubes.

---

## Barra de Navegación Inferior (BottomBar)
Consta de 4 pestañas principales y 1 botón central destacado (+):
1. **Inicio (Posiciones y Resumen)**
2. **Partidos (Fase Regular y Eliminatorias)**
3. **[+] Botón Central: Agregar Equipos**
4. **Estadísticas (Tabla General Liga Hog Rana)**
5. **Historial (Vídeos de Goles)**

---

## Módulo Central [+]: Registro y Catálogo de Equipos
- **Catálogo Predefinido**: Selector categorizado en 3 ligas: **UEFA Champions League**, **UEFA Europa League** y **Copa Libertadores**, mostrando escudo y nombre oficial.
- **Restricción de Unicidad**: Un equipo ya agregado al torneo actual queda bloqueado/oculto en el selector para evitar duplicados.
- **Buscador Instantáneo**: Barra de filtrado en tiempo real por nombre de club.
- **Creación Personalizada**: Si el equipo no existe en el buscador, opción *"Agregar equipo personalizado"* ingresando:
  - Nombre del equipo.
  - Imagen del escudo desde la galería del teléfono.
- **Dato Obligatorio**: Al seleccionar o crear un equipo, se exige ingresar el **Nombre del Capitán** antes de confirmar el registro.

---

## Apartado 1: Tabla de Posiciones y Partidos de la Jornada
- **Podio Superior (Top 3)**: Tarjeta destacada en la parte superior mostrando el 1° (centro, más grande con insignia de corona), 2° (izquierda) y 3° (derecha) lugar con su escudo, nombre y puntos actuales.
- **Tabla de Posiciones**: Lista estructurada con columnas: `Equipo` (Escudo + Nombre), `PJ`, `G`, `E`, `P`, `Pts` (en color verde).
  - Reglas de puntuación: Victoria = 3 pts, Empate = 1 pt, Derrota = 0 pts.
  - Ordenamiento automático: Puntos (DESC), Diferencia de Goles (DESC), Goles a Favor (DESC).
- **Resumen de Jornada Actual**: Vista rápida de los cruces correspondientes a la jornada activa.

---

## Apartado 2: Gestión de Partidos (Pestañas: "Fase" y "Eliminatorias")

### Sub-pestaña A: "Fase" (Jornadas Regulares)
- **Generación por Jornadas (Jornada 1, Jornada 2, ...)**:
  - Requiere mínimo 2 equipos registrados. Solicita confirmación en un diálogo modal antes de sortear.
  - **Equipos Pares**: Sorteo aleatorio emparejando a todos los clubes disponibles.
  - **Equipos Impares (Control de Descanso Justo)**: El sistema elige aleatoriamente los emparejamientos y deja **1 equipo en descanso (Bye)**. Regla estricta: un equipo que ya descansó **no puede volver a descansar** en jornadas futuras hasta que absolutamente todos los demás equipos del torneo hayan descansado el mismo número de veces.
- **Tarjeta de Partido (Match Card)**:
  - Encabezado con número de jornada, estado (`PENDIENTE` o `COMPLETADO` con marcador final), escudos y nombres enfrentados, botón de **Compartir (icono WhatsApp/Imagen)** y botón **Editar / Eliminar**.
- **Detalle y Acta del Partido (Al presionar la casilla)**:
  - Formulario para registrar:
    - Goles de cada equipo y lista de **Goleadores** (nombre del jugador + cantidad de goles).
    - **Asistidores** (nombre del jugador + cantidad de asistencias).
    - **Atajadas de Portero** (nombre del portero de cada equipo + cantidad de atajadas).
    - **Disciplina**: Cantidad de faltas, tarjetas amarillas (con nombres de jugadores) y tarjetas rojas (con nombres de jugadores).
    - **MVP del Partido**: Nombre del jugador y puntuación decimal (rango `1.0` a `10.0`, ej. `9.9`, `9.2`).
  - Al guardar todos los campos y confirmar, el estado pasa a `COMPLETADO` y recalcula instantáneamente la Tabla de Posiciones del Apartado 1 y las Estadísticas del Apartado 3.

### Sub-pestaña B: "Eliminatorias" (Formato Bracket Champions League)
- **Configuración de Cuadro (Desde Octavos de Final)**:
  - Opción 1: Generación automática tomando los mejores clasificados o sorteo directo.
  - Opción 2: Sub-sección manual para asignar qué equipos ocupan las llaves desde Octavos de Final.
- **Lógica Ida y Vuelta + Avance Automático**:
  - Cada llave consta de partido de **Ida** y partido de **Vuelta** (registrando los mismos datos de acta: MVP, goleadores, asistencias, atajadas, tarjetas).
  - Al confirmar y finalizar ambos partidos (Ida y Vuelta), el sistema suma los goles para obtener el **Marcador Global**.
  - **Desempate por Penales**: Si tras confirmar el partido de Vuelta el marcador global está empatado, se despliega obligatoriamente un modal adicional para ingresar el resultado de la tanda de penales (ej. `3 - 4`, `4 - 5`).
  - Determinado el ganador, el sistema crea/actualiza automáticamente el cruce de la siguiente ronda (Cuartos -> Semifinal -> Final).

### Exportación y Compartir Imagen del Partido (Fase y Eliminatorias)
- Al pulsar el botón de compartir en un partido completado, la app renderiza en memoria un gráfico estilizado (`Bitmap`) con:
  - Escudos, nombres de los equipos y resultado final (incluyendo global/penales si aplica).
  - Lista de goleadores con su cantidad de goles.
  - Lista de asistidores.
  - Porteros y cantidad de atajadas.
  - Destacado especial del **MVP del partido** con su calificación decimal.
- Se comparte directamente mediante el `Intent.ACTION_SEND` nativo (WhatsApp, redes sociales, etc.).

---

## Apartado 3: Tabla General de la Liga Hog Rana (Estadísticas)
- Acumulación automática en tiempo real de todos los eventos registrados en los partidos completados, ordenados de **mayor a menor**.
- **3 Pestañas Principales Seleccionables (Cuadro Organizado)** en orden estricto:
  1. **Máximos Goleadores de la Liga**
  2. **Máximos Asistidores de la Liga**
  3. **Máximos Atajadores de la Liga**
- **Filtro / Vista de Disciplina Adicional**:
  - **Jugadores con Tarjetas Amarillas**
  - **Jugadores con Tarjetas Rojas**
- **Formato de Fila**: `[Escudo pequeño 24dp]` + `Nombre del Jugador (y Club)` + `Cantidad total acumulada` alineada a la derecha.

---

## Apartado 4: Historial Histórico (Vídeos de Goles)
- Feed multimedia de partidos y goles históricos del torneo.
- **Formulario de Subida**:
  - Selección del partido asociado (o ingreso manual de Equipos enfrentados, marcador final y nombres de los goleadores).
  - Fuente de vídeo dual:
    1. **Enlace de Google Drive**: Pega el link compartido y el sistema extrae el flujo directo para reproducirlo en el reproductor integrado.
    2. **Multimedia del Teléfono**: Selector nativo (`PickVisualMedia`) para cargar un archivo `.mp4` local.
- **Tarjeta de Reproducción**:
  - Cabecera superior mostrando: Equipos que jugaron, resultado del partido y lista de goleadores.
  - Reproductor de vídeo integrado justo debajo con controles de reproducción eficientes.