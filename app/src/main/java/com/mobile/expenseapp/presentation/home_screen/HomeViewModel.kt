package com.mobile.expenseapp.presentation.home_screen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.expenseapp.R
import com.mobile.expenseapp.common.Constants
import com.mobile.expenseapp.data.local.entity.ScheduleDto
import com.mobile.expenseapp.data.local.entity.TransactionDto
import com.mobile.expenseapp.domain.model.Transaction
import com.mobile.expenseapp.domain.usecase.GetDateUseCase
import com.mobile.expenseapp.domain.usecase.GetFormattedDateUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetAccountUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetAccountsUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetAllTransactionUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetCurrentDayExpTransactionUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetDailyTransactionUseCase
import com.mobile.expenseapp.domain.usecase.read_database.GetMonthlyExpTransactionUse
import com.mobile.expenseapp.domain.usecase.read_database.GetWeeklyExpTransactionUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetCurrencyUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetExpenseLimitUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetLimitDurationUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetLimitKeyUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertAccountsUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertNewScheduleUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertNewTransactionUseCase
import com.mobile.expenseapp.presentation.ui.theme.businessBg
import com.mobile.expenseapp.presentation.ui.theme.clothBg
import com.mobile.expenseapp.presentation.ui.theme.electricBg
import com.mobile.expenseapp.presentation.ui.theme.food_drink
import com.mobile.expenseapp.presentation.ui.theme.gadgetBg
import com.mobile.expenseapp.presentation.ui.theme.giftBg
import com.mobile.expenseapp.presentation.ui.theme.groceryBg
import com.mobile.expenseapp.presentation.ui.theme.healthBg
import com.mobile.expenseapp.presentation.ui.theme.homeBg
import com.mobile.expenseapp.presentation.ui.theme.leisureBg
import com.mobile.expenseapp.presentation.ui.theme.miscBg
import com.mobile.expenseapp.presentation.ui.theme.petBg
import com.mobile.expenseapp.presentation.ui.theme.schBg
import com.mobile.expenseapp.presentation.ui.theme.snackBg
import com.mobile.expenseapp.presentation.ui.theme.subBg
import com.mobile.expenseapp.presentation.ui.theme.taxiBg
import com.mobile.expenseapp.presentation.ui.theme.travelBg
import com.mobile.expenseapp.presentation.ui.theme.vehicleBg
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DecimalFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getDateUseCase: GetDateUseCase,
    private val getFormattedDateUseCase: GetFormattedDateUseCase,
    private val insertDailyTransactionUseCase: InsertNewTransactionUseCase,
    private val insertAccountsUseCase: InsertAccountsUseCase,
    private val getDailyTransactionUseCase: GetDailyTransactionUseCase,
    private val getAllTransactionUseCase: GetAllTransactionUseCase,
    private val getAccountUseCase: GetAccountUseCase,
    private val getAccountsUseCase: GetAccountsUseCase,
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val getExpenseLimitUseCase: GetExpenseLimitUseCase,
    private val getLimitDurationUseCase: GetLimitDurationUseCase,
    private val getLimitKeyUseCase: GetLimitKeyUseCase,
    private val getCurrentDayExpTransactionUseCase: GetCurrentDayExpTransactionUseCase,
    private val getWeeklyExpTransactionUseCase: GetWeeklyExpTransactionUseCase,
    private val getMonthlyExpTransactionUse: GetMonthlyExpTransactionUse,
    private val insertNewScheduleUseCase: InsertNewScheduleUseCase
) : ViewModel() {

    private var decimal: String = String()
    private var isDecimal = MutableStateFlow(false)
    private var duration = MutableStateFlow(0)

    var tabButton = MutableStateFlow(TabButton.TODAY)
        private set

    var transactionType = MutableStateFlow(TransactionType.INCOME)
        private set

    var category = MutableStateFlow(Category.FOOD_DRINK)
        private set

    var account = MutableStateFlow(Account.CASH)
        private set

    var transactionAmount = MutableStateFlow("0.00")
        private set

    var dailyTransaction = MutableStateFlow(emptyList<Transaction>())
        private set

    var monthlyTransaction = MutableStateFlow(mapOf<String, List<Transaction>>())
        private set

    var currentExpenseAmount = MutableStateFlow(0.0)
        private set

    var transactionTitle = MutableStateFlow(String())
        private set

    var scheduleTime = MutableStateFlow(0)
        private set

    var showInfoBanner = MutableStateFlow(false)
        private set

    var addAuto = MutableStateFlow(false)
        private set

    var totalIncome = MutableStateFlow(0.0)
        private set

    var totalExpense = MutableStateFlow(0.0)
        private set

    var formattedDate = MutableStateFlow(String())
        private set

    var date = MutableStateFlow(String())
        private set

    private var currentTime = MutableStateFlow(Calendar.getInstance().time)

    var selectedCurrencyCode = MutableStateFlow(String())
        private set

    var limitAlert = MutableSharedFlow<UIEvent>(replay = 1)
        private set

    var limitKey = MutableStateFlow(false)
        private set

    init {
        val currentDate = getDateUseCase()
        formattedDate.value = getFormattedDateUseCase(currentTime.value)
        date.value = currentDate
        currencyFormat()

        viewModelScope.launch(IO) {
            getLimitDurationUseCase().collect { pref ->
                duration.value = pref
            }
        }

        viewModelScope.launch(IO) {
            getLimitKeyUseCase().collectLatest { pref ->
                limitKey.value = pref
            }
        }

        viewModelScope.launch(IO) {
            when (duration.value) {
                0 -> {
                    getCurrentDayExpTransactionUseCase().collect { result ->
                        val trx = result.map { trans -> trans.toTransaction() }
                        currentExpenseAmount.value = calculateTransaction(trx.map { it.amount })
                    }
                }

                1 -> {
                    getWeeklyExpTransactionUseCase().collect { result ->
                        val trx = result.map { trans -> trans.toTransaction() }
                        currentExpenseAmount.value = calculateTransaction(trx.map { it.amount })
                    }
                }

                else -> {
                    getMonthlyExpTransactionUse().collect { result ->
                        val trx = result.map { trans -> trans.toTransaction() }
                        currentExpenseAmount.value = calculateTransaction(trx.map { it.amount })
                    }
                }
            }

        }

        viewModelScope.launch(IO) {
            getDailyTransactionUseCase(currentDate).collect {
                it?.let { expenses ->
                    dailyTransaction.value =
                        expenses.map { dailyExpense -> dailyExpense.toTransaction() }.reversed()
                }
            }
        }

        viewModelScope.launch(IO) {
            getAllTransactionUseCase().collect { allTransaction ->
                allTransaction?.let {
                    val allSortedTrx = allTransaction.map { it.toTransaction() }.reversed()
                    monthlyTransaction.value = allSortedTrx.groupBy { monthlyExpense ->
                        getFormattedDateUseCase(monthlyExpense.date)
                    }
                }
            }
        }

        viewModelScope.launch(IO) {
            getAccountsUseCase().collect { accountsDto ->
                val accounts = accountsDto.map { it.toAccount() }
                val income = calculateTransaction(accounts.map { it.income })
                val expense = calculateTransaction(accounts.map { it.expense })

                totalIncome.value = income
                totalExpense.value = expense
            }
        }
    }

    private fun calculateTransaction(transactions: List<Double>): Double {
        return transactions.sumOf {
            it
        }
    }

    fun selectTabButton(button: TabButton) {
        tabButton.value = button
    }

    fun selectTransactionType(button: TransactionType) {
        transactionType.value = button
    }

    fun selectCategory(category: Category) {
        this.category.value = category
    }

    fun selectAccount(account: Account) {
        this.account.value = account
    }

    fun setTransactionTitle(title: String) {
        transactionTitle.value = title
    }

    fun setTimeSchedule(time: Int) {
        scheduleTime.value = time
    }

    fun setCurrentTime(time: Date) {
        currentTime.value = time
    }

    fun setAutoAdd(newState: Boolean) {
        addAuto.value = newState
    }

    fun insertDailyTransaction(
        date: String,
        amount: Double,
        category: String,
        transactionType: String,
        transactionTitle: String,
        navigateBack: () -> Unit
    ) {
        viewModelScope.launch(IO) {
            if (amount <= 0.0) {
                showInfoBanner.value = true
                delay(2000)
                showInfoBanner.value = false
                return@launch
            }

            val newTransaction = TransactionDto(
                currentTime.value,
                date,
                amount,
                account.value.title,
                category,
                transactionType,
                transactionTitle
            )
            insertDailyTransactionUseCase(newTransaction)
            val checkAuto = addAuto.value
            if (checkAuto) {
                val schedule = ScheduleDto(newTransaction.date,scheduleTime.value.toLong(), TimeUnit.DAYS.toString(), newTransaction.date )
                insertNewScheduleUseCase(schedule)
            }

            if (transactionType == Constants.INCOME) {
                val currentAccount = getAccountUseCase(account.value.title).first()
                val newIncomeAmount = currentAccount.income + amount
                val balance = newIncomeAmount - currentAccount.expense

                currentAccount.income = newIncomeAmount
                currentAccount.balance = balance

                insertAccountsUseCase(listOf(currentAccount))
            } else {
                val currentAccount = getAccountUseCase(account.value.title).first()
                val newExpenseAmount = currentAccount.expense + amount
                val balance = currentAccount.income - newExpenseAmount

                currentAccount.expense = newExpenseAmount
                currentAccount.balance = balance

                insertAccountsUseCase(listOf(currentAccount))
            }
            withContext(Main) {
                navigateBack()
            }
        }
    }

    fun setTransaction(amount: String) {
        val value = transactionAmount.value
        val whole = value.substring(0, value.indexOf("."))

        if (amount == ".") {
            isDecimal.value = true
            return
        }

        if (isDecimal.value) {
            if (decimal.length == 2) {
                decimal = decimal.substring(0, decimal.length - 1) + amount
            } else {
                decimal += amount
            }
            val newDecimal = decimal.toDouble() / 100.0
            transactionAmount.value = String.format("%.2f", whole.toDouble() + newDecimal)
            return
        }

        if (whole == "0") {
            transactionAmount.value = String.format("%.2f", amount.toDouble())
        } else {
            transactionAmount.value = String.format("%.2f", (whole + amount).toDouble())
        }
    }

    fun backspace() {
        val value = transactionAmount.value
        var whole = value.substring(0, value.indexOf("."))

        if (value == "0.00") {
            return
        }

        if (isDecimal.value) {
            decimal = if (decimal.length == 2) {
                decimal.substring(0, decimal.length - 1)
            } else {
                isDecimal.value = false
                "0"
            }
            val newDecimal = decimal.toDouble() / 100.0
            transactionAmount.value = String.format("%.2f", whole.toDouble() + newDecimal)
            decimal = String()

            return
        }

        whole = if (whole.length - 1 == 0)
            "0"
        else
            whole.substring(0, whole.length - 1)
        transactionAmount.value = String.format("%.2f", whole.toDouble())
    }

    fun displayTransaction(
        transactionDate: String?,
        transactionPos: Int?,
        transactionStatus: Int?
    ) {
        if (transactionPos != -1 && transactionStatus != -1) {
            val transaction = if (transactionStatus == 0)
                dailyTransaction.value[transactionPos!!]
            else {
                transactionDate?.let {
                    monthlyTransaction.value[it]!![transactionPos!!]
                }
            }

            setTransactionTitle(transaction!!.title)
            currentTime.value = transaction.date
            Account.values().forEach {
                if (it.title == transaction.account)
                    selectAccount(it)
                return@forEach
            }
            transactionAmount.value = transaction.amount.toString()
            Category.values().forEach {
                if (it.title == transaction.category)
                    selectCategory(it)
            }
        }
    }

    fun updateTransaction(
        transactionDate: String?,
        transactionPos: Int?,
        transactionStatus: Int?,
        navigateBack: () -> Unit
    ) {
        if (transactionPos != -1 && transactionStatus != -1) {
            val transaction = if (transactionStatus == 0)
                dailyTransaction.value[transactionPos!!]
            else {
                transactionDate?.let {
                    monthlyTransaction.value[it]!![transactionPos!!]
                }
            }
            viewModelScope.launch(IO) {
                if (transactionAmount.value.toDouble() != transaction!!.amount) {   // new amount

                    val currentAccount = getAccountUseCase(account.value.title).first()
                    if (transaction.transactionType == TransactionType.INCOME.title) {
                        currentAccount.income = currentAccount.income - transaction.amount
                        currentAccount.income =
                            currentAccount.income + transactionAmount.value.toDouble()
                        currentAccount.balance = currentAccount.income - currentAccount.expense
                    } else {
                        currentAccount.expense = currentAccount.expense - transaction.amount
                        currentAccount.expense =
                            currentAccount.expense + transactionAmount.value.toDouble()
                        currentAccount.balance = currentAccount.income - currentAccount.expense
                    }
                    insertAccountsUseCase(listOf(currentAccount))
                }
                val updateTransaction = TransactionDto(
                    transaction.date,
                    transaction.dateOfEntry,
                    transactionAmount.value.toDouble(),
                    account.value.title,
                    category.value.title,
                    transaction.transactionType,
                    transactionTitle.value
                )
                insertDailyTransactionUseCase(updateTransaction)
                withContext(Main) {
                    navigateBack()
                }
            }
        }
    }

    fun displayExpenseLimitWarning() {
        viewModelScope.launch(IO) {
            getExpenseLimitUseCase().collectLatest { expenseAmount ->
                if (expenseAmount <= 0.0) return@collectLatest
                val threshold = 0.8 * expenseAmount
                when {
                    currentExpenseAmount.value > expenseAmount -> {
                        val expenseOverflow =
                            (currentExpenseAmount.value - expenseAmount).toString().amountFormat()
                        val info =
                            "${selectedCurrencyCode.value} $expenseOverflow over specified limit"
                        limitAlert.emit(UIEvent.Alert(info))
                    }

                    currentExpenseAmount.value > threshold -> {
                        val expenseAvailable =
                            (expenseAmount - currentExpenseAmount.value).toString().amountFormat()
                        val info =
                            "${selectedCurrencyCode.value} $expenseAvailable away from specified limit"
                        limitAlert.emit(UIEvent.Alert(info))
                    }

                    else -> limitAlert.emit(UIEvent.NoAlert())
                }
            }
        }
    }

    private fun currencyFormat() {
        viewModelScope.launch(IO) {
            getCurrencyUseCase().collect { selectedCurrency ->
                selectedCurrencyCode.value = selectedCurrency
            }
        }
    }

    sealed class UIEvent {
        data class Alert(val info: String) : UIEvent()
        data class NoAlert(val info: String = String()) : UIEvent()
    }
}

fun String.amountFormat(): String {
    val amountFormatter = DecimalFormat("#,##0.00")
    return " " + amountFormatter.format(this.toDouble())
}

enum class TabButton(val title: String) {
    TODAY("Today"), MONTH("Month")
}

enum class TransactionType(var title: String) {
    INCOME("Income"), EXPENSE("Expense"), TRANSFER("Transfer"),
}

enum class Account(val title: String, val content: Int, val iconRes: Int, val color: Color) {
    CASH("Cash", R.string.pay_with_cash, R.drawable.cash, leisureBg),
    BANK("Bank", R.string.pay_with_bank, R.drawable.bank, subBg),
    CARD("Card", R.string.pay_with_card, R.drawable.credit_card, healthBg)
}

enum class Category(
    val title: String, // For use when you don't need localization
    val content: Int, // Resource ID for the string (for localization)
    val iconRes: Int,
    val bgRes: Color,
    val colorRes: Color = Color.White
) {
    FOOD_DRINK(
        "Food & Drink",
        R.string.category_food_drink,
        R.drawable.drink,
        food_drink,
        Color.Black
    ),
    CLOTHING("Clothing", R.string.category_clothing, R.drawable.clothing, clothBg, Color.Black),
    HOME("Home", R.string.category_home, R.drawable.home, homeBg, Color.Black),
    HEALTH("Health", R.string.category_health, R.drawable.health, healthBg, Color.Black),
    SCHOOL("School", R.string.category_school, R.drawable.school, schBg, Color.Black),
    GROCERY("Grocery", R.string.category_grocery, R.drawable.grocery, groceryBg, Color.Black),
    ELECTRICITY(
        "Electricity",
        R.string.category_electricity,
        R.drawable.electricity,
        electricBg,
        Color.Black
    ),
    BUSINESS("Business", R.string.category_business, R.drawable.business, businessBg, Color.Black),
    VEHICLE("Vehicle", R.string.category_vehicle, R.drawable.vehicle, vehicleBg, Color.Black),
    TAXI("Taxi", R.string.category_taxi, R.drawable.taxi, taxiBg, Color.Black),
    LEISURE("Leisure", R.string.category_leisure, R.drawable.leisure, leisureBg, Color.Black),
    GADGET("Gadget", R.string.category_gadget, R.drawable.gadget, gadgetBg, Color.Black),
    TRAVEL("Travel", R.string.category_travel, R.drawable.travel, travelBg, Color.Black),
    SUBSCRIPTION(
        "Subscription",
        R.string.category_subscription,
        R.drawable.sub,
        subBg,
        Color.Black
    ),
    PET("Pet", R.string.category_pet, R.drawable.pet, petBg, Color.Black),
    SNACK("Snack", R.string.category_snack, R.drawable.snack, snackBg, Color.Black),
    GIFT("Gift", R.string.category_gift, R.drawable.gift, giftBg, Color.Black),
    MISC("Miscellaneous", R.string.category_misc, R.drawable.misc, miscBg, Color.Black)
}



