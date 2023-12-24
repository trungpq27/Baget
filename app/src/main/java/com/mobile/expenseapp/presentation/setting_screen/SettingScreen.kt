package com.mobile.expenseapp.presentation.setting_screen

import LanguageContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mobile.expenseapp.R
import com.mobile.expenseapp.presentation.setting_screen.components.CurrencySetting
import com.mobile.expenseapp.presentation.setting_screen.components.DarkModeSetting
import com.mobile.expenseapp.presentation.setting_screen.components.EraseContent
import com.mobile.expenseapp.presentation.setting_screen.components.EraseSetting
import com.mobile.expenseapp.presentation.setting_screen.components.LanguageSetting
import com.mobile.expenseapp.presentation.setting_screen.components.LimitContent
import com.mobile.expenseapp.presentation.setting_screen.components.LimitSetting
import com.mobile.expenseapp.presentation.setting_screen.components.ReminderSetting
import com.mobile.expenseapp.presentation.setting_screen.components.ScheduleList
import com.mobile.expenseapp.presentation.setting_screen.components.ScheduleSetting
import com.mobile.expenseapp.util.spacing

@ExperimentalMaterialApi
@ExperimentalUnitApi
@Composable
fun SettingScreen(
    settingViewModel: SettingViewModel = hiltViewModel(),
    navController: NavController
) {

    val currency by settingViewModel.currency.collectAsState()
    val language by settingViewModel.language.collectAsState()
    val scheduleList by settingViewModel.scheduleList.collectAsState()

    val modalBottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden
    )
    val scope = rememberCoroutineScope()
    val sheetRankState = remember {
        mutableStateOf(0)
    }
    ModalBottomSheetLayout(
        sheetContent = {
            Box(Modifier.defaultMinSize(minHeight = 1.dp)) {
                when (sheetRankState.value) {
                    1 -> {
                        LimitContent(modalBottomSheetState, scope, navController)
                    }

                    2 -> {
                        EraseContent(modalBottomSheetState, scope, navController)
                    }

                    3 -> {
                        LanguageContent(modalBottomSheetState, scope, navController)
                    }

                    4 -> {
                        ScheduleList(modalBottomSheetState, scope, navController,onScheduleClick = {}, onDeleteClick = {})
                    }
                }
            }
        },

        sheetState = modalBottomSheetState,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Surface(color = MaterialTheme.colors.background) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(
                        text = navController.context.getString(R.string.nav_settings),
                        style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.W700),
                        color = MaterialTheme.colors.onSurface,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = MaterialTheme.spacing.medium,
                                vertical = MaterialTheme.spacing.small
                            ),
                        textAlign = TextAlign.Start
                    )

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                    ) {

                        CurrencySetting(currency, navController)
                        ScheduleSetting(modalBottomSheetState, scope, navController){
                            sheetRankState.value = it
                        }
                        LimitSetting(modalBottomSheetState, scope, navController) {
                            sheetRankState.value = it
                        }

                        ReminderSetting(navController)
                        DarkModeSetting(navController)
                        EraseSetting(modalBottomSheetState, scope, navController) {
                            sheetRankState.value = it
                        }
                    }
                }
            }
        }
    }
}

