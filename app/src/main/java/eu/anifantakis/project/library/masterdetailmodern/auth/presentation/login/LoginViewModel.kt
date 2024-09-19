package eu.anifantakis.project.library.masterdetailmodern.auth.presentation.login

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
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.UiText
import eu.anifantakis.project.library.masterdetailmodern.core.domain.util.DataResult
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

sealed interface LoginAction {
    data object OnTogglePasswordVisibilityClick: LoginAction
    data object OnLoginClick: LoginAction
    data object OnRegisterClick: LoginAction
}

data class LoginState(
    val username: TextFieldValue = TextFieldValue(),
    val password: TextFieldValue = TextFieldValue(),
    val isPasswordVisible: Boolean = false,
    val passwordValidationState: PasswordValidationState = PasswordValidationState(),
    val isValidUsername: Boolean = false,
    val canLogin: Boolean = false,
    val isLoggingIn: Boolean = false
)

sealed interface LoginEvent {
    data object LoginSuccess: LoginEvent
    data class Error(val error: UiText): LoginEvent
}

class LoginViewModel(
    private val userDataValidator: UserDataValidator,
    private val authRepository: AuthRepository
): ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    fun onUsernameChanged(username: TextFieldValue) {
        val isValidUsername = userDataValidator.isValidUsername(username.text)
        state = state.copy(
            username = username,
            isValidUsername = isValidUsername,
            canLogin = isValidUsername && state.passwordValidationState.isValidPassword
                    && !state.isLoggingIn
        )
    }

    fun onPasswordChanged(password: TextFieldValue) {
        val passwordValidation = userDataValidator.validatePassword(password.text)
        state = state.copy(
            password = password,
            passwordValidationState = passwordValidation,
            canLogin = state.isValidUsername && passwordValidation.isValidPassword
                    && !state.isLoggingIn
        )
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnTogglePasswordVisibilityClick -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }
            is LoginAction.OnLoginClick -> {
                login()
            }
            else -> Unit
        }
    }

    private fun login() {
        viewModelScope.launch {
            state = state.copy(isLoggingIn = true)
            val result = authRepository.login(
                username = state.username.text.trim(),
                password = state.password.text
            )
            state = state.copy(isLoggingIn = false)

            when(result) {
                is DataResult.Failure -> {
                    if(result.error == DataError.Network.UNAUTHORIZED) {
                        eventChannel.send(LoginEvent.Error(
                            UiText.StringResource(R.string.error_username_password_incorrect)
                        ))
                    } else {
                        eventChannel.send(LoginEvent.Error(result.error.asUiText()))
                    }
                }

                is DataResult.Success -> {
                    eventChannel.send(LoginEvent.LoginSuccess)
                }
            }
        }
    }
}