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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import es.elchecf.app.designsystem.component.ElcheButton
import es.elchecf.app.designsystem.component.ElcheButtonVariant
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
fun ProfileRoute(onNavigateToMembership: () -> Unit) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    ProfileScreen(
        uiState = uiState,
        onLogin = viewModel::login,
        onLogout = viewModel::logout,
        onNavigateToMembership = onNavigateToMembership,
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onLogin: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateToMembership: () -> Unit = {},
) {
    var selectedBenefit by remember { mutableStateOf<Benefit?>(null) }

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(ElcheSpacing.screenMargin),
    ) {
        ProfileHeader(profile = uiState.profile, isLoggedIn = uiState.isLoggedIn, onLogin = onLogin)

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
                    BenefitRow(benefit = benefit, onMoreClick = { selectedBenefit = benefit })
                }
            }
        }

        SectionHeader(title = "Configuración", modifier = Modifier.padding(top = ElcheSpacing.xl))
        Column(modifier = Modifier.padding(top = ElcheSpacing.md)) {
            ConfigRow(icon = ElcheProfileIcon.PersonalInfo, label = "Información personal")
            ConfigRow(icon = ElcheProfileIcon.Notifications, label = "Notificaciones")
            ConfigRow(icon = ElcheProfileIcon.Cookies, label = "Cookies")
            if (!uiState.isLoggedIn) {
                ConfigRow(icon = ElcheProfileIcon.Login, label = "¿Eres socio? Inicia sesión aquí", onClick = onLogin)
            }
        }

        SectionHeader(title = "Legal", modifier = Modifier.padding(top = ElcheSpacing.xl))
        Column(modifier = Modifier.padding(top = ElcheSpacing.md)) {
            ConfigRow(icon = ElcheProfileIcon.Privacy, label = "Política de privacidad")
            ConfigRow(icon = ElcheProfileIcon.Legal, label = "Condiciones legales")
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

    val benefit = selectedBenefit
    if (benefit != null) {
        BenefitDetailSheet(
            benefit = benefit,
            onDismiss = { selectedBenefit = null },
            onCta = {
                selectedBenefit = null
                onNavigateToMembership()
            },
        )
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
private fun BenefitRow(
    benefit: Benefit,
    onMoreClick: () -> Unit,
) {
    ElcheCard(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = benefit.imageUrl,
                contentDescription = benefit.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(56.dp).clip(ElcheShape.Card).background(ElcheColor.GreenSoft),
            )
            Column(modifier = Modifier.padding(start = ElcheSpacing.md).weight(1f)) {
                Text(
                    text = benefit.title.uppercase(),
                    style = ElcheTheme.typography.bodyS.copy(fontWeight = FontWeight.Bold),
                )
                Text(
                    text = benefit.subtitle,
                    style = ElcheTheme.typography.bodyS,
                    color = ElcheColor.InkMuted,
                )
            }
            IconButton(onClick = onMoreClick) {
                Icon(imageVector = ElcheProfileIcon.More, contentDescription = "Más opciones")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BenefitDetailSheet(
    benefit: Benefit,
    onDismiss: () -> Unit,
    onCta: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        Column(modifier = Modifier.fillMaxWidth().padding(ElcheSpacing.lg)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = benefit.title.uppercase(),
                    style = ElcheTheme.typography.titleM,
                    modifier = Modifier.weight(1f),
                )
                IconButton(onClick = onDismiss) {
                    Icon(imageVector = ElcheProfileIcon.Close, contentDescription = "Cerrar")
                }
            }
            Text(
                text = benefit.detail,
                style = ElcheTheme.typography.body,
                color = ElcheColor.InkMuted,
                modifier = Modifier.padding(top = ElcheSpacing.lg, bottom = ElcheSpacing.xl),
            )
            ElcheButton(
                text = "Consigue el Carnet Franjiverde",
                onClick = onCta,
                variant = ElcheButtonVariant.Accent,
                modifier = Modifier.fillMaxWidth(),
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
                            Benefit(
                                id = "1",
                                title = "Descuento en tienda oficial",
                                subtitle = "10% en tu próxima compra online",
                                detail = "10% de descuento en tu compra en las tiendas oficiales del Elche CF.",
                                imageUrl = "",
                            ),
                        ),
                ),
        )
    }
}
