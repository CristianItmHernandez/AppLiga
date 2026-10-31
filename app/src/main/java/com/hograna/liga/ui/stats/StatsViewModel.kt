package com.hograna.liga.ui.stats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hograna.liga.data.local.dao.StatsDao
import com.hograna.liga.domain.model.StatRow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class StatsUiState(
    val scorers: List<StatRow> = emptyList(),
    val assisters: List<StatRow> = emptyList(),
    val savers: List<StatRow> = emptyList(),
    val yellowCards: List<StatRow> = emptyList(),
    val redCards: List<StatRow> = emptyList(),
    val isLoading: Boolean = true
)

@HiltViewModel
class StatsViewModel @Inject constructor(
    private val statsDao: StatsDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                statsDao.getTopScorers(),
                statsDao.getTopAssisters(),
                statsDao.getTopSavers(),
                statsDao.getYellowCards(),
                statsDao.getRedCards()
            ) { scorers, assisters, savers, yellows, reds ->
                StatsUiState(
                    scorers = scorers,
                    assisters = assisters,
                    savers = savers,
                    yellowCards = yellows,
                    redCards = reds,
                    isLoading = false
                )
            }.collect { state ->
                _uiState.update { state }
            }
        }
    }
}
