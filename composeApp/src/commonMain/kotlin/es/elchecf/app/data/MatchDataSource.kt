package es.elchecf.app.data

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.Prediction

/**
 * Misma interfaz para [es.elchecf.app.data.mock.MockMatchDataSource] (hasta Fase 8) y la futura
 * fuente Ktor: cambiar de mock a real es cambiar el binding en el módulo de Koin (CLAUDE.md §0.7).
 */
interface MatchDataSource {
    suspend fun fetchNextMatch(): AppResult<Match?, AppError>

    suspend fun fetchSeasonMatches(): AppResult<List<Match>, AppError>

    suspend fun sendPrediction(prediction: Prediction): AppResult<Unit, AppError>
}
