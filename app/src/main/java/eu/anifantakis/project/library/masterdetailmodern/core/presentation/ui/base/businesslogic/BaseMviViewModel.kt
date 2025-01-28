package eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.base.businesslogic

import androidx.compose.runtime.getValue
import androidx.lifecycle.viewModelScope
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.toComposeState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * A generic base for Redux-like MVI in Android.
 * @param S The screen's State type
 * @param I The screen's Intent type
 * @param E One-off Effects that the UI handles exactly once
 */
abstract class BaseMviViewModel<S, I, E>(
    initialState: S
) : BaseViewModel() {

    // The current immutable State
    private val _viewState = MutableStateFlow(initialState)
    val state: StateFlow<S> = _viewState.asStateFlow()
    val composeState by _viewState.toComposeState(viewModelScope)

    // For ephemeral effects like Toasts or navigation
    private val _viewEffect = MutableSharedFlow<E>(replay = 0)
    val viewEffect: SharedFlow<E> = _viewEffect

    // Convenient way to read the latest state
    protected val currentState: S
        get() = _viewState.value

    /**
     * Called by the UI (or internal code) whenever an Intent occurs.
     *  1) We run the 'reduce' function with (oldState + intent).
     *  2) We update the StateFlow with the new State.
     *  3) If there's an effect, we emit it for the UI to consume.
     */
    fun processIntent(intent: I) {
        val (newState, effect) = reduce(currentState, intent)
        setState(newState)
        effect?.let { postEffect(it) }
    }

    /**
     * The child ViewModel implements how to combine oldState + Intent.
     * The result is (newState, optionalEffect).
     */
    protected abstract fun reduce(
        oldState: S,
        intent: I
    ): Pair<S, E?>

    /**
     * Updates our StateFlow with a fresh state.
     */
    protected fun setState(newState: S) {
        _viewState.update { newState }
    }

    /**
     * Emits a single effect that the UI will handle once.
     */
    protected fun postEffect(effect: E) {
        viewModelScope.launch {
            _viewEffect.emit(effect)
        }
    }
}