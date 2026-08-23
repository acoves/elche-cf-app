package es.elchecf.app.designsystem.icon

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Iconos de las 5 pestañas (CLAUDE.md §1). De momento Material Icons Extended tal cual;
 * si el club aporta iconografía propia, se sustituye aquí sin tocar la navegación.
 */
object ElcheIcon {
    val ForYou: ImageVector = Icons.Filled.Shield
    val Calendar: ImageVector = Icons.Filled.CalendarMonth
    val Clips: ImageVector = Icons.Filled.PlayCircle
    val Shop: ImageVector = Icons.Filled.ShoppingBag
    val Profile: ImageVector = Icons.Filled.Person
}
