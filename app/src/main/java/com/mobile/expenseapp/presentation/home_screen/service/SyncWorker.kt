package com.mobile.expenseapp.presentation.home_screen.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.mobile.expenseapp.domain.usecase.read_database.GetLocalDataUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetLoginTokenUseCase
import com.mobile.expenseapp.service.APIRepository
import java.util.concurrent.TimeUnit

class SyncWorker(
    context: Context,
    params: WorkerParameters,
    private val getLocalDataUseCase: GetLocalDataUseCase,
    private val getLoginTokenUseCase: GetLoginTokenUseCase,
) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        doSync()
        return Result.success()
    }

    private suspend fun doSync() {
        getLoginTokenUseCase().collect { token ->
            if (token != null) {
                getLocalDataUseCase().collect { localData ->
                    APIRepository.syncPost(token, localData)
                }
            } else {
                Log.d("sync", "Not login")
            }
        }
    }

    companion object {
        fun schedulePeriodicWork(context: Context) {
            val periodicWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
                repeatInterval = 10, repeatIntervalTimeUnit = TimeUnit.MINUTES
            ).build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "Sync",
                ExistingPeriodicWorkPolicy.KEEP,
                periodicWorkRequest
            )
        }
    }
}