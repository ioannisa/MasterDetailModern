package eu.anifantakis.project.library.masterdetailmodern.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.authGraph

@Composable
fun NavigationRoot(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "auth"
    ) {
        authGraph(navController)
    }
}