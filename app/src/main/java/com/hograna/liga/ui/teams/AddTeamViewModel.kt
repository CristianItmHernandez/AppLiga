package com.hograna.liga.ui.teams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hograna.liga.data.catalog.CatalogTeam
import com.hograna.liga.data.local.entity.TeamEntity
import com.hograna.liga.data.repository.TournamentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AddTeamUiState(
    val registeredTeamNames: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddTeamViewModel @Inject constructor(
    private val repository: TournamentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTeamUiState())
    val uiState: StateFlow<AddTeamUiState> = _uiState.asStateFlow()

    init {
        observeRegisteredTeams()
    }

    private fun observeRegisteredTeams() {
        viewModelScope.launch {
            repository.getAllTeams().collect { teams ->
                _uiState.update { it.copy(registeredTeamNames = teams.map { t -> t.name }.toSet()) }
            }
        }
    }

    suspend fun addTeamFromCatalog(catalogTeam: CatalogTeam, captainName: String): Result<Unit> =
        saveTeam(
            TeamEntity(
                name = catalogTeam.name,
                leagueOrigin = catalogTeam.leagueOrigin,
                crestUri = catalogTeam.crestUrl,
                captainName = captainName.trim()
            )
        )

    suspend fun addCustomTeam(
        name: String,
        captainName: String,
        crestUri: String
    ): Result<Unit> {
        if (name.isBlank() || captainName.isBlank() || crestUri.isBlank()) {
            return Result.failure(IllegalArgumentException("Completa el nombre, capitán y escudo"))
        }
        return saveTeam(
            TeamEntity(
                name = name.trim(),
                leagueOrigin = "CUSTOM",
                crestUri = crestUri,
                captainName = captainName.trim()
            )
        )
    }

    private suspend fun saveTeam(team: TeamEntity): Result<Unit> {
        _uiState.update { it.copy(isLoading = true, error = null) }
        return try {
            repository.insertTeam(team)
            Result.success(Unit)
        } catch (error: Exception) {
            _uiState.update { it.copy(error = error.message ?: "No se pudo registrar el equipo") }
            Result.failure(error)
        } finally {
            _uiState.update { it.copy(isLoading = false) }
        }
    }
}
