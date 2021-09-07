package wise.military.life.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val colors = lightColors(
    primary = Color(0xFF42a5f5),
    primaryVariant = Color(0xFF0077c2),
    secondary = Color(0xFF80d6ff)
)

@Composable
fun MaterialTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colors = colors,
        content = content
    )
}
