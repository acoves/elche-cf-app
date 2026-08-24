package es.elchecf.app.feature.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import es.elchecf.app.core.webview.AppWebView
import es.elchecf.app.designsystem.theme.ElcheTheme

/** URL de Tienda, también usada por [es.elchecf.app.prefetchShopWebView] para precargarla al
 * arrancar la app (ver App.kt) — vive aquí porque es este archivo el dueño de las URLs de Tienda. */
const val TIENDA_URL = "https://tienda.elchecf.es"

private enum class ShopTab(
    val label: String,
    val title: String,
    val url: String?,
) {
    Tienda("Tienda", "Tienda", TIENDA_URL),
    Entradas("Entradas", "Entradas", "https://entradas.elchecf.es"),

    // FASE 6: sin URL a propósito — contenido de Membership pendiente de decidir, no hay nada que
    // cargar todavía (antes apuntaba a la home del club, que no era la sección real de abonados).
    Membership("Membership", "Membership", null),
}

@Composable
fun ShopScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(ShopTab.Tienda) }

    Column(modifier = modifier.fillMaxSize()) {
        TabRow(
            selectedTabIndex = selectedTab.ordinal,
            containerColor = MaterialTheme.colorScheme.background,
        ) {
            ShopTab.entries.forEach { tab ->
                Tab(
                    selected = tab == selectedTab,
                    onClick = { selectedTab = tab },
                    text = { Text(text = tab.label, style = ElcheTheme.typography.label) },
                )
            }
        }
        val url = selectedTab.url
        if (url != null) {
            AppWebView(url = url, title = selectedTab.title, modifier = Modifier.weight(1f))
        }
    }
}
