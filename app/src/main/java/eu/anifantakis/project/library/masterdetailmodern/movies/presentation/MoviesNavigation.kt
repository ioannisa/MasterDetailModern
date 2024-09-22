package eu.anifantakis.project.library.masterdetailmodern.movies.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.NavGraph
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.movieslist.MoviesListScreenRoot
import kotlinx.serialization.Serializable

@Serializable
sealed interface MoviesNavType {
    @Serializable data object MoviesList: MoviesNavType
}

fun NavGraphBuilder.moviesGraph(navController: NavHostController) {
    navigation<NavGraph.Movies>(
        startDestination = MoviesNavType.MoviesList,
    ) {
        composable<MoviesNavType.MoviesList> {
            MoviesListScreenRoot()
        }
    }
}