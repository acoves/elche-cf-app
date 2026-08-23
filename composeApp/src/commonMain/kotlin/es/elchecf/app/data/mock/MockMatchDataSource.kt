package es.elchecf.app.data.mock

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.MatchDataSource
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.MatchStatus
import es.elchecf.app.domain.model.Prediction
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

// FASE 4: datos de ejemplo, no oficiales del club (CLAUDE.md §10 — sin escudos/assets reales sin permiso).
// FASE 8: se sustituye por una fuente Ktor real; MatchRepository no cambia.
private val elche = DemoTeams.elche

// FASE 5: partidos de la temporada de ejemplo, repartidos cada ~9 días desde hoy.
private val seasonMatches: List<Match> =
    listOf(
        DemoTeams.realMadrid to false,
        DemoTeams.barcelona to true,
        DemoTeams.sevilla to false,
        DemoTeams.villarreal to true,
        DemoTeams.athleticClub to false,
        DemoTeams.realBetis to true,
        DemoTeams.valencia to false,
        DemoTeams.celtaVigo to true,
    ).mapIndexed { index, (rival, isHome) ->
        val kickoff = Clock.System.now() + (3 + index * 9).days + 2.hours
        Match(
            id = "demo-match-${index + 1}",
            home = if (isHome) elche else rival,
            away = if (isHome) rival else elche,
            kickoffInstant = kickoff,
            competition = "LaLiga",
            venue = if (isHome) "Martínez Valero" else "Estadio ${rival.shortName}",
            status = MatchStatus.Scheduled,
        )
    }

class MockMatchDataSource : MatchDataSource {
    override suspend fun fetchNextMatch(): AppResult<Match?, AppError> {
        delay(NETWORK_DELAY_MS)
        return AppResult.Success(seasonMatches.first())
    }

    override suspend fun fetchSeasonMatches(): AppResult<List<Match>, AppError> {
        delay(NETWORK_DELAY_MS)
        return AppResult.Success(seasonMatches)
    }

    override suspend fun sendPrediction(prediction: Prediction): AppResult<Unit, AppError> = AppResult.Success(Unit)

    private companion object {
        const val NETWORK_DELAY_MS = 300L
    }
}
