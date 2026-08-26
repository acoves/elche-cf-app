package es.elchecf.app.feature.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import es.elchecf.app.core.webview.AppWebView
import es.elchecf.app.designsystem.component.ElcheSegmentedTabRow
import es.elchecf.app.designsystem.theme.ElcheSpacing

/** URL de Tienda, también usada por [es.elchecf.app.prefetchShopWebView] para precargarla al
 * arrancar la app (ver App.kt) — vive aquí porque es este archivo el dueño de las URLs de Tienda. */
const val TIENDA_URL = "https://tienda.elchecf.es"

/** No `private`: [es.elchecf.app.App] necesita nombrar [Membership] para poder abrir Tienda
 * directamente en esa sub-pestaña desde el botón del pop-up de un beneficio en Perfil. */
enum class ShopTab(
    val label: String,
    val title: String,
    val url: String?,
) {
    Tienda("Tienda", "Tienda", TIENDA_URL),
    Entradas("Entradas", "Entradas", "https://entradas.elchecf.es"),

    // Sin URL a propósito: Membership ya no es un WebView, tiene contenido nativo propio
    // (ver MembershipContent.kt) — antes apuntaba a la home del club, que no era la sección
    // real de abonados.
    Membership("Membership", "Membership", null),
}

@Composable
fun ShopScreen(
    initialTab: ShopTab? = null,
    onInitialTabConsumed: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var selectedTab by remember { mutableStateOf(initialTab ?: ShopTab.Tienda) }

    // Consume la pestaña solicitada desde fuera (p. ej. Perfil → beneficio → Membership) una sola
    // vez: si el usuario vuelve a Tienda por su cuenta después, se ve la primera pestaña de siempre.
    LaunchedEffect(initialTab) {
        if (initialTab != null) {
            selectedTab = initialTab
            onInitialTabConsumed()
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        ElcheSegmentedTabRow(
            tabs = ShopTab.entries,
            selected = selectedTab,
            onSelect = { selectedTab = it },
            label = { it.label },
            modifier = Modifier.padding(horizontal = ElcheSpacing.lg, vertical = ElcheSpacing.sm),
        )
        if (selectedTab == ShopTab.Membership) {
            MembershipContent(modifier = Modifier.weight(1f))
        } else {
            val url = selectedTab.url
            if (url != null) {
                AppWebView(url = url, title = selectedTab.title, modifier = Modifier.weight(1f))
            }
        }
    }
}
