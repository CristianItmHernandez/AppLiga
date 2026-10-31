package com.hograna.liga.domain.usecase

import android.content.Context
import android.content.ClipData
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
import coil.request.ImageRequest
import coil.request.ErrorResult
import coil.request.SuccessResult
import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.MatchPlayerEventEntity
import com.hograna.liga.data.repository.TournamentRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class ShareMatchGraphicUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val repository: TournamentRepository
) {

    private val bgColor = android.graphics.Color.parseColor("#121212")
    private val accentColor = android.graphics.Color.parseColor("#2ECC71")
    private val goldColor = android.graphics.Color.parseColor("#F1C40F")
    private val textColor = android.graphics.Color.WHITE
    private val subTextColor = android.graphics.Color.parseColor("#B0B0B0")

    suspend fun share(matchId: Long) {
        val shareFile = withContext(Dispatchers.IO) {
            val match = repository.getMatchById(matchId)
                ?: throw IllegalArgumentException("No se encontró el partido")
            check(match.status == "COMPLETED") { "Solo se pueden compartir partidos completados" }
            val awayTeamId = match.awayTeamId
                ?: throw IllegalStateException("El partido no tiene un equipo visitante")
            val teams = repository.getAllTeamsSuspend().associateBy { it.id }
            val homeTeam = teams[match.homeTeamId]
                ?: throw IllegalStateException("No se encontró el equipo local")
            val awayTeam = teams[awayTeamId]
                ?: throw IllegalStateException("No se encontró el equipo visitante")
            val events = repository.getPlayerEventsForMatchSuspend(matchId)
            val knockoutSummary = match.knockoutTieId?.let { tieId ->
                buildKnockoutSummary(tieId)
            }
            val homeCrest = loadCrest(homeTeam.crestUri)
            val awayCrest = loadCrest(awayTeam.crestUri)
            val bitmap = try {
                renderMatchGraphic(
                    match = match,
                    homeTeamName = homeTeam.name,
                    awayTeamName = awayTeam.name,
                    homeCrest = homeCrest,
                    awayCrest = awayCrest,
                    events = events,
                    knockoutSummary = knockoutSummary
                )
            } finally {
                homeCrest?.recycle()
                awayCrest?.recycle()
            }

            val file = File(context.cacheDir, "match_${matchId}_${System.currentTimeMillis()}.png")
            try {
                FileOutputStream(file).use { output ->
                    check(bitmap.compress(Bitmap.CompressFormat.PNG, 100, output)) {
                        "No se pudo codificar la imagen del partido"
                    }
                }
            } finally {
                bitmap.recycle()
            }
            file
        }

        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            shareFile
        )
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            clipData = ClipData.newUri(context.contentResolver, "Resumen del partido", uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(
            Intent.createChooser(sendIntent, "Compartir partido").apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
        )
    }

    private suspend fun loadCrest(uri: String): Bitmap? {
        if (uri.isBlank()) return null
        val result = context.imageLoader.execute(
            ImageRequest.Builder(context)
                .data(uri)
                .size(160)
                .allowHardware(false)
                .build()
        )
        return when (result) {
            is SuccessResult -> result.drawable.toBitmap(
                width = 160,
                height = 160,
                config = Bitmap.Config.ARGB_8888
            )
            is ErrorResult -> throw IllegalStateException("No se pudo cargar un escudo", result.throwable)
            else -> throw IllegalStateException("La carga del escudo no terminó correctamente")
        }
    }

    private suspend fun buildKnockoutSummary(tieId: Long): String {
        val tie = repository.getTieById(tieId)
            ?: throw IllegalStateException("No se encontró la llave del partido")
        val teamAId = tie.teamAId
            ?: throw IllegalStateException("La llave no tiene ambos equipos asignados")
        val teamBId = tie.teamBId
            ?: throw IllegalStateException("La llave no tiene ambos equipos asignados")
        val legs = repository.getMatchesByTieSuspend(tieId).filter {
            it.status == "COMPLETED" && !it.isBye
        }
        val teamAGoals = legs.sumOf { leg ->
            if (leg.homeTeamId == teamAId) leg.homeGoals else leg.awayGoals
        }
        val teamBGoals = legs.sumOf { leg ->
            if (leg.homeTeamId == teamBId) leg.homeGoals else leg.awayGoals
        }
        val teamA = repository.getAllTeamsSuspend().firstOrNull { it.id == teamAId }?.name
            ?: throw IllegalStateException("No se encontró el primer equipo de la llave")
        val teamB = repository.getAllTeamsSuspend().firstOrNull { it.id == teamBId }?.name
            ?: throw IllegalStateException("No se encontró el segundo equipo de la llave")
        val score = "Global: $teamA $teamAGoals - $teamBGoals $teamB"
        val penalties = if (tie.teamAPenalties != null && tie.teamBPenalties != null) {
            " | Penales: ${tie.teamAPenalties} - ${tie.teamBPenalties}"
        } else {
            ""
        }
        return "$score$penalties"
    }

    private fun renderMatchGraphic(
        match: MatchEntity,
        homeTeamName: String,
        awayTeamName: String,
        homeCrest: Bitmap?,
        awayCrest: Bitmap?,
        events: List<MatchPlayerEventEntity>,
        knockoutSummary: String?
    ): Bitmap {
        val width = 1080
        val margin = 64f
        val contentWidth = width - margin * 2
        val eventGroups = listOf(
            "GOAL" to "Goleadores",
            "ASSIST" to "Asistencias",
            "SAVE" to "Atajadas",
            "YELLOW_CARD" to "Tarjetas amarillas",
            "RED_CARD" to "Tarjetas rojas"
        ).mapNotNull { (type, label) ->
            events.filter { it.eventType == type }
                .takeIf { it.isNotEmpty() }
                ?.let { label to it }
        }
        val bodyPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = textColor
            textSize = 25f
            typeface = Typeface.DEFAULT
            textAlign = Paint.Align.LEFT
        }
        val estimatedBodyLines = eventGroups.sumOf { (_, group) ->
            1 + group.sumOf { event ->
                ((bodyPaint.measureText("${event.playerName}  ×${event.count}") / contentWidth).toInt() + 1)
                    .coerceAtLeast(1)
            }
        }
        val height = (360 + estimatedBodyLines * 34 + if (match.mvpPlayerName != null) 72 else 0)
            .coerceAtLeast(500)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(bgColor)
        val accentPaint = Paint().apply { color = accentColor }
        canvas.drawRect(0f, 0f, width.toFloat(), 10f, accentPaint)

        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accentColor
            textSize = 24f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("LIGA HOG RANA  •  ACTA DEL PARTIDO", width / 2f, 54f, titlePaint)

        drawCrest(canvas, homeCrest, RectF(100f, 82f, 250f, 232f))
        drawCrest(canvas, awayCrest, RectF(830f, 82f, 980f, 232f))

        val teamPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = textColor
            textSize = 29f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(homeTeamName, width / 4f, 267f, teamPaint)
        canvas.drawText(awayTeamName, width * 3 / 4f, 267f, teamPaint)

        val scorePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = textColor
            textSize = 64f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText("${match.homeGoals}  -  ${match.awayGoals}", width / 2f, 180f, scorePaint)

        var yPosition = 315f
        knockoutSummary?.let {
            val summaryPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = goldColor
                textSize = 22f
                typeface = Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(it, width / 2f, yPosition, summaryPaint)
            yPosition += 38f
        }

        val headingPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = accentColor
            textSize = 24f
            typeface = Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.LEFT
        }
        eventGroups.forEach { (label, group) ->
            canvas.drawText(label, margin, yPosition, headingPaint)
            yPosition += 32f
            group.forEach { event ->
                yPosition = drawWrappedText(
                    canvas = canvas,
                    text = "${event.playerName}  ×${event.count}",
                    x = margin + 16f,
                    y = yPosition,
                    maxWidth = contentWidth - 16f,
                    paint = bodyPaint
                )
            }
            yPosition += 10f
        }

        match.mvpPlayerName?.let { name ->
            val mvpPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = goldColor
                textSize = 25f
                typeface = Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }
            canvas.drawText(
                "MVP: $name${match.mvpRating?.let { "  ${"%.1f".format(it)}" }.orEmpty()}",
                width / 2f,
                yPosition + 12f,
                mvpPaint
            )
        }

        canvas.drawRect(0f, height - 10f, width.toFloat(), height.toFloat(), accentPaint)
        return bitmap
    }

    private fun drawCrest(canvas: Canvas, bitmap: Bitmap?, bounds: RectF) {
        if (bitmap == null) return
        val circle = Path().apply {
            addCircle(bounds.centerX(), bounds.centerY(), bounds.width() / 2f, Path.Direction.CW)
        }
        canvas.save()
        canvas.clipPath(circle)
        canvas.drawBitmap(bitmap, null, bounds, Paint(Paint.ANTI_ALIAS_FLAG))
        canvas.restore()
    }

    private fun drawWrappedText(
        canvas: Canvas,
        text: String,
        x: Float,
        y: Float,
        maxWidth: Float,
        paint: Paint
    ): Float {
        var remaining = text
        var baseline = y
        while (remaining.isNotBlank()) {
            val characters = paint.breakText(remaining, true, maxWidth, null).coerceAtLeast(1)
            canvas.drawText(remaining.substring(0, characters).trimEnd(), x, baseline, paint)
            remaining = remaining.substring(characters).trimStart()
            baseline += 32f
        }
        return baseline
    }
}
