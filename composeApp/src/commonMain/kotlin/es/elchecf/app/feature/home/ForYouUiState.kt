package es.elchecf.app.feature.home

import es.elchecf.app.domain.model.Match

/** Un solo UiState con isLoading/error/datos, nunca sealed Loading/Success/Error (CLAUDE.md §3). */
data class ForYouUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val match: Match? = null,
    val quizScore: Pair<Int, Int>? = null,
    val showValorateMatch: Boolean = false,
    val predictionSent: Boolean = false,
)
