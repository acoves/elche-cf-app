package es.elchecf.app.domain.repository

import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile
import kotlinx.coroutines.flow.StateFlow

interface ProfileRepository {
    val profile: StateFlow<UserProfile>

    suspend fun getBenefits(): List<Benefit>

    fun updateProfile(
        firstName: String,
        lastName: String,
    )

    fun updateAvatar(avatarUrl: String)
}
