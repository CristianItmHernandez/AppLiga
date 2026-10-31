package com.hograna.liga.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hograna.liga.domain.model.StreakBadgeType

@Composable
fun StreakBadgesRow(
    results: List<StreakBadgeType>,
    modifier: Modifier = Modifier,
    showEmptySlots: Boolean = false
) {
    val visibleResults = if (showEmptySlots) {
        List(5 - results.size.coerceAtMost(5)) { null } + results.takeLast(5)
    } else {
        results.takeLast(5)
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        visibleResults.forEachIndexed { index, result ->
            if (result == null) {
                Box(Modifier.size(15.dp))
            } else {
                StreakBadge(result, emphasize = index == visibleResults.lastIndex)
            }
        }
    }
}

@Composable
private fun StreakBadge(result: StreakBadgeType, emphasize: Boolean) {
    val description = when (result) {
        StreakBadgeType.WIN -> "Victoria"
        StreakBadgeType.DRAW -> "Empate"
        StreakBadgeType.LOSS -> "Derrota"
    }
    val size = if (emphasize) 17.dp else 15.dp
    if (result == StreakBadgeType.LOSS && emphasize) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Color.White)
                .semantics { contentDescription = description },
            contentAlignment = Alignment.Center
        ) {
            Box(
                Modifier
                    .size(13.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFEF4444)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    Modifier
                        .size(9.dp)
                        .border(1.dp, Color.White, CircleShape)
                )
                Text(
                    text = "✕",
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    } else {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(
                    when (result) {
                        StreakBadgeType.WIN -> Color(0xFF16A34A)
                        StreakBadgeType.DRAW -> Color(0xFF64748B)
                        StreakBadgeType.LOSS -> Color(0xFFEF4444)
                    }
                )
                .semantics { contentDescription = description },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (result) {
                    StreakBadgeType.WIN -> "✓"
                    StreakBadgeType.DRAW -> "−"
                    StreakBadgeType.LOSS -> "✕"
                },
                color = Color.White,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
