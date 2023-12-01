package com.mobile.expenseapp.di

import android.app.Application
import androidx.work.Configuration
import com.mobile.expenseapp.presentation.home_screen.service.SyncWorker
import com.mobile.expenseapp.presentation.setting_screen.service.ResetWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.InternalCoroutinesApi
import javax.inject.Inject

@HiltAndroidApp
@InternalCoroutinesApi
class BagetApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var factory: ResetWorkerFactory

    override fun onCreate() {
        super.onCreate()

//        // Example: Schedule a daily notification task
//        notificationDailyTask(applicationContext)
//
//        // Example: Create a one-time work request for auto-adding entries
//        createAutoAddWorkRequest(applicationContext)

        SyncWorker.schedulePeriodicWork(applicationContext)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(factory)
            .build()
    }
}
