package eu.anifantakis.project.library.masterdetailmodern.movies.data.di

import eu.anifantakis.project.library.masterdetailmodern.movies.data.MoviesRepositoryImpl
import eu.anifantakis.project.library.masterdetailmodern.movies.data.datasource.LocalMoviesDataSourceImpl
import eu.anifantakis.project.library.masterdetailmodern.movies.data.datasource.RemoteMoviesDataSourceImpl
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.MoviesRepository
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource.LocalMoviesDataSource
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource.RemoteMoviesDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val moviesDataModule = module {

    singleOf(::MoviesRepositoryImpl).bind<MoviesRepository>()

    singleOf(::LocalMoviesDataSourceImpl).bind<LocalMoviesDataSource>()
    singleOf(::RemoteMoviesDataSourceImpl).bind<RemoteMoviesDataSource>()

}