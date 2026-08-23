package es.elchecf.app.data.repository

import es.elchecf.app.data.ProfileDataSource
import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile
import es.elchecf.app.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val dataSource: ProfileDataSource,
) : ProfileRepository {
    override suspend fun getProfile(): UserProfile = dataSource.fetchProfile()

    override suspend fun getBenefits(): List<Benefit> = dataSource.fetchBenefits()
}
