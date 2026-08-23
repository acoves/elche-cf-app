package es.elchecf.app.di

import es.elchecf.app.feature.home.ForYouViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModelOf(::ForYouViewModel)
    }
