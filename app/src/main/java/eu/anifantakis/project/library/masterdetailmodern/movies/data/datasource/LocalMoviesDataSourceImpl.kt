package eu.anifantakis.project.library.masterdetailmodern.movies.data.datasource

import android.database.sqlite.SQLiteFullException
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataResult
import eu.anifantakis.project.library.masterdetailmodern.database.dao.MoviesDao
import eu.anifantakis.project.library.masterdetailmodern.movies.data.mappers.toEntity
import eu.anifantakis.project.library.masterdetailmodern.movies.data.mappers.toMovie
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource.LocalMoviesDataSource
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource.MovieId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalMoviesDataSourceImpl(
    private val moviesDao: MoviesDao
): LocalMoviesDataSource {
    override fun getMovies(): Flow<List<Movie>> {
        return moviesDao.getRuns()
            .map { movieEntities ->
                movieEntities.map { it.toMovie() }
            }
    }

    override suspend fun upsertMovie(movie: Movie): DataResult<MovieId, DataError.Local> {
        return try {
            val entity = movie.toEntity()
            moviesDao.upsertMovie(entity)
            DataResult.Success(entity.id)
        } catch (e: SQLiteFullException) {
            DataResult.Failure(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertMovies(movies: List<Movie>): DataResult<List<MovieId>, DataError.Local> {
        return try {
            val entities = movies.map { it.toEntity() }
            moviesDao.upsertMovies(entities)
            DataResult.Success(entities.map { it.id })
        } catch (e: SQLiteFullException) {
            DataResult.Failure(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteMovie(id: Int) {
        moviesDao.deleteMovie(id)
    }

    override suspend fun deleteAllMovies() {
        moviesDao.deleteAllMovies()
    }
}