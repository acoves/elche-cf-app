package es.elchecf.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import es.elchecf.app.core.webview.prefetchShopWebView
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.feature.shop.ShopTab
import es.elchecf.app.feature.shop.TIENDA_URL
import es.elchecf.app.navigation.ElcheBottomBar
import es.elchecf.app.navigation.RootNavHost
import es.elchecf.app.navigation.Route

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

        // Solo Tienda se precarga (ver ShopWebViewPrefetch.kt): probar lo mismo con las 3 pestañas
        // de Tienda a la vez notó impacto de rendimiento en el arranque, así que Entradas/Membership
        // se quedan con su carga normal, al seleccionarlas el usuario.
        LaunchedEffect(Unit) { prefetchShopWebView(TIENDA_URL) }

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                ElcheBottomBar(
                    currentRoute = currentRoute,
                    onSelect = { route ->
                        currentRoute = route
                        navController.navigate(route) { launchSingleTop = true }
                    },
                )
            },
        ) { innerPadding ->
            RootNavHost(
                navController = navController,
                requestedShopTab = requestedShopTab,
                onShopTabConsumed = { requestedShopTab = null },
                onNavigateToMembership = {
                    requestedShopTab = ShopTab.Membership
                    currentRoute = Route.Shop
                    navController.navigate(Route.Shop) { launchSingleTop = true }
                },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}
