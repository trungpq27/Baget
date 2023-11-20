package com.mobile.expenseapp.presentation.auth_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.expenseapp.domain.usecase.read_datastore.GetLoginTokenUseCase
import com.mobile.expenseapp.domain.usecase.write_database.SyncFromRemoteUseCase
import com.mobile.expenseapp.domain.usecase.write_datastore.EditLoginTokenUseCase
import com.mobile.expenseapp.service.APIRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val editLoginTokenUseCase: EditLoginTokenUseCase,
    private val getLoginTokenUseCase: GetLoginTokenUseCase,
    private val syncFromRemoteUseCase: SyncFromRemoteUseCase
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState
    fun register(username: String, password: String) {
        viewModelScope.launch(IO) {
            APIRepository.register(username, password)
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch(IO) {
            _loginState.value = LoginState.Loading
            val token = APIRepository.login(username, password)
            if (token != null) {
                editLoginTokenUseCase(token)
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Failure
            }
        }
    }

    fun syncToRemoteWhenLogin() {
        viewModelScope.launch(IO) {
            getLoginTokenUseCase().collect {
                if (it != null) {
                    val remoteData = APIRepository.syncGet(it)
                    if (remoteData != null) {
                        syncFromRemoteUseCase(remoteData)
                    }
                } else {
                    println("You are not login")
                }
            }
        }
        _loginState.value = LoginState.Idle
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    object Failure : LoginState()
//    data class Failure(val errorMessage: String) : LoginState()
}