package eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import eu.anifantakis.project.library.masterdetailmodern.R


object Icons {
    val check: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.check)

    val stop: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.stop)

    val start: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.start)

    val logout: ImageVector
        @Composable
        get() = ImageVector.vectorResource(id = R.drawable.ic_logout)
}