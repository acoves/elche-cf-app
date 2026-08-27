package es.elchecf.app.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import es.elchecf.app.domain.model.ClubTeam
import es.elchecf.app.feature.calendar.CalendarScreen
import es.elchecf.app.feature.clips.ClipsScreen
import es.elchecf.app.feature.home.ForYouRoute
import es.elchecf.app.feature.profile.ProfileRoute
import es.elchecf.app.feature.shop.ShopScreen
import es.elchecf.app.feature.shop.ShopTab

/** Duración del fundido entre pestañas: rápido a propósito, para disimular el cambio de
 * contenido sin que se note como una animación "de verdad" (CLAUDE.md §4.3 pide 200–250ms para
 * movimiento normal, pero aquí es más corto porque es solo para tapar el corte, no una transición
 * protagonista). */
private const val TAB_FADE_MS = 120

@Composable
fun RootNavHost(
    navController: NavHostController,
    requestedShopTab: ShopTab?,
    onShopTabConsumed: () -> Unit,
    requestedCalendarTeam: ClubTeam?,
    onCalendarTeamConsumed: () -> Unit,
    onNavigateToMembership: () -> Unit,
    onNavigateToShop: () -> Unit,
    onNavigateToCalendar: (ClubTeam) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Route.ForYou,
        modifier = modifier,
        enterTransition = { fadeIn(tween(TAB_FADE_MS)) },
        exitTransition = { fadeOut(tween(TAB_FADE_MS)) },
        popEnterTransition = { fadeIn(tween(TAB_FADE_MS)) },
        popExitTransition = { fadeOut(tween(TAB_FADE_MS)) },
    ) {
        composable<Route.ForYou> {
            ForYouRoute(onNavigateToShop = onNavigateToShop, onNavigateToCalendar = onNavigateToCalendar)
        }
        composable<Route.Calendar> {
            CalendarScreen(initialTeam = requestedCalendarTeam, onInitialTeamConsumed = onCalendarTeamConsumed)
        }
        composable<Route.Clips> { ClipsScreen() }
        composable<Route.Shop> {
            ShopScreen(initialTab = requestedShopTab, onInitialTabConsumed = onShopTabConsumed)
        }
        composable<Route.Profile> { ProfileRoute(onNavigateToMembership = onNavigateToMembership) }
    }
}
