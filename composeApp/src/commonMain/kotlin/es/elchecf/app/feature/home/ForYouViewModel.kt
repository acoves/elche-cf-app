package es.elchecf.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.MatchStatus
import es.elchecf.app.domain.model.Prediction
import es.elchecf.app.domain.repository.MatchRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

private val VALORATE_WINDOW = 48.hours

private fun Match.isRecentlyFinished(now: kotlin.time.Instant): Boolean =
    status == MatchStatus.Finished && (now - kickoffInstant) <= VALORATE_WINDOW

class ForYouViewModel(
    private val matchRepository: MatchRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ForYouUiState())
    val uiState: StateFlow<ForYouUiState> = _uiState.asStateFlow()

    init {
        loadMatch()
    }

    private fun loadMatch() {
        viewModelScope.launch {
            val match = matchRepository.getNextMatch()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    match = match,
                    // FASE 4: sin backend de quiz todavía, puntuación de ejemplo fija.
                    quizScore = if (match != null) 3 to 5 else null,
                    showValorateMatch = match?.isRecentlyFinished(Clock.System.now()) == true,
                )
            }
        }
    }

    fun submitPrediction(
        homeGoals: Int,
        awayGoals: Int,
    ) {
        val matchId = _uiState.value.match?.id ?: return
        viewModelScope.launch {
            when (matchRepository.submitPrediction(Prediction(matchId, homeGoals, awayGoals))) {
                is AppResult.Success -> _uiState.update { it.copy(predictionSent = true, error = null) }
                is AppResult.Failure -> _uiState.update { it.copy(error = "No se pudo enviar la predicción.") }
            }
        }
    }
}
