package com.hograna.liga.ui.standings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hograna.liga.data.local.dao.MatchDao
import com.hograna.liga.data.local.dao.StandingsDao
import com.hograna.liga.data.local.dao.TeamDao
import com.hograna.liga.data.local.entity.MatchEntity
import com.hograna.liga.data.local.entity.TeamEntity
import com.hograna.liga.domain.model.StandingRow
import com.hograna.liga.domain.model.StreakBadgeType
import com.hograna.liga.domain.usecase.GetTeamStreaksUseCase
import com.hograna.liga.domain.usecase.ShareStandingsGraphicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StandingsUiState(
    val standings: List<StandingRow> = emptyList(),
    val streaks: Map<Long, List<StreakBadgeType>> = emptyMap(),
    val currentMatchday: Int = 0,
    val currentMatches: List<MatchSummary> = emptyList(),
    val shareError: String? = null,
    val isLoading: Boolean = true
)

data class MatchSummary(
    val match: MatchEntity,
    val homeTeam: TeamEntity?,
    val awayTeam: TeamEntity?
)

@HiltViewModel
class StandingsViewModel @Inject constructor(
    private val standingsDao: StandingsDao,
    private val matchDao: MatchDao,
    private val teamDao: TeamDao,
    private val getTeamStreaks: GetTeamStreaksUseCase,
    private val shareStandingsGraphic: ShareStandingsGraphicUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StandingsUiState())
    val uiState: StateFlow<StandingsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                standingsDao.getStandings(),
                matchDao.getRegularMatches(),
                teamDao.getAllTeams()
            ) { standings, matches, teams ->
                val latestMatchday = matches.maxOfOrNull { it.matchdayOrRound } ?: 0
                val teamsById = teams.associateBy { it.id }
                StandingsUiState(
                    standings = standings,
                    streaks = getTeamStreaks(matches),
                    currentMatchday = latestMatchday,
                    currentMatches = matches
                        .filter { it.matchdayOrRound == latestMatchday }
                        .map { MatchSummary(it, teamsById[it.homeTeamId], it.awayTeamId?.let(teamsById::get)) },
                    isLoading = false
                )
            }.collect { state ->
                _uiState.value = state.copy(shareError = _uiState.value.shareError)
            }
        }
    }

    fun shareTopTen() {
        val state = _uiState.value
        viewModelScope.launch {
            try {
                shareStandingsGraphic.share(state.standings, state.streaks)
                _uiState.value = _uiState.value.copy(shareError = null)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = _uiState.value.copy(
                    shareError = exception.message ?: "No se pudo compartir la tabla"
                )
            }
        }
    }

    fun dismissShareError() {
        _uiState.value = _uiState.value.copy(shareError = null)
    }
}
