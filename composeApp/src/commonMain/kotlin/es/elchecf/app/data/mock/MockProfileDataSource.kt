package es.elchecf.app.data.mock

import es.elchecf.app.data.ProfileDataSource
import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile
import kotlinx.coroutines.delay

// FASE 7: perfil de ejemplo, no datos reales de ningún socio.
private val profile =
    UserProfile(
        id = "demo-user-1",
        fullName = "Antonio Franjiverde",
        avatarUrl = "",
        memberStatusLabel = "Socio · 2 años",
    )

// Mejora post-Fase 7: beneficios reales del Carnet Franjiverde (abonados.elchecf.es), no
// inventados — verificados en agosto 2026 (precio 49,99€, campaña de abonos 2026/2027).
// Imágenes de Wikimedia Commons, con licencia libre (CLAUDE.md §10: nada de assets oficiales
// del club descargados sin autorización). URLs directas de upload.wikimedia.org (CDN), no la
// redirección Special:FilePath: pedir 6 a la vez a través de esa redirección devuelve 429
// (rate limit) porque cada una pasa antes por el servidor de la wiki, no por el CDN.
private val benefits =
    listOf(
        Benefit(
            id = "benefit-welcome-pack",
            title = "Pack de bienvenida",
            subtitle = "Bufanda, pulsera y carnet físico",
            detail =
                "Al hacerte con el Carnet Franjiverde recibes un pack de bienvenida con " +
                    "bufanda, pulsera y tu carnet físico de socio.",
            imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/4a/" +
                    "Nike_Brazil_national_football_team_scarf.JPG/500px-Nike_Brazil_national_football_team_scarf.JPG",
        ),
        Benefit(
            id = "benefit-match-discount",
            title = "Descuento en partidos seleccionados",
            subtitle = "50% en entradas para dos partidos",
            detail =
                "50% de descuento en las entradas de dos partidos de la temporada, " +
                    "seleccionados por el club.",
            imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/" +
                    "Estadio_Mart%C3%ADnez_Valero.JPG/500px-Estadio_Mart%C3%ADnez_Valero.JPG",
        ),
        Benefit(
            id = "benefit-priority-tickets",
            title = "Acceso prioritario a entradas",
            subtitle = "24h de ventaja en partidos tipo A",
            detail =
                "Prioridad de 24 horas para comprar tu entrada en los partidos tipo A de la " +
                    "temporada, como Real Madrid o FC Barcelona.",
            imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/1/16/" +
                    "Spanish_football_fans.JPG/500px-Spanish_football_fans.JPG",
        ),
        Benefit(
            id = "benefit-away-tickets",
            title = "Entradas para partidos fuera de casa",
            subtitle = "Exclusividad tras los abonados",
            detail =
                "Exclusividad en la venta de entradas para los desplazamientos del equipo, " +
                    "justo después de los abonados.",
            imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/4/40/" +
                    "Fans_von_Borussia_Dortmund_2005.jpg/500px-Fans_von_Borussia_Dortmund_2005.jpg",
        ),
        Benefit(
            id = "benefit-shop-discount",
            title = "Descuento en tienda oficial",
            subtitle = "10% en tiendas del Elche CF",
            detail = "10% de descuento en tu compra en las tiendas oficiales del Elche CF, online y físicas.",
            imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/" +
                    "Football_kit_01.jpg/500px-Football_kit_01.jpg",
        ),
        Benefit(
            id = "benefit-ilicitano-femenino",
            title = "Elche Ilicitano y Femenino",
            subtitle = "Acceso a los partidos del filial y el femenino",
            detail =
                "Tu Carnet Franjiverde también te da acceso a los partidos como local del " +
                    "Elche Ilicitano y del Elche CF Femenino.",
            imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c7/" +
                    "Chelsea_FC_Women_v_Everton_FC_Women%2C_12_September_2021_%2824%29.jpg/" +
                    "500px-Chelsea_FC_Women_v_Everton_FC_Women%2C_12_September_2021_%2824%29.jpg",
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
