package eu.anifantakis.project.library.masterdetailmodern.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import eu.anifantakis.project.library.masterdetailmodern.R
import eu.anifantakis.project.library.masterdetailmodern.auth.domain.UserDataValidator
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.Icons
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.UIConst
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppActionButton
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppBackground
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppPasswordTextField
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppTextField
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.ui.ObserveAsEvents
import eu.anifantakis.project.library.masterdetailmodern.ui.theme.correctGreen
import org.koin.androidx.compose.koinViewModel


@Composable
fun RegisterScreenRoot(
    onSignInClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvents(viewModel.events) { event ->
        keyboardController?.hide()

        when (event) {
            is RegisterEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG
                ).show()
            }

            is RegisterEvent.RegistrationSuccess -> {
                onSuccessfulRegistration()
            }

            is RegisterEvent.GotoLogin -> {
                onSignInClick()
            }
        }
    }

    RegisterScreen(
        state = viewModel.state,
        onAction = viewModel::onAction,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onUsernameChanged = viewModel::onUsernameChanged
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    onEmailChanged: (TextFieldValue) -> Unit,
    onPasswordChanged: (TextFieldValue) -> Unit,
    onUsernameChanged: (TextFieldValue) -> Unit
) {
    AppBackground{
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
            Header(onAction)
            Spacer(Modifier.height(UIConst.paddingDouble))

            // LOGIN SECTION
            LoginSection(
                state = state,
                onAction = onAction,
                onEmailChanged = onEmailChanged,
                onPasswordChanged = onPasswordChanged,
                onUsernameChanged = onUsernameChanged
            )

        }
    }
}

@Composable
private fun Header(
    onAction: (RegisterAction) -> Unit
) {
    Column {
        Text(
            text = stringResource(R.string.create_account),
            style = MaterialTheme.typography.headlineMedium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(UIConst.paddingExtraSmall),
        ) {
            Text(
                text = "Already have an account?",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Login",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.surfaceTint
                ),
                modifier = Modifier
                    .clickable {
                        onAction(
                            RegisterAction.OnLoginClick
                        )
                    }
            )
        }
    }
}

@Composable
private fun LoginSection(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    onEmailChanged: (TextFieldValue) -> Unit,
    onPasswordChanged: (TextFieldValue) -> Unit,
    onUsernameChanged: (TextFieldValue) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(UIConst.padding)
    ) {
        AppTextField(
            value = state.email,
            onValueChange = onEmailChanged,
            startIcon = Icons.email,
            endIcon = if (state.isEmailValid) {
                Icons.check
            } else null,
            hint = stringResource(R.string.example_email),
            title = stringResource(R.string.email),
            additionalInfo = stringResource(R.string.must_be_a_valid_email),
            keyboardType = KeyboardType.Email
        )

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
                RegisterAction.OnTogglePasswordVisibilityClick
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(UIConst.paddingExtraSmall)
        ) {
            PasswordRequirement(
                text = stringResource(R.string.at_least_x_characters, UserDataValidator.MIN_PASSWORD_LENGTH),
                isValid = state.passwordValidationState.hasMinLength
            )

            PasswordRequirement(
                text = stringResource(R.string.contains_number),
                isValid = state.passwordValidationState.hasNumber
            )

            PasswordRequirement(
                text = stringResource(R.string.contains_lowercase_character),
                isValid = state.passwordValidationState.hasLowerCaseCharacter
            )

            PasswordRequirement(
                text = stringResource(R.string.contains_uppercase_character),
                isValid = state.passwordValidationState.hasUpperCaseCharacter
            )

            PasswordRequirement(
                text = stringResource(R.string.contains_symbol),
                isValid = state.passwordValidationState.hasSymbolCharacter
            )
        }

        AppActionButton(
            text = stringResource(R.string.register),
            isLoading = state.isRegistering,
            //enabled = state.canRegister,
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                onAction(
                    RegisterAction.OnRegisterClick
                )
            }
        )
    }
}

@Composable
private fun PasswordRequirement(
    text: String,
    isValid: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(UIConst.padding)
    ) {
        Icon(
            imageVector = if (isValid) Icons.check else Icons.close,
            contentDescription = null,
            tint = if (isValid) {
                correctGreen
            } else {
                MaterialTheme.colorScheme.error
            }
        )

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(
        state = RegisterState(),
        onAction = {},
        onPasswordChanged = {},
        onUsernameChanged = {},
        onEmailChanged = {}
    )
}