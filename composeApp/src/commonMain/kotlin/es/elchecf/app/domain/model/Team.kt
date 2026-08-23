package es.elchecf.app.domain.model

/** CLAUDE.md §6. `primaryColorHex` alimenta el fondo dividido de [VersusCard], nunca hardcodeado por pantalla. */
data class Team(
    val id: String,
    val name: String,
    val shortName: String,
    val crestUrl: String,
    val primaryColorHex: String,
)
