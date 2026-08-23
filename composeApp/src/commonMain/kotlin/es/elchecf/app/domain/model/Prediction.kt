package es.elchecf.app.domain.model

/** CLAUDE.md §6 / §5.1 "Acierta el resultado". Persistencia real llega en Fase 8; hasta entonces, solo en memoria. */
data class Prediction(
    val matchId: String,
    val homeGoals: Int,
    val awayGoals: Int,
)
