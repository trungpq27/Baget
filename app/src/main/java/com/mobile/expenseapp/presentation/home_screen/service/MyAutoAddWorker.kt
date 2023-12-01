package com.mobile.expenseapp.presentation.home_screen.service

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.mobile.expenseapp.common.Constants
import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.data.local.entity.TransactionDto
import com.mobile.expenseapp.domain.usecase.read_database.GetAccountUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetAllScheduleUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetTransactionByTimestampUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertAccountsUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertNewTransactionUseCase
import com.mobile.expenseapp.domain.usecase.write_database.UpdateScheduleUseCase
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

@InternalCoroutinesApi
class MyAutoAddWorker (
    context: Context,
    params: WorkerParameters,
    private val insertNewTransactionUseCase: InsertNewTransactionUseCase,
    private val getTransactionByTimestampUseCase: GetTransactionByTimestampUseCase,
    private val getAllScheduleUseCase: GetAllScheduleUseCase,
    private val updateScheduleUseCase: UpdateScheduleUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val insertAccountsUseCase: InsertAccountsUseCase,
) : CoroutineWorker(context, params) {
    private suspend fun autoAdd() {
        val schedules = getAllScheduleUseCase()

        schedules.collect { scheduleList ->
            Log.d("AutoAdd", "All Schedules: $scheduleList")
            if (scheduleList != null) {
            // Iterate through schedules and insert transactions as needed
                for (schedule in scheduleList) {
                    if (isTimeToAddTransaction(schedule)) {
                        //Create a sample transaction or retrieve it as needed
                        val transaction = createTransaction(getTransactionByTimestampUseCase(schedule.transactionDto).first())

                        // Insert the transaction
                        insertNewTransactionUseCase(transaction)
                        handelAccount(transaction)

                        // Update the lastTimeAdded in the schedule
                        updateLastTimeAdded(schedule, Calendar.getInstance().time)

                        Log.d("AutoAdd", "Transaction added: $transaction")
                    }
                }
            }
        }
    }

    private suspend fun handelAccount(transactionDto: TransactionDto) {
        val currentAccount = getAccountUseCase(transactionDto.account).first()
        val newAmount = if (transactionDto.transactionType == Constants.INCOME) {
            currentAccount.income + transactionDto.amount
        } else {
            currentAccount.expense + transactionDto.amount
        }
        val balance = if (transactionDto.transactionType == Constants.INCOME) {
            newAmount - currentAccount.expense
        } else {
            currentAccount.income - newAmount
        }

        currentAccount.apply {
            if (transactionDto.transactionType == Constants.INCOME) {
                income = newAmount
            } else {
                expense = newAmount
            }
            this.balance = balance
        }
        insertAccountsUseCase(listOf(currentAccount))
    }
    private fun updateLastTimeAdded(schedule: ScheduleDto, newTime: Date) {
        // Update the lastTimeAdded in the schedule
        val updatedSchedule = schedule.copy(lastTimeAdded = newTime)
        // Update the schedule in the database
        updateScheduleUseCase(updatedSchedule)
    }
    private fun isTimeToAddTransaction(schedule: ScheduleDto): Boolean {
        // Implement your logic to check if it's time to add a transaction
        // For example, compare the current time with the lastTimeAdded and timeSchedule
        val currentTime = Calendar.getInstance().timeInMillis
        val lastTimeAdded = schedule.lastTimeAdded.time
        val timeSchedule = TimeUnit.valueOf(schedule.timeUnit).toMillis(schedule.timeSchedule)
        return currentTime - lastTimeAdded >= timeSchedule
    }

    private fun createTransaction(transactionDto: TransactionDto): TransactionDto {
        // Implement your logic to create a sample transaction
        return TransactionDto(
            date = Date(),
            dateOfEntry = SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().time), // Adjust as needed
            amount = transactionDto.amount, // Adjust as needed
            account = transactionDto.account, // Adjust as needed
            category = transactionDto.category, // Adjust as needed
            transactionType = transactionDto.transactionType,
            title = Date().toString()
        )
    }

    override suspend fun doWork(): Result {
        Log.d("AutoAdd", "successful")
        delay(1000)
        autoAdd()
        return Result.success()
    }


    companion object {
        fun createAutoAddWorkRequest(context: Context) {
            val currentDate = Calendar.getInstance()
            val dueDate = Calendar.getInstance()
            // Set Execution around 9pm
            dueDate.set(Calendar.HOUR_OF_DAY, 0)
            dueDate.set(Calendar.MINUTE, 0)
            dueDate.set(Calendar.SECOND, 0)
            if (dueDate.before(currentDate)) {
                dueDate.add(Calendar.HOUR_OF_DAY, 24)
            }
            val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis
            val uniqueWorkName = "MyAutoAddWorker"
            val autoAddWorkRequest = OneTimeWorkRequestBuilder<MyAutoAddWorker>()
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)  // Set the delay to start the worker
                .addTag(uniqueWorkName)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                uniqueWorkName,
                ExistingWorkPolicy.REPLACE,
                autoAddWorkRequest
            )
        }
    }
}


