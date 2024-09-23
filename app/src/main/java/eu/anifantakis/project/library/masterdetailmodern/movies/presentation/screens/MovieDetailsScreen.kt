package eu.anifantakis.project.library.masterdetailmodern.movies.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import eu.anifantakis.project.library.masterdetailmodern.movies.presentation.MoviesViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MovieDetailsScreen(
    vieModel: MoviesViewModel = koinViewModel()
) {
    Text(
        modifier = Modifier.padding(top = 48.dp),
        text = "Detail Screen -> ${vieModel.state.selectedMovie?.id ?: 0}"
    )
}