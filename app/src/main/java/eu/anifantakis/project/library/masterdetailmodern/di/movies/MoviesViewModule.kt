package eu.anifantakis.project.library.masterdetailmodern.di.movies

import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.screens.viewmodel.MoviesViewModelRedux
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.screens.viewmodel.MoviesViewModelSimpleMVI
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val moviesViewModule = module {
    viewModelOf(::MoviesViewModelRedux)
    viewModelOf(::MoviesViewModelSimpleMVI)
}