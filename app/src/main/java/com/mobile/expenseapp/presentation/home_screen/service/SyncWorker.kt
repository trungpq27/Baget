package com.mobile.expenseapp.presentation.home_screen.service

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
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
            val workManager = WorkManager.getInstance(context)
            val existingWorkInfo = workManager.getWorkInfosByTag("baget_sync").get()

            if (existingWorkInfo.isNullOrEmpty()) {
                val constraints =
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                val periodicWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(
                    repeatInterval = 30, repeatIntervalTimeUnit = TimeUnit.SECONDS
                ).setConstraints(constraints).addTag("baget_sync").build()

                workManager.enqueue(periodicWorkRequest)
            }
        }
    }
}