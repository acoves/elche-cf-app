package es.elchecf.app.feature.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheButtonVariant
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.feature.profile.MemberLoginSheet

private val SHEET_TOP_CORNER = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)

/**
 * Pantalla de bienvenida al arrancar la app sin sesión (ver App.kt): vídeo de fondo en bucle,
 * texto de bienvenida y CTA "Continuar" que revela las formas de acceder, con "Omitir" arriba a
 * la derecha para entrar a la app tal cual, sin iniciar sesión — mismo comportamiento que ya
 * tenía la app antes de que existiera esta pantalla.
 */
@Composable
fun OnboardingScreen(
    onLogin: () -> Unit,
    onSkip: () -> Unit,
) {
    var showAuthOptions by remember { mutableStateOf(false) }
    var showMemberLogin by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        LoopingBackgroundVideo(modifier = Modifier.fillMaxSize())

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0f to Color.Transparent,
                            0.5f to Color.Transparent,
                            1f to Color.Black.copy(alpha = 0.78f),
                        ),
                    ),
        )

        Box(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
            if (showAuthOptions) {
                Text(
                    text = "OMITIR",
                    style = ElcheTheme.typography.label,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(ElcheSpacing.lg)
                            .clickable(onClick = onSkip),
                )

                Column(
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .clip(SHEET_TOP_CORNER)
                            .background(Color.White)
                            .padding(ElcheSpacing.lg),
                ) {
                    Text(
                        text = "Regístrate gratis o inicia sesión",
                        style = ElcheTheme.typography.titleM,
                        color = ElcheColor.Ink,
                        modifier = Modifier.padding(bottom = ElcheSpacing.lg),
                    )
                    AuthOptionRow(icon = Icons.Filled.Email, label = "Email", onClick = {})
                    AuthOptionRow(
                        icon = Icons.Filled.Shield,
                        label = "Inicia sesión como socio",
                        onClick = { showMemberLogin = true },
                    )
                }
            } else {
                Column(modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().padding(ElcheSpacing.lg)) {
                    Text(text = "FRANJIVERDE,", style = ElcheTheme.typography.displayL, color = Color.White)
                    Text(
                        text = "TÚ ERES EL SIGUIENTE FICHAJE",
                        style = ElcheTheme.typography.displayM,
                        color = Color.White,
                        modifier = Modifier.padding(bottom = ElcheSpacing.xl),
                    )
                    ElcheButton(
                        text = "Continuar",
                        onClick = { showAuthOptions = true },
                        variant = ElcheButtonVariant.Accent,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }

    if (showMemberLogin) {
        MemberLoginSheet(
            onDismiss = { showMemberLogin = false },
            onContinue = {
                onLogin()
                showMemberLogin = false
            },
        )
    }
}

@Composable
private fun AuthOptionRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = ElcheSpacing.xs)
                .clip(RoundedCornerShape(ElcheSpacing.md))
                .background(ElcheColor.Divider.copy(alpha = 0.5f))
                .clickable(onClick = onClick)
                .padding(ElcheSpacing.md),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = ElcheColor.Ink)
        Text(
            text = label,
            style = ElcheTheme.typography.body,
            color = ElcheColor.Ink,
            modifier = Modifier.padding(start = ElcheSpacing.sm),
        )
    }
}
