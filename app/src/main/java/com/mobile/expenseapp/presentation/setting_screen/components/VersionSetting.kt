package com.mobile.expenseapp.presentation.setting_screen.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.expenseapp.R
import com.mobile.expenseapp.util.spacing

@Composable
fun VersionSetting(navController: NavController) {
    TextButton(
        onClick = {
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
            text = navController.context.getString(R.string.settings_version),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.weight(2f),
            textAlign = TextAlign.Start
        )

        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            val context = LocalContext.current
            val packageManager = context.packageManager.getPackageInfo("com.mobile.expenseapp", 0)
            Text(
                text = packageManager.versionName,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}