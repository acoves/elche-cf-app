package es.elchecf.app.feature.calendar.players

import es.elchecf.app.domain.model.Player

data class PlayersUiState(
    val isLoading: Boolean = true,
    val squad: List<Player> = emptyList(),
)
