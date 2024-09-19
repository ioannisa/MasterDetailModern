package eu.anifantakis.project.library.masterdetailmodern.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import eu.anifantakis.project.library.masterdetailmodern.R
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.Icons
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.UIConst
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppActionButton
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppBackground
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppPasswordTextField
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppTextField
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    onSignupClick: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvents(viewModel.events) { event ->
        keyboardController?.hide()

        when (event) {
            is LoginEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            is LoginEvent.LoginSuccess -> {
                Toast.makeText(
                    context,
                    R.string.login_success,
                    Toast.LENGTH_LONG
                ).show()
                onLoginSuccess()
            }
        }
    }

    LoginScreen(
        state = viewModel.state,

        // the commented "onAction" line forwards everything to viewmodel
        // but we deal wit RegisterClick here and forward anything else to viewmodel

        //onAction = viewModel::onAction,
        onAction = { action ->
            when (action) {
                is LoginAction.OnRegisterClick -> {
                    onSignupClick()
                }
                else -> {
                    viewModel.onAction(action)
                }
            }
        },

        onUsernameChanged = viewModel::onUsernameChanged,
        onPasswordChanged = viewModel::onPasswordChanged
    )
}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onUsernameChanged: (TextFieldValue) -> Unit,
    onPasswordChanged: (TextFieldValue) -> Unit
) {
    AppBackground {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(
                    horizontal = UIConst.padding,
                    vertical = UIConst.paddingDouble
                )
                .padding(top = UIConst.padding),
            verticalArrangement = Arrangement.spacedBy(UIConst.padding)
        ) {
            Text(
                text = stringResource(R.string.hi_there),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = stringResource(R.string.app_welcome_text),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(UIConst.paddingDouble))

            LoginSection(
                state = state,
                onAction = onAction,
                onUsernameChanged = onUsernameChanged,
                onPasswordChanged = onPasswordChanged
            )
        }
    }
}

@Composable
private fun LoginSection(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onPasswordChanged: (TextFieldValue) -> Unit,
    onUsernameChanged: (TextFieldValue) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(UIConst.padding)
    ) {
        AppTextField(
            value = state.username,
            onValueChange = onUsernameChanged,
            endIcon = if (state.isValidUsername) {
                Icons.check
            } else null,
            startIcon = Icons.person,
            hint = stringResource(R.string.username),
            title = stringResource(R.string.username),
            keyboardType = KeyboardType.Text
        )

        AppPasswordTextField(
            value = state.password,
            onValueChange = onPasswordChanged,
            isPasswordVisible = state.isPasswordVisible,
            hint = stringResource(R.string.password_hint),
            title = stringResource(R.string.password),
        ) {
            onAction(
                LoginAction.OnTogglePasswordVisibilityClick
            )
        }

        AppActionButton(
            text = stringResource(R.string.login),
            isLoading = state.isLoggingIn,
            //enabled = state.canLogin,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAction(
                    LoginAction.OnLoginClick
                )
            }
        )
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(
        state = LoginState(),
        onAction = {},
        onUsernameChanged = {},
        onPasswordChanged ={}
    )
}