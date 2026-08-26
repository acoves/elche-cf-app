package es.elchecf.app.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.icon.ElcheProfileIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile

@Composable
internal fun ProfileMainScreen(
    uiState: ProfileUiState,
    onAvatarClick: () -> Unit,
    onPersonalInfoClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onCookiesClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLogout: () -> Unit,
    onPrivacyClick: () -> Unit,
    onLegalClick: () -> Unit,
    onBenefitMoreClick: (Benefit) -> Unit,
    onFranjaShowcaseClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(ElcheSpacing.screenMargin),
    ) {
        ProfileHeader(
            profile = uiState.profile,
            isLoggedIn = uiState.isLoggedIn,
            onLogin = onLoginClick,
            onAvatarClick = onAvatarClick,
        )

        if (uiState.isLoggedIn && uiState.benefits.isNotEmpty()) {
            SectionHeader(
                title = "Beneficios",
                modifier = Modifier.padding(top = ElcheSpacing.xl),
                // Sin pantalla de listado completo todavía (fuera de alcance): de momento solo
                // reproduce el formato de la referencia, no navega a ningún sitio.
                action = { Text(text = "Ver todo", style = ElcheTheme.typography.bodyS, color = ElcheColor.InkMuted) },
            )
            Column(
                modifier = Modifier.padding(top = ElcheSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ElcheSpacing.sm),
            ) {
                uiState.benefits.forEach { benefit ->
                    BenefitRow(benefit = benefit, onMoreClick = { onBenefitMoreClick(benefit) })
                }
            }
        }

        SectionHeader(title = "Configuración", modifier = Modifier.padding(top = ElcheSpacing.xl))
        Column(modifier = Modifier.padding(top = ElcheSpacing.md)) {
            ConfigRow(
                icon = ElcheProfileIcon.PersonalInfo,
                label = "Información personal",
                onClick = onPersonalInfoClick,
            )
            ConfigRow(icon = ElcheProfileIcon.Notifications, label = "Notificaciones", onClick = onNotificationsClick)
            ConfigRow(icon = ElcheProfileIcon.Cookies, label = "Cookies", onClick = onCookiesClick)
            if (!uiState.isLoggedIn) {
                ConfigRow(
                    icon = ElcheProfileIcon.Login,
                    label = "¿Eres socio? Inicia sesión aquí",
                    onClick = onLoginClick,
                )
            }
        }

        SectionHeader(title = "Legal", modifier = Modifier.padding(top = ElcheSpacing.xl))
        Column(modifier = Modifier.padding(top = ElcheSpacing.md)) {
            ConfigRow(icon = ElcheProfileIcon.Privacy, label = "Política de privacidad", onClick = onPrivacyClick)
            ConfigRow(icon = ElcheProfileIcon.Legal, label = "Condiciones legales", onClick = onLegalClick)
        }

        // Fila temporal para decidir si se adopta la franja como firma de marca (ver
        // feature/showcase/FranjaShowcaseScreen.kt) — quitar esta sección junto con ese archivo
        // una vez decidido.
        SectionHeader(title = "Extra", modifier = Modifier.padding(top = ElcheSpacing.xl))
        Column(modifier = Modifier.padding(top = ElcheSpacing.md)) {
            ConfigRow(
                icon = ElcheProfileIcon.Preview,
                label = "Vista previa: detalles",
                onClick = onFranjaShowcaseClick,
            )
        }

        if (uiState.isLoggedIn) {
            Text(
                text = "Cerrar sesión",
                style = ElcheTheme.typography.body,
                color = ElcheColor.CrestRed,
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = ElcheSpacing.xxl)
                        .clickable(onClick = onLogout),
            )
        }
        Text(
            text = "Eliminar la cuenta",
            style = ElcheTheme.typography.bodyS,
            color = ElcheColor.InkMuted,
            textAlign = TextAlign.Center,
            textDecoration = TextDecoration.Underline,
            // FASE 7: sin flujo de borrado de cuenta todavía — solo reproduce el formato de la
            // referencia, no hay nada que este botón deba hacer aún.
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.lg),
        )
        Text(
            text = "VERSIÓN DE LA APP: 1.0.0",
            style = ElcheTheme.typography.label,
            color = ElcheColor.InkMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = ElcheSpacing.xl, bottom = ElcheSpacing.lg),
        )
    }
}

@Composable
private fun ProfileHeader(
    profile: UserProfile?,
    isLoggedIn: Boolean,
    onLogin: () -> Unit,
    onAvatarClick: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier =
                Modifier
                    .size(88.dp)
                    .clip(CircleShape)
                    .background(ElcheColor.Green)
                    .clickable(onClick = onAvatarClick),
            contentAlignment = Alignment.Center,
        ) {
            val avatarUrl = profile?.avatarUrl
            if (!avatarUrl.isNullOrBlank()) {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(88.dp).clip(CircleShape),
                )
            } else {
                Text(
                    text = profile?.fullName?.take(1) ?: "?",
                    style = ElcheTheme.typography.displayM,
                    color = ElcheColor.White,
                )
            }
            Box(
                modifier =
                    Modifier
                        .align(Alignment.BottomEnd)
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(ElcheColor.Gold),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    imageVector = ElcheProfileIcon.Camera,
                    contentDescription = "Cambiar avatar",
                    tint = ElcheColor.GoldDeep,
                    modifier = Modifier.size(16.dp),
                )
            }
        }

        if (isLoggedIn && profile != null) {
            Text(
                text = profile.fullName.uppercase(),
                style = ElcheTheme.typography.titleL,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = ElcheSpacing.md),
            )
            Box(
                modifier =
                    Modifier
                        .padding(top = ElcheSpacing.xs)
                        .background(ElcheColor.GreenSoft, ElcheShape.Pill)
                        .padding(horizontal = ElcheSpacing.sm, vertical = ElcheSpacing.xs),
            ) {
                Text(
                    text = profile.memberStatusLabel,
                    style = ElcheTheme.typography.label,
                    color = ElcheColor.Green,
                )
            }
        } else {
            Text(
                text = "NO HAS INICIADO SESIÓN",
                style = ElcheTheme.typography.titleL,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = ElcheSpacing.md),
            )
            ElcheButton(
                text = "Iniciar sesión",
                onClick = onLogin,
                modifier = Modifier.padding(top = ElcheSpacing.sm),
            )
        }
    }
}

@Composable
private fun ConfigRow(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit = {},
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(vertical = ElcheSpacing.md),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = ElcheColor.Green)
        Text(
            text = label,
            style = ElcheTheme.typography.body,
            modifier = Modifier.weight(1f).padding(start = ElcheSpacing.md),
        )
        Icon(imageVector = ElcheProfileIcon.ChevronRight, contentDescription = null, tint = ElcheColor.InkMuted)
    }
}
