package es.elchecf.app.data.remote

import es.elchecf.app.core.network.FOOTBALL_DATA_BASE_URL
import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.MatchDataSource
import es.elchecf.app.data.mapper.toDomain
import es.elchecf.app.data.mock.MockMatchDataSource
import es.elchecf.app.data.remote.dto.MatchesResponseDto
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.MatchStatus
import es.elchecf.app.domain.model.Prediction
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/** ID de Elche CF en football-data.org (confirmado vía `/competitions/PD/teams`, no cambia entre temporadas). */
private const val ELCHE_TEAM_ID = 285

/** FASE 8: football-data.org real, solo para [ClubTeam.PrimerEquipo] — el plan gratuito no cubre
 * Liga F ni las categorías regionales (verificado agosto 2026, CLAUDE.md §12), así que Femenino e
 * Ilicitano caen a [MockMatchDataSource]. sendPrediction sigue mock: la API es de solo lectura.
 * Si la llamada real falla (sin conexión, límite de peticiones del plan gratuito, timeout...)
 * también cae a [MockMatchDataSource] en vez de dejar "Para ti" sin cuenta atrás/pronóstico/quiz:
 * mejor mostrar datos de ejemplo que una pantalla muerta. */
class FootballDataMatchDataSource(
    private val client: HttpClient,
) : MatchDataSource {
    private val mockFallback = MockMatchDataSource()

    override suspend fun fetchNextMatch(): AppResult<Match?, AppError> =
        when (val result = fetchSeasonMatches(ClubTeam.PrimerEquipo)) {
            is AppResult.Success -> AppResult.Success(result.value.nextUnplayed())
            is AppResult.Failure -> result
        }

    override suspend fun fetchSeasonMatches(team: ClubTeam): AppResult<List<Match>, AppError> {
        if (team != ClubTeam.PrimerEquipo) return mockFallback.fetchSeasonMatches(team)
        return try {
            val response =
                client
                    .get("$FOOTBALL_DATA_BASE_URL/teams/$ELCHE_TEAM_ID/matches") {
                        parameter("competitions", "PD")
                    }.body<MatchesResponseDto>()
            AppResult.Success(response.matches.map { it.toDomain() })
        } catch (e: Exception) {
            mockFallback.fetchSeasonMatches(team)
        }
    }

    override suspend fun sendPrediction(prediction: Prediction): AppResult<Unit, AppError> = AppResult.Success(Unit)

    private fun List<Match>.nextUnplayed(): Match? =
        filter { it.status != MatchStatus.Finished }
            .minByOrNull { it.kickoffInstant }
}
