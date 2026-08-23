package es.elchecf.app.data.remote

import es.elchecf.app.core.network.FOOTBALL_DATA_BASE_URL
import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.StandingsDataSource
import es.elchecf.app.data.mapper.toDomain
import es.elchecf.app.data.remote.dto.StandingsResponseDto
import es.elchecf.app.domain.model.StandingRow
import es.elchecf.app.domain.repository.Competition
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

private const val TOTAL_STANDINGS_TYPE = "TOTAL"

/** FASE 8: football-data.org real. La Copa del Rey no está en el plan gratis — se sigue solo
 * en el bracket de ejemplo ([es.elchecf.app.data.mock.MockCupDataSource], sin cambios). */
class FootballDataStandingsDataSource(
    private val client: HttpClient,
) : StandingsDataSource {
    override suspend fun fetchStandings(competition: Competition): AppResult<List<StandingRow>, AppError> =
        when (competition) {
            Competition.Copa -> AppResult.Success(emptyList())
            Competition.LaLiga ->
                try {
                    val response =
                        client
                            .get("$FOOTBALL_DATA_BASE_URL/competitions/PD/standings")
                            .body<StandingsResponseDto>()
                    val table =
                        response.standings
                            .firstOrNull { it.type == TOTAL_STANDINGS_TYPE }
                            ?.table
                            .orEmpty()
                    AppResult.Success(table.map { it.toDomain() })
                } catch (e: Exception) {
                    AppResult.Failure(AppError.Network(e.message ?: "No se pudo cargar la clasificación"))
                }
        }
}
