package es.elchecf.app.domain.repository

import es.elchecf.app.domain.model.StandingRow

enum class Competition { LaLiga, Copa }

interface StandingsRepository {
    suspend fun getStandings(competition: Competition): List<StandingRow>
}
