package eu.anifantakis.project.library.masterdetailmodern.di.movies

import eu.anifantakis.project.library.masterdetailmodern.movies.data.MoviesRepositoryImpl
import eu.anifantakis.project.library.masterdetailmodern.movies.data.datasource.LocalMoviesDataSourceImpl
import eu.anifantakis.project.library.masterdetailmodern.movies.data.datasource.RemoteMoviesDataSourceImpl
import eu.anifantakis.project.library.masterdetailmodern.movies.data.usecase.FetchMoviesFromNetworkUseCaseImpl
import eu.anifantakis.project.library.masterdetailmodern.movies.data.usecase.LoadMoviesUseCaseImpl
import eu.anifantakis.project.library.masterdetailmodern.movies.data.usecase.MoviesUseCaseManagerImpl
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.MoviesRepository
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource.LocalMoviesDataSource
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource.RemoteMoviesDataSource
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.usecase.FetchMoviesFromNetworkUseCase
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.usecase.LoadMoviesUseCase
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.usecase.MoviesUseCaseManager
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val moviesDataModule = module {

    singleOf(::MoviesRepositoryImpl).bind<MoviesRepository>()

    singleOf(::LocalMoviesDataSourceImpl).bind<LocalMoviesDataSource>()
    singleOf(::RemoteMoviesDataSourceImpl).bind<RemoteMoviesDataSource>()

    // UseCases and UseCase Managers
    factoryOf(::LoadMoviesUseCaseImpl).bind<LoadMoviesUseCase>()
    factoryOf(::FetchMoviesFromNetworkUseCaseImpl).bind<FetchMoviesFromNetworkUseCase>()
    factoryOf(::MoviesUseCaseManagerImpl).bind<MoviesUseCaseManager>()
}