package com.mobile.expenseapp.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import com.mobile.expenseapp.util.CompactSpacing
import com.mobile.expenseapp.util.ExpandedSpacing
import com.mobile.expenseapp.util.LocalSpacing
import com.mobile.expenseapp.util.MediumSpacing
import com.mobile.expenseapp.util.WindowInfo
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

private val LightColorPalette = lightColors(
    primary = LightBlue500,
    secondary = LightBlue500,
    background = Grey100,
    surface = White,
    error = Red500,
    onSurface = Black,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.Black
)

@Composable
fun BagetTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    val windowInfo = rememberWindowInfo()

    CompositionLocalProvider(
        when (windowInfo.screenHeightInfo) {
            is WindowInfo.WindowType.Compact -> {
                LocalSpacing provides CompactSpacing()
            }
            is WindowInfo.WindowType.Medium -> {
                LocalSpacing provides MediumSpacing()
            }
            else -> LocalSpacing provides ExpandedSpacing()
        }
    ) {
        MaterialTheme(
            colors = colors,
            typography = typography,
            shapes = Shapes,
            content = content
        )
    }
}