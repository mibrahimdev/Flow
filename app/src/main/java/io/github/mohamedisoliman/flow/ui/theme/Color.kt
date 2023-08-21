package io.github.mohamedisoliman.flow.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFFBB86FC)
val Purple500 = Color(0xFF6200EE)
val Purple700 = Color(0xFF3700B3)
val Teal200 = Color(0xFF03DAC5)

object Figma {

    val White = Color(0xFFFFFFFF)
    val Green = Color(0xFF07E092)
    val Blue = Color(0xFF3D4ABA)
    val Pink = Color(0xFFFD5B71)
    val LightGrey = Color(0xFFFAFAFF)
    val Grey3 = Color(0xFF828282)
    val Black = Color(0xFF070417)
    val Purple = Color(0xFF9B51E0)
    val Orange = Color(0xFFFFA656)
    val DarkBlack = Color(0xFF18152C)
    val Background = Color(0xFFE9E9FF)
    val DarkBackground = Color(0xFF292639)
}

@get:Composable
val Colors.ArrowColor: Color
    get() = Figma.Grey3
