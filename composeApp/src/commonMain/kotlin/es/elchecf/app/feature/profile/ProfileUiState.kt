package es.elchecf.app.feature.profile

import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile

data class ProfileUiState(
    val isLoading: Boolean = true,
    val isLoggedIn: Boolean = true,
    val profile: UserProfile? = null,
    val benefits: List<Benefit> = emptyList(),
)
