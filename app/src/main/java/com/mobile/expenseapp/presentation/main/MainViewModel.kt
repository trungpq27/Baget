package com.mobile.expenseapp.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.expenseapp.domain.usecase.read_datastore.GetIsLoggedInUseCase
import com.mobile.expenseapp.domain.usecase.read_datastore.GetOnBoardingKeyUseCase
import com.mobile.expenseapp.presentation.navigation.Screen
import com.mobile.expenseapp.service.APIRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@InternalCoroutinesApi
class MainViewModel @Inject constructor(
    private val getOnBoardingKeyUseCase: GetOnBoardingKeyUseCase,
    private val getIsLoggedInUseCase: GetIsLoggedInUseCase
) : ViewModel() {

    var isLoading = MutableStateFlow(true)
        private set

    var startDestination = MutableStateFlow(Screen.WelcomeScreen.route)
        private set

    init {
        viewModelScope.launch(IO) {
            getOnBoardingKeyUseCase().collect { completed ->
                if (completed)
                    startDestination.value = Screen.SignInScreen.route
            }

            isLoading.value = false
        }

        viewModelScope.launch(IO) {
            getIsLoggedInUseCase().collect { isLoggedIn ->
                if(APIRepository.healthCheck()){
                    if(isLoggedIn){
                        startDestination.value = Screen.HomeScreen.route
                    }else {
                        startDestination.value = Screen.SignInScreen.route
                    }
                }else {
                    startDestination.value = Screen.HomeScreen.route
                }
            }
        }
    }
}