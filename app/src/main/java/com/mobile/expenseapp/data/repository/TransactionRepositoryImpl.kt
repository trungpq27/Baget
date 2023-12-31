package com.mobile.expenseapp.data.repository

import com.mobile.expenseapp.data.local.TransactionDao
import com.mobile.expenseapp.data.local.entity.AccountDto
import com.mobile.expenseapp.data.local.entity.LocalData
import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.data.local.entity.TransactionDto
import com.mobile.expenseapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val dao: TransactionDao) : TransactionRepository {
    override suspend fun syncDatabase(data: LocalData) {
        dao.syncDatabase(data)
    }

    override suspend fun eraseDatabase() {
        dao.eraseDatabase()
    }

    override suspend fun insertSchedules(schedules: List<ScheduleDto>) {
        dao.insertSchedules(schedules)
    }

    override suspend fun deleteSchedule(schedule: ScheduleDto) {
        dao.deleteSchedule(schedule)
    }

    override suspend fun insertTransactions(dailyExpense: List<TransactionDto>) {
        dao.insertTransactions(transaction = dailyExpense)
    }

    override suspend fun getTransactionByTimestamp(timestamp: Date): Flow<TransactionDto> {
        return dao.getTransactionByTimestamp(timestamp)
    }

    override suspend fun insertSchedule(schedule: ScheduleDto) {
        dao.insertSchedule(schedule)
    }

    override fun getAllSchedules(): Flow<List<ScheduleDto>> {
        return dao.getAllSchedules()
    }

    override fun updateSchedule(schedule: ScheduleDto) {
        dao.updateSchedule(schedule)
    }

    override fun eraseSchedules() {
        dao.eraseSchedules()
    }

    override suspend fun insertTransaction(dailyExpense: TransactionDto) {
        dao.insertTransaction(transaction = dailyExpense)
    }

    override suspend fun insertAccounts(accounts: List<AccountDto>) {
        dao.insertAccounts(accounts)
    }

    override fun eraseAccounts() {
        dao.eraseAccounts()
    }

    override fun getTransactionByAccount(accountType: String): Flow<List<TransactionDto>> {
        return dao.getTransactionByAccount(accountType)
    }

    override fun getDailyTransaction(entryDate: String): Flow<List<TransactionDto>> {
        return dao.getDailyTransaction(entryDate)
    }

    override fun getAccount(account: String): Flow<AccountDto> {
        return dao.getAccount(account)
    }

    override fun getAccounts(): Flow<List<AccountDto>> {
        return dao.getAccounts()
    }

    override fun getAllTransaction(): Flow<List<TransactionDto>> {
        return dao.getAllTransaction()
    }

    override fun eraseTransaction() {
        dao.eraseTransactions()
    }

    override fun getCurrentDayExpTransaction(): Flow<List<TransactionDto>> {
        return dao.getCurrentDayExpTransaction()
    }

    override fun getWeeklyExpTransaction(): Flow<List<TransactionDto>> {
        return dao.getWeeklyExpTransaction()
    }

    override fun getMonthlyExpTransaction(): Flow<List<TransactionDto>> {
        return dao.getMonthlyExpTransaction()
    }

    override fun get3DayTransaction(transactionType: String): Flow<List<TransactionDto>> {
        return dao.get3DayTransaction(transactionType)
    }

    override fun get7DayTransaction(transactionType: String): Flow<List<TransactionDto>> {
        return dao.get7DayTransaction(transactionType)
    }

    override fun get14DayTransaction(transactionType: String): Flow<List<TransactionDto>> {
        return dao.get14DayTransaction(transactionType)
    }

    override fun getStartOfMonthTransaction(transactionType: String): Flow<List<TransactionDto>> {
        return dao.getStartOfMonthTransaction(transactionType)
    }

    override fun getLastMonthTransaction(transactionType: String): Flow<List<TransactionDto>> {
        return dao.getLastMonthTransaction(transactionType)
    }

    override fun getTransactionByType(transactionType: String): Flow<List<TransactionDto>> {
        return dao.getTransactionByType(transactionType)
    }
}