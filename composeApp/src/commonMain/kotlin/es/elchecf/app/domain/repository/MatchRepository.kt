package es.elchecf.app.domain.repository

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.Prediction

interface MatchRepository {
    suspend fun getNextMatch(): AppResult<Match?, AppError>

    /** CLAUDE.md §5.2: calendario mensual — todos los partidos de la temporada de ejemplo. */
    suspend fun getSeasonMatches(): AppResult<List<Match>, AppError>

    suspend fun submitPrediction(prediction: Prediction): AppResult<Unit, AppError>
}
