package eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource

import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataResult
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import kotlinx.coroutines.flow.Flow

typealias MovieId = Int

interface LocalMoviesDataSource {

    fun getMovies(): Flow<List<Movie>>
    suspend fun upsertMovie(movie: Movie): DataResult<MovieId, DataError.Local>
    suspend fun upsertMovies(movies: List<Movie>): DataResult<List<MovieId>, DataError.Local>
    suspend fun deleteMovie(id: Int)
    suspend fun deleteAllMovies()

}