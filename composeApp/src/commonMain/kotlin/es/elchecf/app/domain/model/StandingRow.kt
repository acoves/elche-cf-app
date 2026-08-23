package es.elchecf.app.domain.model

/** CLAUDE.md §6 / §5.2. `goalDiff` = DG, `points` = PTS. */
data class StandingRow(
    val position: Int,
    val team: Team,
    val played: Int,
    val won: Int,
    val drawn: Int,
    val lost: Int,
    val goalDiff: Int,
    val points: Int,
)
