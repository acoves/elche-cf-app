package es.elchecf.app.data.mock

import es.elchecf.app.data.ProfileDataSource
import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile
import kotlinx.coroutines.delay

// FASE 7: perfil y beneficios de ejemplo, no datos reales de ningún socio.
private val profile =
    UserProfile(
        id = "demo-user-1",
        fullName = "Antonio Franjiverde",
        avatarUrl = "",
        memberStatusLabel = "Socio · 2 años",
    )

private val benefits =
    listOf(
        Benefit(
            id = "benefit-1",
            title = "Descuento en tienda oficial",
            subtitle = "10% en tu próxima compra online",
            imageUrl = "",
        ),
        Benefit(
            id = "benefit-2",
            title = "Acceso prioritario a entradas",
            subtitle = "Compra antes que el público general",
            imageUrl = "",
        ),
        Benefit(
            id = "benefit-3",
            title = "Contenido exclusivo",
            subtitle = "Vídeos y entrevistas solo para socios",
            imageUrl = "",
        ),
        Benefit(
            id = "benefit-4",
            title = "Entrenamiento abierto",
            subtitle = "Una invitación al año a ver al equipo entrenar",
            imageUrl = "",
        ),
    )

class MockProfileDataSource : ProfileDataSource {
    override suspend fun fetchProfile(): UserProfile {
        delay(NETWORK_DELAY_MS)
        return profile
    }

    override suspend fun fetchBenefits(): List<Benefit> {
        delay(NETWORK_DELAY_MS)
        return benefits
    }

    private companion object {
        const val NETWORK_DELAY_MS = 300L
    }
}
