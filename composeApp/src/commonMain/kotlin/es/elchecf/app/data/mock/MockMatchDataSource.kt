package es.elchecf.app.data.mock

import es.elchecf.app.core.result.AppError
import es.elchecf.app.core.result.AppResult
import es.elchecf.app.data.MatchDataSource
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
private val elche =
    Team(
        id = "elche-cf",
        name = "Elche CF",
        shortName = "ELCHE",
        crestUrl = "",
        primaryColorHex = "#05642C",
    )

private val rival =
    Team(
        id = "rival-demo",
        name = "Real Madrid",
        shortName = "R. MADRID",
        crestUrl = "",
        primaryColorHex = "#1B458F",
    )

class MockMatchDataSource : MatchDataSource {
    override suspend fun fetchNextMatch(): Match {
        delay(NETWORK_DELAY_MS)
        return Match(
            id = "demo-match-1",
            home = elche,
            away = rival,
            kickoffInstant = Clock.System.now() + 3.days + 2.hours,
            competition = "LaLiga",
            venue = "Martínez Valero",
            status = MatchStatus.Scheduled,
        )
    }

    override suspend fun sendPrediction(prediction: Prediction): AppResult<Unit, AppError> = AppResult.Success(Unit)

    private companion object {
        const val NETWORK_DELAY_MS = 300L
    }
}
