package com.mobile.expenseapp.presentation.setting_screen.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mobile.expenseapp.R
import com.mobile.expenseapp.presentation.setting_screen.SettingViewModel
import com.mobile.expenseapp.util.spacing

@Composable
fun DarkModeSetting(
    navController: NavController,
    settingViewModel: SettingViewModel = hiltViewModel(),
) {
    val isDarkModeEnabled by settingViewModel.isDarkMode.collectAsState()

    TextButton(
        onClick = {
            // Toggle Dark Mode

        },
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            ),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface
        ),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.spacing.medium,
            vertical = 20.dp
        )
    ) {
        Text(
            text = navController.context.getString(R.string.settings_dark_mode),
            style = MaterialTheme.typography.button,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )

        Switch(modifier = Modifier.padding(end = MaterialTheme.spacing.small), switch = isDarkModeEnabled || isSystemInDarkTheme()) { switched ->
            settingViewModel.editDarkMode(enabled = switched)
        }
    }
}
