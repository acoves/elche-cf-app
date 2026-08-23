package es.elchecf.app.feature.shop

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

// FASE 3: esqueleto de pantalla. FASE 6: sub-tabs Tienda · Entradas · Membership (AppWebView).
@Composable
fun ShopScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(ElcheSpacing.screenMargin)) {
        SectionHeader(title = "Tienda")
        Text(text = "Tienda, entradas y membership: llegan en la Fase 6.", style = ElcheTheme.typography.bodyS)
    }
}
