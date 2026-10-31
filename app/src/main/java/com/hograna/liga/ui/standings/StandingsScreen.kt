package com.hograna.liga.ui.standings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hograna.liga.R
import com.hograna.liga.domain.model.StandingRow
import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.TeamEntity
import com.hograna.liga.ui.components.StreakBadgesRow
import com.hograna.liga.ui.theme.Background
import com.hograna.liga.ui.theme.CardBackground
import com.hograna.liga.ui.theme.GoldAccent
import com.hograna.liga.ui.theme.GreenNeon
import com.hograna.liga.ui.theme.Surface
import com.hograna.liga.ui.theme.SurfaceVariant
import com.hograna.liga.ui.theme.TextDisabled
import com.hograna.liga.ui.theme.TextPrimary
import com.hograna.liga.ui.theme.TextSecondary

@Composable
fun StandingsScreen(viewModel: StandingsViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.shareError) {
        uiState.shareError?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.dismissShareError()
        }
    }

    if (uiState.isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = GreenNeon)
        }
        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(horizontal = 16.dp)
        ) {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Posiciones",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    IconButton(
                        onClick = viewModel::shareTopTen,
                        enabled = uiState.standings.isNotEmpty()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = stringResource(R.string.share_standings),
                            tint = GreenNeon
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (uiState.standings.size >= 3) {
                item {
                    PodiumSection(standings = uiState.standings.take(3))
                    Spacer(modifier = Modifier.height(16.dp))
                }
            } else if (uiState.standings.isEmpty()) {
                item {
                    Box(
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Agrega equipos para ver las posiciones",
                            color = TextDisabled,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }

            if (uiState.currentMatchday > 0) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            "Jornada actual · ${uiState.currentMatchday}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenNeon,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                        uiState.currentMatches.forEach { summary ->
                            MatchdaySummaryRow(
                                match = summary.match,
                                homeTeam = summary.homeTeam,
                                awayTeam = summary.awayTeam
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            if (uiState.standings.isNotEmpty()) {
                item {
                    StandingsTableHeader()
                }
                itemsIndexed(uiState.standings, key = { _, row -> row.teamId }) { index, row ->
                    StandingsRow(
                        position = index + 1,
                        row = row,
                        streak = uiState.streaks[row.teamId].orEmpty().takeLast(5)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun MatchdaySummaryRow(
    match: MatchEntity,
    homeTeam: TeamEntity?,
    awayTeam: TeamEntity?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(CardBackground, RoundedCornerShape(10.dp))
            .padding(horizontal = 10.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = homeTeam?.crestUri,
            contentDescription = homeTeam?.name,
            modifier = Modifier.size(28.dp).clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Text(
            homeTeam?.name ?: "Equipo",
            color = TextPrimary,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f).padding(start = 6.dp)
        )
        Text(
            when {
                match.isBye -> "DESCANSA"
                match.status == "COMPLETED" -> "${match.homeGoals} - ${match.awayGoals}"
                else -> "vs"
            },
            color = if (match.status == "COMPLETED") GreenNeon else TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 6.dp)
        )
        Text(
            awayTeam?.name ?: "",
            color = TextPrimary,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
        if (awayTeam != null) {
            AsyncImage(
                model = awayTeam.crestUri,
                contentDescription = awayTeam.name,
                modifier = Modifier.size(28.dp).clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun PodiumSection(standings: List<StandingRow>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "🏆 Podio",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                // 2nd place (left)
                if (standings.size >= 2) {
                    PodiumItem(
                        standing = standings[1],
                        position = 2,
                        crestSize = 56.dp,
                        isCenter = false
                    )
                }
                // 1st place (center, bigger)
                PodiumItem(
                    standing = standings[0],
                    position = 1,
                    crestSize = 80.dp,
                    isCenter = true
                )
                // 3rd place (right)
                if (standings.size >= 3) {
                    PodiumItem(
                        standing = standings[2],
                        position = 3,
                        crestSize = 56.dp,
                        isCenter = false
                    )
                }
            }
        }
    }
}

@Composable
private fun PodiumItem(
    standing: StandingRow,
    position: Int,
    crestSize: androidx.compose.ui.unit.Dp,
    isCenter: Boolean
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (isCenter) {
            Text("👑", fontSize = 24.sp)
        }
        AsyncImage(
            model = standing.crestUri,
            contentDescription = standing.teamName,
            modifier = Modifier
                .size(crestSize)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = standing.teamName,
            fontSize = if (isCenter) 13.sp else 11.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = "${standing.points} pts",
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent
        )
        Text(
            text = when (position) { 1 -> "🥇"; 2 -> "🥈"; else -> "🥉" },
            fontSize = 18.sp
        )
    }
}

@Composable
private fun StandingsTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceVariant, RoundedCornerShape(8.dp))
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("#", fontSize = 11.sp, color = TextSecondary, modifier = Modifier.width(22.dp))
        Spacer(modifier = Modifier.width(56.dp))
        Text(
            stringResource(R.string.standings),
            fontSize = 11.sp,
            color = TextSecondary,
            modifier = Modifier.weight(1f)
        )
        listOf(R.string.pj, R.string.wins, R.string.draws, R.string.losses, R.string.pts).forEach {
            Text(
                stringResource(it),
                fontSize = 11.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(24.dp)
            )
        }
        Text(
            text = stringResource(R.string.last_five),
            fontSize = 9.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(87.dp)
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun StandingsRow(
    position: Int,
    row: StandingRow,
    streak: List<com.hograna.liga.domain.model.StreakBadgeType>
) {
    val bgColor = when (position) {
        1 -> GoldAccent.copy(alpha = 0.06f)
        2, 3 -> GreenNeon.copy(alpha = 0.04f)
        else -> Color.Transparent
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .background(bgColor, RoundedCornerShape(6.dp))
            .padding(horizontal = 4.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "$position",
            fontSize = 12.sp,
            color = TextSecondary,
            modifier = Modifier.width(22.dp)
        )
        AsyncImage(
            model = row.crestUri,
            contentDescription = row.teamName,
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = row.teamName,
            fontSize = 13.sp,
            color = TextPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            "${row.played}",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(24.dp)
        )
        Text(
            "${row.won}",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(24.dp)
        )
        Text(
            "${row.drawn}",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(24.dp)
        )
        Text(
            "${row.lost}",
            fontSize = 13.sp,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(24.dp)
        )
        Text(
            "${row.points}",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = GreenNeon,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(24.dp)
        )
        StreakBadgesRow(
            results = streak,
            modifier = Modifier.width(87.dp),
            showEmptySlots = true
        )
    }
}
