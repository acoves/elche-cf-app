package es.elchecf.app.feature.calendar

import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.model.Match

data class MonthlyCalendarUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val displayedMonth: YearMonth = YearMonth.current(),
    val selectedTeam: ClubTeam = ClubTeam.PrimerEquipo,
    val matches: List<Match> = emptyList(),
)
