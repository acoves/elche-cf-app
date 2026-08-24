package es.elchecf.app.domain.model

/**
 * Selector de equipo del club (CLAUDE.md §5.2). Solo [PrimerEquipo] tiene datos reales
 * (football-data.org no cubre Liga F ni las categorías regionales donde juega el filial) —
 * [Femenino] e [Ilicitano] se sirven con datos de ejemplo, ver `data/mock/MockMatchDataSource.kt`
 * y `data/mock/MockStandingsDataSource.kt`.
 */
enum class ClubTeam(
    val label: String,
) {
    PrimerEquipo("Primer equipo"),
    Femenino("Elche Femenino"),
    Ilicitano("Elche Ilicitano"),
}
