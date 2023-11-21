package com.mobile.expenseapp.presentation.main

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.work.OneTimeWorkRequest
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.mobile.expenseapp.util.MyWorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG: String = MainActivity::class.java.simpleName

    private lateinit var workManager: WorkManager
    private lateinit var timeWorkRequest: OneTimeWorkRequest
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    private val PERIODIC_REQUEST_TAG: String = "periodic_request"
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        workManager = WorkManager.getInstance(applicationContext)
        createButtons()
    }

    private fun createButtons() {
        btnStart = Button(this)
        btnStart.text = "Start One-Time Work"
        btnStart.setOnClickListener(this)

        btnStop = Button(this)
        btnStop.text = "Start Periodic Work"
        btnStop.setOnClickListener(this)

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val linearLayout = LinearLayout(this)
        linearLayout.orientation = LinearLayout.VERTICAL
        linearLayout.addView(btnStart, layoutParams)
        linearLayout.addView(btnStop, layoutParams)

        setContentView(linearLayout)
    }

    override fun onClick(view: View?) {
        when (view) {
            btnStart -> setupOneTimeWorker()
            btnStop -> if (!isScheduleWork(PERIODIC_REQUEST_TAG)) setupPeriodicWorker()
        }
    }

    private fun setupOneTimeWorker() {
        timeWorkRequest = OneTimeWorkRequest.Builder(MyWorkManager::class.java).build()
        workManager.enqueue(timeWorkRequest)
    }

    private fun setupPeriodicWorker() {
        val work = PeriodicWorkRequest.Builder(
            MyWorkManager::class.java,
            15,
            TimeUnit.MINUTES
        )
        periodicWorkRequest = work.build()
        workManager.enqueue(periodicWorkRequest)
    }

    private fun isScheduleWork(tag: String): Boolean {
        val statuses = workManager.getWorkInfosByTag(tag)
        if (statuses.get().isEmpty()) return false
        var running = false

        for (state in statuses.get()) {
            running =
                state.state == WorkInfo.State.RUNNING || state.state == WorkInfo.State.ENQUEUED
        }
        return running
    }
}


//package com.mobile.expenseapp.presentation.main
//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.compose.animation.ExperimentalAnimationApi
//import androidx.compose.foundation.ExperimentalFoundationApi
//import androidx.compose.material.ExperimentalMaterialApi
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.Surface
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.ExperimentalComposeUiApi
//import androidx.compose.ui.unit.ExperimentalUnitApi
//import androidx.work.OneTimeWorkRequestBuilder
//import androidx.work.WorkManager
//import com.google.accompanist.pager.ExperimentalPagerApi
//import com.mobile.expenseapp.presentation.navigation.MainScreen
//import com.mobile.expenseapp.presentation.ui.theme.BagetTheme
//import com.mobile.expenseapp.util.MyWorkManager
//import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.coroutines.InternalCoroutinesApi
//import javax.inject.Inject
//
//@ExperimentalComposeUiApi
//@InternalCoroutinesApi
//@AndroidEntryPoint
//@ExperimentalPagerApi
//@ExperimentalUnitApi
//@ExperimentalMaterialApi
//@ExperimentalFoundationApi
//@ExperimentalAnimationApi
//class MainActivity : ComponentActivity() {
//
//    @Inject
//    lateinit var mainViewModel: MainViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        // Create a one-time work request
//        val workRequest = OneTimeWorkRequestBuilder<MyWorkManager>().build()
//
//        // Enqueue the work request
//        WorkManager.getInstance(this).enqueue(workRequest)
//
//
//
//        setContent {
//            BagetTheme {
//                Surface(color = MaterialTheme.colors.background) {
//                    val destination by mainViewModel.startDestination.collectAsState()
//                    MainScreen(
//                        startDestination = destination,
//                    )
//                }
//            }
//        }
//    }
//}