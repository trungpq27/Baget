package com.mobile.expenseapp.presentation.home_screen.service

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
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.google.accompanist.pager.ExperimentalPagerApi
import com.mobile.expenseapp.R
import com.mobile.expenseapp.presentation.main.MainActivity
import kotlinx.coroutines.InternalCoroutinesApi
import java.util.Calendar
import java.util.concurrent.TimeUnit

@InternalCoroutinesApi
@ExperimentalComposeUiApi
//@InternalCoroutinesApi
//@AndroidEntryPoint
@ExperimentalPagerApi
@ExperimentalUnitApi
@ExperimentalMaterialApi
@ExperimentalFoundationApi
@ExperimentalAnimationApi
class MyWorkManager(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        Log.d("Worker", "Successful")
        createNotification(
            applicationContext.getString(R.string.noti_title),
            applicationContext.getString(R.string.noti_content)
        )
        return Result.success()
    }

    private fun createNotification(title: String, description: String) {

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
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance()
            // Set Execution around 9pm
            dueDate.set(Calendar.HOUR_OF_DAY, 21)
            dueDate.set(Calendar.MINUTE, 0)
            dueDate.set(Calendar.SECOND, 0)
            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
            }
            val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
            val dailyWorkRequest = OneTimeWorkRequestBuilder<MyWorkManager>()
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
//                .addTag(TAG_OUTPUT)
                .build()

            val uniqueWorkName = "Notification"

            WorkManager.getInstance(context).enqueueUniqueWork(
                uniqueWorkName,
                ExistingWorkPolicy.REPLACE,
                dailyWorkRequest
            )
        }
    }
}
