package eu.anifantakis.project.library.masterdetailmodern.movies.data.usecase

import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.usecase.FetchMoviesFromNetworkUseCase
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.usecase.LoadMoviesUseCase
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.usecase.MoviesUseCaseManager
import kotlinx.coroutines.flow.Flow

class MoviesUseCaseManagerImpl(
    private val loadMoviesUseCase: LoadMoviesUseCase,
    private val fetchMoviesFromNetworkUseCase: FetchMoviesFromNetworkUseCase
): MoviesUseCaseManager {
    override suspend fun loadMovies(): Flow<List<Movie>> {
        return loadMoviesUseCase()
    }

    override suspend fun fetchMovies() {
        fetchMoviesFromNetworkUseCase()
    }
}