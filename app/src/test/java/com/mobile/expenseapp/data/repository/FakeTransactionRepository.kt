package com.mobile.expenseapp.data.repository

import com.mobile.expenseapp.data.local.entity.AccountDto
import com.mobile.expenseapp.data.local.entity.LocalData
import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.data.local.entity.TransactionDto
import com.mobile.expenseapp.domain.repository.TransactionRepository
import com.mobile.expenseapp.presentation.home_screen.Account
import com.mobile.expenseapp.presentation.home_screen.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Calendar
import java.util.Date

class FakeTransactionRepository : TransactionRepository {
    private val date: Date = Calendar.getInstance().time
    private val trxList = listOf(
        TransactionDto(
            date,
            "2022-04-28",
            500.0,
            Account.CASH.title,
            Category.FOOD_DRINK.title,
            "expense",
            ""
        ),
        TransactionDto(
            date,
            "2022-04-28",
            200.0,
            Account.BANK.title,
            Category.MISC.title,
            "expense",
            ""
        )
    )
    private val accList = listOf(
        AccountDto(1, Account.CASH.title, 5.0, 10.0, 5.0),
        AccountDto(2, Account.CARD.title, 5.0, 10.0, 5.0),
        AccountDto(3, Account.BANK.title, 5.0, 10.0, 5.0)
    )

    override suspend fun syncDatabase(data: LocalData) {
        TODO("Not yet implemented")
    }

    override suspend fun eraseDatabase() {
        TODO("Not yet implemented")
    }

    override suspend fun insertSchedules(schedules: List<ScheduleDto>) {
    }

    override suspend fun insertTransactions(dailyExpense: List<TransactionDto>) {
        return
    }

    override suspend fun getTransactionByTimestamp(timestamp: Date): Flow<TransactionDto> {
        TODO("Not yet implemented")
    }

    private val scheduleList = listOf<ScheduleDto>(

    )

    override suspend fun insertTransaction(dailyExpense: TransactionDto) {
        return
    }

    override suspend fun insertAccounts(accounts: List<AccountDto>) {
        return
    }

    override fun eraseAccounts() {
        TODO("Not yet implemented")
    }

    override suspend fun insertSchedule(schedule: ScheduleDto) {
        return
    }

    override fun updateSchedule(schedule: ScheduleDto) {
    }

    override fun getDailyTransaction(entryDate: String): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }

    override fun getTransactionByAccount(accountType: String): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }

    override fun getAccount(account: String): Flow<AccountDto> {
        return flow {
            emit(accList[0])
        }
    }

    override fun getAccounts(): Flow<List<AccountDto>> {
        return flow {
            emit(accList)
        }
    }

    override fun getAllTransaction(): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }

    override fun getAllSchedules(): Flow<List<ScheduleDto>> {
        return flow {
            emit(scheduleList)
        }
    }

    override fun eraseTransaction() {

    }

    override fun eraseSchedules() {

    }

    override suspend fun deleteSchedule(schedule: ScheduleDto) {
        TODO("Not yet implemented")
    }

    override fun getCurrentDayExpTransaction(): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }

    override fun getWeeklyExpTransaction(): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }

    override fun getMonthlyExpTransaction(): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }

    override fun get3DayTransaction(transactionType: String): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }

    override fun get7DayTransaction(transactionType: String): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }

    override fun get14DayTransaction(transactionType: String): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }

    override fun getStartOfMonthTransaction(transactionType: String): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }

    override fun getLastMonthTransaction(transactionType: String): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }

    override fun getTransactionByType(transactionType: String): Flow<List<TransactionDto>> {
        return flow {
            emit(trxList)
        }
    }
}