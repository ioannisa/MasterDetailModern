package eu.anifantakis.project.library.masterdetailmodern.core.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.authGraph
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.scaffold.ApplicationScaffold
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.moviesGraph
import kotlinx.serialization.Serializable


sealed interface NavGraph {
    @Serializable data object Auth: NavGraph
    @Serializable data object Movies: NavGraph
}

@Composable
fun NavigationRoot(
    innerPadding: PaddingValues,
    isLoggedIn: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    ApplicationScaffold(navController = navController) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = if (!isLoggedIn) NavGraph.Auth else NavGraph.Movies
        ) {

            authGraph(navController)
            moviesGraph(navController, paddingValues)
        }
    }
}

