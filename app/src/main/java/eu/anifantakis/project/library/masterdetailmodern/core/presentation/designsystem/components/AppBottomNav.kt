package eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import eu.anifantakis.project.library.masterdetailmodern.R
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.NavGraph

data class BottomNavigationItem(
    val label : String = "",
    val imageResource : Int = R.drawable.ic_launcher_foreground,
    val route : NavGraph = NavGraph.Auth
) {

    //function to get the list of bottomNavigationItems
    fun bottomNavigationItems() : List<BottomNavigationItem> {
        return listOf(
            BottomNavigationItem(
                label = "Home",
                imageResource = R.drawable.start,
                route = NavGraph.Auth
            ),
            BottomNavigationItem(
                label = "Search",
                imageResource = R.drawable.stop,
                route = NavGraph.Movies
            ),
        )
    }
}

@Composable
fun AppBottomNav(navController: NavHostController) {

    var navigationSelectedItem by remember { mutableIntStateOf(0) }

    NavigationBar(tonalElevation = 4.dp) {
        BottomNavigationItem().bottomNavigationItems().forEachIndexed { index, navigationItem ->

            //iterating all items with their respective indexes
            NavigationBarItem(
                selected = index == navigationSelectedItem,
                label = {
                    Text(navigationItem.label)
                },
                icon = {
                    Icon(painter = painterResource(id = navigationItem.imageResource), contentDescription = navigationItem.label)
                },
                onClick = {

//                    navigationSelectedItem = index
//                    navController.popAndNavigate(
//                        popTo = navController.graph.findStartDestination().id,
//                        navigate = navigationItem.route
//                    )

                    navigationSelectedItem = index
                    navController.navigate(navigationItem.route) {

                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}