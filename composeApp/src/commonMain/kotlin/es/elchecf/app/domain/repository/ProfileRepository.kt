package es.elchecf.app.domain.repository

import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): UserProfile

    suspend fun getBenefits(): List<Benefit>
}
