package es.elchecf.app.feature.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.model.Benefit
import es.elchecf.app.domain.model.UserProfile
import es.elchecf.app.feature.profile.legal.LegalScreen
import es.elchecf.app.feature.profile.legal.LegalTermsContent
import es.elchecf.app.feature.profile.legal.PrivacyPolicyContent
import es.elchecf.app.feature.showcase.FranjaShowcaseScreen
import org.koin.compose.viewmodel.koinViewModel

private sealed interface ProfileSubScreen {
    data object Main : ProfileSubScreen

    data object Notifications : ProfileSubScreen

    data object NotificationsDirecto : ProfileSubScreen

    data object PrivacyPolicy : ProfileSubScreen

    data object LegalTerms : ProfileSubScreen

    /** Temporal — ver [FranjaShowcaseScreen]. */
    data object FranjaShowcase : ProfileSubScreen
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
                onFranjaShowcaseClick = { subScreen = ProfileSubScreen.FranjaShowcase },
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
        ProfileSubScreen.FranjaShowcase ->
            FranjaShowcaseScreen(onBack = { subScreen = ProfileSubScreen.Main })
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
