package es.elchecf.app.feature.calendar

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

private const val MONTHS_IN_YEAR = 12

data class YearMonth(
    val year: Int,
    val month: Int,
) {
    fun next(): YearMonth = if (month == MONTHS_IN_YEAR) YearMonth(year + 1, 1) else YearMonth(year, month + 1)

    fun previous(): YearMonth = if (month == 1) YearMonth(year - 1, MONTHS_IN_YEAR) else YearMonth(year, month - 1)

    val firstDay: LocalDate get() = LocalDate(year, month, 1)

    val daysInMonth: Int get() = firstDay.daysUntil(next().firstDay)

    companion object {
        fun current(timeZone: TimeZone = TimeZone.currentSystemDefault()): YearMonth {
            val today =
                Clock.System
                    .now()
                    .toLocalDateTime(timeZone)
                    .date
            return YearMonth(today.year, today.month.number)
        }

        fun of(
            instant: Instant,
            timeZone: TimeZone = TimeZone.currentSystemDefault(),
        ): YearMonth {
            val date = instant.toLocalDateTime(timeZone).date
            return YearMonth(date.year, date.month.number)
        }
    }
}
