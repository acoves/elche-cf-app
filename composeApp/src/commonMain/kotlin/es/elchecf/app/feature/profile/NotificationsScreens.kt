package es.elchecf.app.feature.profile

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import es.elchecf.app.core.platform.openNotificationSettings
import es.elchecf.app.designsystem.icon.ElcheProfileIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme

@Composable
fun NotificationsScreen(
    onBack: () -> Unit,
    onOpenDirecto: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Mejora post-Fase 7: sin backend de notificaciones todavía — el estado del toggle es local
    // a esta pantalla, no persiste al salir. Igual que las 4 de "Directo", ver más abajo.
    var contenidoDestacado by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().padding(horizontal = ElcheSpacing.screenMargin)) {
        ProfileSubScreenHeader(title = "Notificaciones", onBack = onBack)

        Text(
            text = "¡Sé el primero a saberlo todo!",
            style = ElcheTheme.typography.titleL,
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
        )
        Text(
            text =
                "Selecciona las notificaciones que deseas recibir. Puedes modificar esto más " +
                    "tarde en las opciones de configuración.",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.InkMuted,
            modifier = Modifier.padding(top = ElcheSpacing.sm),
        )

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .clickable(onClick = ::openNotificationSettings)
                    .padding(vertical = ElcheSpacing.xl),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text =
                    "Habilita las notificaciones automáticas para la app del Elche CF en los " +
                        "ajustes de tu dispositivo tocando aquí.",
                style = ElcheTheme.typography.body,
                modifier = Modifier.weight(1f),
            )
            Icon(imageVector = ElcheProfileIcon.ArrowForward, contentDescription = null, tint = ElcheColor.Green)
        }

        NotificationRow(
            title = "Directo",
            subtitle = "Notificaciones en tiempo real durante los partidos: alineaciones, goles y tarjetas rojas.",
            onClick = onOpenDirecto,
        )
        ToggleRow(
            title = "Contenido destacado",
            subtitle = "Sé el primero en enterarte de las últimas noticias, vídeos y mucho más.",
            checked = contenidoDestacado,
            onCheckedChange = { contenidoDestacado = it },
        )
    }
}

@Composable
fun NotificationsDirectoScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var tarjetasRojas by remember { mutableStateOf(false) }
    var fasesDelPartido by remember { mutableStateOf(false) }
    var goles by remember { mutableStateOf(false) }
    var alineaciones by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxSize().padding(horizontal = ElcheSpacing.screenMargin)) {
        ProfileSubScreenHeader(title = "Directo", onBack = onBack)
        ToggleRow(title = "Tarjetas rojas", checked = tarjetasRojas, onCheckedChange = { tarjetasRojas = it })
        ToggleRow(
            title = "Fases del partido (inicio, descanso, final)",
            checked = fasesDelPartido,
            onCheckedChange = { fasesDelPartido = it },
        )
        ToggleRow(title = "Goles", checked = goles, onCheckedChange = { goles = it })
        ToggleRow(title = "Alineaciones", checked = alineaciones, onCheckedChange = { alineaciones = it })
    }
}

@Composable
private fun NotificationRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = ElcheSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title.uppercase(), style = ElcheTheme.typography.titleM)
            Text(
                text = subtitle,
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.InkMuted,
                modifier = Modifier.padding(top = ElcheSpacing.xs),
            )
        }
        Icon(imageVector = ElcheProfileIcon.ChevronRight, contentDescription = null, tint = ElcheColor.InkMuted)
    }
}

@Composable
private fun ToggleRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    subtitle: String? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = ElcheSpacing.lg),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = ElcheSpacing.md)) {
            Text(text = title.uppercase(), style = ElcheTheme.typography.titleM)
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = ElcheTheme.typography.bodyS,
                    color = ElcheColor.InkMuted,
                    modifier = Modifier.padding(top = ElcheSpacing.xs),
                )
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(checkedTrackColor = ElcheColor.Green),
        )
    }
}
