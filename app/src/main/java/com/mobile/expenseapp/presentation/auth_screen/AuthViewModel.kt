package com.mobile.expenseapp.presentation.auth_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobile.expenseapp.domain.usecase.read_datastore.GetLoginTokenUseCase
import com.mobile.expenseapp.domain.usecase.write_database.EraseDatabaseUseCase
import com.mobile.expenseapp.domain.usecase.write_database.SyncFromRemoteUseCase
import com.mobile.expenseapp.domain.usecase.write_datastore.EditIsLoggedInUseCase
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
    private val syncFromRemoteUseCase: SyncFromRemoteUseCase,
    private val eraseDatabaseUseCase: EraseDatabaseUseCase,
    private val editIsLoggedInUseCase: EditIsLoggedInUseCase
) : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> get() = _registerState
    fun register(username: String, password: String) {
        viewModelScope.launch(IO) {
            val result = APIRepository.register(username, password)
            if (result) {
                _registerState.value = RegisterState.Success
            } else {
                _registerState.value = RegisterState.Failure
            }
        }
    }

    fun login(username: String, password: String) {
        viewModelScope.launch(IO) {
            _loginState.value = LoginState.Loading
            val result = APIRepository.login(username, password)
            result.fold(onSuccess = { token ->
                editLoginTokenUseCase(token)
                editIsLoggedInUseCase(true)
                _loginState.value = LoginState.Success
            }, onFailure = { _ ->
                _loginState.value = LoginState.Failure
            })
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
                    Log.d("sync", "Not login")
                }
            }
        }
        _loginState.value = LoginState.Idle
    }

    fun clearOnFreshLogin() {
        viewModelScope.launch {
            eraseDatabaseUseCase()
        }
    }
}

sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    object Failure : RegisterState()
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    object Failure : LoginState()
//    data class Failure(val errorMessage: String) : LoginState()
}