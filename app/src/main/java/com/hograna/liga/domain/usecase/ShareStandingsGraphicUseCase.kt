package com.hograna.liga.domain.usecase

import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import coil.imageLoader
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.hograna.liga.domain.model.StandingsSharePayload
import com.hograna.liga.domain.model.StandingRow
import com.hograna.liga.domain.model.StandingWithStreakUiModel
import com.hograna.liga.domain.model.StreakBadgeType
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ShareStandingsGraphicUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {

    suspend fun share(
        standings: List<StandingRow>,
        streaks: Map<Long, List<StreakBadgeType>>
    ) {
        require(standings.isNotEmpty()) { "No hay equipos para compartir la tabla" }

        val payload = standings.take(10).mapIndexed { index, row ->
            StandingWithStreakUiModel(
                position = index + 1,
                teamId = row.teamId,
                teamName = row.teamName,
                crestUri = row.crestUri,
                played = row.played,
                won = row.won,
                drawn = row.drawn,
                lost = row.lost,
                points = row.points,
                lastFiveStreak = streaks[row.teamId].orEmpty().takeLast(5)
            )
        }.let { rows ->
            StandingsSharePayload(
                topThreePodium = rows.take(3),
                topTenTable = rows
            )
        }

        val shareFile = withContext(Dispatchers.IO) {
            val crests = mutableMapOf<Long, Bitmap?>()
            try {
                payload.topTenTable.forEach { team ->
                    crests[team.teamId] = loadCrest(team.crestUri)
                }

                val bitmap = render(payload, crests)
                try {
                    val file = File(context.cacheDir, "shared_standings_top10.png")
                    FileOutputStream(file).use { output ->
                        check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)) {
                            "No se pudo codificar la tabla de posiciones"
                        }
                    }
                    file
                } finally {
                    bitmap.recycle()
                }
            } finally {
                crests.values.filterNotNull().forEach(Bitmap::recycle)
            }
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            shareFile
        )
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            clipData = ClipData.newUri(context.contentResolver, "Tabla de posiciones Top 10", uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(
            Intent.createChooser(sendIntent, "Compartir tabla de posiciones").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }

    private suspend fun loadCrest(uri: String): Bitmap? {
        if (uri.isBlank()) return null
        val result = context.imageLoader.execute(
            ImageRequest.Builder(context)
                .data(uri)
                .size(CREST_SIZE)
                .allowHardware(false)
                .build()
        )
        return when (result) {
            is SuccessResult -> result.drawable.toBitmap(
                width = CREST_SIZE,
                height = CREST_SIZE,
                config = Bitmap.Config.ARGB_8888
            )
            is ErrorResult -> throw IllegalStateException(
                "No se pudo cargar el escudo para compartir",
                result.throwable
            )
            else -> throw IllegalStateException("La carga de un escudo no terminó correctamente")
        }
    }

    private fun render(
        payload: StandingsSharePayload,
        crests: Map<Long, Bitmap?>
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(BACKGROUND)

        drawTrophy(canvas, 88f, 66f)
        val titlePaint = paint(TEXT, 42f, Typeface.create("sans-serif", Typeface.BOLD))
        drawFittedText(
            canvas,
            "${payload.tournamentTitle} - Tabla Oficial (Top 10)",
            140f,
            78f,
            876f,
            titlePaint,
            Paint.Align.LEFT
        )
        canvas.drawText(
            "CLASIFICACIÓN GENERAL",
            66f,
            112f,
            paint(ACCENT, 23f, Typeface.create("sans-serif-medium", Typeface.NORMAL))
        )
        canvas.drawRoundRect(
            RectF(64f, 138f, WIDTH - 64f, 410f),
            28f,
            28f,
            paint(SURFACE)
        )
        drawPodium(canvas, payload.topThreePodium, crests)

        val tableTop = 432f
        canvas.drawRoundRect(
            RectF(64f, tableTop, WIDTH - 64f, tableTop + 62f),
            16f,
            16f,
            paint(HEADER)
        )
        drawTableHeader(canvas, tableTop)

        payload.topTenTable.forEachIndexed { index, row ->
            val top = tableTop + 72f + index * 78f
            val bottom = top + 68f
            val rowColor = if (index % 2 == 0) SURFACE else BACKGROUND
            canvas.drawRoundRect(
                RectF(64f, top, WIDTH - 64f, bottom),
                12f,
                12f,
                paint(rowColor)
            )
            if (index < 3) {
                canvas.drawRoundRect(
                    RectF(64f, top, 72f, bottom),
                    4f,
                    4f,
                    paint(if (index == 0) GOLD else ACCENT)
                )
            }
            drawStandingRow(canvas, row, crests[row.teamId], top)
        }

        canvas.drawText(
            "La liga Alfa Bravo",
            64f,
            1380f,
            paint(SUBTEXT, 20f, Typeface.create("sans-serif-medium", Typeface.NORMAL))
        )
        return bitmap
    }

    private fun drawTrophy(canvas: Canvas, centerX: Float, centerY: Float) {
        val trophyPaint = paint(GOLD)
        val bowl = Path().apply {
            moveTo(centerX - 20f, centerY - 23f)
            lineTo(centerX + 20f, centerY - 23f)
            lineTo(centerX + 16f, centerY - 2f)
            cubicTo(centerX + 13f, centerY + 13f, centerX - 13f, centerY + 13f, centerX - 16f, centerY - 2f)
            close()
        }
        canvas.drawPath(bowl, trophyPaint)
        canvas.drawRoundRect(
            RectF(centerX - 5f, centerY + 8f, centerX + 5f, centerY + 24f),
            3f,
            3f,
            trophyPaint
        )
        canvas.drawRoundRect(
            RectF(centerX - 17f, centerY + 23f, centerX + 17f, centerY + 30f),
            3f,
            3f,
            trophyPaint
        )
        val handlePaint = paint(GOLD, strokeWidth = 4f, style = Paint.Style.STROKE)
        canvas.drawArc(
            RectF(centerX - 31f, centerY - 20f, centerX - 12f, centerY + 5f),
            250f,
            205f,
            false,
            handlePaint
        )
        canvas.drawArc(
            RectF(centerX + 12f, centerY - 20f, centerX + 31f, centerY + 5f),
            85f,
            205f,
            false,
            handlePaint
        )
    }

    private fun drawPodium(
        canvas: Canvas,
        podium: List<StandingWithStreakUiModel>,
        crests: Map<Long, Bitmap?>
    ) {
        if (podium.isEmpty()) return
        val centers = when (podium.size) {
            1 -> listOf(540f)
            2 -> listOf(350f, 730f)
            else -> listOf(250f, 540f, 830f)
        }
        val podiumOrder = if (podium.size >= 3) {
            listOf(podium[1], podium[0], podium[2])
        } else {
            podium
        }
        podiumOrder.forEachIndexed { index, row ->
            val centerX = centers[index]
            val centerY = if (row.position == 1) 235f else 250f
            val crestSize = if (row.position == 1) 104f else 82f
            val crest = crests[row.teamId]
            if (crest != null) {
                canvas.drawBitmap(
                    crest,
                    null,
                    RectF(
                        centerX - crestSize / 2,
                        centerY - crestSize / 2,
                        centerX + crestSize / 2,
                        centerY + crestSize / 2
                    ),
                    paint(TEXT)
                )
            } else {
                canvas.drawCircle(centerX, centerY, crestSize / 2, paint(HEADER))
            }
            if (row.position == 1) {
                drawCrown(canvas, centerX, 172f)
            }
            val namePaint = paint(TEXT, 23f, Typeface.create("sans-serif-medium", Typeface.BOLD))
            drawFittedText(canvas, row.teamName, centerX, 330f, 240f, namePaint, Paint.Align.CENTER)
            canvas.drawText(
                "${row.position}° · ${row.points} pts",
                centerX,
                368f,
                paint(GOLD, 21f, Typeface.create("sans-serif", Typeface.BOLD), Paint.Align.CENTER)
            )
        }
    }

    private fun drawCrown(canvas: Canvas, centerX: Float, centerY: Float) {
        val crown = Path().apply {
            moveTo(centerX - 22f, centerY + 10f)
            lineTo(centerX - 18f, centerY - 12f)
            lineTo(centerX - 6f, centerY - 1f)
            lineTo(centerX, centerY - 17f)
            lineTo(centerX + 6f, centerY - 1f)
            lineTo(centerX + 18f, centerY - 12f)
            lineTo(centerX + 22f, centerY + 10f)
            close()
        }
        canvas.drawPath(crown, paint(GOLD))
        canvas.drawRect(
            centerX - 20f,
            centerY + 13f,
            centerX + 20f,
            centerY + 18f,
            paint(GOLD)
        )
    }

    private fun drawTableHeader(canvas: Canvas, top: Float) {
        val y = top + 40f
        val headerPaint = paint(SUBTEXT, 19f, Typeface.create("sans-serif", Typeface.BOLD))
        canvas.drawText("#", 86f, y, headerPaint)
        canvas.drawText("Equipo", 150f, y, headerPaint)
        listOf("PJ" to 565f, "G" to 620f, "E" to 670f, "P" to 720f, "Pts" to 780f)
            .forEach { (text, x) -> canvas.drawText(text, x, y, headerPaint) }
        canvas.drawText("Últimos 5", 832f, y, headerPaint)
    }

    private fun drawStandingRow(
        canvas: Canvas,
        row: StandingWithStreakUiModel,
        crest: Bitmap?,
        top: Float
    ) {
        val baseline = top + 44f
        val numberPaint = paint(SUBTEXT, 22f, Typeface.DEFAULT_BOLD)
        canvas.drawText(row.position.toString(), 88f, baseline, numberPaint)

        if (crest != null) {
            canvas.drawBitmap(crest, null, RectF(112f, top + 11f, 158f, top + 57f), paint(TEXT))
        } else {
            canvas.drawCircle(135f, top + 34f, 22f, paint(HEADER))
        }

        drawFittedText(
            canvas,
            row.teamName,
            172f,
            baseline,
            370f,
            paint(TEXT, 23f, Typeface.create("sans-serif-medium", Typeface.NORMAL)),
            Paint.Align.LEFT
        )
        val statPaint = paint(SUBTEXT, 20f, Typeface.DEFAULT)
        listOf(
            row.played.toString() to 565f,
            row.won.toString() to 620f,
            row.drawn.toString() to 670f,
            row.lost.toString() to 720f
        ).forEach { (value, x) -> canvas.drawText(value, x, baseline, statPaint) }
        canvas.drawText(
            row.points.toString(),
            780f,
            baseline,
            paint(ACCENT, 23f, Typeface.DEFAULT_BOLD)
        )
        row.lastFiveStreak.takeLast(5).forEachIndexed { index, result ->
            drawStreakBadge(canvas, 846f + index * 34f, top + 34f, result)
        }
    }

    private fun drawStreakBadge(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        result: StreakBadgeType
    ) {
        val color = when (result) {
            StreakBadgeType.WIN -> WIN
            StreakBadgeType.DRAW -> DRAW
            StreakBadgeType.LOSS -> LOSS
        }
        if (result == StreakBadgeType.LOSS) {
            canvas.drawCircle(centerX, centerY, 14f, paint(TEXT))
            canvas.drawCircle(centerX, centerY, 11f, paint(LOSS))
            canvas.drawCircle(
                centerX,
                centerY,
                8f,
                paint(TEXT, strokeWidth = 2f, style = Paint.Style.STROKE)
            )
        } else {
            canvas.drawCircle(centerX, centerY, 12f, paint(color))
        }
        val symbol = when (result) {
            StreakBadgeType.WIN -> "✓"
            StreakBadgeType.DRAW -> "−"
            StreakBadgeType.LOSS -> "✕"
        }
        canvas.drawText(
            symbol,
            centerX,
            centerY + 6f,
            paint(TEXT, 17f, Typeface.DEFAULT_BOLD, Paint.Align.CENTER)
        )
    }

    private fun drawFittedText(
        canvas: Canvas,
        text: String,
        x: Float,
        y: Float,
        maxWidth: Float,
        sourcePaint: Paint,
        align: Paint.Align
    ) {
        val fittedPaint = Paint(sourcePaint).apply { textAlign = align }
        while (fittedPaint.textSize > 16f && fittedPaint.measureText(text) > maxWidth) {
            fittedPaint.textSize -= 1f
        }
        val visibleText = if (fittedPaint.measureText(text) <= maxWidth) {
            text
        } else {
            var shortened = text
            while (shortened.isNotEmpty() &&
                fittedPaint.measureText("$shortened…") > maxWidth
            ) {
                shortened = shortened.dropLast(1)
            }
            "$shortened…"
        }
        canvas.drawText(visibleText, x, y, fittedPaint)
    }

    private fun paint(
        color: Int,
        textSize: Float = 0f,
        typeface: Typeface = Typeface.DEFAULT,
        align: Paint.Align = Paint.Align.LEFT,
        strokeWidth: Float = 0f,
        style: Paint.Style = Paint.Style.FILL
    ) = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        this.color = color
        this.textSize = textSize
        this.typeface = typeface
        textAlign = align
        this.strokeWidth = strokeWidth
        this.style = style
    }

    private companion object {
        const val WIDTH = 1080
        const val HEIGHT = 1440
        const val CREST_SIZE = 64
        val BACKGROUND = android.graphics.Color.rgb(18, 18, 18)
        val SURFACE = android.graphics.Color.rgb(30, 30, 30)
        val HEADER = android.graphics.Color.rgb(42, 42, 42)
        val TEXT = android.graphics.Color.WHITE
        val SUBTEXT = android.graphics.Color.rgb(176, 176, 176)
        val ACCENT = android.graphics.Color.rgb(46, 204, 113)
        val GOLD = android.graphics.Color.rgb(241, 196, 15)
        val WIN = android.graphics.Color.rgb(22, 163, 74)
        val DRAW = android.graphics.Color.rgb(100, 116, 139)
        val LOSS = android.graphics.Color.rgb(239, 68, 68)
    }
}
