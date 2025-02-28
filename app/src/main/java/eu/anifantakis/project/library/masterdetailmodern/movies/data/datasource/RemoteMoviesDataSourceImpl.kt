package eu.anifantakis.project.library.masterdetailmodern.movies.data.datasource

import eu.anifantakis.project.library.masterdetailmodern.core.data.networking.MoviesHttpClient
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataResult
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.map
import eu.anifantakis.project.library.masterdetailmodern.movies.data.dto.MoviesDto
import eu.anifantakis.project.library.masterdetailmodern.movies.data.mappers.toMovie
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.datasource.RemoteMoviesDataSource

class RemoteMoviesDataSourceImpl(
    private val httpClient: MoviesHttpClient
) : RemoteMoviesDataSource {
    override suspend fun getMovies(): DataResult<List<Movie>, DataError.Network> {
        return httpClient.get<MoviesDto>(
            route = "/3/movie/top_rated",

            // example parameters
//            queryParameters = mapOf(
//                "language" to "en-US",
//                "page" to 1
//            )
        ).map { moviesDtp ->
            moviesDtp.results.map { movieDto ->
                movieDto.toMovie()
            }
        }
    }
}