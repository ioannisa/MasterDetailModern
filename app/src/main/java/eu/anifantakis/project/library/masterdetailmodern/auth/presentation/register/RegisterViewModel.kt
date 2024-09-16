package eu.anifantakis.project.library.masterdetailmodern.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.anifantakis.project.library.masterdetailmodern.R
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.AuthRepository
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.PasswordValidationState
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.UserDataValidator
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataError
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.Result
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.UiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface RegisterAction {
    data object OnTogglePasswordVisibilityClick: RegisterAction
    data object OnRegisterClick: RegisterAction
    data object OnLoginClick: RegisterAction
}

sealed interface RegisterEvent {
    data object RegistrationSuccess: RegisterEvent
    data class Error(val error: UiText): RegisterEvent
}

data class RegisterState(
    val email: TextFieldState = TextFieldState(),
    val isEmailValid: Boolean = false,
    val password: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false
)

class RegisterViewModel(
    private val userDataValidator: UserDataValidator,
    private val repository: AuthRepository
): ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            launch {
                snapshotFlow { state.email.text }
                    .collectLatest { email ->
                        val isValidEmail = userDataValidator.isValidEmail(email.toString())
                        state = state.copy(
                            isEmailValid = isValidEmail,
                            canRegister = isValidEmail && state.passwordValidationState.isValidPassword
                                    && !state.isRegistering
                        )
                    }
            }

            launch {
                snapshotFlow { state.password.text }
                    .collectLatest { password ->
                        val passwordValidation = userDataValidator.validatePassword(password.toString())
                        state = state.copy(
                            passwordValidationState = passwordValidation,
                            canRegister = state.isEmailValid && passwordValidation.isValidPassword
                                    && !state.isRegistering
                        )
                    }
            }

        }
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }
            is RegisterAction.OnRegisterClick -> {
                register()
            }

            is RegisterAction.OnLoginClick -> {

            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            // simulate network call
            state = state.copy(isRegistering = true)
            delay(1500L)

            val result = repository.register(
                email = state.email.text.toString().trim(),
                password = state.password.text.toString()
            )

            state = state.copy(isRegistering = false)

            when (result) {
                is Result.Success -> {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
                }
                is Result.Failure -> {
                    // failure could be 409 conflict, email already exists
                    // but because our dummy api supports only login we check for 401 unauthorized

                    if (result.error == DataError.Network.UNAUTHORIZED) {
                        eventChannel.send(RegisterEvent.Error(
                            UiText.StringResource(R.string.error_email_exists)
                        ))
                    }
                }
            }
        }
    }
}