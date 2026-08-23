package es.elchecf.app.domain.repository

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.domain.model.StandingRow

enum class Competition { LaLiga, Copa }

interface StandingsRepository {
    suspend fun getStandings(competition: Competition): AppResult<List<StandingRow>, AppError>
}
