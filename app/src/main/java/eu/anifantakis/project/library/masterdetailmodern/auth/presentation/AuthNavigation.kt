package eu.anifantakis.project.library.masterdetailmodern.auth.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.intro.IntroScreenRoot
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.login.LoginScreenRoot
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.register.RegisterScreenRoot
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.NavTree
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.MoviesNavTree
import kotlinx.serialization.Serializable

sealed interface AuthNavTree {
    @Serializable data object Intro: AuthNavTree
    @Serializable data object Register: AuthNavTree
    @Serializable data object Login: AuthNavTree
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {

    navigation<NavTree.Auth>(
        startDestination = AuthNavTree.Intro,
    ) {
        composable<AuthNavTree.Intro> {
            IntroScreenRoot(
                onSignInClick = { navController.navigate(AuthNavTree.Login) },
                onSignUpClick = { navController.navigate(AuthNavTree.Register) }
            )
        }

        composable<AuthNavTree.Register> {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate(AuthNavTree.Login) {
                        popUpTo(AuthNavTree.Register) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },

                onSuccessfulRegistration = { navController.navigate(AuthNavTree.Login) }
            )
        }

        composable<AuthNavTree.Login> {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate(MoviesNavTree.MoviesList) {
                        popUpTo(AuthNavTree.Intro) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSignupClick = {
                    navController.navigate(AuthNavTree.Login) {
                        popUpTo(AuthNavTree.Login) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    }
}