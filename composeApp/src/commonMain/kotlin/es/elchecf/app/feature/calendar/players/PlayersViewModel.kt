package es.elchecf.app.feature.calendar.players

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.elchecf.app.domain.repository.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PlayersViewModel(
    private val playerRepository: PlayerRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlayersUiState())
    val uiState: StateFlow<PlayersUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val squad = playerRepository.getSquad()
            _uiState.update { it.copy(isLoading = false, squad = squad) }
        }
    }
}
