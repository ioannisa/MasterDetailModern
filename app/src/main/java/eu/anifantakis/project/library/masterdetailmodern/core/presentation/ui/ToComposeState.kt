package eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


fun <T> StateFlow<T>.toComposeState(scope: CoroutineScope): State<T> {
    val composeState = mutableStateOf(value)
    scope.launch {
        this@toComposeState.collect { newValue ->
            composeState.value = newValue
        }
    }
    return composeState
}