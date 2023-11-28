package com.mobile.expenseapp.di

import android.app.Application
import androidx.work.Configuration
import com.mobile.expenseapp.presentation.home_screen.service.MyAutoAddWorker
import com.mobile.expenseapp.presentation.home_screen.service.MyWorkManager.Companion.notificationDailyTask
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

        // Perform application-level initializations here

        // Example: Schedule a daily notification task
        notificationDailyTask(applicationContext)

        // Example: Create a one-time work request for auto-adding entries
        MyAutoAddWorker.createAutoAddWorkRequest(applicationContext)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(factory)
            .build()
    }
}
