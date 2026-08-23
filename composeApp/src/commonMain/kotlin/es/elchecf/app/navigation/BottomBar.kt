package es.elchecf.app.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import es.elchecf.app.designsystem.icon.ElcheIcon
import es.elchecf.app.designsystem.theme.ElcheColor
import es.elchecf.app.designsystem.theme.ElcheTheme

private data class BottomBarItem(
    val route: Route,
    val icon: ImageVector,
    val label: String,
)

private val bottomBarItems =
    listOf(
        BottomBarItem(Route.ForYou, ElcheIcon.ForYou, "Para ti"),
        BottomBarItem(Route.Calendar, ElcheIcon.Calendar, "Calendario"),
        BottomBarItem(Route.Clips, ElcheIcon.Clips, "Clips"),
        BottomBarItem(Route.Shop, ElcheIcon.Shop, "Tienda"),
        BottomBarItem(Route.Profile, ElcheIcon.Profile, "Perfil"),
    )

/**
 * Bottom bar de 5 tabs (CLAUDE.md §1). El indicador de tab activo usa el verde de marca.
 * [currentRoute] se lleva aparte del `NavController` (no vía introspección de destino):
 * más simple y suficiente mientras las 5 tabs sean planas, sin backstacks anidados.
 */
@Composable
fun ElcheBottomBar(
    currentRoute: Route,
    onSelect: (Route) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier, containerColor = MaterialTheme.colorScheme.surface) {
        bottomBarItems.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onSelect(item.route) },
                icon = { Icon(imageVector = item.icon, contentDescription = item.label) },
                label = { Text(text = item.label, style = ElcheTheme.typography.label) },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = ElcheColor.Green,
                        selectedTextColor = ElcheColor.Green,
                        indicatorColor = ElcheColor.GreenSoft,
                        unselectedIconColor = ElcheColor.InkMuted,
                        unselectedTextColor = ElcheColor.InkMuted,
                    ),
            )
        }
    }
}
