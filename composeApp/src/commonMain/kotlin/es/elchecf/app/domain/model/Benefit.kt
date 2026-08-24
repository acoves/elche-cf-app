package es.elchecf.app.domain.model

/** [detail] es el texto largo del pop-up de información (Perfil → Beneficios → "···"); [title] y
 * [subtitle] son los que se ven en la fila de la lista. */
data class Benefit(
    val id: String,
    val title: String,
    val subtitle: String,
    val detail: String,
    val imageUrl: String,
)
