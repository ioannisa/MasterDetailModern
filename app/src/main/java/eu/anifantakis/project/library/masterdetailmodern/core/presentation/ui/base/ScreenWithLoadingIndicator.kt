package eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.scaffold.ScaffoldViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun ScreenWithLoadingIndicator (
    topAppBarConfig: TopAppBarConfig = TopAppBarConfig(),
    loadingConfig: LoadingConfig = LoadingConfig(),
    lifecycleConfig: LifecycleConfig,

    scaffoldViewModel: ScaffoldViewModel = koinViewModel(),
    paddingValues: PaddingValues? = null,
    extraPaddings: ExtraPaddings = ExtraPaddings(),

    content: @Composable () -> Unit
) {
    val topBarHeight by scaffoldViewModel.topBarHeight.collectAsState(0)

    ContentWithLifecycleEvents(
        lifecycleConfig = LifecycleConfig(
            onResume = lifecycleConfig.onResume,
            onStart = lifecycleConfig.onStart,
            onPause = lifecycleConfig.onPause,
            onStop = lifecycleConfig.onStop,
            onDestroy = lifecycleConfig.onDestroy,
            onCreate = lifecycleConfig.onCreate,
            onFirstCreate = lifecycleConfig.onFirstCreate,
            onAny = lifecycleConfig.onAny
        )
    )

    LaunchedEffect(topAppBarConfig) {
        scaffoldViewModel.updateAppBar(
            title = topAppBarConfig.title,
            onBackPress = topAppBarConfig.onBackPress
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                PaddingValues(
                    top = (paddingValues?.calculateTopPadding() ?: 0.dp) + extraPaddings.top,
                    start = (paddingValues?.calculateStartPadding(LocalLayoutDirection.current) ?: 0.dp) + extraPaddings.start,
                    end = (paddingValues?.calculateEndPadding(LocalLayoutDirection.current) ?: 0.dp) + extraPaddings.end,
                    bottom = (paddingValues?.calculateBottomPadding() ?: 0.dp) + extraPaddings.bottom
                )
            )
    ) {
        Column {
            //MyTopAppBar(title = topAppBarTitle, onBackPress = topAppBarOnBackPress)
            content()
        }
        LoadingIndicator(loadingConfig.isLoading, loadingConfig.criticalContent)
    }
}

@Composable
private fun ContentWithLifecycleEvents(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    lifecycleConfig: LifecycleConfig
) {
    val onFirstCreateExecuted = rememberSaveable { mutableStateOf(false) }
    val onCreateExecuted = remember { mutableStateOf(false) }

    DisposableEffect(lifecycleOwner.lifecycle) {
        val observer = LifecycleEventObserver { source, event ->
            lifecycleConfig.onAny()
            when (event) {
                Lifecycle.Event.ON_RESUME -> lifecycleConfig.onResume()
                Lifecycle.Event.ON_START -> lifecycleConfig.onStart()
                Lifecycle.Event.ON_PAUSE -> lifecycleConfig.onPause()
                Lifecycle.Event.ON_STOP -> lifecycleConfig.onStop()
                Lifecycle.Event.ON_DESTROY -> lifecycleConfig.onDestroy()
                Lifecycle.Event.ON_CREATE -> {
                    // classic onCreate that repeats on orientation change
                    if (!onCreateExecuted.value) {
                        lifecycleConfig.onCreate()
                        onCreateExecuted.value = true
                    }

                    // custom onCreate that executes once regardless of orientation change
                    if (!onFirstCreateExecuted.value) {
                        lifecycleConfig.onFirstCreate()
                        onFirstCreateExecuted.value = true
                    }
                }
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

data class TopAppBarConfig(
    val title: String? = null,
    val onBackPress: (() -> Unit)? = null
)

data class LoadingConfig(
    var isLoading: Boolean = false,
    val criticalContent: Boolean = false
)

data class LifecycleConfig(
    val onResume: () -> Unit = {},
    val onStart: () -> Unit = {},
    val onPause: () -> Unit = {},
    val onStop: () -> Unit = {},
    val onDestroy: () -> Unit = {},
    val onCreate: () -> Unit = {},
    val onFirstCreate: () -> Unit = {},
    val onAny: () -> Unit = {},
)

data class ExtraPaddings(
    val top: Dp = 0.dp,
    val start: Dp = 0.dp,
    val end: Dp = 0.dp,
    val bottom: Dp = 0.dp
) {
    constructor(all: Dp) : this(top = all, start = all, end = all, bottom = all)
    constructor(horizontal: Dp, vertical: Dp) : this(top = vertical, start = horizontal, end = horizontal, bottom = vertical)
}