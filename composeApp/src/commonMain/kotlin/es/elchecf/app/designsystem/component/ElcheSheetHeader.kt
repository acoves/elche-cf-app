package es.elchecf.app.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

/**
 * Cabecera de `ModalBottomSheet`: título + botón de cerrar. Antes duplicada a mano en cada sheet
 * de Perfil (beneficio, avatar, información personal, socio, cookies) — un solo sitio ahora.
 */
@Composable
fun ElcheSheetHeader(
    title: String,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title.uppercase(), style = ElcheTheme.typography.titleM, modifier = Modifier.weight(1f))
        IconButton(onClick = onClose) {
            Icon(imageVector = Icons.Filled.Close, contentDescription = "Cerrar")
        }
    }
}

@Preview
@Composable
private fun ElcheSheetHeaderPreview() {
    ElcheTheme {
        ElcheSheetHeader(
            title = "Elige un avatar",
            onClose = {},
            modifier = Modifier.padding(ElcheSpacing.lg),
        )
    }
}
