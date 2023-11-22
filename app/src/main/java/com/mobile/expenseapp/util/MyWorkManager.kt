package com.mobile.expenseapp.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mobile.expenseapp.R
import com.mobile.expenseapp.presentation.main.MainActivity
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.Calendar
import java.util.concurrent.TimeUnit

@ExperimentalComposeUiApi
@InternalCoroutinesApi
@ExperimentalPagerApi
@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
class MyWorkManager(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("Worker", "Successful")
        createNotification(
            applicationContext.getString(R.string.noti_title),
            applicationContext.getString(R.string.noti_content)
        )
        return Result.success()
    }

    fun createNotification(title: String, description: String) {

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Notification channel creation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel("101", "channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        // Intent to open your activity when the notification is tapped
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Notification creation with the PendingIntent
        val notificationBuilder = NotificationCompat.Builder(applicationContext, "101")
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent) // Set the PendingIntent
            .setAutoCancel(true)

        // Notify with the unique ID
        notificationManager.notify(1, notificationBuilder.build())
    }

    companion object {
        fun notificationDailyTask(context: Context) {
            val now = System.currentTimeMillis()
            val ninePM = getNinePMInMillis(now)
            val initialDelay = ninePM - now

            // Create a periodic work request with an initial delay
            val workRequest = PeriodicWorkRequestBuilder<MyWorkManager>(
                repeatInterval = 1, // repeatInterval in days
                repeatIntervalTimeUnit = TimeUnit.DAYS
            )
                .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                .build()

            // Enqueue the work request
            WorkManager.getInstance(context).enqueue(workRequest)
        }

        private fun getNinePMInMillis(currentTimeMillis: Long): Long {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = currentTimeMillis
            calendar.set(Calendar.HOUR_OF_DAY, 21) // 9 PM
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            return calendar.timeInMillis
        }
    }
}
