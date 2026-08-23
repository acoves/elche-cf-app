package es.elchecf.app.domain.model

import kotlin.time.Instant

enum class MatchStatus { Scheduled, Live, Finished }

data class Score(
    val homeGoals: Int,
    val awayGoals: Int,
)

/** CLAUDE.md §6. `kickoffInstant` siempre en UTC; se formatea con la zona del dispositivo en la UI. */
data class Match(
    val id: String,
    val home: Team,
    val away: Team,
    val kickoffInstant: Instant,
    val competition: String,
    val venue: String,
    val status: MatchStatus,
    val score: Score? = null,
)
