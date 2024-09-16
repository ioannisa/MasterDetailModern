package eu.anifantakis.project.library.masterdetailmodern.core.presentation.designsystem

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.dp

object UIConst {

    val padding = 16.dp
    val paddingDouble = 32.dp
    val paddingSmall = 8.dp
    val paddingExtraSmall = 4.dp

    fun grayOutColor(color: Color, blendFactor: Float = 0.5f): Color {
        return lerp(color, Color.Gray, blendFactor)
    }
}