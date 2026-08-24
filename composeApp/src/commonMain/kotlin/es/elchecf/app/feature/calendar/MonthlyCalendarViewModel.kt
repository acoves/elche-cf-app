package es.elchecf.app.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.repository.MatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MonthlyCalendarViewModel(
    private val matchRepository: MatchRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MonthlyCalendarUiState())
    val uiState: StateFlow<MonthlyCalendarUiState> = _uiState.asStateFlow()

    init {
        loadMatches(ClubTeam.PrimerEquipo)
    }

    fun selectTeam(team: ClubTeam) {
        if (team == _uiState.value.selectedTeam) return
        loadMatches(team)
    }

    private fun loadMatches(team: ClubTeam) {
        _uiState.update { it.copy(isLoading = true, error = null, selectedTeam = team) }
        viewModelScope.launch {
            when (val result = matchRepository.getSeasonMatches(team)) {
                is AppResult.Success -> _uiState.update { it.copy(isLoading = false, matches = result.value) }
                is AppResult.Failure ->
                    _uiState.update { it.copy(isLoading = false, error = "No se pudo cargar el calendario.") }
            }
        }
    }

    fun goToPreviousMonth() {
        _uiState.update { it.copy(displayedMonth = it.displayedMonth.previous()) }
    }

    fun goToNextMonth() {
        _uiState.update { it.copy(displayedMonth = it.displayedMonth.next()) }
    }
}
