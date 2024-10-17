package eu.anifantakis.project.library.masterdetailmodern.auth.presentation.intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.anifantakis.lib.securepersist.PersistManager
import eu.anifantakis.project.library.masterdetailmodern.BuildConfig
import eu.anifantakis.project.library.masterdetailmodern.R
import eu.anifantakis.project.library.masterdetailmodern.core.data.networking.AuthHttpClient
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.Icons
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.UIConst
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppActionButton
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppBackground
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components.AppOutlinedActionButton
import eu.anifantakis.project.library.masterdetailmodern.ui.theme.AppTypography
import timber.log.Timber

@Composable
fun IntroScreenRoot(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit
) {
    IntroScreen(
        onAction = { action ->
            when (action) {
                IntroAction.OnSignUpClick -> onSignUpClick()
                IntroAction.OnSignInClick -> onSignInClick()
            }
        }
    )
}

@Composable
fun IntroScreen(
    onAction: (IntroAction) -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {

        val pm = PersistManager(context, "${BuildConfig.APPLICATION_ID}.securedPersistence")
        val authInfo by pm.preference(AuthHttpClient.AuthInfo())

        Timber.tag("PERSISTENCE").d(
            "accessToken:${authInfo.accessToken}, refreshToken:${authInfo.refreshToken}, userId:${authInfo.userId}"
        )
    }

    AppBackground {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            LogoVertical()
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UIConst.padding)
                .padding(bottom = 48.dp),
            verticalArrangement = Arrangement.spacedBy(UIConst.paddingSmall)
        ) {
            Text(
                text = stringResource(R.string.welcome_to_app),
                color = MaterialTheme.colorScheme.onBackground,
                style = AppTypography.headlineMedium
            )

            Text(
                text = stringResource(R.string.app_description),
                color = MaterialTheme.colorScheme.onBackground,
                style = AppTypography.bodyMedium
            )

            Spacer(modifier = Modifier.height(UIConst.padding))

            AppActionButton(
                text = "Sign Up",
                isLoading = false,
                onClick = { onAction(IntroAction.OnSignUpClick) }
            )

            AppOutlinedActionButton(
                text = "Sign In",
                isLoading = false,
                onClick = { onAction(IntroAction.OnSignInClick) }
            )
        }
    }
}

@Composable
private fun LogoVertical(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.fastFood,
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(96.dp)
                .height(96.dp)
        )
    }
}

@Preview
@Composable
fun IntroScreenPreview() {
    IntroScreen(onAction = {})
}