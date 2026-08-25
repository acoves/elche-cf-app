package es.elchecf.app.data.repository

import es.elchecf.app.data.ProfileDataSource
import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile
import es.elchecf.app.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.StateFlow

class ProfileRepositoryImpl(
    private val dataSource: ProfileDataSource,
) : ProfileRepository {
    override val profile: StateFlow<UserProfile> = dataSource.profile

    override suspend fun getBenefits(): List<Benefit> = dataSource.fetchBenefits()

    override fun updateProfile(
        firstName: String,
        lastName: String,
    ) = dataSource.updateProfile(firstName, lastName)

    override fun updateAvatar(avatarUrl: String) = dataSource.updateAvatar(avatarUrl)
}
