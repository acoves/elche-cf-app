package es.elchecf.app.data.repository

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.MatchDataSource
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.Prediction
import es.elchecf.app.domain.repository.MatchRepository

// FASE 4: delega 1:1 en el data source, sin mapper (el mock ya produce modelos de dominio).
// FASE 8: el data source real devuelve DTOs y este repositorio pasa a mapearlos.
class MatchRepositoryImpl(
    private val dataSource: MatchDataSource,
) : MatchRepository {
    override suspend fun getNextMatch(): Match? = dataSource.fetchNextMatch()

    override suspend fun getSeasonMatches(): List<Match> = dataSource.fetchSeasonMatches()

    override suspend fun submitPrediction(prediction: Prediction): AppResult<Unit, AppError> =
        dataSource.sendPrediction(prediction)
}
