package eu.anifantakis.project.library.masterdetailmodern.movies.presentation


import androidx.lifecycle.viewModelScope
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.UiText
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.businesslogic.BaseMviViewModel
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.MoviesRepository
import kotlinx.coroutines.launch

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

class MoviesViewModel(
    private val moviesRepository: MoviesRepository
) : BaseMviViewModel<MoviesListState, MoviesListIntent, MoviesListEffect>(
    initialState = MoviesListState()
) {
    init {
        processIntent(MoviesListIntent.LoadMovies)
    }

    override fun reduce(
        oldState: MoviesListState,
        intent: MoviesListIntent
    ): MoviesListState {
        return when (intent) {
            is MoviesListIntent.LoadMovies -> oldState.copy(isLoading = true)

            is MoviesListIntent.SelectMovie -> {
                val movie = oldState.movies.firstOrNull { it.id == intent.movieId }
                oldState.copy(selectedMovie = movie)
            }
        }
    }

    override fun handleIntent(intent: MoviesListIntent) {
        when (intent) {
            is MoviesListIntent.LoadMovies -> loadMovies()
            is MoviesListIntent.SelectMovie -> {
                val movie = currentState.movies.firstOrNull { it.id == intent.movieId }
                movie?.let { postEffect(MoviesListEffect.GotoMovieDetails(it.id)) }
            }
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            try {
                launch {
                    moviesRepository.getMovies().collect { movies ->
                        setState(currentState.copy(movies = movies))
                    }
                }

                // Fetch fresh movies
                moviesRepository.fetchMovies()

                setState(currentState.copy(isLoading = false))
                postEffect(MoviesListEffect.MoviesListSuccess)
            } catch (e: Exception) {
                setState(currentState.copy(isLoading = false))
                postEffect(MoviesListEffect.Error(
                    UiText.DynamicString(e.message ?: "Unknown error")
                ))
            }
        }
    }
}