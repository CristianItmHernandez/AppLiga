package com.hograna.liga.ui.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.MatchPlayerEventEntity
import com.hograna.liga.data.local.entity.TeamEntity
import com.hograna.liga.domain.usecase.CompleteMatchInput
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
import kotlinx.coroutines.launch

private data class PlayerEventDraft(
    val name: String = "",
    val count: Int = 1,
    val teamId: Long
)

private fun List<PlayerEventDraft>.toMatchEvents(
    matchId: Long,
    eventType: String
): List<MatchPlayerEventEntity> = filter { it.name.isNotBlank() }.map {
    MatchPlayerEventEntity(
        matchId = matchId,
        teamId = it.teamId,
        playerName = it.name.trim(),
        eventType = eventType,
        count = it.count
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchDetailSheet(
    match: MatchEntity,
    viewModel: MatchesViewModel,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val homeTeam = viewModel.teamById(match.homeTeamId)
    val awayTeam = viewModel.teamById(match.awayTeamId)

    // Local draft state initialized from existing match data
    var homeGoals by remember { mutableIntStateOf(match.homeGoals) }
    var awayGoals by remember { mutableIntStateOf(match.awayGoals) }
    var homeFouls by remember { mutableIntStateOf(match.homeFouls) }
    var awayFouls by remember { mutableIntStateOf(match.awayFouls) }
    var mvpName by remember { mutableStateOf(match.mvpPlayerName ?: "") }
    var mvpRating by remember { mutableFloatStateOf((match.mvpRating ?: 8.0).toFloat()) }

    val eventFlow = remember(match.id) { viewModel.playerEvents(match.id) }
    val savedEvents by eventFlow.collectAsStateWithLifecycle(initialValue = null)
    val scorers = remember(match.id) { mutableStateListOf<PlayerEventDraft>() }
    val assisters = remember(match.id) { mutableStateListOf<PlayerEventDraft>() }
    val homeGoalkeepers = remember(match.id) { mutableStateListOf<PlayerEventDraft>() }
    val awayGoalkeepers = remember(match.id) { mutableStateListOf<PlayerEventDraft>() }
    val yellowCards = remember(match.id) { mutableStateListOf<PlayerEventDraft>() }
    val redCards = remember(match.id) { mutableStateListOf<PlayerEventDraft>() }
    var eventsLoaded by remember(match.id) { mutableStateOf(false) }
    val playerTeams = listOfNotNull(homeTeam, awayTeam)
    val defaultTeamId = match.homeTeamId

    LaunchedEffect(savedEvents, match.id) {
        val events = savedEvents ?: return@LaunchedEffect
        if (eventsLoaded) return@LaunchedEffect
        events.filter { it.eventType == "GOAL" }.forEach {
            scorers.add(PlayerEventDraft(it.playerName, it.count, it.teamId))
        }
        events.filter { it.eventType == "ASSIST" }.forEach {
            assisters.add(PlayerEventDraft(it.playerName, it.count, it.teamId))
        }
        events.filter { it.eventType == "SAVE" && it.teamId == match.homeTeamId }.forEach {
            homeGoalkeepers.add(PlayerEventDraft(it.playerName, it.count, it.teamId))
        }
        events.filter { it.eventType == "SAVE" && it.teamId == match.awayTeamId }.forEach {
            awayGoalkeepers.add(PlayerEventDraft(it.playerName, it.count, it.teamId))
        }
        events.filter { it.eventType == "YELLOW_CARD" }.forEach {
            yellowCards.add(PlayerEventDraft(it.playerName, it.count, it.teamId))
        }
        events.filter { it.eventType == "RED_CARD" }.forEach {
            redCards.add(PlayerEventDraft(it.playerName, it.count, it.teamId))
        }
        eventsLoaded = true
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Surface
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Acta del Partido", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Teams & score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    AsyncImage(
                        model = homeTeam?.crestUri,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp).clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Text(homeTeam?.name ?: "Local", fontSize = 12.sp, color = TextPrimary)
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    GoalCounter(
                        label = homeTeam?.name?.take(3) ?: "LOC",
                        value = homeGoals,
                        onIncrement = { homeGoals++ },
                        onDecrement = { if (homeGoals > 0) homeGoals-- }
                    )
                    Text("vs", fontSize = 14.sp, color = TextDisabled)
                    GoalCounter(
                        label = awayTeam?.name?.take(3) ?: "VIS",
                        value = awayGoals,
                        onIncrement = { awayGoals++ },
                        onDecrement = { if (awayGoals > 0) awayGoals-- }
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    AsyncImage(
                        model = awayTeam?.crestUri,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp).clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Text(awayTeam?.name ?: "Visitante", fontSize = 12.sp, color = TextPrimary)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            SectionDivider()

            // Scorers
            SectionHeader("⚽ Goleadores") { scorers.add(PlayerEventDraft(teamId = defaultTeamId)) }
            scorers.forEachIndexed { i, draft ->
                PlayerEventRow(
                    draft = draft,
                    onNameChange = { scorers[i] = scorers[i].copy(name = it) },
                    onCountChange = { scorers[i] = scorers[i].copy(count = it) },
                    onRemove = { scorers.removeAt(i) },
                    teams = playerTeams,
                    onTeamChange = { scorers[i] = scorers[i].copy(teamId = it) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            SectionDivider()

            // Assisters
            SectionHeader("🎯 Asistidores") { assisters.add(PlayerEventDraft(teamId = defaultTeamId)) }
            assisters.forEachIndexed { i, draft ->
                PlayerEventRow(
                    draft = draft,
                    onNameChange = { assisters[i] = assisters[i].copy(name = it) },
                    onCountChange = { assisters[i] = assisters[i].copy(count = it) },
                    onRemove = { assisters.removeAt(i) },
                    teams = playerTeams,
                    onTeamChange = { assisters[i] = assisters[i].copy(teamId = it) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            SectionDivider()

            // Goalkeepers saves
            SectionHeader("🧤 Atajadas Local (${homeTeam?.name ?: ""})") {
                homeGoalkeepers.add(PlayerEventDraft(teamId = defaultTeamId))
            }
            homeGoalkeepers.forEachIndexed { i, draft ->
                PlayerEventRow(
                    draft = draft,
                    onNameChange = { homeGoalkeepers[i] = homeGoalkeepers[i].copy(name = it) },
                    onCountChange = { homeGoalkeepers[i] = homeGoalkeepers[i].copy(count = it) },
                    onRemove = { homeGoalkeepers.removeAt(i) },
                    countLabel = "Atajadas",
                    teams = playerTeams,
                    onTeamChange = { homeGoalkeepers[i] = homeGoalkeepers[i].copy(teamId = it) }
                )
            }
            SectionHeader("🧤 Atajadas Visitante (${awayTeam?.name ?: ""})") {
                awayGoalkeepers.add(PlayerEventDraft(teamId = match.awayTeamId ?: defaultTeamId))
            }
            awayGoalkeepers.forEachIndexed { i, draft ->
                PlayerEventRow(
                    draft = draft,
                    onNameChange = { awayGoalkeepers[i] = awayGoalkeepers[i].copy(name = it) },
                    onCountChange = { awayGoalkeepers[i] = awayGoalkeepers[i].copy(count = it) },
                    onRemove = { awayGoalkeepers.removeAt(i) },
                    countLabel = "Atajadas",
                    teams = playerTeams,
                    onTeamChange = { awayGoalkeepers[i] = awayGoalkeepers[i].copy(teamId = it) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            SectionDivider()

            // Discipline
            Text("🟨 Disciplina", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
            Spacer(modifier = Modifier.height(8.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                CounterField(label = "Faltas Local", value = homeFouls, onValueChange = { homeFouls = it })
                CounterField(label = "Faltas Visit.", value = awayFouls, onValueChange = { awayFouls = it })
            }
            SectionHeader("🟨 Tarjetas Amarillas") { yellowCards.add(PlayerEventDraft(teamId = defaultTeamId)) }
            yellowCards.forEachIndexed { i, draft ->
                PlayerEventRow(
                    draft = draft,
                    onNameChange = { yellowCards[i] = yellowCards[i].copy(name = it) },
                    onCountChange = { yellowCards[i] = yellowCards[i].copy(count = it) },
                    onRemove = { yellowCards.removeAt(i) },
                    countLabel = "Tarjetas",
                    teams = playerTeams,
                    onTeamChange = { yellowCards[i] = yellowCards[i].copy(teamId = it) }
                )
            }
            SectionHeader("🟥 Tarjetas Rojas") { redCards.add(PlayerEventDraft(teamId = defaultTeamId)) }
            redCards.forEachIndexed { i, draft ->
                PlayerEventRow(
                    draft = draft,
                    onNameChange = { redCards[i] = redCards[i].copy(name = it) },
                    onCountChange = { redCards[i] = redCards[i].copy(count = it) },
                    onRemove = { redCards.removeAt(i) },
                    countLabel = "Tarjetas",
                    teams = playerTeams,
                    onTeamChange = { redCards[i] = redCards[i].copy(teamId = it) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            SectionDivider()

            // MVP
            Text("⭐ MVP del Partido", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = GoldAccent)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = mvpName,
                onValueChange = { mvpName = it },
                label = { Text("Nombre del jugador", color = TextDisabled) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GoldAccent,
                    unfocusedBorderColor = TextDisabled,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Rating: ", color = TextSecondary, fontSize = 13.sp)
                Text(
                    "%.1f".format(mvpRating),
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.width(48.dp)
                )
                Slider(
                    value = mvpRating,
                    onValueChange = { mvpRating = it },
                    valueRange = 1f..10f,
                    steps = 89,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = GoldAccent,
                        activeTrackColor = GoldAccent
                    )
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    val allEvents = scorers.toMatchEvents(match.id, "GOAL") +
                        assisters.toMatchEvents(match.id, "ASSIST") +
                        homeGoalkeepers.toMatchEvents(match.id, "SAVE") +
                        awayGoalkeepers.toMatchEvents(match.id, "SAVE") +
                        yellowCards.toMatchEvents(match.id, "YELLOW_CARD") +
                        redCards.toMatchEvents(match.id, "RED_CARD")

                    viewModel.completeMatch(
                        CompleteMatchInput(
                            matchId = match.id,
                            homeGoals = homeGoals,
                            awayGoals = awayGoals,
                            homeFouls = homeFouls,
                            awayFouls = awayFouls,
                            mvpPlayerName = mvpName.trim().ifBlank { null },
                            mvpRating = mvpName.trim()
                                .takeIf { it.isNotBlank() }
                                ?.let { mvpRating.toDouble() },
                            playerEvents = allEvents
                        )
                    )
                    scope.launch { sheetState.hide() }
                    onDismiss()
                },
                enabled = eventsLoaded,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = GreenNeon)
            ) {
                Text("Confirmar y Guardar", color = Background, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(color = SurfaceVariant, thickness = 1.dp)
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
private fun SectionHeader(label: String, onAdd: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = TextPrimary)
        TextButton(onClick = onAdd) {
            Icon(Icons.Default.Add, contentDescription = null, tint = GreenNeon, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text("Agregar", fontSize = 12.sp, color = GreenNeon)
        }
    }
}

@Composable
private fun PlayerEventRow(
    draft: PlayerEventDraft,
    onNameChange: (String) -> Unit,
    onCountChange: (Int) -> Unit,
    onRemove: () -> Unit,
    countLabel: String = "Cantidad",
    teams: List<TeamEntity>,
    onTeamChange: (Long) -> Unit
) {
    var teamMenuExpanded by remember(draft.teamId) { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .background(CardBackground, RoundedCornerShape(8.dp))
            .padding(8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = draft.name,
                onValueChange = onNameChange,
                placeholder = { Text("Nombre del jugador", color = TextDisabled, fontSize = 12.sp) },
                modifier = Modifier.weight(1f),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenNeon,
                    unfocusedBorderColor = TextDisabled,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (draft.count > 1) onCountChange(draft.count - 1) }, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
                }
                Text(
                    "${draft.count}",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.width(20.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                IconButton(onClick = { onCountChange(draft.count + 1) }, modifier = Modifier.size(28.dp)) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = GreenNeon, modifier = Modifier.size(14.dp))
                }
            }
            IconButton(onClick = onRemove, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Close, contentDescription = null, tint = Color(0xFFE74C3C), modifier = Modifier.size(14.dp))
            }
        }
        if (teams.size > 1) {
            Box {
                TextButton(onClick = { teamMenuExpanded = true }) {
                    Text(
                        "Equipo: ${teams.firstOrNull { it.id == draft.teamId }?.name ?: "Seleccionar"}",
                        color = GreenNeon,
                        fontSize = 11.sp
                    )
                }
                DropdownMenu(
                    expanded = teamMenuExpanded,
                    onDismissRequest = { teamMenuExpanded = false }
                ) {
                    teams.forEach { team ->
                        DropdownMenuItem(
                            text = { Text(team.name) },
                            onClick = {
                                onTeamChange(team.id)
                                teamMenuExpanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun GoalCounter(label: String, value: Int, onIncrement: () -> Unit, onDecrement: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onDecrement, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Remove, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp))
        }
        Text("$value", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextPrimary, modifier = Modifier.width(36.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
        IconButton(onClick = onIncrement, modifier = Modifier.size(32.dp)) {
            Icon(Icons.Default.Add, contentDescription = null, tint = GreenNeon, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun CounterField(label: String, value: Int, onValueChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 11.sp, color = TextSecondary)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (value > 0) onValueChange(value - 1) }, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Remove, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(14.dp))
            }
            Text("$value", color = TextPrimary, fontWeight = FontWeight.Bold, modifier = Modifier.width(28.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            IconButton(onClick = { onValueChange(value + 1) }, modifier = Modifier.size(28.dp)) {
                Icon(Icons.Default.Add, contentDescription = null, tint = GreenNeon, modifier = Modifier.size(14.dp))
            }
        }
    }
}
