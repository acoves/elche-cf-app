package es.elchecf.app.di

import es.elchecf.app.data.AuthDataSource
import es.elchecf.app.data.CupDataSource
import es.elchecf.app.data.MatchDataSource
import es.elchecf.app.data.PlayerDataSource
import es.elchecf.app.data.ProfileDataSource
import es.elchecf.app.data.StandingsDataSource
import es.elchecf.app.data.mock.MockAuthDataSource
import es.elchecf.app.data.mock.MockCupDataSource
import es.elchecf.app.data.mock.MockMatchDataSource
import es.elchecf.app.data.mock.MockPlayerDataSource
import es.elchecf.app.data.mock.MockProfileDataSource
import es.elchecf.app.data.mock.MockStandingsDataSource
import es.elchecf.app.data.repository.AuthRepositoryImpl
import es.elchecf.app.data.repository.CupRepositoryImpl
import es.elchecf.app.data.repository.MatchRepositoryImpl
import es.elchecf.app.data.repository.PlayerRepositoryImpl
import es.elchecf.app.data.repository.ProfileRepositoryImpl
import es.elchecf.app.data.repository.StandingsRepositoryImpl
import es.elchecf.app.domain.repository.AuthRepository
import es.elchecf.app.domain.repository.CupRepository
import es.elchecf.app.domain.repository.MatchRepository
import es.elchecf.app.domain.repository.PlayerRepository
import es.elchecf.app.domain.repository.ProfileRepository
import es.elchecf.app.domain.repository.StandingsRepository
import org.koin.dsl.module

// FASE 8: cambiar el binding de cada XxxDataSource a la fuente Ktor real — los repositorios no cambian.
val dataModule =
    module {
        single<MatchDataSource> { MockMatchDataSource() }
        single<MatchRepository> { MatchRepositoryImpl(get()) }

        single<StandingsDataSource> { MockStandingsDataSource() }
        single<StandingsRepository> { StandingsRepositoryImpl(get()) }

        single<PlayerDataSource> { MockPlayerDataSource() }
        single<PlayerRepository> { PlayerRepositoryImpl(get()) }

        single<CupDataSource> { MockCupDataSource() }
        single<CupRepository> { CupRepositoryImpl(get()) }

        single<ProfileDataSource> { MockProfileDataSource() }
        single<ProfileRepository> { ProfileRepositoryImpl(get()) }

        // single: el estado de sesión en memoria debe sobrevivir a que el usuario cambie de pantalla.
        single<AuthDataSource> { MockAuthDataSource() }
        single<AuthRepository> { AuthRepositoryImpl(get()) }
    }
