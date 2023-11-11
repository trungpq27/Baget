package com.mobile.expenseapp.presentation.ui.theme

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobile.expenseapp.presentation.setting_screen.SettingViewModel
import com.mobile.expenseapp.util.CompactSpacing
import com.mobile.expenseapp.util.ExpandedSpacing
import com.mobile.expenseapp.util.LocalSpacing
import com.mobile.expenseapp.util.MediumSpacing
import com.mobile.expenseapp.util.WindowInfo
import com.mobile.expenseapp.util.rememberWindowInfo

private val DarkColorPalette = darkColors(
    primary = DarkPrimary100,
    secondary = DarkSecondary100,
    background = DarkBackGround,
    surface = DarkPrimary30,
    error = Red200,
    onSurface = White,
    onPrimary = White,
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

@ExperimentalMaterialApi
@Composable
fun BagetTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
//    val colors = if (darkTheme) {
//        DarkColorPalette
//    } else {
//        LightColorPalette
//    }
    val colors = DarkColorPalette

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