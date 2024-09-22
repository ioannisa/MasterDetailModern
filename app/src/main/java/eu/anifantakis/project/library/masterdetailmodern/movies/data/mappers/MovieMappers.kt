package eu.anifantakis.project.library.masterdetailmodern.movies.data.mappers

import eu.anifantakis.project.library.masterdetailmodern.database.entity.MovieEntity
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.toDate
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.toFormattedString
import eu.anifantakis.project.library.masterdetailmodern.movies.data.dto.MovieDto
import java.util.Date

fun MovieEntity.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        releaseDate = this.releaseDate.toDate() ?: Date(),
        voteAverage = voteAverage,
        posterPath = posterPath,
        backdropPath = backdropPath
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        overview = this.overview,
        releaseDate = this.releaseDate.toFormattedString(),
        voteAverage = voteAverage,
        posterPath = posterPath,
        backdropPath = backdropPath
    )
}

fun MovieDto.toMovie(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        releaseDate = this.releaseDate.toDate() ?: Date(),
        voteAverage = voteAverage,
        posterPath = posterPath,
        backdropPath = backdropPath
    )
}