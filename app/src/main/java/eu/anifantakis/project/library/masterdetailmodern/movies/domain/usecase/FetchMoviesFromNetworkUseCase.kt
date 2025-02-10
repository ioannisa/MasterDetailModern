package eu.anifantakis.project.library.masterdetailmodern.movies.domain.usecase

interface FetchMoviesFromNetworkUseCase {
    suspend operator fun invoke()
}