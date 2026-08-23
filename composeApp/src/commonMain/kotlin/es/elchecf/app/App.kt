package es.elchecf.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import es.elchecf.app.designsystem.theme.ElcheTheme
import es.elchecf.app.navigation.ElcheBottomBar
import es.elchecf.app.navigation.RootNavHost
import es.elchecf.app.navigation.Route

@Composable
fun App() {
    ElcheTheme {
        val navController = rememberNavController()
        var currentRoute by remember { mutableStateOf<Route>(Route.ForYou) }

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
            RootNavHost(navController, modifier = Modifier.padding(innerPadding))
        }
    }
}
