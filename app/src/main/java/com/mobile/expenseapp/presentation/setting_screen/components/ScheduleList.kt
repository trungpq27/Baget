package com.mobile.expenseapp.presentation.setting_screen.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.presentation.setting_screen.SettingViewModel
import kotlinx.coroutines.CoroutineScope

@Composable
@ExperimentalMaterialApi
fun ScheduleList(
    modalBottomSheetState: ModalBottomSheetState,
    scope: CoroutineScope,
    navController: NavController,
    onScheduleClick: (ScheduleDto) -> Unit,
    onDeleteClick: (ScheduleDto) -> Unit,
    settingViewModel: SettingViewModel = hiltViewModel()
) {
    val schedules by settingViewModel.scheduleList.collectAsState()
    Column {
        schedules.forEach { schedule ->
            ScheduleItem(
                schedule = schedule,
                onScheduleClick = { onScheduleClick(schedule) },
                onDeleteClick = { settingViewModel.deleteSchedule(schedule) }
            )
            Spacer(
                modifier = Modifier
                    .height(8.dp) // Add spacing between items if needed
            )
        }
    }
}

@Composable
fun ScheduleItem(
    schedule: ScheduleDto,
    onScheduleClick: () -> Unit,
    onDeleteClick: () -> Unit,
    settingViewModel: SettingViewModel = hiltViewModel()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Display schedule information
            settingViewModel.getDetailTransaction(schedule.transactionDto)
            val transaction by settingViewModel.transaction.collectAsState()

            Text(
                text = "Transaction:",
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
            Text(
                text = "${transaction}",
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Repeat:",
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                color = Color.Gray
            )
            Text(
                text = "${schedule.timeSchedule} ${schedule.timeUnit}",
                style = MaterialTheme.typography.body1,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier
                        .padding(end = 8.dp)
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}