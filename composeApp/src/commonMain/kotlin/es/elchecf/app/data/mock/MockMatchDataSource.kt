package es.elchecf.app.data.mock

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.MatchDataSource
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.MatchStatus
import es.elchecf.app.domain.model.Prediction
import es.elchecf.app.domain.model.Team
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

// FASE 4: datos de ejemplo, no oficiales del club (CLAUDE.md §10 — sin escudos/assets reales sin permiso).
// FASE 8: se sustituye por una fuente Ktor real; MatchRepository no cambia.
private val elche = DemoTeams.elche

private fun buildSeason(
    competition: String,
    rivals: List<Pair<Team, Boolean>>,
): List<Match> =
    rivals.mapIndexed { index, (rival, isHome) ->
        val kickoff = Clock.System.now() + (3 + index * 9).days + 2.hours
        Match(
            id = "demo-match-$competition-${index + 1}",
            home = if (isHome) elche else rival,
            away = if (isHome) rival else elche,
            kickoffInstant = kickoff,
            competition = competition,
            venue = if (isHome) "Martínez Valero" else "Estadio ${rival.shortName}",
            status = MatchStatus.Scheduled,
        )
    }

// FASE 5: partidos de la temporada de ejemplo, repartidos cada ~9 días desde hoy.
private val primerEquipoMatches: List<Match> =
    buildSeason(
        "LaLiga",
        listOf(
            DemoTeams.realMadrid to false,
            DemoTeams.barcelona to true,
            DemoTeams.sevilla to false,
            DemoTeams.villarreal to true,
            DemoTeams.athleticClub to false,
            DemoTeams.realBetis to true,
            DemoTeams.valencia to false,
            DemoTeams.celtaVigo to true,
        ),
    )

// Mejora post-Fase 5: football-data.org no cubre Liga F — calendario de ejemplo, no el fixture real.
private val femeninoMatches: List<Match> =
    buildSeason(
        "Liga F",
        listOf(
            DemoTeams.barcelonaFemeni to false,
            DemoTeams.realMadridFemenino to true,
            DemoTeams.levanteFemenino to false,
            DemoTeams.sevillaFemenino to true,
            DemoTeams.realSociedadFemenino to false,
        ),
    )

// Mejora post-Fase 5: football-data.org no cubre las categorías regionales — calendario de
// ejemplo, no el grupo/fixture real de la temporada.
private val ilicitanoMatches: List<Match> =
    buildSeason(
        "2ª Federación",
        listOf(
            DemoTeams.hercules to false,
            DemoTeams.eldense to true,
            DemoTeams.alicanteCf to false,
            DemoTeams.yeclano to true,
            DemoTeams.intercity to false,
        ),
    )

private fun matchesFor(team: ClubTeam): List<Match> =
    when (team) {
        ClubTeam.PrimerEquipo -> primerEquipoMatches
        ClubTeam.Femenino -> femeninoMatches
        ClubTeam.Ilicitano -> ilicitanoMatches
    }

class MockMatchDataSource : MatchDataSource {
    override suspend fun fetchNextMatch(): AppResult<Match?, AppError> {
        delay(NETWORK_DELAY_MS)
        return AppResult.Success(primerEquipoMatches.first())
    }

    override suspend fun fetchSeasonMatches(team: ClubTeam): AppResult<List<Match>, AppError> {
        delay(NETWORK_DELAY_MS)
        return AppResult.Success(matchesFor(team))
    }

    override suspend fun sendPrediction(prediction: Prediction): AppResult<Unit, AppError> = AppResult.Success(Unit)

    private companion object {
        const val NETWORK_DELAY_MS = 300L
    }
}
