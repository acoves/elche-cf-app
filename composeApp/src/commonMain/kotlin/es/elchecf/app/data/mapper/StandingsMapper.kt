package es.elchecf.app.data.mapper

import es.elchecf.app.data.remote.dto.StandingsRowDto
import es.elchecf.app.domain.model.StandingRow

fun StandingsRowDto.toDomain(): StandingRow =
    StandingRow(
        position = position,
        team = team.toDomain(),
        played = playedGames,
        won = won,
        drawn = draw,
        lost = lost,
        goalDiff = goalDifference,
        points = points,
    )
