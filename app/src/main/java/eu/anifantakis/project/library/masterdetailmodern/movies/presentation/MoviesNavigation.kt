package eu.anifantakis.project.library.masterdetailmodern.movies.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import androidx.navigation.toRoute
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.NavGraph
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.movieslist.MoviesListScreenRoot
import kotlinx.serialization.Serializable

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
            MoviesListScreenRoot(
                onNavigateToMovieDetails = { movieId ->
                    navController.navigate(MoviesNavType.MovieDetail(movieId))
                }
            )
        }

        composable<MoviesNavType.MovieDetail> {
            val args = it.toRoute<MoviesNavType.MovieDetail>()
            val movieId = args.movieId

            Text(
                modifier = Modifier.padding(top = 48.dp),
                text = "Detail Screen -> $movieId"
            )
        }
    }
}