package es.elchecf.app.domain.model

/** CLAUDE.md §5.5. `memberStatusLabel` ya viene formateado del backend ("Socio · 2 años"). */
data class UserProfile(
    val id: String,
    val fullName: String,
    val avatarUrl: String,
    val memberStatusLabel: String,
)
