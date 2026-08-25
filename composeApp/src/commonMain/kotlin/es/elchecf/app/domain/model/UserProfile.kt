package es.elchecf.app.domain.model

/** CLAUDE.md §5.5. `memberStatusLabel` ya viene formateado del backend ("Socio · 2 años").
 * [firstName]/[lastName] separados porque la pantalla de "Información personal" los edita por
 * separado (ver referencia de diseño en Perfil). */
data class UserProfile(
    val id: String,
    val firstName: String,
    val lastName: String,
    val avatarUrl: String,
    val memberStatusLabel: String,
) {
    val fullName: String get() = "$firstName $lastName"
}
