package com.mobile.expenseapp.domain.repository

import com.mobile.expenseapp.data.local.entity.AccountDto
import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.data.local.entity.TransactionDto
import kotlinx.coroutines.flow.Flow
import java.util.Date

interface TransactionRepository {
    suspend fun insertSchedules(schedules: List<ScheduleDto>)
    suspend fun insertTransactions(dailyExpense: List<TransactionDto>)
    suspend fun getTransactionByTimestamp(timestamp: Date): Flow<TransactionDto>
    suspend fun insertSchedule(schedule: ScheduleDto)
    fun updateSchedule(schedule: ScheduleDto)
    fun getAllSchedules(): Flow<List<ScheduleDto>>

    fun eraseSchedules()

    suspend fun insertTransaction(dailyExpense: TransactionDto)

    suspend fun insertAccounts(accounts: List<AccountDto>)
    fun eraseAccounts()

    fun getDailyTransaction(entryDate: String): Flow<List<TransactionDto>>

    fun getTransactionByAccount(accountType: String): Flow<List<TransactionDto>>

    fun getAccount(account: String): Flow<AccountDto>

    fun getAccounts(): Flow<List<AccountDto>>

    fun getAllTransaction(): Flow<List<TransactionDto>>

    fun eraseTransaction()

    fun getCurrentDayExpTransaction(): Flow<List<TransactionDto>>

    fun getWeeklyExpTransaction(): Flow<List<TransactionDto>>

    fun getMonthlyExpTransaction(): Flow<List<TransactionDto>>

    fun get3DayTransaction(transactionType: String): Flow<List<TransactionDto>>

    fun get7DayTransaction(transactionType: String): Flow<List<TransactionDto>>

    fun get14DayTransaction(transactionType: String): Flow<List<TransactionDto>>

    fun getStartOfMonthTransaction(transactionType: String): Flow<List<TransactionDto>>

    fun getLastMonthTransaction(transactionType: String): Flow<List<TransactionDto>>

    fun getTransactionByType(transactionType: String): Flow<List<TransactionDto>>
}