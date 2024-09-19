package eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.anifantakis.project.library.masterdetailmodern.R
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.Icons
import eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem.UIConst

@Composable
fun AppPasswordTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isPasswordVisible: Boolean,
    hint: String,
    title: String?,
    modifier: Modifier = Modifier,
    onTogglePasswordVisibility: () -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(UIConst.paddingExtraSmall)
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            textStyle = LocalTextStyle.current.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            singleLine = true,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onBackground),
            visualTransformation = if (isPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            modifier = Modifier
                .clip(RoundedCornerShape(UIConst.padding))
                .background(
                    if (isFocused) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    } else {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    }
                )
                .border(
                    width = 1.dp,
                    color = if (isFocused) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        Color.Transparent
                    },
                    shape = RoundedCornerShape(UIConst.padding)
                )
                .padding(horizontal = 12.dp)
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = UIConst.paddingSmall),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall)
                ) {
                    Icon(
                        imageVector = Icons.padlock,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Box(
                        modifier = Modifier.weight(1f)
                    ) {
                        if (value.text.isEmpty() && !isFocused) {
                            Text(
                                text = hint,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        innerTextField()
                    }

                    IconButton(
                        onClick = onTogglePasswordVisibility
                    ) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.visibilityOn else Icons.visibilityOff,
                            contentDescription = if (isPasswordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        )
    }
}



@Preview
@Composable
private fun AppPasswordTextFieldPreview() {
    AppBackground {

        var isPasswordVisible by remember { mutableStateOf(false) }
        var passwordValue by remember { mutableStateOf(TextFieldValue("")) }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppPasswordTextField(
                value = passwordValue,
                onValueChange = { passwordValue = it },
                isPasswordVisible = isPasswordVisible,
                hint = "Enter Password here",
                title = "Password Field",
                modifier = Modifier.fillMaxWidth(),
                onTogglePasswordVisibility = { isPasswordVisible = !isPasswordVisible }
            )
        }
    }
}
