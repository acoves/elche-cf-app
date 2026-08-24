package es.elchecf.app.data.mock

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.StandingsDataSource
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.domain.model.StandingRow
import es.elchecf.app.domain.model.Team
import es.elchecf.app.domain.repository.Competition
import kotlinx.coroutines.delay

private fun buildStandings(order: List<Team>): List<StandingRow> =
    order.mapIndexed { index, team ->
        val played = 10
        val won = (7 - index / 3).coerceIn(0, played)
        val lost = (index / 4).coerceIn(0, played - won)
        val drawn = played - won - lost
        val goalsFor = 20 - index
        val goalsAgainst = 8 + index
        StandingRow(
            position = index + 1,
            team = team,
            played = played,
            won = won,
            drawn = drawn,
            lost = lost,
            goalDiff = goalsFor - goalsAgainst,
            points = won * 3 + drawn,
        )
    }

// FASE 5: clasificación LaLiga de ejemplo, con el Elche en mitad de tabla. Sin datos de Copa
// todavía (la fase de la sección de Copa vive en el bracket, no en esta tabla).
private val laLigaStandings: List<StandingRow> =
    buildStandings(
        listOf(
            DemoTeams.realMadrid,
            DemoTeams.barcelona,
            DemoTeams.atleticoMadrid,
            DemoTeams.athleticClub,
            DemoTeams.realSociedad,
            DemoTeams.villarreal,
            DemoTeams.sevilla,
            DemoTeams.celtaVigo,
            DemoTeams.rayoVallecano,
            DemoTeams.osasuna,
            DemoTeams.getafe,
            DemoTeams.elche,
            DemoTeams.realBetis,
            DemoTeams.valencia,
            DemoTeams.mallorca,
            DemoTeams.alaves,
            DemoTeams.gironaFc,
            DemoTeams.espanyol,
            DemoTeams.lasPalmas,
            DemoTeams.leganes,
        ),
    )

// Mejora post-Fase 5: football-data.org no cubre Liga F ni las categorías regionales (verificado
// agosto 2026, CLAUDE.md §12) — tablas de ejemplo, no la clasificación real de la temporada.
private val femeninoStandings: List<StandingRow> =
    buildStandings(
        listOf(
            DemoTeams.barcelonaFemeni,
            DemoTeams.realMadridFemenino,
            DemoTeams.elche,
            DemoTeams.sevillaFemenino,
            DemoTeams.levanteFemenino,
            DemoTeams.realSociedadFemenino,
        ),
    )

private val ilicitanoStandings: List<StandingRow> =
    buildStandings(
        listOf(
            DemoTeams.hercules,
            DemoTeams.elche,
            DemoTeams.eldense,
            DemoTeams.alicanteCf,
            DemoTeams.yeclano,
            DemoTeams.intercity,
        ),
    )

private fun standingsFor(team: ClubTeam): List<StandingRow> =
    when (team) {
        ClubTeam.PrimerEquipo -> laLigaStandings
        ClubTeam.Femenino -> femeninoStandings
        ClubTeam.Ilicitano -> ilicitanoStandings
    }

class MockStandingsDataSource : StandingsDataSource {
    override suspend fun fetchStandings(
        competition: Competition,
        team: ClubTeam,
    ): AppResult<List<StandingRow>, AppError> {
        delay(NETWORK_DELAY_MS)
        return AppResult.Success(
            when (competition) {
                Competition.LaLiga -> standingsFor(team)
                Competition.Copa -> emptyList() // FASE 5: la Copa se sigue en el bracket, no en tabla
            },
        )
    }

    private companion object {
        const val NETWORK_DELAY_MS = 300L
    }
}
