package com.mobile.expenseapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mobile.expenseapp.common.Constants
import com.mobile.expenseapp.data.local.entity.AccountDto
import com.mobile.expenseapp.data.local.entity.LocalData
import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.data.local.entity.TransactionDto
import kotlinx.coroutines.flow.Flow
import java.util.Date

@Dao
interface TransactionDao {
    @Transaction
    suspend fun syncDatabase(data: LocalData){
        eraseDatabase()
        insertTransactions(data.transactions)
        insertSchedules(data.schedules)
        insertAccounts(data.accounts)
    }

    @Transaction
    suspend fun eraseDatabase(){
        eraseAccounts()
        eraseSchedules()
        eraseTransactions()
    }
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedules(transaction: List<ScheduleDto>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transaction: List<TransactionDto>)

    @Query("SELECT * FROM transaction_table WHERE timestamp = :transactionTimestamp")
    fun getTransactionByTimestamp(transactionTimestamp: Date): Flow<TransactionDto>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: ScheduleDto)

    @Query("SELECT * FROM schedule_table")
    fun getAllSchedules(): Flow<List<ScheduleDto>>

    @Update
    fun updateSchedule(schedule: ScheduleDto)

    @Delete
    fun deleteSchedule(schedule: ScheduleDto)

    @Query("DELETE FROM schedule_table")
    fun eraseSchedules()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionDto)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<AccountDto>)

    @Query("DELETE FROM account_table")
    fun eraseAccounts()

    @Query("SELECT * FROM transaction_table WHERE entry_date = :entryDate")
    fun getDailyTransaction(entryDate: String): Flow<List<TransactionDto>>

    @Query("SELECT * FROM transaction_table WHERE account = :accountType")
    fun getTransactionByAccount(accountType: String): Flow<List<TransactionDto>>

    @Query("SELECT * FROM account_table WHERE account = :account")
    fun getAccount(account: String): Flow<AccountDto>

    @Query("SELECT * FROM account_table")
    fun getAccounts(): Flow<List<AccountDto>>

    @Query("SELECT * FROM transaction_table")
    fun getAllTransaction(): Flow<List<TransactionDto>>

    @Query("DELETE FROM transaction_table")
    fun eraseTransactions()

    @Query("SELECT * FROM transaction_table WHERE entry_date = date('now', 'localtime') AND transaction_type = :transactionType")
    fun getCurrentDayExpTransaction(transactionType: String = Constants.EXPENSE): Flow<List<TransactionDto>>

    @Query("SELECT * FROM transaction_table WHERE entry_date BETWEEN date('now', '-7 day') AND date('now', 'localtime') AND transaction_type = :transactionType")
    fun getWeeklyExpTransaction(transactionType: String = Constants.EXPENSE): Flow<List<TransactionDto>>

    @Query("SELECT * FROM transaction_table WHERE entry_date BETWEEN date('now', '-1 month') AND date('now', 'localtime') AND transaction_type = :transactionType")
    fun getMonthlyExpTransaction(transactionType: String = Constants.EXPENSE): Flow<List<TransactionDto>>

    @Query("SELECT * FROM transaction_table WHERE entry_date BETWEEN date('now', '-3 day') AND date('now', 'localtime') AND transaction_type = :transactionType")
    fun get3DayTransaction(transactionType: String): Flow<List<TransactionDto>>

    @Query("SELECT * FROM transaction_table WHERE entry_date BETWEEN date('now', '-7 day') AND date('now', 'localtime') AND transaction_type = :transactionType")
    fun get7DayTransaction(transactionType: String): Flow<List<TransactionDto>>

    @Query("SELECT * FROM transaction_table WHERE entry_date BETWEEN date('now', '-14 day') AND date('now', 'localtime') AND transaction_type = :transactionType")
    fun get14DayTransaction(transactionType: String): Flow<List<TransactionDto>>

    @Query("SELECT * FROM transaction_table WHERE entry_date BETWEEN date('now', 'start of month') AND date('now', 'localtime') AND transaction_type = :transactionType")
    fun getStartOfMonthTransaction(transactionType: String): Flow<List<TransactionDto>>

    @Query("SELECT * FROM transaction_table WHERE entry_date BETWEEN date('now', '-1 month') AND date('now', 'start of month') AND transaction_type = :transactionType")
    fun getLastMonthTransaction(transactionType: String): Flow<List<TransactionDto>>

    @Query("SELECT * FROM transaction_table WHERE transaction_type = :transactionType")
    fun getTransactionByType(transactionType: String): Flow<List<TransactionDto>>
}
