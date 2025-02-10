package eu.anifantakis.project.library.masterdetailmodern.movies.domain.usecase

import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesUseCaseManager {
    suspend fun loadMovies(): Flow<List<Movie>>
    suspend fun fetchMovies()

}