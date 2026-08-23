package es.elchecf.app.designsystem.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Radios de marca (CLAUDE.md §4.3). Los componentes del design system usan estos directamente. */
object ElcheShape {
    val Card = RoundedCornerShape(20.dp)
    val CardLarge = RoundedCornerShape(24.dp)
    val Button = RoundedCornerShape(28.dp)
    val Pill = RoundedCornerShape(percent = 50)
}

internal fun elcheShapes(): Shapes =
    Shapes(
        extraSmall = RoundedCornerShape(8.dp),
        small = RoundedCornerShape(12.dp),
        medium = ElcheShape.Card,
        large = ElcheShape.CardLarge,
        extraLarge = ElcheShape.Button,
    )
