package es.elchecf.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import es.elchecf.app.feature.calendar.CalendarScreen
import es.elchecf.app.feature.clips.ClipsScreen
import es.elchecf.app.feature.home.ForYouRoute
import es.elchecf.app.feature.profile.ProfileRoute
import es.elchecf.app.feature.shop.ShopScreen
import es.elchecf.app.feature.shop.ShopTab

@Composable
fun RootNavHost(
    navController: NavHostController,
    requestedShopTab: ShopTab?,
    onShopTabConsumed: () -> Unit,
    onNavigateToMembership: () -> Unit,
    modifier: Modifier = Modifier,
) {
    NavHost(navController = navController, startDestination = Route.ForYou, modifier = modifier) {
        composable<Route.ForYou> { ForYouRoute() }
        composable<Route.Calendar> { CalendarScreen() }
        composable<Route.Clips> { ClipsScreen() }
        composable<Route.Shop> {
            ShopScreen(initialTab = requestedShopTab, onInitialTabConsumed = onShopTabConsumed)
        }
        composable<Route.Profile> { ProfileRoute(onNavigateToMembership = onNavigateToMembership) }
    }
}
