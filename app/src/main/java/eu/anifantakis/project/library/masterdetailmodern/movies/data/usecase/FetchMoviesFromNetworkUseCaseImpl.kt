package eu.anifantakis.project.library.masterdetailmodern.movies.data.usecase

import eu.anifantakis.project.library.masterdetailmodern.movies.domain.MoviesRepository
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.usecase.FetchMoviesFromNetworkUseCase

class FetchMoviesFromNetworkUseCaseImpl(
    private val moviesRepository: MoviesRepository
): FetchMoviesFromNetworkUseCase {
    override suspend fun invoke() {
        moviesRepository.fetchMovies()
    }
}