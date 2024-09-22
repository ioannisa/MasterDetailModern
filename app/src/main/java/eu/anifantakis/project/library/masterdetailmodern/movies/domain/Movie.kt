package eu.anifantakis.project.library.masterdetailmodern.movies.domain

import java.util.Date

data class Movie(
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: Date,
    val voteAverage: Double,
    val posterPath: String,
    val backdropPath: String
)