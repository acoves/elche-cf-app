package es.elchecf.app.data.remote

import es.elchecf.app.core.network.FOOTBALL_DATA_BASE_URL
import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.MatchDataSource
import es.elchecf.app.data.mapper.toDomain
import es.elchecf.app.data.remote.dto.MatchesResponseDto
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.MatchStatus
import es.elchecf.app.domain.model.Prediction
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

/** ID de Elche CF en football-data.org (confirmado vía `/competitions/PD/teams`, no cambia entre temporadas). */
private const val ELCHE_TEAM_ID = 285

/** FASE 8: football-data.org real. sendPrediction sigue mock: es una API de solo lectura, no hay
 * backend propio de predicciones todavía — CLAUDE.md no ha elegido proveedor para eso. */
class FootballDataMatchDataSource(
    private val client: HttpClient,
) : MatchDataSource {
    override suspend fun fetchNextMatch(): AppResult<Match?, AppError> =
        when (val result = fetchSeasonMatches()) {
            is AppResult.Success -> AppResult.Success(result.value.nextUnplayed())
            is AppResult.Failure -> result
        }

    override suspend fun fetchSeasonMatches(): AppResult<List<Match>, AppError> =
        try {
            val response =
                client
                    .get("$FOOTBALL_DATA_BASE_URL/teams/$ELCHE_TEAM_ID/matches") {
                        parameter("competitions", "PD")
                    }.body<MatchesResponseDto>()
            AppResult.Success(response.matches.map { it.toDomain() })
        } catch (e: Exception) {
            AppResult.Failure(AppError.Network(e.message ?: "No se pudo cargar el calendario"))
        }

    override suspend fun sendPrediction(prediction: Prediction): AppResult<Unit, AppError> = AppResult.Success(Unit)

    private fun List<Match>.nextUnplayed(): Match? =
        filter { it.status != MatchStatus.Finished }
            .minByOrNull { it.kickoffInstant }
}
