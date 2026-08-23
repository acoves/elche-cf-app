package es.elchecf.app.feature.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

// FASE 3: esqueleto de pantalla. FASE 4: VersusCard + cuenta atrás, predictor, quiz, valorar partido.
@Composable
fun ForYouScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(ElcheSpacing.screenMargin)) {
        SectionHeader(title = "Para ti")
        Text(text = "Match center: llega en la Fase 4.", style = ElcheTheme.typography.bodyS)
    }
}
