package es.elchecf.app

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import es.elchecf.app.core.webview.prefetchShopWebView
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.domain.repository.AuthRepository
import es.elchecf.app.feature.onboarding.OnboardingScreen
import es.elchecf.app.feature.shop.ShopTab
import es.elchecf.app.feature.shop.TIENDA_URL
import es.elchecf.app.navigation.ElcheBottomBar
import es.elchecf.app.navigation.RootNavHost
import es.elchecf.app.navigation.Route
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun App() {
    ElcheTheme {
        // Mejora post-Fase 7 (Perfil): fotos de beneficios cargadas por red (Coil 3 + motor Ktor,
        // reutilizando el mismo stack de red que football-data.org — ver core/network).
        setSingletonImageLoaderFactory { context ->
            ImageLoader
                .Builder(context)
                .components { add(KtorNetworkFetcherFactory()) }
                .build()
        }

        val navController = rememberNavController()
        var currentRoute by remember { mutableStateOf<Route>(Route.ForYou) }
        var requestedShopTab by remember { mutableStateOf<ShopTab?>(null) }

        // Patrón estándar de bottom nav (sin esto, cada cambio de pestaña creaba una entrada
        // nueva en el back stack → ViewModel nuevo → "Cargando" otra vez y la pantalla
        // anterior/nueva se veían mezcladas un instante). popUpTo+saveState / restoreState
        // reutiliza la instancia de cada pestaña y restaura su estado ya cargado en vez de
        // arrancar de cero.
        fun navigateToTab(route: Route) {
            currentRoute = route
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }

        // Solo Tienda se precarga (ver ShopWebViewPrefetch.kt): probar lo mismo con las 3 pestañas
        // de Tienda a la vez notó impacto de rendimiento en el arranque, así que Entradas/Membership
        // se quedan con su carga normal, al seleccionarlas el usuario.
        LaunchedEffect(Unit) { prefetchShopWebView(TIENDA_URL) }

        val authRepository = koinInject<AuthRepository>()
        val isLoggedIn by authRepository.isLoggedIn.collectAsState()
        val coroutineScope = rememberCoroutineScope()
        // Se puede omitir para esta sesión de la app sin iniciar sesión de verdad — al volver a
        // arrancar la app (proceso nuevo) se vuelve a mostrar, tal como se pidió.
        var onboardingSkipped by remember { mutableStateOf(false) }

        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                bottomBar = {
                    ElcheBottomBar(
                        currentRoute = currentRoute,
                        onSelect = { route -> navigateToTab(route) },
                    )
                },
            ) { innerPadding ->
                RootNavHost(
                    navController = navController,
                    requestedShopTab = requestedShopTab,
                    onShopTabConsumed = { requestedShopTab = null },
                    onNavigateToMembership = {
                        requestedShopTab = ShopTab.Membership
                        navigateToTab(Route.Shop)
                    },
                    onNavigateToShop = { navigateToTab(Route.Shop) },
                    modifier = Modifier.padding(innerPadding),
                )
            }

            if (!isLoggedIn && !onboardingSkipped) {
                OnboardingScreen(
                    onLogin = { coroutineScope.launch { authRepository.login() } },
                    onSkip = { onboardingSkipped = true },
                )
            }
        }
    }
}
