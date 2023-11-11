package com.mobile.expenseapp.presentation.setting_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.expenseapp.data.local.entity.AccountDto
import com.mobile.expenseapp.domain.usecase.read_datastore.GetCurrencyUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetExpenseLimitUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetLimitDurationUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetLimitKeyUseCase
import com.mobile.expenseapp.domain.usecase.write_datastore.EditDarkModeUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetDarkModeUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetLanguageUseCase
import com.mobile.expenseapp.domain.usecase.write_database.EraseTransactionUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertAccountsUseCase
import com.mobile.expenseapp.domain.usecase.write_datastore.EditExpenseLimitUseCase
import com.mobile.expenseapp.domain.usecase.write_datastore.EditLanguageUseCase
import com.mobile.expenseapp.domain.usecase.write_datastore.EditLimitDurationUseCase
import com.mobile.expenseapp.domain.usecase.write_datastore.EditLimitKeyUseCase
import com.mobile.expenseapp.domain.usecase.write_datastore.EraseDatastoreUseCase
import com.mobile.expenseapp.presentation.home_screen.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val getCurrencyUseCase: GetCurrencyUseCase,
    private val insertAccountsUseCase: InsertAccountsUseCase,
    private val eraseTransactionUseCase: EraseTransactionUseCase,
    private val getExpenseLimitUseCase: GetExpenseLimitUseCase,
    private val editExpenseLimitUseCase: EditExpenseLimitUseCase,
    private val getLimitKeyUseCase: GetLimitKeyUseCase,
    private val editLimitKeyUseCase: EditLimitKeyUseCase,
    private val editLimitDurationUseCase: EditLimitDurationUseCase,
    private val getLimitDurationUseCase: GetLimitDurationUseCase,
    private val eraseDatastoreUseCase: EraseDatastoreUseCase,
    private val getDarkModeUseCase: GetDarkModeUseCase,
    private val editDarkModeUseCase: EditDarkModeUseCase,
    private val getLanguageUseCase: GetLanguageUseCase,
    private val editLanguageUseCase: EditLanguageUseCase
) : ViewModel() {


    var currency = MutableStateFlow(String())
        private set

    var expenseLimit = MutableStateFlow(.0)
        private set

    var expenseLimitDuration = MutableStateFlow(0)
        private set
    var isDarkMode = MutableStateFlow(false)
        private set
    var language = MutableStateFlow(String())
        private set
    var reminderLimit = MutableStateFlow(false)
        private set

    init {
        viewModelScope.launch(IO) {
            getCurrencyUseCase().collect { selectedCurrency->
                currency.value = selectedCurrency
            }
        }

        viewModelScope.launch(IO) {
            getExpenseLimitUseCase().collect { expenseAmount ->
                expenseLimit.value = expenseAmount
            }
        }

        viewModelScope.launch(IO) {
            getLimitKeyUseCase().collect { limitKey ->
                reminderLimit.value = limitKey
            }
        }

        viewModelScope.launch(IO) {
            getLimitDurationUseCase().collect { duration ->
                expenseLimitDuration.value = duration
            }
        }

        viewModelScope.launch(IO) {
            getDarkModeUseCase().collect { mode ->
                isDarkMode.value = mode
            }
        }

        viewModelScope.launch(IO) {
            getLanguageUseCase().collect { selectedLanguage ->
                language.value = selectedLanguage
            }
        }
    }

    fun eraseTransaction() {
        viewModelScope.launch(IO) {
            // reset account
            insertAccountsUseCase(
                listOf(
                    AccountDto(1, Account.CASH.title, 0.0, 0.0, 0.0),
                    AccountDto(2, Account.BANK.title, 0.0, 0.0, 0.0),
                    AccountDto(3, Account.CARD.title, 0.0, 0.0, 0.0)
                )
            )
            // erase transactions
            eraseTransactionUseCase()
            // erase datastore
            eraseDatastoreUseCase()
        }
    }

    fun editExpenseLimit(amount: Double) {
        viewModelScope.launch(IO) {
            editExpenseLimitUseCase(amount)
        }
    }

    fun editLanguage(selectedLanguage: String) {
        viewModelScope.launch(IO) {
            editLanguageUseCase(selectedLanguage)
        }
    }
    fun editLimitKey(enabled: Boolean) {
        viewModelScope.launch(IO) {
            editLimitKeyUseCase(enabled)
        }
    }

    fun editDarkMode(enabled: Boolean) {
        viewModelScope.launch(IO) {
            editDarkModeUseCase(enabled)
        }
    }

    fun editLimitDuration(duration: Int) {
        viewModelScope.launch(IO) {
            editLimitDurationUseCase(duration)
        }
    }
}