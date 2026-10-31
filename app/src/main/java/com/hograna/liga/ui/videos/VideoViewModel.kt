package com.hograna.liga.ui.videos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hograna.liga.data.local.entity.VideoHighlightEntity
import com.hograna.liga.data.repository.TournamentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class VideoUiState(
    val videos: List<VideoHighlightEntity> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

@HiltViewModel
class VideoViewModel @Inject constructor(
    private val repository: TournamentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(VideoUiState())
    val uiState: StateFlow<VideoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllVideos().collect { videos ->
                _uiState.update { it.copy(videos = videos, isLoading = false) }
            }
        }
    }

    fun deleteVideo(video: VideoHighlightEntity) {
        viewModelScope.launch {
            try {
                repository.deleteVideo(video)
            } catch (error: Exception) {
                _uiState.update { it.copy(error = error.message ?: "No se pudo eliminar el vídeo") }
            }
        }
    }

    fun addVideo(video: VideoHighlightEntity) {
        viewModelScope.launch {
            try {
                repository.insertVideo(video)
                _uiState.update { it.copy(error = null) }
            } catch (error: Exception) {
                _uiState.update { it.copy(error = error.message ?: "No se pudo guardar el vídeo") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
