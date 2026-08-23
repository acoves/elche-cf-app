package es.elchecf.app.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme

// CrestRed se usa como `error`: es el único hueco semántico que Material3 exige
// y que el rojo heráldico puede ocupar sin romper la regla de CLAUDE.md §4.1
// (rojo/azul del escudo solo para casa/fuera y el propio escudo, nunca como acento decorativo).
internal fun elcheLightColorScheme(): ColorScheme =
    lightColorScheme(
        primary = ElcheColor.Green,
        onPrimary = ElcheColor.White,
        primaryContainer = ElcheColor.GreenSoft,
        onPrimaryContainer = ElcheColor.GreenDeep,
        secondary = ElcheColor.Gold,
        onSecondary = ElcheColor.GoldDeep,
        secondaryContainer = ElcheColor.Gold,
        onSecondaryContainer = ElcheColor.GoldDeep,
        background = ElcheColor.White,
        onBackground = ElcheColor.Ink,
        surface = ElcheColor.White,
        onSurface = ElcheColor.Ink,
        surfaceVariant = ElcheColor.GreenSoft,
        onSurfaceVariant = ElcheColor.InkMuted,
        outline = ElcheColor.Divider,
        outlineVariant = ElcheColor.Divider,
        error = ElcheColor.CrestRed,
        onError = ElcheColor.White,
    )

// FASE 2: paleta oscura definida pero sin pulir (CLAUDE.md §4.1) — se revisa al final del proyecto.
internal fun elcheDarkColorScheme(): ColorScheme =
    darkColorScheme(
        primary = ElcheColor.Green,
        onPrimary = ElcheColor.White,
        primaryContainer = ElcheColor.GreenDeep,
        onPrimaryContainer = ElcheColor.White,
        secondary = ElcheColor.Gold,
        onSecondary = ElcheColor.GoldDeep,
        secondaryContainer = ElcheColor.GreenDeep,
        onSecondaryContainer = ElcheColor.Gold,
        background = ElcheColor.GreenDeep,
        onBackground = ElcheColor.White,
        surface = ElcheColor.GreenDeep,
        onSurface = ElcheColor.White,
        surfaceVariant = ElcheColor.GreenDeep,
        onSurfaceVariant = ElcheColor.White,
        outline = ElcheColor.Divider,
        outlineVariant = ElcheColor.Divider,
        error = ElcheColor.CrestRed,
        onError = ElcheColor.White,
    )
