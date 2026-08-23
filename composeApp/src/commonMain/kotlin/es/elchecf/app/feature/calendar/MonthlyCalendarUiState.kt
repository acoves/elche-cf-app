package es.elchecf.app.feature.calendar

import es.elchecf.app.domain.model.Match

data class MonthlyCalendarUiState(
    val isLoading: Boolean = true,
    val displayedMonth: YearMonth = YearMonth.current(),
    val matches: List<Match> = emptyList(),
)
