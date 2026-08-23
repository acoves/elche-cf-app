package es.elchecf.app.feature.calendar.standings

import es.elchecf.app.domain.model.CupTie
import es.elchecf.app.domain.model.StandingRow
import es.elchecf.app.domain.repository.Competition

data class StandingsUiState(
    val isLoading: Boolean = true,
    val selectedCompetition: Competition = Competition.LaLiga,
    val standings: List<StandingRow> = emptyList(),
    val cupBracket: List<CupTie> = emptyList(),
)
