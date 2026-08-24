package es.elchecf.app.feature.calendar.standings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.repository.Competition
import es.elchecf.app.domain.repository.CupRepository
import es.elchecf.app.domain.repository.StandingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StandingsViewModel(
    private val standingsRepository: StandingsRepository,
    private val cupRepository: CupRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(StandingsUiState())
    val uiState: StateFlow<StandingsUiState> = _uiState.asStateFlow()

    init {
        loadStandings(Competition.LaLiga, ClubTeam.PrimerEquipo)
        viewModelScope.launch {
            val bracket = cupRepository.getBracket()
            _uiState.update { it.copy(cupBracket = bracket) }
        }
    }

    fun selectCompetition(competition: Competition) {
        _uiState.update { it.copy(selectedCompetition = competition) }
        if (competition == Competition.LaLiga && _uiState.value.standings.isEmpty()) {
            loadStandings(competition, _uiState.value.selectedTeam)
        }
    }

    fun selectTeam(team: ClubTeam) {
        if (team == _uiState.value.selectedTeam) return
        loadStandings(_uiState.value.selectedCompetition, team)
    }

    private fun loadStandings(
        competition: Competition,
        team: ClubTeam,
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, selectedTeam = team) }
            when (val result = standingsRepository.getStandings(competition, team)) {
                is AppResult.Success -> _uiState.update { it.copy(isLoading = false, standings = result.value) }
                is AppResult.Failure ->
                    _uiState.update { it.copy(isLoading = false, error = "No se pudo cargar la clasificación.") }
            }
        }
    }
}
