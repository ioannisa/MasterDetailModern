package eu.anifantakis.project.library.masterdetailmodern.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.authGraph
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.moviesGraph
import kotlinx.serialization.Serializable

sealed interface NavTree {
    @Serializable data object Auth: NavTree
    @Serializable data object Movies: NavTree
}

@Composable
fun NavigationRoot(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = NavTree.Auth
    ) {
        authGraph(navController)
        moviesGraph(navController)
    }
}