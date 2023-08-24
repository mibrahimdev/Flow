package io.github.mohamedisoliman.flow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val darkColorPalette = darkColors(

    primaryVariant = Figma.DarkBlack,

    primary = Figma.Black,
    secondary = Figma.White,
    background = Figma.Black,
    surface = Figma.DarkBackground,

    onPrimary = Figma.White,
    onSecondary = Figma.Grey3,
    onBackground = Figma.White,
    onSurface = Figma.White,
)

private val lightColorPalette = lightColors(

    primaryVariant = Figma.White,

    primary = Figma.White,
    secondary = Figma.DarkBackground,

    background = Figma.LightGrey,
    surface = Figma.Background,

    onPrimary = Figma.Black,
    onSecondary = Figma.White,
    onBackground = Color.Black,
    onSurface = Color.Black,

    )

@Composable
fun FlowTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        darkColorPalette
    } else {
        lightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}