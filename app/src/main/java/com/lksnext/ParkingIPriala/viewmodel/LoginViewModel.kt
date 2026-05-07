package com.lksnext.ParkingIPriala.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lksnext.ParkingIPriala.ui.auth.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
    data class PasswordResetSent(val email: String) : LoginState()
}

class LoginViewModel(
    private val repository: AuthRepository = AuthRepository()
) : ViewModel() {

    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            repository.loginUser(email, password)
                .onSuccess { _state.value = LoginState.Success }
                .onFailure { _state.value = LoginState.Error(it.message ?: "Error desconocido") }
        }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            repository.resetPassword(email)
                .onSuccess { _state.value = LoginState.PasswordResetSent(email) }
                .onFailure { _state.value = LoginState.Error(it.message ?: "Error al enviar email") }
        }
    }
}
