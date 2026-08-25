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
import es.elchecf.app.feature.profile.legal.LegalScreen
import es.elchecf.app.feature.profile.legal.LegalTermsContent
import es.elchecf.app.feature.profile.legal.PrivacyPolicyContent
import org.koin.compose.viewmodel.koinViewModel

private sealed interface ProfileSubScreen {
    data object Main : ProfileSubScreen

    data object Notifications : ProfileSubScreen

    data object NotificationsDirecto : ProfileSubScreen

    data object PrivacyPolicy : ProfileSubScreen

    data object LegalTerms : ProfileSubScreen
}

private sealed interface ProfileSheet {
    data object PersonalInfo : ProfileSheet

    data object MemberLogin : ProfileSheet

    data object AvatarPicker : ProfileSheet

    data object Cookies : ProfileSheet
}

@Composable
fun ProfileRoute(onNavigateToMembership: () -> Unit) {
    val viewModel = koinViewModel<ProfileViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    ProfileScreen(
        uiState = uiState,
        onLogin = viewModel::login,
        onLogout = viewModel::logout,
        onNavigateToMembership = onNavigateToMembership,
        onUpdateProfile = viewModel::updateProfile,
        onUpdateAvatar = viewModel::updateAvatar,
    )
}

@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onLogin: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateToMembership: () -> Unit = {},
    onUpdateProfile: (firstName: String, lastName: String) -> Unit = { _, _ -> },
    onUpdateAvatar: (String) -> Unit = {},
) {
    var subScreen by remember { mutableStateOf<ProfileSubScreen>(ProfileSubScreen.Main) }
    var sheet by remember { mutableStateOf<ProfileSheet?>(null) }
    var selectedBenefit by remember { mutableStateOf<Benefit?>(null) }

    when (subScreen) {
        ProfileSubScreen.Main ->
            ProfileMainScreen(
                uiState = uiState,
                onAvatarClick = { sheet = ProfileSheet.AvatarPicker },
                onPersonalInfoClick = { sheet = ProfileSheet.PersonalInfo },
                onNotificationsClick = { subScreen = ProfileSubScreen.Notifications },
                onCookiesClick = { sheet = ProfileSheet.Cookies },
                onLoginClick = { sheet = ProfileSheet.MemberLogin },
                onLogout = onLogout,
                onPrivacyClick = { subScreen = ProfileSubScreen.PrivacyPolicy },
                onLegalClick = { subScreen = ProfileSubScreen.LegalTerms },
                onBenefitMoreClick = { selectedBenefit = it },
            )
        ProfileSubScreen.Notifications ->
            NotificationsScreen(
                onBack = { subScreen = ProfileSubScreen.Main },
                onOpenDirecto = { subScreen = ProfileSubScreen.NotificationsDirecto },
            )
        ProfileSubScreen.NotificationsDirecto ->
            NotificationsDirectoScreen(onBack = { subScreen = ProfileSubScreen.Notifications })
        ProfileSubScreen.PrivacyPolicy ->
            LegalScreen(page = PrivacyPolicyContent, onBack = { subScreen = ProfileSubScreen.Main })
        ProfileSubScreen.LegalTerms ->
            LegalScreen(page = LegalTermsContent, onBack = { subScreen = ProfileSubScreen.Main })
    }

    when (sheet) {
        ProfileSheet.PersonalInfo ->
            PersonalInfoSheet(
                profile = uiState.profile,
                onDismiss = { sheet = null },
                onSave = { firstName, lastName ->
                    onUpdateProfile(firstName, lastName)
                    sheet = null
                },
            )
        ProfileSheet.MemberLogin ->
            MemberLoginSheet(
                onDismiss = { sheet = null },
                onContinue = {
                    onLogin()
                    sheet = null
                },
            )
        ProfileSheet.AvatarPicker ->
            AvatarPickerSheet(
                currentAvatarUrl = uiState.profile?.avatarUrl.orEmpty(),
                onDismiss = { sheet = null },
                onSave = {
                    onUpdateAvatar(it)
                    sheet = null
                },
            )
        ProfileSheet.Cookies -> CookiesSheet(onDismiss = { sheet = null })
        null -> Unit
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
private fun ProfileMainScreen(
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
                    profile = UserProfile("1", "Antonio", "Franjiverde", "", "Socio · 2 años"),
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
