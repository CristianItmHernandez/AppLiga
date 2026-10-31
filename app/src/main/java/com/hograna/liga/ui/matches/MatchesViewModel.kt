package com.hograna.liga.ui.matches

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hograna.liga.data.local.dao.StandingsDao
import com.hograna.liga.data.local.entity.KnockoutTieEntity
import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.MatchPlayerEventEntity
import com.hograna.liga.data.local.entity.TeamEntity
import com.hograna.liga.data.repository.TournamentRepository
import com.hograna.liga.domain.usecase.AdvanceKnockoutTieUseCase
import com.hograna.liga.domain.usecase.CompleteMatchInput
import com.hograna.liga.domain.usecase.CompleteMatchUseCase
import com.hograna.liga.domain.usecase.GenerateMatchdayUseCase
import com.hograna.liga.domain.usecase.InitializeBracketResult
import com.hograna.liga.domain.usecase.InitializeKnockoutBracketUseCase
import com.hograna.liga.domain.usecase.KnockoutResult
import com.hograna.liga.domain.usecase.ManualMatchInput
import com.hograna.liga.domain.usecase.ManualMatchResult
import com.hograna.liga.domain.usecase.ShareMatchGraphicUseCase
import com.hograna.liga.domain.usecase.UpsertManualMatchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MatchesUiState(
    val regularMatches: List<MatchEntity> = emptyList(),
    val knockoutMatches: List<MatchEntity> = emptyList(),
    val knockoutTies: List<KnockoutTieEntity> = emptyList(),
    val teams: List<TeamEntity> = emptyList(),
    val latestMatchday: Int = 0,
    val isLoading: Boolean = false,
    val pendingKnockoutTieId: Long? = null,
    val pendingPenaltiesTeamA: Long? = null,
    val pendingPenaltiesTeamB: Long? = null,
    val error: String? = null
)

enum class BracketSeedMode { TOP_RANKED, RANDOM_DRAW, MANUAL }

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val repository: TournamentRepository,
    private val generateMatchdayUseCase: GenerateMatchdayUseCase,
    private val completeMatchUseCase: CompleteMatchUseCase,
    private val advanceKnockoutTieUseCase: AdvanceKnockoutTieUseCase,
    private val shareMatchGraphicUseCase: ShareMatchGraphicUseCase,
    private val upsertManualMatchUseCase: UpsertManualMatchUseCase,
    private val initializeKnockoutBracketUseCase: InitializeKnockoutBracketUseCase,
    private val standingsDao: StandingsDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchesUiState())
    val uiState: StateFlow<MatchesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.getRegularMatches(),
                repository.getAllTeams(),
                repository.getLatestMatchday(),
                repository.getKnockoutMatches(),
                repository.getAllTies()
            ) { regularMatches, teams, latestDay, knockoutMatches, ties ->
                MatchesSnapshot(
                    regularMatches = regularMatches,
                    teams = teams,
                    latestMatchday = latestDay ?: 0,
                    knockoutMatches = knockoutMatches,
                    ties = ties
                )
            }.collect { snapshot ->
                _uiState.update {
                    it.copy(
                        regularMatches = snapshot.regularMatches,
                        teams = snapshot.teams,
                        latestMatchday = snapshot.latestMatchday,
                        knockoutMatches = snapshot.knockoutMatches,
                        knockoutTies = snapshot.ties
                    )
                }
            }
        }
    }

    fun generateNextMatchday() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val nextMatchday = _uiState.value.latestMatchday + 1
            when (val result = generateMatchdayUseCase.invoke(nextMatchday)) {
                is GenerateMatchdayUseCase.Result.Success ->
                    _uiState.update { it.copy(isLoading = false) }
                is GenerateMatchdayUseCase.Result.NotEnoughTeams ->
                    _uiState.update {
                        it.copy(isLoading = false, error = "Se necesitan al menos 2 equipos")
                    }
                is GenerateMatchdayUseCase.Result.Error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.cause.message ?: "No se pudo generar la jornada"
                        )
                    }
            }
        }
    }

    fun completeMatch(input: CompleteMatchInput) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = completeMatchUseCase.invoke(input)) {
                is CompleteMatchUseCase.Result.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    try {
                        val match = repository.getMatchById(input.matchId)
                        match?.knockoutTieId?.let { checkKnockoutAdvancement(it) }
                    } catch (error: Exception) {
                        _uiState.update {
                            it.copy(error = error.message ?: "No se pudo actualizar la eliminatoria")
                        }
                    }
                }
                is CompleteMatchUseCase.Result.MatchNotFound ->
                    _uiState.update { it.copy(isLoading = false, error = "Partido no encontrado") }
                is CompleteMatchUseCase.Result.Error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.cause.message ?: "No se pudo guardar el acta"
                        )
                    }
            }
        }
    }

    fun createManualMatch(input: ManualMatchInput) {
        viewModelScope.launch {
            when (val result = upsertManualMatchUseCase.invoke(input)) {
                is ManualMatchResult.Saved -> _uiState.update { it.copy(error = null) }
                ManualMatchResult.TeamNotFound ->
                    _uiState.update { it.copy(error = "Uno de los equipos ya no está registrado") }
                ManualMatchResult.MatchNotFound ->
                    _uiState.update { it.copy(error = "No se encontró el partido que querías editar") }
                is ManualMatchResult.Invalid ->
                    _uiState.update { it.copy(error = result.reason) }
                is ManualMatchResult.Error ->
                    _uiState.update {
                        it.copy(error = result.cause.message ?: "No se pudo guardar el partido")
                    }
            }
        }
    }

    fun initializeBracket(mode: BracketSeedMode, manualTeamIds: List<Long> = emptyList()) {
        viewModelScope.launch {
            try {
                val selectedIds = when (mode) {
                    BracketSeedMode.TOP_RANKED ->
                        standingsDao.getStandings().first().take(16).map { it.teamId }
                    BracketSeedMode.RANDOM_DRAW ->
                        _uiState.value.teams.shuffled().take(16).map { it.id }
                    BracketSeedMode.MANUAL -> manualTeamIds
                }
                when (val result = initializeKnockoutBracketUseCase.invoke(selectedIds)) {
                    InitializeBracketResult.Success -> _uiState.update { it.copy(error = null) }
                    InitializeBracketResult.NotEnoughTeams ->
                        _uiState.update {
                            it.copy(error = "Se necesitan 16 equipos para iniciar los octavos")
                        }
                    InitializeBracketResult.AlreadyInitialized ->
                        _uiState.update { it.copy(error = "El bracket ya está iniciado") }
                    is InitializeBracketResult.InvalidSelection ->
                        _uiState.update { it.copy(error = result.reason) }
                    is InitializeBracketResult.Error ->
                        _uiState.update {
                            it.copy(error = result.cause.message ?: "No se pudo iniciar el bracket")
                        }
                }
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(error = error.message ?: "No se pudo preparar la selección")
                }
            }
        }
    }

    private suspend fun checkKnockoutAdvancement(tieId: Long) {
        when (val result = advanceKnockoutTieUseCase.invoke(tieId)) {
            is KnockoutResult.NeedsPenalties ->
                _uiState.update {
                    it.copy(
                        pendingKnockoutTieId = result.tieId,
                        pendingPenaltiesTeamA = result.teamAId,
                        pendingPenaltiesTeamB = result.teamBId
                    )
                }
            is KnockoutResult.Error ->
                _uiState.update {
                    it.copy(error = result.cause.message ?: "No se pudo avanzar la llave")
                }
            else -> Unit
        }
    }

    fun submitPenalties(tieId: Long, penA: Int, penB: Int) {
        if (penA < 0 || penB < 0 || penA == penB) {
            _uiState.update { it.copy(error = "La tanda debe tener un ganador y valores válidos") }
            return
        }
        viewModelScope.launch {
            when (val result = advanceKnockoutTieUseCase.invokePenalties(tieId, penA, penB)) {
                is KnockoutResult.WinnerDetermined ->
                    _uiState.update {
                        it.copy(
                            pendingKnockoutTieId = null,
                            pendingPenaltiesTeamA = null,
                            pendingPenaltiesTeamB = null,
                            error = null
                        )
                    }
                is KnockoutResult.Error ->
                    _uiState.update {
                        it.copy(error = result.cause.message ?: "No se pudieron guardar los penales")
                    }
                else ->
                    _uiState.update { it.copy(error = "No se pudo determinar el ganador de la llave") }
            }
        }
    }

    fun deleteMatch(matchId: Long) {
        viewModelScope.launch {
            try {
                repository.deleteMatchById(matchId)
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(error = error.message ?: "No se pudo eliminar el partido")
                }
            }
        }
    }

    fun shareMatch(matchId: Long) {
        viewModelScope.launch {
            try {
                shareMatchGraphicUseCase.share(matchId)
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(error = error.message ?: "No se pudo compartir el partido")
                }
            }
        }
    }

    fun playerEvents(matchId: Long) = repository.getPlayerEventsForMatch(matchId)

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun teamById(id: Long?): TeamEntity? =
        _uiState.value.teams.find { it.id == id }

    private data class MatchesSnapshot(
        val regularMatches: List<MatchEntity>,
        val teams: List<TeamEntity>,
        val latestMatchday: Int,
        val knockoutMatches: List<MatchEntity>,
        val ties: List<KnockoutTieEntity>
    )
}
