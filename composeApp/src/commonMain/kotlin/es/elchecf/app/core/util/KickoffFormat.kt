package es.elchecf.app.core.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

private val kickoffFormat =
    LocalDateTime.Format {
        day()
        chars("/")
        monthNumber()
        chars(" · ")
        hour()
        chars(":")
        minute()
    }

/** "23/8 · 20:00" en la zona horaria del dispositivo (CLAUDE.md §6: kickoff se guarda en UTC). */
fun Instant.toKickoffLabel(timeZone: TimeZone = TimeZone.currentSystemDefault()): String =
    kickoffFormat.format(toLocalDateTime(timeZone))
