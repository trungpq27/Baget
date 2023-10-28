package com.mobile.expenseapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.mobile.expenseapp.data.local.entity.AccountDto
import com.mobile.expenseapp.data.local.entity.TransactionDto
import com.mobile.expenseapp.presentation.home_screen.Account
import com.mobile.expenseapp.presentation.home_screen.Category
import com.mobile.expenseapp.presentation.home_screen.TransactionType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TransactionDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var transactionDatabase: TransactionDatabase
    lateinit var transactionDao: TransactionDao
    lateinit var newTrx: MutableList<TransactionDto>
    lateinit var accounts: List<AccountDto>
    lateinit var date: Date
    lateinit var dateOfEntry: String

    @Before
    fun setup() {
        transactionDatabase = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            TransactionDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        transactionDao = transactionDatabase.transactionDao
        newTrx = mutableListOf()
        for (i in 1..2) {
            date = Calendar.getInstance().time
            dateOfEntry = SimpleDateFormat("yyyy-MM-dd").format(date)
            newTrx.add(
                TransactionDto(
                    date = date,
                    dateOfEntry = dateOfEntry,
                    amount = 500.0,
                    account = "Cash",
                    category = Category.FOOD_DRINK.title,
                    transactionType = "expense",
                    title = "Lunch snack"
                )
            )
        }
        // refresh time
        date = Calendar.getInstance().time
        dateOfEntry = SimpleDateFormat("yyyy-MM-dd").format(date)
        newTrx.add(
            TransactionDto(
                date = date,
                dateOfEntry = dateOfEntry,
                amount = 500.0,
                account = "Cash",
                category = Category.FOOD_DRINK.title,
                transactionType = "income",
                title = "Lunch snack"
            )
        )
        accounts = listOf(
            AccountDto(1, "Cash", 0.0, 0.0, 0.0),
            AccountDto(2, "Card", 0.0, 0.0, 0.0),
            AccountDto(3, "Bank", 0.0, 0.0, 0.0)
        )
    }

    @After
    fun teardown() {
        transactionDatabase.close()
    }

    @Test
    fun `insert_transaction_adds_a_new_record_to_the_transaction_table`() = runBlocking {
        transactionDao.insertTransaction(newTrx[0])
        transactionDao.getAllTransaction().test {
            val trx = awaitItem()
            assertThat(trx.size).isEqualTo(1)
        }
    }

    @Test
    fun `insert_account_adds_a_new_record_to_the_account_table`() = runBlocking {
        transactionDao.insertAccounts(accounts)
        transactionDao.getAccounts().test {
            val allAccounts = awaitItem()
            assertThat(allAccounts.size).isEqualTo(3)
        }
    }

    @Test
    fun `retrieving_daily_transaction_record_returns_only_transactions_for_current_day`() =
        runBlocking {
            transactionDao.insertTransaction(transaction = newTrx[0])
            transactionDao.insertTransaction(transaction = newTrx[1])

            transactionDao.getDailyTransaction("2022/04/23").test {
                val trx = awaitItem()
                assertThat(trx.size).isEqualTo(1)
            }
        }

    @Test
    fun `retrieving_account_record_by_account_type_returns_only_the_specific_account`() =
        runBlocking {
            transactionDao.insertAccounts(accounts)
            transactionDao.getAccount(Account.CASH.title).test {
                val account = awaitItem()
                assertThat(account.accountType).isEqualTo(Account.CASH.title)
            }
        }

    @Test
    fun `retrieiving_transaction_record_by_transaction_type_returns_only_the_specific_transaction`() =
        runBlocking {
            transactionDao.insertTransaction(newTrx[0])
            transactionDao.insertTransaction(newTrx[1])
            transactionDao.insertTransaction(newTrx[2])
            transactionDao.getTransactionByType(TransactionType.EXPENSE.title).test {
                val trx = awaitItem()
                assertThat(trx.size).isEqualTo(2)
            }
        }

    @Test
    fun `retrieving_transactions_by_account_type_returns_only_transactions_performed_on_the_account`() =
        runBlocking {
            transactionDao.insertTransaction(newTrx[0])
            transactionDao.getTransactionByAccount(Account.CASH.title).test {
                val trx = awaitItem()
                assertThat(trx[0].account).isEqualTo(Account.CASH.title)
            }
        }

    @Test
    fun `retrieving_all_records_in_account_table`() = runBlocking {
        transactionDao.insertAccounts(accounts)
        transactionDao.getAccounts().test {
            val accounts = awaitItem()
            assertThat(accounts.size).isEqualTo(3)
        }
    }

    @Test
    fun `erase_all_transactions_clears_the_records_in_transaction_table`() = runBlocking {
        transactionDao.insertTransaction(newTrx[0])
        transactionDao.insertTransaction(newTrx[1])

        transactionDao.eraseTransaction()
        transactionDao.getAllTransaction().test {
            val trx = awaitItem()
            assertThat(trx.size).isEqualTo(0)
        }
    }

    @Test
    fun `retrieve_all_expense_transactions_performed_on_current_day`() = runBlocking {
        transactionDao.insertTransaction(newTrx[0])
        transactionDao.insertTransaction(newTrx[1])

        transactionDao.getCurrentDayExpTransaction().test {
            val trx = awaitItem()
            assertThat(trx.size).isEqualTo(2)
        }
    }

    @Test
    fun `retrieve_all_expense_transactions_performed_within_30_days`() = runBlocking {
        newTrx.clear()
        for (i in 1..5) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            date = calendar.time
            dateOfEntry = SimpleDateFormat("yyyy-MM-dd").format(date)
            newTrx.add(
                TransactionDto(
                    date = date,
                    dateOfEntry = dateOfEntry,
                    amount = 500.0,
                    account = "Cash",
                    category = Category.FOOD_DRINK.title,
                    transactionType = "expense",
                    title = "Lunch snack"
                )
            )
        }

        newTrx.forEach {
            transactionDao.insertTransaction(it)
        }
        transactionDao.getWeeklyExpTransaction().test {
            val trx = awaitItem()
            assertThat(trx.size).isEqualTo(5)
        }
    }

    @Test
    fun `retrieve_all_expense_transactions_performed_within_seven_days`() = runBlocking {
        newTrx.clear()
        for (i in 1..30) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            date = calendar.time
            dateOfEntry = SimpleDateFormat("yyyy-MM-dd").format(date)
            newTrx.add(
                TransactionDto(
                    date = date,
                    dateOfEntry = dateOfEntry,
                    amount = 500.0,
                    account = "Cash",
                    category = Category.FOOD_DRINK.title,
                    transactionType = "expense",
                    title = "Lunch snack"
                )
            )
        }

        newTrx.forEach {
            transactionDao.insertTransaction(it)
        }
        transactionDao.getWeeklyExpTransaction().test {
            val trx = awaitItem()
            assertThat(trx.size).isEqualTo(30)
        }
    }

    @Test
    fun `retrieve_all_transactions_performed_three_days_ago`() = runBlocking {
        newTrx.clear()
        for (i in 1..4) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            date = calendar.time
            dateOfEntry = SimpleDateFormat("yyyy-MM-dd").format(date)
            newTrx.add(
                TransactionDto(
                    date = date,
                    dateOfEntry = dateOfEntry,
                    amount = 500.0,
                    account = "Cash",
                    category = Category.FOOD_DRINK.title,
                    transactionType = "income",
                    title = "salary"
                )
            )
        }

        newTrx.forEach {
            transactionDao.insertTransaction(it)
        }
        transactionDao.get3DayTransaction(TransactionType.INCOME.title).test {
            val trx = awaitItem()
            assertThat(trx.size).isEqualTo(3)
        }
    }

    @Test
    fun `retrieve_all_transactions_performed_seven_days_ago`() = runBlocking {
        newTrx.clear()
        for (i in 1..8) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            date = calendar.time
            dateOfEntry = SimpleDateFormat("yyyy-MM-dd").format(date)
            newTrx.add(
                TransactionDto(
                    date = date,
                    dateOfEntry = dateOfEntry,
                    amount = 500.0,
                    account = "Cash",
                    category = Category.FOOD_DRINK.title,
                    transactionType = "income",
                    title = "salary"
                )
            )
        }

        newTrx.forEach {
            transactionDao.insertTransaction(it)
        }
        transactionDao.get7DayTransaction(TransactionType.INCOME.title).test {
            val trx = awaitItem()
            assertThat(trx.size).isEqualTo(7)
        }
    }

    @Test
    fun `retrieve_all_transactions_performed_fourteen_days_ago`() = runBlocking {
        newTrx.clear()
        for (i in 1..15) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            date = calendar.time
            dateOfEntry = SimpleDateFormat("yyyy-MM-dd").format(date)
            newTrx.add(
                TransactionDto(
                    date = date,
                    dateOfEntry = dateOfEntry,
                    amount = 500.0,
                    account = "Cash",
                    category = Category.FOOD_DRINK.title,
                    transactionType = "income",
                    title = "salary"
                )
            )
        }

        newTrx.forEach {
            transactionDao.insertTransaction(it)
        }
        transactionDao.get3DayTransaction(TransactionType.INCOME.title).test {
            val trx = awaitItem()
            assertThat(trx.size).isEqualTo(14)
        }
    }
}
