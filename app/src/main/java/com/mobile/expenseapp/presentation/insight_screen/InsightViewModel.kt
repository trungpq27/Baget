package com.mobile.expenseapp.presentation.insight_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.expenseapp.common.Constants
import com.mobile.expenseapp.domain.model.Transaction
import com.mobile.expenseapp.domain.usecase.read_database.Get14DayTransaction
import com.mobile.expenseapp.domain.usecase.read_database.Get3DayTransaction
import com.mobile.expenseapp.domain.usecase.read_database.Get7DayTransaction
import com.mobile.expenseapp.domain.usecase.read_database.GetLastMonthTransaction
import com.mobile.expenseapp.domain.usecase.read_database.GetStartOfMonthTransaction
import com.mobile.expenseapp.domain.usecase.read_database.GetTransactionByTypeUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetCurrencyUseCase
import com.mobile.expenseapp.presentation.home_screen.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsightViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val get3DayTransaction: Get3DayTransaction,
    private val get7DayTransaction: Get7DayTransaction,
    private val get14DayTransaction: Get14DayTransaction,
    private val getStartOfMonthTransaction: GetStartOfMonthTransaction,
    private val getLastMonthTransaction: GetLastMonthTransaction,
    private val getAllTransaction: GetTransactionByTypeUseCase
) : ViewModel() {

    private var _tabButton = MutableStateFlow(TransactionType.INCOME)
    val tabButton: StateFlow<TransactionType> = _tabButton

    private var _filteredTransaction = MutableStateFlow(emptyList<Transaction>())
    val filteredTransaction: StateFlow<List<Transaction>> = _filteredTransaction

    private var _totalLastMonthTransaction = MutableStateFlow(0.0)
    val totalLastMonthTransaction: StateFlow<Double> = _totalLastMonthTransaction

    private var _totalThisMonthTransaction = MutableStateFlow(0.0)
    val totalThisMonthTransaction: StateFlow<Double> = _totalThisMonthTransaction
    var selectedCurrencyCode = MutableStateFlow(String())
        private set

    fun selectTabButton(tab: TransactionType, duration: Int =5) {
        _tabButton.value = tab
        getFilteredTransaction(duration)
        getTotalLastMonthTransaction()
        getTotalThisMonthTransaction()
    }

    init {
        getTotalLastMonthTransaction()
        getTotalThisMonthTransaction()
        getFilteredTransaction()
        currencyFormat()
    }

    private fun currencyFormat() {
        viewModelScope.launch(IO) {
            getCurrencyUseCase().collect { selectedCurrency ->
                selectedCurrencyCode.value = selectedCurrency
            }
        }
    }
    fun getTotalLastMonthTransaction() {
        viewModelScope.launch(IO) {
            if (_tabButton.value == TransactionType.INCOME) {
                getLastMonthTransaction(Constants.INCOME).collectLatest { filteredResults ->
                    _totalLastMonthTransaction.value = filteredResults.sumOf { it.amount }
                }
            } else {
                getLastMonthTransaction(Constants.EXPENSE).collectLatest { filteredResults ->
                    _totalLastMonthTransaction.value = filteredResults.sumOf { it.amount }
                }
            }
        }
    }

    fun getTotalThisMonthTransaction() {
        viewModelScope.launch(IO) {
            if (_tabButton.value == TransactionType.INCOME) {
                getStartOfMonthTransaction(Constants.INCOME).collectLatest { filteredResults ->
                    _totalThisMonthTransaction.value = filteredResults.sumOf{ it.amount }
                }
            } else {
                getStartOfMonthTransaction(Constants.EXPENSE).collectLatest { filteredResults ->
                    _totalThisMonthTransaction.value = filteredResults.sumOf{ it.amount }
                }
            }
        }
    }


    fun getFilteredTransaction(duration: Int = 5) {
        viewModelScope.launch(IO) {
            if (_tabButton.value == TransactionType.INCOME) {
                filterTransaction(duration, Constants.INCOME)
            } else {
                filterTransaction(duration, Constants.EXPENSE)
            }
        }
    }

    private suspend fun filterTransaction(duration: Int, transactionType: String = Constants.INCOME) {
        when (duration) {
            0 -> {
                get3DayTransaction(transactionType).collectLatest { filteredResults ->
                    _filteredTransaction.value = filteredResults.map { it.toTransaction() }
                }
            }
            1 -> {
                get7DayTransaction(transactionType).collectLatest { filteredResults ->
                    _filteredTransaction.value = filteredResults.map { it.toTransaction() }
                }
            }
            2 -> {
                get14DayTransaction(transactionType).collectLatest { filteredResults ->
                    _filteredTransaction.value = filteredResults.map { it.toTransaction() }
                }
            }
            3 -> {
                getStartOfMonthTransaction(transactionType).collectLatest { filteredResults ->
                    _filteredTransaction.value = filteredResults.map { it.toTransaction() }
                }
            }
            4 -> {
                getLastMonthTransaction(transactionType).collectLatest { filteredResults ->
                    _filteredTransaction.value = filteredResults.map { it.toTransaction() }
                }
            }
            5 -> {
                getAllTransaction(transactionType).collectLatest { filteredResults ->
                    _filteredTransaction.value = filteredResults.map { it.toTransaction() }
                }
            }
        }
    }
}