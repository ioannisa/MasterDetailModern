package eu.anifantakis.project.library.masterdetailmodern

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.AuthRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

data class MainState(
    val isLoggedIn: Boolean = false,
    val isCheckingAuth: Boolean = false
)


class MainViewModel(
    private val authRepository: AuthRepository,
): ViewModel() {

    var state by mutableStateOf(MainState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(isCheckingAuth = true)

            val authInfo = authRepository.fetchAuthInfo()
            state = state.copy(
                isLoggedIn = (authInfo.userId > 0)
            )

            delay(2.seconds)

            state = state.copy(isCheckingAuth = false)
        }
    }
}