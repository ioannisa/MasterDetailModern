package eu.anifantakis.project.library.masterdetailmodern.movies.presentation.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.LifecycleConfig
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.ScreenWithLoadingIndicator
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.TopAppBarConfig
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.screens.viewmodel.MoviesViewModelRedux
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailsScreen(
    paddingValues: PaddingValues,
    viewModel: MoviesViewModelRedux = koinViewModel()
) {
    ScreenWithLoadingIndicator(
        topAppBarConfig = TopAppBarConfig(
            title = "Article Details",
            onBackPress = {  }
        ),
        lifecycleConfig = LifecycleConfig(
            onStart = {  }
        ),
        paddingValues = paddingValues,
    ) {

        Text(
            modifier = Modifier.padding(top = 48.dp),
            text = "Detail Screen -> ${viewModel.composeState.selectedMovie?.id ?: 0}"
        )
    }
}