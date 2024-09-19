package eu.anifantakis.project.library.masterdetailmodern.core.presentation

import android.net.Uri
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.authGraph
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.moviesGraph
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

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
    NavHost(
        navController = navController,
        startDestination = if (!isLoggedIn) NavGraph.Auth else NavGraph.Movies
    ) {
        authGraph(navController)
        moviesGraph(navController)
    }
}

inline fun <reified T : Any> NavHostController.popAndNavigate(popTo: T, navigate: T) {
    this.navigate(navigate) {
        popUpTo(popTo) {
            inclusive = true
            saveState = true
        }
        restoreState = true
    }
}
