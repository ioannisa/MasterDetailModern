package eu.anifantakis.project.library.masterdetailmodern.movies.presentation.movieslist

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import eu.anifantakis.project.library.masterdetailmodern.R
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.year
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.UIConst
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppBackground
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.ObserveAsEvents
import eu.anifantakis.project.library.masterdetailmodern.movies.domain.Movie
import eu.anifantakis.project.library.masterdetailmodern.ui.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun MoviesListScreenRoot(
    viewModel: MoviesListViewModel = koinViewModel()
) {
    ObserveAsEvents(viewModel.events) { event ->

    }

    MoviesListScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )
}

@Composable
private fun MoviesListScreen(
    state: MoviesListState,
    onAction: (MoviesListAction) -> Unit,
) {
    AppBackground {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UIConst.padding)
                .padding(bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(UIConst.paddingSmall)
        ) {
            LazyColumn() {
                items(
                    items = state.movies,
                    key = { it.id }
                ) { movie ->
                    RowItem(
                        movie = movie,
                        modifier = Modifier
                            .clickable {

                            }
                    )
                }
            }
        }
    }
}

@Composable
fun RowItem(movie: Movie, modifier: Modifier = Modifier) {
    Text(movie.title)

    Card(
        modifier = modifier.
        padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Row {
            ThumbnailLoader(imagePath = movie.posterPath)
            Column(
                modifier = Modifier
                    .align(Alignment.Top)
                    .fillMaxSize()
                    .heightIn(120.dp)
                    .padding(8.dp)
                ,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.titleLarge,

                    )

                Text(text = movie.releaseDate.year().toString())
            }
        }
    }
}

@Composable
fun ThumbnailLoader(imagePath: String?) {
    val imagePainter: Painter

    if (!imagePath.isNullOrEmpty()) {
        val imageUrl = "https://image.tmdb.org/t/p/w200$imagePath"
        imagePainter = rememberAsyncImagePainter(imageUrl)
    }
    else {
        imagePainter = painterResource(R.drawable.ic_launcher_foreground)
    }

    Image(
        painter = imagePainter,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        alignment = Alignment.TopCenter,
        modifier = Modifier.size(120.dp)
    )
}

@Preview
@Composable
private fun MoviesListScreenPreview() {
    AppTheme {
        MoviesListScreen(
            state = MoviesListState(),
            onAction = {}
        )
    }
}