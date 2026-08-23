package es.elchecf.app.feature.clips

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

// FASE 3: esqueleto de pantalla. CLAUDE.md §5.3 no asigna a Clips una fase numerada en §8;
// pendiente aclarar con el usuario cuándo se construye el feed real (lista de miniaturas + duración).
@Composable
fun ClipsScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(ElcheSpacing.screenMargin)) {
        SectionHeader(title = "Clips")
        Text(text = "Feed de vídeos/noticias: fase pendiente de asignar.", style = ElcheTheme.typography.bodyS)
    }
}
