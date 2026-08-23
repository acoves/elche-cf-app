package es.elchecf.app.data.repository

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.StandingsDataSource
import es.elchecf.app.domain.model.StandingRow
import es.elchecf.app.domain.repository.Competition
import es.elchecf.app.domain.repository.StandingsRepository

class StandingsRepositoryImpl(
    private val dataSource: StandingsDataSource,
) : StandingsRepository {
    override suspend fun getStandings(competition: Competition): AppResult<List<StandingRow>, AppError> =
        dataSource.fetchStandings(competition)
}
