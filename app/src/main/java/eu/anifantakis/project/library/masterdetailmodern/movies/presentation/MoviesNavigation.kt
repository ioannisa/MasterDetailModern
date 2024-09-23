package eu.anifantakis.project.library.masterdetailmodern.movies.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.NavGraph
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.sharedViewModel
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.screens.MovieDetailsScreen
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.screens.MoviesListScreenRoot
import kotlinx.serialization.Serializable
import timber.log.Timber

@Serializable
sealed interface MoviesNavType {
    @Serializable
    data object MoviesList: MoviesNavType

    @Serializable
    data class MovieDetail(val movieId: Int): MoviesNavType
}

fun NavGraphBuilder.moviesGraph(navController: NavHostController) {
    navigation<NavGraph.Movies>(
        startDestination = MoviesNavType.MoviesList,
    ) {
        composable<MoviesNavType.MoviesList> {
            val viewModel = it.sharedViewModel<MoviesViewModel>(navController)
            MoviesListScreenRoot(
                onNavigateToMovieDetails = { movieId ->
                    navController.navigate(MoviesNavType.MovieDetail(movieId))
                },
                viewModel = viewModel
            )
        }

        composable<MoviesNavType.MovieDetail> {
            // if we want to pass type safe arguments this is how to do it
            val args = it.toRoute<MoviesNavType.MovieDetail>()
            val movieId = args.movieId

            // if we want to pass a shared view model, this is how to do it
            val viewModel = it.sharedViewModel<MoviesViewModel>(navController)

            MovieDetailsScreen(
                vieModel = viewModel
            )
        }
    }
}