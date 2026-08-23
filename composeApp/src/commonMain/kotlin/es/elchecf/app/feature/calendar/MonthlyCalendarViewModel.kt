package es.elchecf.app.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
        viewModelScope.launch {
            val matches = matchRepository.getSeasonMatches()
            _uiState.update { it.copy(isLoading = false, matches = matches) }
        }
    }

    fun goToPreviousMonth() {
        _uiState.update { it.copy(displayedMonth = it.displayedMonth.previous()) }
    }

    fun goToNextMonth() {
        _uiState.update { it.copy(displayedMonth = it.displayedMonth.next()) }
    }
}
