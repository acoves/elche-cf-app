package es.elchecf.app.navigation

import kotlinx.serialization.Serializable

/** Rutas de las 5 pestañas (CLAUDE.md §1), tipadas para `navigation-compose` vía kotlinx.serialization. */
sealed interface Route {
    @Serializable
    data object ForYou : Route

    @Serializable
    data object Calendar : Route

    @Serializable
    data object Clips : Route

    @Serializable
    data object Shop : Route

    @Serializable
    data object Profile : Route
}
