package eu.anifantakis.project.library.masterdetailmodern.auth.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.intro.IntroScreenRoot
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.register.RegisterScreenRoot

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        route = "auth",
        startDestination = "intro"
    ) {
        composable("intro") {
            IntroScreenRoot(
                onSignInClick = { navController.navigate("login") },
                onSignUpClick = { navController.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate("login") {
                        popUpTo("register") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },

                onSuccessfulRegistration = { navController.navigate("login") }
            )
        }
    }
}