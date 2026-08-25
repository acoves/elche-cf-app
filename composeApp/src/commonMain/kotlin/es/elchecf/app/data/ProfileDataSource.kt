package es.elchecf.app.data

import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile
import kotlinx.coroutines.flow.StateFlow

interface ProfileDataSource {
    val profile: StateFlow<UserProfile>

    suspend fun fetchBenefits(): List<Benefit>

    fun updateProfile(
        firstName: String,
        lastName: String,
    )

    fun updateAvatar(avatarUrl: String)
}
