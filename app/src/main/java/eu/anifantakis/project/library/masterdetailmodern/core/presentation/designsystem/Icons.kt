package eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import eu.anifantakis.project.library.masterdetailmodern.R


object Icons {
    val stop: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.stop)

    val start: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.start)

    val logout: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_logout)

    val check: ImageVector
        @Composable
        get() = Icons.Default.Check

    val close: ImageVector
        @Composable
        get() = Icons.Default.Close

    val email: ImageVector
        @Composable
        get() = Icons.Outlined.Email

    val padlock: ImageVector
        @Composable
        get() = Icons.Outlined.Lock

    val visibilityOn: ImageVector
        @Composable
        get() = Icons.Outlined.Visibility

    val visibilityOff: ImageVector
        @Composable
        get() = Icons.Outlined.VisibilityOff

    val restaurant: ImageVector
        @Composable
        get() = Icons.Outlined.Restaurant

    val fastFood: ImageVector
        @Composable
        get() = Icons.Outlined.Fastfood
}