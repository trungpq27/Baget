package com.mobile.expenseapp.presentation.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mobile.expenseapp.presentation.home_screen.service.MyAutoAddWorker
import com.mobile.expenseapp.presentation.home_screen.service.MyWorkManager
import com.mobile.expenseapp.presentation.home_screen.service.SyncWorker
import com.mobile.expenseapp.presentation.navigation.MainScreen
import com.mobile.expenseapp.presentation.ui.theme.BagetTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@ExperimentalComposeUiApi
@InternalCoroutinesApi
@AndroidEntryPoint
@ExperimentalPagerApi
@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MyWorkManager.notificationDailyTask(applicationContext)

        MyAutoAddWorker.createAutoAddWorkRequest(applicationContext)

        SyncWorker.schedulePeriodicWork(applicationContext)
        setContent {
            BagetTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val destination by mainViewModel.startDestination.collectAsState()
                    MainScreen(
                        startDestination = destination,
                    )
                }
            }
        }
    }
}