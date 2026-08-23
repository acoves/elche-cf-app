package es.elchecf.app.domain.repository

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.Prediction

interface MatchRepository {
    suspend fun getNextMatch(): Match?

    suspend fun submitPrediction(prediction: Prediction): AppResult<Unit, AppError>
}
