package com.mobile.expenseapp.presentation.home_screen.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.mobile.expenseapp.data.local.entity.TransactionDto
import com.mobile.expenseapp.domain.usecase.write_database.InsertNewTransactionUseCase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.Date

@InternalCoroutinesApi
class MyAutoAddWorker (
    context: Context,
    params: WorkerParameters,
    private val insertNewTransactionUseCase: InsertNewTransactionUseCase
) : CoroutineWorker(context, params) {
    private suspend fun autoAdd() = runBlocking {
        val currentDate = Calendar.getInstance()
        val entryDate = Calendar.getInstance()
        entryDate.set(Calendar.HOUR_OF_DAY, 9)
        entryDate.set(Calendar.MINUTE, 0)
        entryDate.set(Calendar.SECOND, 0)

        if (entryDate.before(currentDate)) {
            entryDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        // Create a sample transaction
        val transaction = TransactionDto(
            date = Date(),
            dateOfEntry = "2023-24-01", // Adjust as needed
            amount = 100.0, // Adjust as needed
            account = "Cash", // Adjust as needed
            category = "Groceries", // Adjust as needed
            transactionType = "Expense",
            title = "Auto-added Entry"
        )

        // Use the use case to insert the transaction
        insertNewTransactionUseCase(transaction)
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
