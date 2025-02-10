package eu.anifantakis.project.library.masterdetailmodern.movies.presentation


import androidx.lifecycle.viewModelScope
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.UiText
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.businesslogic.BaseMviViewModel
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.MoviesRepository
import kotlinx.coroutines.launch

sealed interface MoviesListAction {
    data object LoadMovies : MoviesListAction
    data class SelectMovie(val movieId: Int) : MoviesListAction
}

sealed interface MoviesListEvent {
    data object MoviesListSuccess : MoviesListEvent
    data class Error(val error: UiText) : MoviesListEvent
    data class GotoMovieDetails(val movieId: Int) : MoviesListEvent
}

data class MoviesListState(
    val isLoading: Boolean = false,
    val movies: List<Movie> = emptyList(),
    val selectedMovie: Movie? = null,
)

class MoviesViewModel(
    private val moviesRepository: MoviesRepository
) : BaseMviViewModel<MoviesListState, MoviesListAction, MoviesListEvent>(
    initialState = MoviesListState()
) {
    init {
        processIntent(MoviesListAction.LoadMovies)
    }

    override fun reduce(
        oldState: MoviesListState,
        intent: MoviesListAction
    ): MoviesListState {
        return when (intent) {
            is MoviesListAction.LoadMovies -> oldState.copy(isLoading = true)

            is MoviesListAction.SelectMovie -> {
                val movie = oldState.movies.firstOrNull { it.id == intent.movieId }
                oldState.copy(selectedMovie = movie)
            }
        }
    }

    override fun handleIntent(intent: MoviesListAction) {
        when (intent) {
            is MoviesListAction.LoadMovies -> loadMovies()
            is MoviesListAction.SelectMovie -> {
                val movie = currentState.movies.firstOrNull { it.id == intent.movieId }
                movie?.let { postEffect(MoviesListEvent.GotoMovieDetails(it.id)) }
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
                postEffect(MoviesListEvent.MoviesListSuccess)
            } catch (e: Exception) {
                setState(currentState.copy(isLoading = false))
                postEffect(MoviesListEvent.Error(
                    UiText.DynamicString(e.message ?: "Unknown error")
                ))
            }
        }
    }
}