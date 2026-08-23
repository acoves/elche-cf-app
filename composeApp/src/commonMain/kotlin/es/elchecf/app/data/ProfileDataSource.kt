package es.elchecf.app.data

import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile

interface ProfileDataSource {
    suspend fun fetchProfile(): UserProfile

    suspend fun fetchBenefits(): List<Benefit>
}
