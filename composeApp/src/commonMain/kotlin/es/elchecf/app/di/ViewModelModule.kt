package es.elchecf.app.di

import es.elchecf.app.feature.calendar.MonthlyCalendarViewModel
import es.elchecf.app.feature.calendar.players.PlayersViewModel
import es.elchecf.app.feature.calendar.standings.StandingsViewModel
import es.elchecf.app.feature.home.ForYouViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModelOf(::ForYouViewModel)
        viewModelOf(::MonthlyCalendarViewModel)
        viewModelOf(::StandingsViewModel)
        viewModelOf(::PlayersViewModel)
    }
