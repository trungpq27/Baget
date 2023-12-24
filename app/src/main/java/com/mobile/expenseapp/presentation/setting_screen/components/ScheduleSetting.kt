package com.mobile.expenseapp.presentation.setting_screen.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mobile.expenseapp.util.spacing
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterialApi
fun ScheduleSetting(
    modalBottomSheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    navController: NavController,
    onItemClick: (Int) -> Unit,
) {
    TextButton(
        onClick = {
            onItemClick(4)
            scope.launch {
                modalBottomSheetState.show()
            }

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
            text = "Schedule List",
            style = MaterialTheme.typography.button,
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Start
        )

    }
}
