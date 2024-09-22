package eu.anifantakis.project.library.masterdetailmodern.movies.presentation.di

import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.movieslist.MoviesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val moviesViewModule = module {
    viewModelOf(::MoviesListViewModel)
}