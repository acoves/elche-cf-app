package es.elchecf.app.domain.model

/** CLAUDE.md §5.2: grid agrupado por posición (PORTEROS/DEFENSAS/CENTROCAMPISTAS/DELANTEROS). */
enum class PlayerPosition { Goalkeeper, Defender, Midfielder, Forward }

data class Player(
    val id: String,
    val firstName: String,
    val lastName: String,
    val number: Int,
    val position: PlayerPosition,
    val photoUrl: String,
)
