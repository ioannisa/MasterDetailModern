package eu.anifantakis.project.library.masterdetailmodern.movies.domain

import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.EmptyDataResult
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource.MovieId
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {

    fun getMovies(): Flow<List<Movie>>
    suspend fun fetchMovies(): EmptyDataResult<DataError>
    suspend fun upsertMovie(movie: Movie): EmptyDataResult<DataError>
    suspend fun deleteMovie(id: MovieId)

}