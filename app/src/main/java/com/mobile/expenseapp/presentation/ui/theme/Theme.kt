package com.mobile.expenseapp.presentation.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.mobile.expenseapp.util.rememberWindowInfo


private val DarkColorPalette = darkColors(
    primary = Indigo900,
    secondary = Indigo900,
    background = DeepPurple900,
    surface = DeepPurple300,
    error = Red200,
    onSurface = White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White
)
@Composable
fun ExpenseAppTheme(content: @Composable () -> Unit) {
    val colors = DarkColorPalette

    val windowInfo = rememberWindowInfo()

    CompositionLocalProvider() {
        MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = Shapes,
            content = content
        )
    }
}