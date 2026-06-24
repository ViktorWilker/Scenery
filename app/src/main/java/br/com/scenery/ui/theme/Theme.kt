package br.com.scenery.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val SceneryColorScheme = lightColorScheme(
    background = Background,
    surface = Surface,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    primary = Accent,
    onPrimary = Surface,
    secondary = AccentMuted,
    onSecondary = Surface,
    outline = Border
)
@Composable
fun SceneryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SceneryColorScheme,
        typography = SceneryTypography,
        content = content
    )
}