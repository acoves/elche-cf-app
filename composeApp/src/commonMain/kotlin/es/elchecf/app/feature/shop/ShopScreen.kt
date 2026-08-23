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

private enum class ShopTab(
    val label: String,
    val title: String,
    val url: String,
) {
    Tienda("Tienda", "Tienda", "https://tienda.elchecf.es"),
    Entradas("Entradas", "Entradas", "https://entradas.elchecf.es"),

    // FASE 6: URL exacta de la sección de abonados pendiente de confirmar con el club.
    Membership("Membership", "Membership", "https://www.elchecf.es"),
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
        AppWebView(
            url = selectedTab.url,
            title = selectedTab.title,
            modifier = Modifier.weight(1f),
        )
    }
}
