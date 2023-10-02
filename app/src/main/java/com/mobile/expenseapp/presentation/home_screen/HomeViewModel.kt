package com.mobile.expenseapp.presentation.home_screen

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.DecimalFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
) : ViewModel() {

    private var decimal: String = String()
    private var isDecimal = MutableStateFlow(false)

    var tabButton = MutableStateFlow(TabButton.TODAY)
        private set

    var transactionAmount = MutableStateFlow("0.00")
        private set
    var currentExpenseAmount = MutableStateFlow(0.0)
        private set

    var transactionTitle = MutableStateFlow(String())
        private set

    var showInfoBanner = MutableStateFlow(false)
        private set

    var totalIncome = MutableStateFlow(0.0)
        private set

    var totalExpense = MutableStateFlow(0.0)
        private set

    var formattedDate = MutableStateFlow(String())
        private set

    var date = MutableStateFlow(String())
        private set

    var currentTime = MutableStateFlow(Calendar.getInstance().time)
        private set

    var selectedCurrencyCode = MutableStateFlow(String())
        private set

    var limitAlert = MutableSharedFlow<UIEvent>(replay = 1)
        private set

    var limitKey = MutableStateFlow(false)
        private set

    private fun calculateTransaction(transactions: List<Double>): Double {
        return transactions.sumOf {
            it
        }
    }

    fun selectTabButton(button: TabButton) {
        tabButton.value = button
    }
    fun setTransactionTitle(title: String) {
        transactionTitle.value = title
    }

    fun setCurrentTime(time: Date) {
        currentTime.value = time
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

enum class TransactionType(val title: String) {
    INCOME("income"), EXPENSE("expense")
}