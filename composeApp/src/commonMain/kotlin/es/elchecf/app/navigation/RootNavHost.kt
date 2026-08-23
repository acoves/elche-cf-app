package es.elchecf.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import es.elchecf.app.feature.calendar.CalendarScreen
import es.elchecf.app.feature.clips.ClipsScreen
import es.elchecf.app.feature.home.ForYouScreen
import es.elchecf.app.feature.profile.ProfileScreen
import es.elchecf.app.feature.shop.ShopScreen

@Composable
fun RootNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(navController = navController, startDestination = Route.ForYou, modifier = modifier) {
        composable<Route.ForYou> { ForYouScreen() }
        composable<Route.Calendar> { CalendarScreen() }
        composable<Route.Clips> { ClipsScreen() }
        composable<Route.Shop> { ShopScreen() }
        composable<Route.Profile> { ProfileScreen() }
    }
}
