package es.elchecf.app.domain.model

/** CLAUDE.md §6. `primaryColorHex` alimenta el fondo dividido de [VersusCard], nunca hardcodeado por pantalla. */
data class Team(
    val id: String,
    val name: String,
    val shortName: String,
    val crestUrl: String,
    val primaryColorHex: String,
) {
    companion object {
        /** App oficiosa de un solo club: sirve para distinguir "nosotros" en Match.home/away.
         * FASE 8: coincide con el id real de Elche CF en football-data.org (antes "elche-cf",
         * un valor de mock que nunca podía igualar el id numérico que llega de la API real). */
        const val ELCHE_ID = "285"
    }
}
