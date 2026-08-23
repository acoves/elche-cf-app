package es.elchecf.app.domain.model

/** CLAUDE.md §5.2: bracket octavos → cuartos → semis → final. */
enum class CupRound { RoundOf16, QuarterFinal, SemiFinal, Final }

data class CupTie(
    val round: CupRound,
    val home: Team,
    val away: Team,
    val aggregate: Score?,
    val winner: Team?,
)
