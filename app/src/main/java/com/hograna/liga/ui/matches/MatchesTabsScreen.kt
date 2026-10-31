package com.hograna.liga.ui.matches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GeneratingTokens
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.TeamEntity
import com.hograna.liga.domain.usecase.ManualMatchInput
import com.hograna.liga.ui.theme.Background
import com.hograna.liga.ui.theme.CardBackground
import com.hograna.liga.ui.theme.GreenNeon
import com.hograna.liga.ui.theme.Surface
import com.hograna.liga.ui.theme.SurfaceVariant
import com.hograna.liga.ui.theme.TextDisabled
import com.hograna.liga.ui.theme.TextPrimary
import com.hograna.liga.ui.theme.TextSecondary

@Composable
fun MatchesTabsScreen(viewModel: MatchesViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    var matchToComplete by remember { mutableStateOf<MatchEntity?>(null) }
    var matchToDelete by remember { mutableStateOf<MatchEntity?>(null) }
    var matchToReassign by remember { mutableStateOf<MatchEntity?>(null) }
    var showManualMatchDialog by remember { mutableStateOf(false) }
    var showGenerateDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            containerColor = Surface,
            contentColor = GreenNeon,
            edgePadding = 0.dp
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("Fase Regular", color = if (selectedTab == 0) GreenNeon else TextDisabled) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Eliminatorias", color = if (selectedTab == 1) GreenNeon else TextDisabled) }
            )
        }

        when (selectedTab) {
            0 -> RegularPhaseTab(
                uiState = uiState,
                viewModel = viewModel,
                onGenerateClick = { showGenerateDialog = true },
                onCompleteMatch = { matchToComplete = it },
                onDeleteMatch = { matchToDelete = it },
                onManualPairing = {
                    matchToReassign = it
                    showManualMatchDialog = true
                },
                onShareMatch = { viewModel.shareMatch(it.id) }
            )
            1 -> KnockoutBracketView(viewModel = viewModel)
        }
    }

    if (showManualMatchDialog) {
        ManualMatchDialog(
            teams = uiState.teams,
            matchday = matchToReassign?.matchdayOrRound ?: uiState.latestMatchday + 1,
            existingMatch = matchToReassign,
            onDismiss = {
                showManualMatchDialog = false
                matchToReassign = null
            },
            onSave = { matchday, homeTeamId, awayTeamId ->
                viewModel.createManualMatch(
                    ManualMatchInput(
                        matchId = matchToReassign?.id,
                        matchday = matchday,
                        homeTeamId = homeTeamId,
                        awayTeamId = awayTeamId
                    )
                )
                showManualMatchDialog = false
                matchToReassign = null
            }
        )
    }

    // Generate matchday dialog
    if (showGenerateDialog) {
        AlertDialog(
            onDismissRequest = { showGenerateDialog = false },
            containerColor = Surface,
            title = {
                Text(
                    "Generar Jornada ${uiState.latestMatchday + 1}",
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "¿Confirmar el sorteo de la Jornada ${uiState.latestMatchday + 1} con ${uiState.teams.size} equipos?",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.generateNextMatchday()
                        showGenerateDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = GreenNeon)
                ) {
                    Text("Sortear", color = Background, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showGenerateDialog = false }) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        )
    }

    // Delete confirmation dialog
    matchToDelete?.let { match ->
        AlertDialog(
            onDismissRequest = { matchToDelete = null },
            containerColor = Surface,
            title = { Text("Eliminar partido", color = TextPrimary, fontWeight = FontWeight.Bold) },
            text = { Text("¿Eliminar este partido y todos sus datos?", color = TextSecondary) },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.deleteMatch(match.id)
                        matchToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE74C3C))
                ) {
                    Text("Eliminar", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { matchToDelete = null }) {
                    Text("Cancelar", color = TextSecondary)
                }
            }
        )
    }

    // Match detail sheet
    matchToComplete?.let { match ->
        MatchDetailSheet(
            match = match,
            viewModel = viewModel,
            onDismiss = { matchToComplete = null }
        )
    }

    // Penalties dialog
    uiState.pendingKnockoutTieId?.let { tieId ->
        PenaltiesDialog(
            tieId = tieId,
            teamAId = uiState.pendingPenaltiesTeamA ?: 0L,
            teamBId = uiState.pendingPenaltiesTeamB ?: 0L,
            viewModel = viewModel
        )
    }

    // Error snackbar
    uiState.error?.let { error ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            Snackbar(
                action = {
                    TextButton(onClick = { viewModel.clearError() }) {
                        Text("OK", color = GreenNeon)
                    }
                },
                containerColor = SurfaceVariant
            ) {
                Text(error, color = TextPrimary)
            }
        }
    }
}

@Composable
private fun RegularPhaseTab(
    uiState: MatchesUiState,
    viewModel: MatchesViewModel,
    onGenerateClick: () -> Unit,
    onCompleteMatch: (MatchEntity) -> Unit,
    onDeleteMatch: (MatchEntity) -> Unit,
    onManualPairing: (MatchEntity?) -> Unit,
    onShareMatch: (MatchEntity) -> Unit
) {
    val groupedByMatchday = uiState.regularMatches
        .groupBy { it.matchdayOrRound }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onGenerateClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = GreenNeon)
            ) {
                Icon(
                    Icons.Default.GeneratingTokens,
                    contentDescription = null,
                    tint = Background,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Generar Jornada ${uiState.latestMatchday + 1}",
                    color = Background,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onManualPairing(null) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = SurfaceVariant)
            ) {
                Text("Crear / reasignar partido manual", color = TextPrimary)
            }
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (groupedByMatchday.isEmpty()) {
            item {
                Box(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay jornadas generadas aún", color = TextDisabled, textAlign = TextAlign.Center)
                }
            }
        }

        groupedByMatchday.entries.sortedByDescending { it.key }.forEach { (matchday, matches) ->
            item(key = "header_$matchday") {
                Text(
                    "Jornada $matchday",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = GreenNeon,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            items(matches, key = { it.id }) { match ->
                if (match.isBye) {
                    ByeMatchCard(team = viewModel.teamById(match.homeTeamId))
                } else {
                    MatchCard(
                        match = match,
                        homeTeam = viewModel.teamById(match.homeTeamId),
                        awayTeam = viewModel.teamById(match.awayTeamId),
                        onEdit = { onCompleteMatch(match) },
                        onEditPairing = { onManualPairing(match) },
                        onDelete = { onDeleteMatch(match) },
                        onShare = { onShareMatch(match) }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }
    }
}

@Composable
private fun ByeMatchCard(team: TeamEntity?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = team?.crestUri,
                contentDescription = team?.name,
                modifier = Modifier.size(36.dp).clip(CircleShape),
                contentScale = ContentScale.Fit
            )
            Text(
                team?.name ?: "Equipo",
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f).padding(start = 10.dp)
            )
            Text("DESCANSA", color = TextSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun MatchCard(
    match: MatchEntity,
    homeTeam: com.hograna.liga.data.local.entity.TeamEntity?,
    awayTeam: com.hograna.liga.data.local.entity.TeamEntity?,
    onEdit: () -> Unit,
    onEditPairing: () -> Unit,
    onDelete: () -> Unit,
    onShare: () -> Unit
) {
    val isCompleted = match.status == "COMPLETED"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Home team
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    AsyncImage(
                        model = homeTeam?.crestUri,
                        contentDescription = homeTeam?.name,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        homeTeam?.name ?: "Local",
                        fontSize = 11.sp,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }

                // Score / VS
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(80.dp)
                ) {
                    if (!isCompleted) {
                        IconButton(onClick = onEditPairing, modifier = Modifier.size(32.dp)) {
                            Icon(
                                Icons.Default.SwapHoriz,
                                contentDescription = "Cambiar cruce",
                                tint = TextSecondary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    if (isCompleted) {
                        Text(
                            "${match.homeGoals}  -  ${match.awayGoals}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextPrimary
                        )
                    } else {
                        Text(
                            "vs",
                            fontSize = 16.sp,
                            color = TextDisabled
                        )
                    }
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isCompleted) GreenNeon.copy(alpha = 0.15f) else SurfaceVariant)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            if (isCompleted) "COMPLETADO" else "PENDIENTE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCompleted) GreenNeon else TextDisabled
                        )
                    }
                }

                // Away team
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                    AsyncImage(
                        model = awayTeam?.crestUri,
                        contentDescription = awayTeam?.name,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Fit
                    )
                    Text(
                        awayTeam?.name ?: "Visitante",
                        fontSize = 11.sp,
                        color = TextPrimary,
                        textAlign = TextAlign.Center,
                        maxLines = 2
                    )
                }
            }

            // MVP badge if completed
            if (isCompleted && match.mvpPlayerName != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    "⭐ MVP: ${match.mvpPlayerName}${match.mvpRating?.let { "  ${"%.1f".format(it)}" } ?: ""}",
                    fontSize = 11.sp,
                    color = Color(0xFFF1C40F),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (isCompleted) {
                    IconButton(onClick = onShare, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir", tint = GreenNeon, modifier = Modifier.size(16.dp))
                    }
                }
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = TextSecondary, modifier = Modifier.size(16.dp))
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color(0xFFE74C3C), modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun ManualMatchDialog(
    teams: List<TeamEntity>,
    matchday: Int,
    existingMatch: MatchEntity?,
    onDismiss: () -> Unit,
    onSave: (Int, Long, Long) -> Unit
) {
    var matchdayInput by remember(existingMatch?.id) { mutableStateOf(matchday.toString()) }
    var homeTeamId by remember(existingMatch?.id) {
        mutableStateOf(existingMatch?.homeTeamId ?: teams.firstOrNull()?.id)
    }
    var awayTeamId by remember(existingMatch?.id) {
        mutableStateOf(existingMatch?.awayTeamId ?: teams.getOrNull(1)?.id)
    }
    val valid = teams.size >= 2 &&
        (matchdayInput.toIntOrNull() ?: 0) > 0 &&
        homeTeamId != null &&
        awayTeamId != null &&
        homeTeamId != awayTeamId

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        title = {
            Text(
                if (existingMatch == null) "Crear partido manual" else "Reasignar partido",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column {
                OutlinedTextField(
                    value = matchdayInput,
                    onValueChange = { matchdayInput = it.filter(Char::isDigit).take(4) },
                    label = { Text("Jornada", color = TextSecondary) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = GreenNeon,
                        unfocusedBorderColor = TextDisabled,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                TeamSelector(
                    label = "Local",
                    selectedTeamId = homeTeamId,
                    teams = teams,
                    unavailableTeamId = awayTeamId,
                    onSelect = { homeTeamId = it }
                )
                TeamSelector(
                    label = "Visitante",
                    selectedTeamId = awayTeamId,
                    teams = teams,
                    unavailableTeamId = homeTeamId,
                    onSelect = { awayTeamId = it }
                )
                if (teams.size < 2) {
                    Text("Registra al menos dos equipos antes de crear partidos.", color = TextSecondary)
                }
            }
        },
        confirmButton = {
            Button(
                enabled = valid,
                onClick = {
                    onSave(
                        requireNotNull(matchdayInput.toIntOrNull()),
                        requireNotNull(homeTeamId),
                        requireNotNull(awayTeamId)
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenNeon)
            ) {
                Text("Guardar", color = Background, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextSecondary)
            }
        }
    )
}

@Composable
private fun TeamSelector(
    label: String,
    selectedTeamId: Long?,
    teams: List<TeamEntity>,
    unavailableTeamId: Long?,
    onSelect: (Long) -> Unit
) {
    var expanded by remember(label, selectedTeamId) { mutableStateOf(false) }
    Box {
        TextButton(onClick = { expanded = true }) {
            Text(
                "$label: ${teams.firstOrNull { it.id == selectedTeamId }?.name ?: "Seleccionar equipo"}",
                color = GreenNeon
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            teams.filter { it.id != unavailableTeamId }.forEach { team ->
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
private fun PenaltiesDialog(
    tieId: Long,
    teamAId: Long,
    teamBId: Long,
    viewModel: MatchesViewModel
) {
    var penA by remember { mutableStateOf("") }
    var penB by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {},
        containerColor = Surface,
        title = {
            Text("⚽ Tanda de Penales", color = TextPrimary, fontWeight = FontWeight.Bold)
        },
        text = {
            Column {
                Text(
                    "El global está empatado. Ingresá los penales: " +
                        "${viewModel.teamById(teamAId)?.name ?: "Equipo A"} vs " +
                        (viewModel.teamById(teamBId)?.name ?: "Equipo B"),
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = penA,
                        onValueChange = { if (it.length <= 2) penA = it.filter { c -> c.isDigit() } },
                        label = { Text("Equipo A", color = TextDisabled) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenNeon,
                            unfocusedBorderColor = TextDisabled,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    Text(" - ", color = TextPrimary, fontSize = 20.sp, modifier = Modifier.padding(horizontal = 8.dp))
                    OutlinedTextField(
                        value = penB,
                        onValueChange = { if (it.length <= 2) penB = it.filter { c -> c.isDigit() } },
                        label = { Text("Equipo B", color = TextDisabled) },
                        modifier = Modifier.weight(1f),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GreenNeon,
                            unfocusedBorderColor = TextDisabled,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }
            }
        },
        confirmButton = {
            val valid = penA.isNotBlank() && penB.isNotBlank() &&
                    (penA.toIntOrNull() ?: -1) >= 0 && (penB.toIntOrNull() ?: -1) >= 0 &&
                    penA.toInt() != penB.toInt()
            Button(
                onClick = {
                    viewModel.submitPenalties(tieId, penA.toInt(), penB.toInt())
                },
                enabled = valid,
                colors = ButtonDefaults.buttonColors(containerColor = GreenNeon)
            ) {
                Text("Confirmar", color = Background, fontWeight = FontWeight.Bold)
            }
        }
    )
}
