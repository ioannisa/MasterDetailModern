package eu.anifantakis.project.library.masterdetailmodern.movies.presentation.screens.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.businesslogic.BaseViewModel
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.MoviesRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class MoviesViewModelSimpleMVI(
    private val moviesRepository: MoviesRepository
) : BaseViewModel() {

    var state by mutableStateOf(MoviesListState())
        private set

    private val eventChannel = Channel<MoviesListEffect>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadMovies()

        snapshotFlow { state.selectedMovie }
            .map { movie ->
                eventChannel.send(MoviesListEffect.GotoMovieDetails(movie?.id ?: -1))
            }
            .launchIn(viewModelScope)
    }

    fun onAction(action: MoviesListIntent) {
        when (action) {
            is MoviesListIntent.LoadMovies -> loadMovies()
            is MoviesListIntent.SelectMovie -> selectMovie(action.movieId)
        }
    }

    private fun loadMovies() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)
            moviesRepository.getMovies()
                .collect{
                    state = state.copy(movies = it)
                }
            state = state.copy(isLoading = false)
        }

        viewModelScope.launch {
            moviesRepository.fetchMovies()
        }
    }

    private fun selectMovie(movieId: Int) {
        val movie = state.movies.firstOrNull { it.id == movieId }
        state = state.copy(selectedMovie = movie)
    }
}