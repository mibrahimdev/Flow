package io.github.mohamedisoliman.flow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(

    primary = Figma.Black,
    primaryVariant = Figma.DarkBlack,
    secondary = Figma.White,

    background = Figma.Black,
    surface = Figma.DarkBackground,

    onPrimary = Figma.White,
    onSecondary = Figma.DarkBackground,
    onBackground = Figma.White,
    onSurface = Figma.White,
)

private val LightColorPalette = lightColors(

    primary = Figma.White,
    primaryVariant = Figma.White,
    secondary = Figma.DarkBackground,

    background = Figma.Background,
    surface = Color.White,

    onPrimary = Figma.Black,
    onSecondary = Figma.White,
    onBackground = Color.Black,
    onSurface = Color.Black,

    )

@Composable
fun FlowTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}