package com.mobile.expenseapp.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import com.mobile.expenseapp.presentation.navigation.MainScreen
import com.mobile.expenseapp.presentation.ui.theme.ExpenseAppTheme
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@ExperimentalMaterialApi
@ExperimentalComposeUiApi
@InternalCoroutinesApi
@ExperimentalUnitApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainViewModel: MainViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ExpenseAppTheme {
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