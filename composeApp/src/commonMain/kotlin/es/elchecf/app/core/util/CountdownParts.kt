package es.elchecf.app.core.util

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Instant

data class CountdownParts(
    val days: Long,
    val hours: Long,
    val minutes: Long,
    val seconds: Long,
    val isPast: Boolean,
)

private fun Duration.toCountdownParts(): CountdownParts {
    val past = isNegative()
    val totalSeconds = absoluteValue.inWholeSeconds
    return CountdownParts(
        days = totalSeconds / SECONDS_PER_DAY,
        hours = (totalSeconds % SECONDS_PER_DAY) / SECONDS_PER_HOUR,
        minutes = (totalSeconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE,
        seconds = totalSeconds % SECONDS_PER_MINUTE,
        isPast = past,
    )
}

private const val SECONDS_PER_MINUTE = 60L
private const val SECONDS_PER_HOUR = 3600L
private const val SECONDS_PER_DAY = 86400L

/** Tick de 1s (CLAUDE.md §6): se cancela solo con quien recoja el flow (viewModelScope en Fase 4+). */
fun countdownFlow(
    kickoffInstant: Instant,
    clock: Clock = Clock.System,
): Flow<CountdownParts> =
    flow {
        while (true) {
            emit((kickoffInstant - clock.now()).toCountdownParts())
            delay(1000)
        }
    }
