package es.elchecf.app.feature.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

// FASE 3: esqueleto de pantalla. FASE 5: top-tabs Calendario · Clasificaciones · Jugadores.
@Composable
fun CalendarScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(ElcheSpacing.screenMargin)) {
        SectionHeader(title = "Calendario")
        Text(
            text = "Calendario, clasificaciones y jugadores: llegan en la Fase 5.",
            style = ElcheTheme.typography.bodyS,
        )
    }
}
