package eu.anifantakis.project.library.masterdetailmodern.movies.data

import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataResult
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.EmptyDataResult
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.asEmptyDataResult
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.MoviesRepository
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource.LocalMoviesDataSource
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource.MovieId
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource.RemoteMoviesDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class MoviesRepositoryImpl(
    private val localDataSource: LocalMoviesDataSource,
    private val remoteDataSource: RemoteMoviesDataSource,
    private val applicationScope: CoroutineScope
): MoviesRepository {
    override fun getMovies(): Flow<List<Movie>> {
        return localDataSource.getMovies()
    }

    override suspend fun fetchMovies(): EmptyDataResult<DataError> {
        return when (val result = remoteDataSource.getMovies()) {

            is DataResult.Failure -> {
                result.asEmptyDataResult()
            }
            is DataResult.Success -> {
                applicationScope.async {
                    localDataSource.upsertMovies(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertMovie(movie: Movie): EmptyDataResult<DataError> {
        val localResult = localDataSource.upsertMovie(movie)

        if (localResult !is DataResult.Success) {
            return localResult.asEmptyDataResult()
        }

        return localResult.asEmptyDataResult()
    }

    override suspend fun deleteMovie(id: MovieId) {
        localDataSource.deleteMovie(id)
    }
}