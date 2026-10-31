package com.hograna.liga.ui.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.TeamEntity
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
fun KnockoutBracketView(viewModel: MatchesViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var matchToComplete by remember { mutableStateOf<MatchEntity?>(null) }
    var showSetupDialog by remember { mutableStateOf(false) }

    val rounds = listOf(
        8 to "Octavos",
        4 to "Cuartos",
        2 to "Semifinal",
        1 to "Final"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Eliminatorias", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
            Button(
                enabled = uiState.knockoutTies.isEmpty(),
                onClick = { showSetupDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = GreenNeon)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Background, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    if (uiState.knockoutTies.isEmpty()) "Iniciar bracket" else "Bracket iniciado",
                    color = Background,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (uiState.knockoutTies.isEmpty()) {
            Text(
                "Selecciona los 16 equipos que disputarán los octavos de final.",
                color = TextSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(rememberScrollState())
        ) {
            rounds.forEach { (round, label) ->
                BracketColumn(
                    roundLabel = label,
                    round = round,
                    viewModel = viewModel,
                    onMatchClick = { matchToComplete = it }
                )
                if (round != 1) ConnectorColumn()
            }
        }
    }

    if (showSetupDialog) {
        BracketSetupDialog(
            teams = uiState.teams,
            onDismiss = { showSetupDialog = false },
            onStart = { mode, manualTeamIds ->
                viewModel.initializeBracket(mode, manualTeamIds)
                showSetupDialog = false
            }
        )
    }

    matchToComplete?.let { match ->
        MatchDetailSheet(
            match = match,
            viewModel = viewModel,
            onDismiss = { matchToComplete = null }
        )
    }
}

@Composable
private fun BracketSetupDialog(
    teams: List<TeamEntity>,
    onDismiss: () -> Unit,
    onStart: (BracketSeedMode, List<Long>) -> Unit
) {
    var mode by remember { mutableStateOf(BracketSeedMode.TOP_RANKED) }
    val selectedIds = remember(teams) {
        mutableStateListOf<Long?>().apply {
            repeat(16) { index -> add(teams.getOrNull(index)?.id) }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = { Text("Configurar octavos", color = TextPrimary, fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .heightIn(max = 480.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                BracketModeRow(
                    label = "Mejores clasificados",
                    selected = mode == BracketSeedMode.TOP_RANKED,
                    onClick = { mode = BracketSeedMode.TOP_RANKED }
                )
                BracketModeRow(
                    label = "Sorteo aleatorio",
                    selected = mode == BracketSeedMode.RANDOM_DRAW,
                    onClick = { mode = BracketSeedMode.RANDOM_DRAW }
                )
                BracketModeRow(
                    label = "Asignación manual",
                    selected = mode == BracketSeedMode.MANUAL,
                    onClick = { mode = BracketSeedMode.MANUAL }
                )
                if (mode == BracketSeedMode.MANUAL) {
                    if (teams.size < 16) {
                        Text(
                            "Registra 16 equipos para poder asignarlos manualmente.",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                    repeat(16) { index ->
                        BracketTeamSelector(
                            position = index + 1,
                            teams = teams,
                            selectedTeamId = selectedIds[index],
                            unavailableIds = selectedIds.filterIndexed { selectedIndex, _ -> selectedIndex != index }
                                .filterNotNull()
                                .toSet(),
                            onSelect = { selectedIds[index] = it }
                        )
                    }
                } else if (teams.size < 16) {
                    Text(
                        "Hay ${teams.size} equipos registrados; se necesitan 16.",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            val manualSelectionValid = selectedIds.size == 16 &&
                selectedIds.all { it != null } &&
                selectedIds.filterNotNull().distinct().size == 16
            Button(
                enabled = teams.size >= 16 &&
                    (mode != BracketSeedMode.MANUAL || manualSelectionValid),
                onClick = {
                    onStart(mode, selectedIds.filterNotNull())
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenNeon)
            ) {
                Text("Iniciar", color = Background, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar", color = TextSecondary) }
        }
    )
}

@Composable
private fun BracketModeRow(label: String, selected: Boolean, onClick: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected, onClick = onClick)
        TextButton(onClick = onClick) {
            Text(label, color = TextPrimary)
        }
    }
}

@Composable
private fun BracketTeamSelector(
    position: Int,
    teams: List<TeamEntity>,
    selectedTeamId: Long?,
    unavailableIds: Set<Long>,
    onSelect: (Long) -> Unit
) {
    var expanded by remember(position, selectedTeamId) { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }) {
            Text(
                "$position. ${teams.firstOrNull { it.id == selectedTeamId }?.name ?: "Seleccionar equipo"}",
                color = if (selectedTeamId == null) TextDisabled else TextPrimary,
                textAlign = TextAlign.Start
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            teams.filter { it.id == selectedTeamId || it.id !in unavailableIds }.forEach { team ->
                DropdownMenuItem(
                    text = { Text(team.name) },
                    onClick = {
                        onSelect(team.id)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun BracketColumn(
    roundLabel: String,
    round: Int,
    viewModel: MatchesViewModel,
    onMatchClick: (MatchEntity) -> Unit
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .width(190.dp)
            .fillMaxHeight()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            roundLabel,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = GoldAccent,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        repeat(round) { slotIndex ->
            val tie = state.knockoutTies.firstOrNull {
                it.round == round && it.slotIndex == slotIndex
            }
            BracketTieCard(
                tie = tie,
                matches = tie?.let { current ->
                    state.knockoutMatches.filter { it.knockoutTieId == current.id }
                }.orEmpty(),
                teamA = tie?.teamAId?.let(viewModel::teamById),
                teamB = tie?.teamBId?.let(viewModel::teamById),
                winnerId = tie?.winnerTeamId,
                penalties = tie?.teamAPenalties?.let { a ->
                    tie.teamBPenalties?.let { b -> "$a - $b (penales)" }
                },
                onMatchClick = onMatchClick
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun BracketTieCard(
    tie: com.hograna.liga.data.local.entity.KnockoutTieEntity?,
    matches: List<MatchEntity>,
    teamA: TeamEntity?,
    teamB: TeamEntity?,
    winnerId: Long?,
    penalties: String?,
    onMatchClick: (MatchEntity) -> Unit
) {
    val teamAGlobal = matches.filter { it.status == "COMPLETED" }.sumOf { match ->
        if (match.homeTeamId == tie?.teamAId) match.homeGoals else match.awayGoals
    }
    val teamBGlobal = matches.filter { it.status == "COMPLETED" }.sumOf { match ->
        if (match.homeTeamId == tie?.teamBId) match.homeGoals else match.awayGoals
    }
    val hasScore = matches.any { it.status == "COMPLETED" }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            BracketTeamSlot(teamA?.name, teamA?.crestUri, teamAGlobal.takeIf { hasScore }, winnerId == teamA?.id)
            Spacer(modifier = Modifier.height(4.dp))
            BracketTeamSlot(teamB?.name, teamB?.crestUri, teamBGlobal.takeIf { hasScore }, winnerId == teamB?.id)
            penalties?.let {
                Text(it, color = GoldAccent, fontSize = 9.sp, modifier = Modifier.padding(top = 3.dp))
            }
            matches.sortedBy { it.legNumber }.forEach { match ->
                TextButton(
                    onClick = { onMatchClick(match) },
                    enabled = teamA != null && teamB != null,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val leg = if (match.legNumber == 1) "Ida" else "Vuelta"
                    val score = if (match.status == "COMPLETED") {
                        "${match.homeGoals} - ${match.awayGoals}"
                    } else {
                        "Pendiente"
                    }
                    Text("$leg: $score", fontSize = 10.sp, color = TextSecondary)
                }
            }
        }
    }
}

@Composable
private fun BracketTeamSlot(
    teamName: String?,
    crestUri: String?,
    goals: Int?,
    isWinner: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                if (isWinner) GreenNeon.copy(alpha = 0.1f) else Color.Transparent,
                RoundedCornerShape(4.dp)
            )
            .padding(horizontal = 4.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (crestUri != null) {
            AsyncImage(
                model = crestUri,
                contentDescription = teamName,
                modifier = Modifier.size(20.dp).clip(CircleShape),
                contentScale = ContentScale.Fit
            )
        } else {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(SurfaceVariant)
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            teamName ?: "—",
            fontSize = 10.sp,
            color = if (isWinner) GreenNeon else if (teamName != null) TextPrimary else TextDisabled,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        if (goals != null) {
            Text("$goals", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
        }
    }
}

@Composable
private fun ConnectorColumn() {
    Box(modifier = Modifier.width(24.dp).fillMaxHeight()) {
        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight(0.8f)
                .background(SurfaceVariant)
                .align(Alignment.Center)
        )
    }
}
