package es.elchecf.app.data.mapper

import es.elchecf.app.data.remote.dto.MatchDto
import es.elchecf.app.data.remote.dto.TeamDto
import es.elchecf.app.domain.model.Match
import es.elchecf.app.domain.model.MatchStatus
import es.elchecf.app.domain.model.Score
import es.elchecf.app.domain.model.Team
import kotlin.time.Instant

fun TeamDto.toDomain(): Team =
    Team(
        id = id.toString(),
        name = name,
        shortName = shortName ?: tla ?: name,
        crestUrl = crest ?: "",
        // FASE 8: football-data.org no da el color de marca del equipo; VersusCard ya tiene un
        // fallback (verde/azul) para cuando toColorOrNull() no puede resolver un hex vacío.
        primaryColorHex = "",
    )

fun MatchDto.toDomain(): Match =
    Match(
        id = id.toString(),
        home = homeTeam.toDomain(),
        away = awayTeam.toDomain(),
        kickoffInstant = Instant.parse(utcDate),
        competition = competition.name,
        // FASE 8: el endpoint de partidos no incluye el estadio; se aproxima con el equipo local.
        venue = "Campo de ${homeTeam.shortName ?: homeTeam.name}",
        status = status.toMatchStatus(),
        score =
            score.fullTime.home?.let { home ->
                score.fullTime.away?.let { away -> Score(home, away) }
            },
    )

private fun String.toMatchStatus(): MatchStatus =
    when (this) {
        "SCHEDULED", "TIMED" -> MatchStatus.Scheduled
        "IN_PLAY", "PAUSED" -> MatchStatus.Live
        "FINISHED" -> MatchStatus.Finished
        // POSTPONED/SUSPENDED/CANCELLED: sin estado propio en el dominio todavía, se trata como
        // programado en vez de añadir un caso solo para 3 valores poco frecuentes.
        else -> MatchStatus.Scheduled
    }
