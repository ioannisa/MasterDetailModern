package eu.anifantakis.project.library.masterdetailmodern.core.presentation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
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
    ApplicationScaffold(navController = navController) { scaffoldPadding ->
        NavHost(
            navController = navController,
            startDestination = if (!isLoggedIn) NavGraph.Auth else NavGraph.Movies,
            modifier = Modifier.padding(
                PaddingValues(
                    top = 0.dp, // maxOf(innerPadding.calculateTopPadding(), scaffoldPadding.calculateTopPadding()),
                    bottom = maxOf(innerPadding.calculateBottomPadding(), scaffoldPadding.calculateBottomPadding()),
                    start = maxOf(innerPadding.calculateStartPadding(LayoutDirection.Ltr), scaffoldPadding.calculateStartPadding(LayoutDirection.Ltr)),
                    end = maxOf(innerPadding.calculateEndPadding(LayoutDirection.Ltr), scaffoldPadding.calculateEndPadding(LayoutDirection.Ltr))
                )
            )
        ) {
            authGraph(navController)
            moviesGraph(navController, scaffoldPadding)
        }
    }
}

