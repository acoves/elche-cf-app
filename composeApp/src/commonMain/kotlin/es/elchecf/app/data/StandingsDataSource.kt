package es.elchecf.app.data

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.StandingRow
import es.elchecf.app.domain.repository.Competition

interface StandingsDataSource {
    suspend fun fetchStandings(competition: Competition): AppResult<List<StandingRow>, AppError>
}
