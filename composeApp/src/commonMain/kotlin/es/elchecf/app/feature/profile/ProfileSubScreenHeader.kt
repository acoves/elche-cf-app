package es.elchecf.app.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import es.elchecf.app.designsystem.icon.ElcheCalendarIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/** Cabecera de las sub-pantallas de Perfil (Notificaciones, Legal…): "‹ VOLVER" a la izquierda,
 * título centrado — formato de la referencia de diseño, distinto de [es.elchecf.app.designsystem.component.ElcheTopBar]. */
@Composable
fun ProfileSubScreenHeader(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth().padding(vertical = ElcheSpacing.md)) {
        Row(
            modifier = Modifier.clickable(onClick = onBack).align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(imageVector = BackIcon, contentDescription = null, tint = ElcheColor.Ink)
            Text(text = "VOLVER", style = ElcheTheme.typography.label)
        }
        Text(
            text = title.uppercase(),
            style = ElcheTheme.typography.titleM,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center).padding(horizontal = ElcheSpacing.xxxl),
        )
    }
}

private val BackIcon: ImageVector = ElcheCalendarIcon.Previous
