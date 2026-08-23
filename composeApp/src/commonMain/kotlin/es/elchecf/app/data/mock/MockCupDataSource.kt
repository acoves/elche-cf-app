package es.elchecf.app.data.mock

import es.elchecf.app.data.CupDataSource
import es.elchecf.app.domain.model.CupRound
import es.elchecf.app.domain.model.CupTie
import es.elchecf.app.domain.model.Score
import kotlinx.coroutines.delay

// FASE 5: bracket de ejemplo — el Elche llega a semifinales y cae. La final aún no se ha jugado
// (aggregate/winner = null) para poder ver también el estado "pendiente" del bracket.
private val bracket =
    listOf(
        // Octavos
        CupTie(CupRound.RoundOf16, DemoTeams.elche, DemoTeams.getafe, Score(2, 1), DemoTeams.elche),
        CupTie(CupRound.RoundOf16, DemoTeams.realMadrid, DemoTeams.alaves, Score(3, 0), DemoTeams.realMadrid),
        CupTie(CupRound.RoundOf16, DemoTeams.barcelona, DemoTeams.lasPalmas, Score(4, 1), DemoTeams.barcelona),
        CupTie(CupRound.RoundOf16, DemoTeams.atleticoMadrid, DemoTeams.leganes, Score(2, 0), DemoTeams.atleticoMadrid),
        CupTie(CupRound.RoundOf16, DemoTeams.athleticClub, DemoTeams.espanyol, Score(1, 0), DemoTeams.athleticClub),
        CupTie(CupRound.RoundOf16, DemoTeams.realSociedad, DemoTeams.gironaFc, Score(2, 1), DemoTeams.realSociedad),
        CupTie(CupRound.RoundOf16, DemoTeams.villarreal, DemoTeams.mallorca, Score(3, 1), DemoTeams.villarreal),
        CupTie(CupRound.RoundOf16, DemoTeams.sevilla, DemoTeams.osasuna, Score(2, 1), DemoTeams.sevilla),
        // Cuartos
        CupTie(CupRound.QuarterFinal, DemoTeams.elche, DemoTeams.realMadrid, Score(1, 0), DemoTeams.elche),
        CupTie(CupRound.QuarterFinal, DemoTeams.barcelona, DemoTeams.atleticoMadrid, Score(3, 2), DemoTeams.barcelona),
        CupTie(
            CupRound.QuarterFinal,
            DemoTeams.athleticClub,
            DemoTeams.realSociedad,
            Score(2, 1),
            DemoTeams.athleticClub,
        ),
        CupTie(CupRound.QuarterFinal, DemoTeams.villarreal, DemoTeams.sevilla, Score(2, 0), DemoTeams.villarreal),
        // Semis
        CupTie(CupRound.SemiFinal, DemoTeams.elche, DemoTeams.barcelona, Score(1, 3), DemoTeams.barcelona),
        CupTie(CupRound.SemiFinal, DemoTeams.athleticClub, DemoTeams.villarreal, Score(2, 1), DemoTeams.athleticClub),
        // Final (pendiente de jugar)
        CupTie(CupRound.Final, DemoTeams.barcelona, DemoTeams.athleticClub, null, null),
    )

class MockCupDataSource : CupDataSource {
    override suspend fun fetchBracket(): List<CupTie> {
        delay(NETWORK_DELAY_MS)
        return bracket
    }

    private companion object {
        const val NETWORK_DELAY_MS = 300L
    }
}
