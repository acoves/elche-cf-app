package es.elchecf.app.di

import es.elchecf.app.data.MatchDataSource
import es.elchecf.app.data.mock.MockMatchDataSource
import es.elchecf.app.data.repository.MatchRepositoryImpl
import es.elchecf.app.domain.repository.MatchRepository
import org.koin.dsl.module

// FASE 8: cambiar el binding de MatchDataSource a la fuente Ktor real — MatchRepository no cambia.
val dataModule =
    module {
        single<MatchDataSource> { MockMatchDataSource() }
        single<MatchRepository> { MatchRepositoryImpl(get()) }
    }
