package eu.anifantakis.project.library.masterdetailmodern.movies.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import eu.anifantakis.lib.securepersist.PersistManager
import eu.anifantakis.project.library.masterdetailmodern.BuildConfig
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.NavGraph
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
            val context = LocalContext.current

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column {
                    Text(
                        text = "MOVIES LIST SCREEN"
                    )

                    Spacer(modifier = Modifier.height(100.dp))

                    Text(
                        text = "Logout",
                        modifier = Modifier
                            .clickable {
                                val pm = PersistManager(
                                    context,
                                    "${BuildConfig.APPLICATION_ID}.securedPersistence"
                                )
                                pm.encryptSharedPreference("userId", 0)
                            }
                    )
                }
            }
        }
    }
}