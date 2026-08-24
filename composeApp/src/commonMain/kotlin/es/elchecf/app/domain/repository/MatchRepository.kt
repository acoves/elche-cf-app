package es.elchecf.app.domain.repository

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.Prediction

interface MatchRepository {
    /** Siempre el primer equipo: es lo que alimenta la cuenta atrás de "Para ti". */
    suspend fun getNextMatch(): AppResult<Match?, AppError>

    /** CLAUDE.md §5.2: calendario mensual, según el equipo seleccionado en el selector. */
    suspend fun getSeasonMatches(team: ClubTeam): AppResult<List<Match>, AppError>

    suspend fun submitPrediction(prediction: Prediction): AppResult<Unit, AppError>
}
