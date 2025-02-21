package eu.anifantakis.project.library.masterdetailmodern.movies.presentation.screens.viewmodel


import androidx.lifecycle.viewModelScope
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.UiText
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.businesslogic.BaseMviViewModel
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.MoviesRepository
import kotlinx.coroutines.launch

class MoviesViewModelRedux(
    private val moviesRepository: MoviesRepository
) : BaseMviViewModel<MoviesListState, MoviesListIntent, MoviesListEffect>(
    initialState = MoviesListState()
) {
    init {
        viewModelScope.launch {
            moviesRepository.fetchMovies()
        }
        processIntent(MoviesListIntent.LoadMovies)
    }

    override fun reduce(
        oldState: MoviesListState,
        intent: MoviesListIntent
    ): MoviesListState {
        return when (intent) {
            is MoviesListIntent.LoadMovies -> oldState.copy(isLoading = true)

            is MoviesListIntent.SelectMovie -> oldState.copy(
                selectedMovie = oldState.movies.firstOrNull { it.id == intent.movieId }
            )
        }
    }

    override fun handleIntent(intent: MoviesListIntent, newState: MoviesListState) {
        when (intent) {
            is MoviesListIntent.LoadMovies -> loadMovies()
            is MoviesListIntent.SelectMovie -> {
                newState.selectedMovie?.let { postEffect(MoviesListEffect.GotoMovieDetails(it.id)) }
            }
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            try {
                moviesRepository.getMovies().collect { movies ->
                    setState(currentState.copy(movies = movies)) // ✅ Allowed, as it's a response to a side effect
                }

                // Fetch fresh movies
                moviesRepository.fetchMovies()

                setState(currentState.copy(isLoading = false)) // ✅ Allowed here, avoids extra intent
                postEffect(MoviesListEffect.MoviesListSuccess)

            } catch (e: Exception) {
                setState(currentState.copy(isLoading = false)) // ✅ Manually reset loading state
                postEffect(
                    MoviesListEffect.Error(
                        UiText.DynamicString(e.message ?: "Unknown error")
                    )
                )
            }
        }
    }

}


