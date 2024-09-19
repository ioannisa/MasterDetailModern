package eu.anifantakis.project.library.masterdetailmodern.auth.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.intro.IntroScreenRoot
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.login.LoginScreenRoot
import eu.anifantakis.project.library.masterdetailmodern.auth.presentation.register.RegisterScreenRoot
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.NavGraph
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.popAndNavigate
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.MoviesNavType
import kotlinx.serialization.Serializable

sealed interface AuthNavType {
    @Serializable data object Intro: AuthNavType
    @Serializable data object Register: AuthNavType
    @Serializable data object Login: AuthNavType
}

fun NavGraphBuilder.authGraph(navController: NavHostController) {

    navigation<NavGraph.Auth>(
        startDestination = AuthNavType.Intro,
    ) {
        composable<AuthNavType.Intro> {
            IntroScreenRoot(
                onSignInClick = { navController.navigate(AuthNavType.Login) },
                onSignUpClick = { navController.navigate(AuthNavType.Register) }
            )
        }

        composable<AuthNavType.Register> {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.popAndNavigate(
                        popTo = AuthNavType.Register,
                        navigate =  AuthNavType.Login
                    )
                },
                onSuccessfulRegistration = {
                    navController.navigate(AuthNavType.Login)
                }
            )
        }

        composable<AuthNavType.Login> {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.popAndNavigate(
                        popTo = AuthNavType.Intro,
                        navigate =  MoviesNavType.MoviesList
                    )
                },
                onSignupClick = {
                    navController.popAndNavigate(
                        popTo = AuthNavType.Login,
                        navigate =  AuthNavType.Login
                    )
                }
            )
        }
    }
}