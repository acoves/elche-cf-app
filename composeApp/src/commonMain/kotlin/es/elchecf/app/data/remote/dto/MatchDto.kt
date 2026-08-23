package es.elchecf.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MatchesResponseDto(
    val matches: List<MatchDto>,
)

@Serializable
data class MatchDto(
    val id: Int,
    val utcDate: String,
    val status: String,
    val homeTeam: TeamDto,
    val awayTeam: TeamDto,
    val score: ScoreDto,
    val competition: CompetitionDto,
)

@Serializable
data class ScoreDto(
    val fullTime: ScoreDetailDto,
)

@Serializable
data class ScoreDetailDto(
    val home: Int? = null,
    val away: Int? = null,
)

@Serializable
data class CompetitionDto(
    val name: String,
)
