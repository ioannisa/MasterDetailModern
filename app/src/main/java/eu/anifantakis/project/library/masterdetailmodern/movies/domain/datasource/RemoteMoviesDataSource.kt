package eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource

import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataResult

interface RemoteMoviesDataSource {
    suspend fun getMovies(): DataResult<List<Movie>, DataError.Network>
}