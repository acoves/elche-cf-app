package es.elchecf.app.data.repository

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.MatchDataSource
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.Prediction
import es.elchecf.app.domain.repository.MatchRepository

// FASE 8: delega 1:1 en el data source (mock o Ktor real, ver Koin). El mapeo DTO→dominio vive
// en el data source real; este repositorio no conoce DTOs.
class MatchRepositoryImpl(
    private val dataSource: MatchDataSource,
) : MatchRepository {
    override suspend fun getNextMatch(): AppResult<Match?, AppError> = dataSource.fetchNextMatch()

    override suspend fun getSeasonMatches(team: ClubTeam): AppResult<List<Match>, AppError> =
        dataSource.fetchSeasonMatches(team)

    override suspend fun submitPrediction(prediction: Prediction): AppResult<Unit, AppError> =
        dataSource.sendPrediction(prediction)
}
