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
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheCard
import es.elchecf.app.designsystem.component.SectionHeader
import es.elchecf.app.designsystem.icon.ElcheProfileIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheShape
import es.elchecf.app.designsystem.theme.ElcheSpacing
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileRoute() {
    val viewModel = koinViewModel<ProfileViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    ProfileScreen(uiState = uiState, onLogin = viewModel::login, onLogout = viewModel::logout)
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onLogin: () -> Unit = {},
    onLogout: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(ElcheSpacing.screenMargin),
    ) {
        ProfileHeader(profile = uiState.profile, isLoggedIn = uiState.isLoggedIn, onLogin = onLogin)

        if (uiState.isLoggedIn && uiState.benefits.isNotEmpty()) {
            SectionHeader(title = "Beneficios", modifier = Modifier.padding(top = ElcheSpacing.xl))
            Column(
                modifier = Modifier.padding(top = ElcheSpacing.md),
                verticalArrangement = Arrangement.spacedBy(ElcheSpacing.sm),
            ) {
                uiState.benefits.forEach { benefit -> BenefitRow(benefit) }
            }
        }

        SectionHeader(title = "Configuración", modifier = Modifier.padding(top = ElcheSpacing.xl))
        Column(modifier = Modifier.padding(top = ElcheSpacing.md)) {
            ConfigRow(icon = ElcheProfileIcon.PersonalInfo, label = "Información personal")
            ConfigRow(icon = ElcheProfileIcon.Notifications, label = "Notificaciones")
            ConfigRow(icon = ElcheProfileIcon.Cookies, label = "Cookies")
            ConfigRow(
                icon = if (uiState.isLoggedIn) ElcheProfileIcon.Logout else ElcheProfileIcon.Login,
                label = if (uiState.isLoggedIn) "Cerrar sesión" else "Iniciar sesión",
                onClick = if (uiState.isLoggedIn) onLogout else onLogin,
            )
            ConfigRow(icon = ElcheProfileIcon.Privacy, label = "Política de privacidad")
            ConfigRow(icon = ElcheProfileIcon.Legal, label = "Condiciones legales")
        }
    }
}

@Composable
private fun ProfileHeader(
    profile: UserProfile?,
    isLoggedIn: Boolean,
    onLogin: () -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier.size(64.dp).background(ElcheColor.Green, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = profile?.fullName?.take(1) ?: "?",
                style = ElcheTheme.typography.titleL,
                color = ElcheColor.White,
            )
        }
        Column(modifier = Modifier.padding(start = ElcheSpacing.md)) {
            if (isLoggedIn && profile != null) {
                Text(text = profile.fullName, style = ElcheTheme.typography.titleM)
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
                Text(text = "No has iniciado sesión", style = ElcheTheme.typography.titleM)
                ElcheButton(
                    text = "Iniciar sesión",
                    onClick = onLogin,
                    modifier = Modifier.padding(top = ElcheSpacing.sm),
                )
            }
        }
    }
}

@Composable
private fun BenefitRow(benefit: Benefit) {
    ElcheCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).background(ElcheColor.GreenSoft, ElcheShape.Card))
            Column(modifier = Modifier.padding(start = ElcheSpacing.md).weight(1f)) {
                Text(text = benefit.title.uppercase(), style = ElcheTheme.typography.bodyS)
                Text(
                    text = benefit.subtitle,
                    style = ElcheTheme.typography.bodyS,
                    color = ElcheColor.InkMuted,
                )
            }
            IconButton(onClick = {}) {
                Icon(imageVector = ElcheProfileIcon.More, contentDescription = "Más opciones")
            }
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

@Preview
@Composable
private fun ProfileScreenPreview() {
    ElcheTheme {
        ProfileScreen(
            uiState =
                ProfileUiState(
                    isLoading = false,
                    isLoggedIn = true,
                    profile = UserProfile("1", "Antonio Franjiverde", "", "Socio · 2 años"),
                    benefits =
                        listOf(
                            Benefit("1", "Descuento en tienda oficial", "10% en tu próxima compra online", ""),
                        ),
                ),
        )
    }
}
