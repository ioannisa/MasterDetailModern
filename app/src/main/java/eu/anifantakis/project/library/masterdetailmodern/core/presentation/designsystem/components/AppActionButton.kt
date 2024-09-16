package eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.UIConst.grayOutColor
import eu.anifantakis.project.library.masterdetailmodern.ui.theme.AppTheme

@Composable
fun AppActionButton(
    text: String,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,

    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = grayOutColor(MaterialTheme.colorScheme.primary),
            disabledContentColor = grayOutColor(MaterialTheme.colorScheme.onPrimary)
        ),
        shape = RoundedCornerShape(100f),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp)
                    .align(Alignment.Center)
                    .alpha(if(isLoading) 1f else 0f),
                strokeWidth = 1.5.dp,
                color = grayOutColor(MaterialTheme.colorScheme.onPrimary)
            )

            Text(
                text = text,
                modifier = Modifier
                    .alpha(if(isLoading) 0f else 1f)
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun AppActionButtonPreview() {
    AppTheme {
        AppBackground {
            Column(
                modifier = Modifier
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Loading: false, enabled: truew")
                AppActionButton(
                    text = "Sign up",
                    isLoading = false,
                    enabled = true,
                    onClick = {}
                )

                Text("Loading: false, enabled: false")
                AppActionButton(
                    text = "Register",
                    isLoading = false,
                    enabled = false,
                    onClick = {}
                )

                Text("Loading: true, enabled: false")
                AppActionButton(
                    text = "Register",
                    isLoading = true,
                    enabled = false,
                    onClick = {}
                )

                Text("Loading: true, enabled: true")
                AppActionButton(
                    text = "Register",
                    isLoading = true,
                    enabled = false,
                    onClick = {}
                )
            }
        }
    }
}