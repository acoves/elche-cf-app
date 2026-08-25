package es.elchecf.app.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.icon.ElcheProfileIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.UserProfile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalInfoSheet(
    profile: UserProfile?,
    onDismiss: () -> Unit,
    onSave: (firstName: String, lastName: String) -> Unit,
) {
    var firstName by remember { mutableStateOf(profile?.firstName.orEmpty()) }
    var lastName by remember { mutableStateOf(profile?.lastName.orEmpty()) }
    val hasChanges =
        firstName.isNotBlank() &&
            lastName.isNotBlank() &&
            (firstName != profile?.firstName || lastName != profile.lastName)

    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = ElcheSpacing.lg, vertical = ElcheSpacing.md)) {
            SheetHeader(title = "Información personal", onClose = onDismiss)
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nombre") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElcheColor.Green),
            )
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Apellidos") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.md),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElcheColor.Green),
            )
            ElcheButton(
                text = "Guardar cambios",
                onClick = { onSave(firstName, lastName) },
                enabled = hasChanges,
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.xl, bottom = ElcheSpacing.lg),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberLoginSheet(
    onDismiss: () -> Unit,
    onContinue: () -> Unit,
) {
    var clave by remember { mutableStateOf("") }
    var pin by remember { mutableStateOf("") }
    var pinVisible by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = ElcheSpacing.lg, vertical = ElcheSpacing.md)) {
            SheetHeader(title = "¿Eres socio?", onClose = onDismiss)
            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                label = { Text("Clave") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElcheColor.Green),
            )
            OutlinedTextField(
                value = pin,
                onValueChange = { pin = it },
                label = { Text("PIN") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                visualTransformation = if (pinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { pinVisible = !pinVisible }) {
                        Icon(
                            imageVector =
                                if (pinVisible) ElcheProfileIcon.VisibilityOff else ElcheProfileIcon.Visibility,
                            contentDescription = if (pinVisible) "Ocultar PIN" else "Mostrar PIN",
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.md),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElcheColor.Green),
            )
            // Sin backend de socios todavía (CLAUDE.md §5.5): no hay flujo real de generación de
            // código, así que no se pone como si fuera un botón — solo texto informativo.
            Text(
                text = "¿Olvidaste tu código? Se genera desde la app oficial de socios.",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.InkMuted,
                modifier = Modifier.padding(top = ElcheSpacing.sm),
            )
            ElcheButton(
                text = "Continuar",
                onClick = onContinue,
                enabled = clave.isNotBlank() && pin.isNotBlank(),
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.xl, bottom = ElcheSpacing.lg),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CookiesSheet(onDismiss: () -> Unit) {
    var performance by remember { mutableStateOf(true) }
    var advertising by remember { mutableStateOf(true) }

    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.fillMaxWidth().padding(horizontal = ElcheSpacing.lg, vertical = ElcheSpacing.md)) {
            SheetHeader(title = "Configuración de cookies", onClose = onDismiss)
            Text(
                text =
                    "Usamos cookies propias y de terceros para que la app funcione y para medir " +
                        "su uso. Puedes activar o desactivar cada categoría, salvo las necesarias.",
                style = ElcheTheme.typography.bodyS,
                color = ElcheColor.InkMuted,
                modifier = Modifier.padding(top = ElcheSpacing.md, bottom = ElcheSpacing.lg),
            )
            CookieToggleRow(
                title = "Necesarias",
                subtitle = "Imprescindibles para iniciar sesión y navegar.",
                checked = true,
                onCheckedChange = {},
                enabled = false,
            )
            CookieToggleRow(
                title = "Rendimiento",
                subtitle = "Nos ayudan a entender cómo se usa la app.",
                checked = performance,
                onCheckedChange = { performance = it },
            )
            CookieToggleRow(
                title = "Publicidad",
                subtitle = "Para mostrar promociones más relevantes.",
                checked = advertising,
                onCheckedChange = { advertising = it },
            )
            ElcheButton(
                text = "Guardar preferencias",
                onClick = onDismiss,
                modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg, bottom = ElcheSpacing.lg),
            )
        }
    }
}

@Composable
private fun CookieToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = ElcheSpacing.sm),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f).padding(end = ElcheSpacing.md)) {
            Text(text = title, style = ElcheTheme.typography.body)
            Text(text = subtitle, style = ElcheTheme.typography.label, color = ElcheColor.InkMuted)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled,
            colors = SwitchDefaults.colors(checkedTrackColor = ElcheColor.Green),
        )
    }
}

@Composable
private fun SheetHeader(
    title: String,
    onClose: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = title.uppercase(), style = ElcheTheme.typography.titleM, modifier = Modifier.weight(1f))
        IconButton(onClick = onClose) {
            Icon(imageVector = ElcheProfileIcon.Close, contentDescription = "Cerrar")
        }
    }
}
