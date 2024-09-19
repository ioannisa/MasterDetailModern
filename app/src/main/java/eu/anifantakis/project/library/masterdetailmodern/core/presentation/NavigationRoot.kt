package eu.anifantakis.project.library.masterdetailmodern.core.presentation

import androidx.compose.foundation.layout.PaddingValues
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
    innerPadding: PaddingValues,
    isLoggedIn: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = if (!isLoggedIn) { NavTree.Auth } else { NavTree.Movies }
    ) {
        authGraph(navController)
        moviesGraph(navController)
    }
}