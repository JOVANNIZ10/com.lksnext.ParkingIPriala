package com.lksnext.ParkingIPriala.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lksnext.ParkingIPriala.ui.auth.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
    data class PasswordResetEmailSent(val email: String) : LoginState()
    data class PasswordResetCodeVerified(val email: String, val code: String) : LoginState()
    object PasswordChangedSuccess : LoginState()
}


class LoginViewModel(
    private val repository: AuthRepository = AuthRepository(),
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {


    private val _state = MutableStateFlow<LoginState>(LoginState.Idle)
    val state: StateFlow<LoginState> = _state

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val result = withContext(ioDispatcher) {
                    repository.loginUser(email, password)
                }
                result.onSuccess { 
                    _state.value = LoginState.Success 
                }.onFailure { 
                    _state.value = LoginState.Error(it.message ?: "Error de autenticación") 
                }
            } catch (e: Exception) {
                _state.value = LoginState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    fun sendResetPasswordEmail(email: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val result = withContext(ioDispatcher) {
                    repository.resetPassword(email)
                }
                result.onSuccess { 
                    _state.value = LoginState.PasswordResetEmailSent(email) 
                }.onFailure { 
                    _state.value = LoginState.Error(it.message ?: "Error al enviar email") 
                }
            } catch (e: Exception) {
                _state.value = LoginState.Error("Error al conectar: ${e.message}")
            }
        }
    }

    fun verifyResetCode(code: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val result = withContext(ioDispatcher) {
                    repository.verifyResetCode(code)
                }
                result.onSuccess { email ->
                    _state.value = LoginState.PasswordResetCodeVerified(email, code)
                }.onFailure {
                    _state.value = LoginState.Error("El código introducido no es válido o ha caducado")
                }
            } catch (e: Exception) {
                _state.value = LoginState.Error("Error al verificar código: ${e.message}")
            }
        }
    }

    fun resetPassword(code: String, newPassword: String) {
        viewModelScope.launch {
            _state.value = LoginState.Loading
            try {
                val result = withContext(ioDispatcher) {
                    repository.confirmPasswordReset(code, newPassword)
                }
                result.onSuccess {
                    _state.value = LoginState.PasswordChangedSuccess
                }.onFailure {
                    _state.value = LoginState.Error(it.message ?: "Error al cambiar la contraseña")
                }
            } catch (e: Exception) {
                _state.value = LoginState.Error("Error inesperado: ${e.message}")
            }
        }
    }

    fun resetState() {
        _state.value = LoginState.Idle
    }
}
