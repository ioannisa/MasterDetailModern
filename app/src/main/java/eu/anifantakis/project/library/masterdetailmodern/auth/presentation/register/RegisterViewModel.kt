package eu.anifantakis.project.library.masterdetailmodern.auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
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
    data object GotoLogin: RegisterEvent
}

data class RegisterState(
    val email: TextFieldValue = TextFieldValue(),
    val isEmailValid: Boolean = false,
    val username: TextFieldValue = TextFieldValue(),
    val isValidUsername: Boolean = false,
    val password: TextFieldValue = TextFieldValue(),
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

    fun onUsernameChanged(username: TextFieldValue) {
        val isValidUsername = userDataValidator.isValidUsername(username.text)
        state = state.copy(
            username = username,
            isValidUsername = isValidUsername,
            canRegister = isValidUsername && state.isEmailValid && state.passwordValidationState.isValidPassword
                    && !state.isRegistering
        )
    }

    fun onEmailChanged(email: TextFieldValue) {
        val isValidEmail = userDataValidator.isValidEmail(email.text)
        state = state.copy(
            email = email,
            isEmailValid = isValidEmail,
            canRegister = isValidEmail && state.isValidUsername && state.passwordValidationState.isValidPassword
                    && !state.isRegistering
        )
    }

    fun onPasswordChanged(password: TextFieldValue) {
        val passwordValidation = userDataValidator.validatePassword(password.text)
        state = state.copy(
            password = password,
            passwordValidationState = passwordValidation,
            canRegister = state.isEmailValid && state.isValidUsername && passwordValidation.isValidPassword
                    && !state.isRegistering
        )
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
                viewModelScope.launch {
                    eventChannel.send(RegisterEvent.GotoLogin)
                }
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            state = state.copy(isRegistering = true)

            val result = repository.register(
                username = state.username.text.trim(),
                password = state.password.text
            )

            state = state.copy(isRegistering = false)

            when (result) {
                is Result.Success -> {
                    eventChannel.send(RegisterEvent.RegistrationSuccess)
                }
                is Result.Failure -> {
                    // failure could be 409 conflict, username already exists
                    // but because our dummy api supports only login we check for 401 unauthorized

                    if (result.error in setOf(DataError.Network.UNAUTHORIZED, DataError.Network.CONFLICT)) {
                        eventChannel.send(RegisterEvent.Error(
                            UiText.StringResource(R.string.error_email_exists)
                        ))
                    }
                }
            }
        }
    }
}