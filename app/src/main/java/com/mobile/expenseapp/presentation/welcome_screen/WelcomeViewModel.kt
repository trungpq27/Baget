package com.mobile.expenseapp.presentation.welcome_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.expenseapp.domain.model.CurrencyModel
import com.mobile.expenseapp.domain.usecase.GetCurrencyUseCase
import com.mobile.expenseapp.domain.usecase.write_database.InsertAccountsUseCase
import com.mobile.expenseapp.domain.usecase.write_datastore.EditCurrencyUseCase
import com.mobile.expenseapp.domain.usecase.write_datastore.EditOnBoardingUseCase
import com.mobile.expenseapp.presentation.welcome_screen.components.OnBoardingPage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.util.Currency
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val editOnBoardingUseCase: EditOnBoardingUseCase,
    private val editCurrencyUseCase: EditCurrencyUseCase,
    private val insertAccountsUseCase: InsertAccountsUseCase,
    getCurrencyUseCase: GetCurrencyUseCase
) : ViewModel() {

    var countryCurrencies = mutableStateOf(emptyMap<Char, List<CurrencyModel>>())
        private set

    init {
        countryCurrencies.value = getCurrencyUseCase().groupBy { it.country[0] }
    }

    val listOfPages: State<List<OnBoardingPage>> = mutableStateOf(
        listOf(
            OnBoardingPage.FirstPage,
            OnBoardingPage.SecondPage,
            OnBoardingPage.ThirdPage
        )
    )

    fun saveOnBoardingState(completed: Boolean) {
        viewModelScope.launch(IO) {
            editOnBoardingUseCase(completed = completed)
        }
    }

    fun saveCurrencyLocale() {
        val userLocale = Locale.getDefault()

// Get the currency information based on the user's locale
        val currency = Currency.getInstance(userLocale)
        val currencySymbol = currency.getSymbol(userLocale)
        val currencyCode = currency.currencyCode

        viewModelScope.launch(IO) {
            editCurrencyUseCase(currencyCode)
        }
    }

    fun saveCurrency(currency: String) {
        viewModelScope.launch(IO) {
            editCurrencyUseCase(currency)
        }
    }


}