package eu.anifantakis.project.library.masterdetailmodern.movies.domain.usecase

import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import kotlinx.coroutines.flow.Flow

interface LoadMoviesUseCase {
    suspend operator fun invoke(): Flow<List<Movie>>
}