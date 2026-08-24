package es.elchecf.app.feature.calendar.standings

import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.model.CupTie
import es.elchecf.app.domain.model.StandingRow
import es.elchecf.app.domain.repository.Competition

data class StandingsUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val selectedCompetition: Competition = Competition.LaLiga,
    val selectedTeam: ClubTeam = ClubTeam.PrimerEquipo,
    val standings: List<StandingRow> = emptyList(),
    val cupBracket: List<CupTie> = emptyList(),
)
