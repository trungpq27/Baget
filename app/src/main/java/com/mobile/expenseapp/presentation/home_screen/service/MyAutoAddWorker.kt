package com.mobile.expenseapp.presentation.home_screen.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.data.local.entity.TransactionDto
import com.mobile.expenseapp.domain.usecase.read_database.GetAllScheduleUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetTransactionByTimestampUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertNewScheduleUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertNewTransactionUseCase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

@InternalCoroutinesApi
class MyAutoAddWorker (
    context: Context,
    params: WorkerParameters,
    private val insertNewTransactionUseCase: InsertNewTransactionUseCase,
    private val getTransactionByTimestampUseCase: GetTransactionByTimestampUseCase,
    private val getAllScheduleUseCase: GetAllScheduleUseCase
) : CoroutineWorker(context, params) {
    private suspend fun autoAdd() = runBlocking {
        val schedules = getAllScheduleUseCase()

        schedules.collect { scheduleList ->
            Log.d("AutoAdd", "All Schedules: $scheduleList")

            if (scheduleList != null) {
            // Iterate through schedules and insert transactions as needed
                for (schedule in scheduleList) {
                    if (true) {
//                        Create a sample transaction or retrieve it as needed
                        val transaction = getTransactionByTimestampUseCase(schedule.transactionDto).first()

                        // Insert the transaction
                        insertNewTransactionUseCase(transaction)

                        // Update the lastTimeAdded in the schedule
                        updateLastTimeAdded(schedule, Calendar.getInstance().time)

                        Log.d("AutoAdd", "Transaction added: $transaction")
                    }
                }
            }
        }
    }
    private fun updateLastTimeAdded(schedule: ScheduleDto, newTime: Date) {
        // Update the lastTimeAdded in the schedule
        val updatedSchedule = schedule.copy(lastTimeAdded = newTime)
        // Update the schedule in the database
//        updateScheduleUseCase(updatedSchedule)
    }
//    private fun isTimeToAddTransaction(schedule: ScheduleDto): Boolean {
//        // Implement your logic to check if it's time to add a transaction
//        // For example, compare the current time with the lastTimeAdded and timeSchedule
//        val currentTime = Calendar.getInstance().timeInMillis
//        val lastTimeAdded = schedule.lastTimeAdded.time
//        val timeSchedule = TimeUnit.valueOf(schedule.timeUnit).toMillis(schedule.timeSchedule)
//        return currentTime - lastTimeAdded >= timeSchedule
//    }

    private fun createSampleTransaction(transactionTimestamp: Date): TransactionDto {
        // Implement your logic to create a sample transaction
        return TransactionDto(
            date = transactionTimestamp,
            dateOfEntry = "2023-24-01", // Adjust as needed
            amount = 600.0, // Adjust as needed
            account = "Cash", // Adjust as needed
            category = "Groceries", // Adjust as needed
            transactionType = "Income",
            title = "Auto-added Entry"
        )
    }

    override suspend fun doWork(): Result {
        Log.d("AutoAdd", "successful")
        autoAdd()
        return Result.success()
    }


    companion object {
        fun createAutoAddWorkRequest(context: Context) {
            val autoAddWorkRequest = OneTimeWorkRequestBuilder<MyAutoAddWorker>().build()
            WorkManager.getInstance(context).enqueue(autoAddWorkRequest)
        }
    }
}


