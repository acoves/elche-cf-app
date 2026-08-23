package es.elchecf.app.data

import es.elchecf.app.domain.model.StandingRow
import es.elchecf.app.domain.repository.Competition

interface StandingsDataSource {
    suspend fun fetchStandings(competition: Competition): List<StandingRow>
}
