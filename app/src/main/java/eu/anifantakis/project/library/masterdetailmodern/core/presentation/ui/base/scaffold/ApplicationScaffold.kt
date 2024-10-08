package eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.scaffold

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppBottomNav
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.AppTopAppBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ApplicationScaffold(
    scaffoldViewModel: ScaffoldViewModel = koinViewModel(),
    navController: NavHostController,
    content: @Composable (PaddingValues) -> Unit
) {
    val title by scaffoldViewModel.title.collectAsState()
    val onBackPress by scaffoldViewModel.onBackPress.collectAsState()

    Scaffold(
        topBar = { if (title != null) AppTopAppBar(title, onBackPress) },
        bottomBar = { AppBottomNav(navController) }
    ) { paddingValues ->
        content(paddingValues)
    }
}