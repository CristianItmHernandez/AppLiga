package com.hograna.liga.ui.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hograna.liga.domain.model.StatRow
import com.hograna.liga.ui.theme.Background
import com.hograna.liga.ui.theme.CardBackground
import com.hograna.liga.ui.theme.GoldAccent
import com.hograna.liga.ui.theme.GreenNeon
import com.hograna.liga.ui.theme.Surface
import com.hograna.liga.ui.theme.SurfaceVariant
import com.hograna.liga.ui.theme.TextDisabled
import com.hograna.liga.ui.theme.TextPrimary
import com.hograna.liga.ui.theme.TextSecondary
import com.hograna.liga.ui.theme.YellowCard
import com.hograna.liga.ui.theme.ErrorRed

private enum class StatsTab(val label: String, val emoji: String) {
    SCORERS("Goleadores", "⚽"),
    ASSISTERS("Asistidores", "🎯"),
    SAVERS("Atajadores", "🧤")
}

private enum class DisciplineFilter(val label: String) {
    NONE("Ninguno"),
    YELLOW("Amarillas"),
    RED("Rojas")
}

@Composable
fun HogRanaStatsScreen(viewModel: StatsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    var disciplineFilter by remember { mutableStateOf(DisciplineFilter.NONE) }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GreenNeon)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        // Title
        Text(
            "Estadísticas Liga Hog Rana",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
        )

        // 3 main tabs
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Surface,
            contentColor = GreenNeon,
            edgePadding = 0.dp
        ) {
            StatsTab.entries.forEachIndexed { index, tab ->
                Tab(
                    selected = selectedTab == index,
                    onClick = {
                        selectedTab = index
                        disciplineFilter = DisciplineFilter.NONE
                    },
                    text = {
                        Text(
                            "${tab.emoji} ${tab.label}",
                            color = if (selectedTab == index) GreenNeon else TextDisabled,
                            fontSize = 13.sp
                        )
                    }
                )
            }
        }

        // Discipline filter chips
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            FilterChip(
                selected = disciplineFilter == DisciplineFilter.YELLOW,
                onClick = {
                    disciplineFilter = if (disciplineFilter == DisciplineFilter.YELLOW)
                        DisciplineFilter.NONE else DisciplineFilter.YELLOW
                },
                label = { Text("🟨 Amarillas", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = YellowCard.copy(alpha = 0.2f),
                    selectedLabelColor = YellowCard,
                    labelColor = TextSecondary
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterChip(
                selected = disciplineFilter == DisciplineFilter.RED,
                onClick = {
                    disciplineFilter = if (disciplineFilter == DisciplineFilter.RED)
                        DisciplineFilter.NONE else DisciplineFilter.RED
                },
                label = { Text("🟥 Rojas", fontSize = 12.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = ErrorRed.copy(alpha = 0.2f),
                    selectedLabelColor = ErrorRed,
                    labelColor = TextSecondary
                )
            )
        }

        // Stats list
        val displayList = when {
            disciplineFilter == DisciplineFilter.YELLOW -> uiState.yellowCards
            disciplineFilter == DisciplineFilter.RED -> uiState.redCards
            else -> when (selectedTab) {
                0 -> uiState.scorers
                1 -> uiState.assisters
                else -> uiState.savers
            }
        }

        val listLabel = when {
            disciplineFilter == DisciplineFilter.YELLOW -> "Tarjetas Amarillas"
            disciplineFilter == DisciplineFilter.RED -> "Tarjetas Rojas"
            else -> StatsTab.entries[selectedTab].let { "${it.emoji} Máximos ${it.label}" }
        }

        if (displayList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 60.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Sin estadísticas aún\nCompletá partidos para ver datos",
                    color = TextDisabled,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        } else {
            // Section header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceVariant)
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "#",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.width(28.dp)
                )
                Text(
                    listLabel,
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "Total",
                    fontSize = 11.sp,
                    color = TextSecondary,
                    modifier = Modifier.width(48.dp),
                    textAlign = TextAlign.End
                )
            }

            LazyColumn(modifier = Modifier.fillMaxSize()) {
                itemsIndexed(displayList, key = { _, row -> row.playerName + row.teamName }) { index, row ->
                    StatRowItem(position = index + 1, row = row, isTopThree = index < 3)
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}

@Composable
private fun StatRowItem(position: Int, row: StatRow, isTopThree: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Position badge
        Box(
            modifier = Modifier
                .size(28.dp)
                .background(
                    when (position) {
                        1 -> GoldAccent.copy(alpha = 0.2f)
                        2 -> SurfaceVariant
                        3 -> SurfaceVariant.copy(alpha = 0.7f)
                        else -> CardBackground
                    },
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "$position",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = when (position) {
                    1 -> GoldAccent
                    2, 3 -> TextSecondary
                    else -> TextDisabled
                }
            )
        }

        Spacer(modifier = Modifier.width(10.dp))

        // Crest (24dp)
        AsyncImage(
            model = row.crestUri,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(10.dp))

        // Player + team
        Column(modifier = Modifier.weight(1f)) {
            Text(
                row.playerName,
                fontSize = 14.sp,
                fontWeight = if (isTopThree) FontWeight.SemiBold else FontWeight.Normal,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                row.teamName,
                fontSize = 11.sp,
                color = TextSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        // Total count
        Text(
            "${row.total}",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = when (position) {
                1 -> GoldAccent
                2, 3 -> GreenNeon
                else -> TextPrimary
            },
            modifier = Modifier.width(48.dp),
            textAlign = TextAlign.End
        )
    }
}
