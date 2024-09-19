package eu.anifantakis.project.library.masterdetailmodern.movies.presentation

import androidx.compose.material3.Text
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.NavTree
import kotlinx.serialization.Serializable

sealed interface MoviesNavTree {
    @Serializable data object MoviesList: MoviesNavTree
}

fun NavGraphBuilder.moviesGraph(navController: NavHostController) {
    navigation<NavTree.Movies>(
        startDestination = MoviesNavTree.MoviesList,
    ) {
        composable<MoviesNavTree.MoviesList> {
            Text(
               text = "MOVIES LIST SCREEN"
            )
        }
    }
}