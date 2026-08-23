package es.elchecf.app.feature.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

// FASE 3: esqueleto de pantalla. FASE 7: cabecera, beneficios, configuración, auth mock.
@Composable
fun ProfileScreen(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize().padding(ElcheSpacing.screenMargin)) {
        SectionHeader(title = "Perfil")
        Text(text = "Beneficios y configuración: llegan en la Fase 7.", style = ElcheTheme.typography.bodyS)
    }
}
