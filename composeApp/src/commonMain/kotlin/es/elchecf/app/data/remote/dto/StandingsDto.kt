package es.elchecf.app.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class StandingsResponseDto(
    val standings: List<StandingsGroupDto>,
)

@Serializable
data class StandingsGroupDto(
    val type: String,
    val table: List<StandingsRowDto>,
)

@Serializable
data class StandingsRowDto(
    val position: Int,
    val team: TeamDto,
    val playedGames: Int,
    val won: Int,
    val draw: Int,
    val lost: Int,
    val goalDifference: Int,
    val points: Int,
)
