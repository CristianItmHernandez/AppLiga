package com.hograna.liga.ui.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.MatchPlayerEventEntity
import com.hograna.liga.data.local.entity.TeamEntity
import com.hograna.liga.ui.theme.Background
import com.hograna.liga.ui.theme.CardBackground
import com.hograna.liga.ui.theme.GoldAccent
import com.hograna.liga.ui.theme.GreenNeon
import com.hograna.liga.ui.theme.TextPrimary
import com.hograna.liga.ui.theme.TextSecondary

/**
 * Visual preview of the shareable match graphic.
 * The actual exported Bitmap is rendered by [ShareMatchGraphicUseCase].
 */
@Composable
fun ShareableMatchCard(
    match: MatchEntity,
    homeTeam: TeamEntity?,
    awayTeam: TeamEntity?,
    events: List<MatchPlayerEventEntity>
) {
    val scorers = events.filter { it.eventType == "GOAL" }
    val assisters = events.filter { it.eventType == "ASSIST" }
    val saves = events.filter { it.eventType == "SAVE" }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Background, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        // Top accent bar
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(GreenNeon, RoundedCornerShape(2.dp))
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Teams and score
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = homeTeam?.crestUri,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp).clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
                Text(homeTeam?.name ?: "Local", fontSize = 12.sp, color = TextPrimary, textAlign = TextAlign.Center)
            }

            Text(
                "${match.homeGoals}  -  ${match.awayGoals}",
                fontSize = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextPrimary
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = awayTeam?.crestUri,
                    contentDescription = null,
                    modifier = Modifier.size(56.dp).clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
                Text(awayTeam?.name ?: "Visitante", fontSize = 12.sp, color = TextPrimary, textAlign = TextAlign.Center)
            }
        }

        if (scorers.isNotEmpty()) {
            Spacer(modifier = Modifier.height(12.dp))
            Text("⚽ Goleadores", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenNeon)
            scorers.forEach { event ->
                Text("  ${event.playerName}  ×${event.count}", fontSize = 12.sp, color = TextPrimary)
            }
        }

        if (assisters.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text("🎯 Asistidores", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenNeon)
            assisters.forEach { event ->
                Text("  ${event.playerName}  ×${event.count}", fontSize = 12.sp, color = TextPrimary)
            }
        }

        if (saves.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Text("🧤 Atajadas", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenNeon)
            saves.forEach { event ->
                Text("  ${event.playerName}  ×${event.count}", fontSize = 12.sp, color = TextPrimary)
            }
        }

        if (match.mvpPlayerName != null) {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(GoldAccent.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⭐", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text("MVP del Partido", fontSize = 10.sp, color = GoldAccent)
                    Text(
                        "${match.mvpPlayerName}${match.mvpRating?.let { "  ${"%.1f".format(it)}" } ?: ""}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(GreenNeon, RoundedCornerShape(2.dp))
        )
    }
}
