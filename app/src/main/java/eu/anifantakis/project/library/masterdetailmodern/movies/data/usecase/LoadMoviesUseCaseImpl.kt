package eu.anifantakis.project.library.masterdetailmodern.movies.data.usecase

import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.MoviesRepository
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.usecase.LoadMoviesUseCase
import kotlinx.coroutines.flow.Flow

class LoadMoviesUseCaseImpl(
    private val moviesRepository: MoviesRepository
): LoadMoviesUseCase {
    override suspend fun invoke(): Flow<List<Movie>> {
        return moviesRepository.getMovies()
    }
}