package eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import org.koin.androidx.compose.koinViewModel

inline fun <reified T : Any> NavHostController.popAndNavigate(popTo: T, navigate: T) {
    this.navigate(navigate) {
        popUpTo(popTo) {
            inclusive = true
            saveState = true
        }
        restoreState = true
    }
}

/**
 * Allow for shared ViewModel within nested navigation using Koin
 */
@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavController
): T {
    val navGraphRoute = destination.parent?.route
    return if (navGraphRoute != null) {
        val parentEntry = remember(this) {
            navController.getBackStackEntry(navGraphRoute)
        }
        koinViewModel(viewModelStoreOwner = parentEntry)
    } else {
        koinViewModel()
    }
}