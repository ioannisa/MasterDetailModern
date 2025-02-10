package eu.anifantakis.project.library.masterdetailmodern.movies.presentation.screens.viewmodel

import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.UiText
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie

sealed interface MoviesListIntent {
    data object LoadMovies : MoviesListIntent
    data class SelectMovie(val movieId: Int) : MoviesListIntent
}

sealed interface MoviesListEffect {
    data object MoviesListSuccess : MoviesListEffect
    data class Error(val error: UiText) : MoviesListEffect
    data class GotoMovieDetails(val movieId: Int) : MoviesListEffect
}

data class MoviesListState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val selectedMovie: Movie? = null,
)