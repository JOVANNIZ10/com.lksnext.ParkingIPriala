package com.lksnext.ParkingIPriala.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lksnext.ParkingIPriala.viewmodel.LoginState
import com.lksnext.ParkingIPriala.viewmodel.LoginViewModel

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            is LoginState.Success -> {
                Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }
            is LoginState.Error -> {
                Toast.makeText(context, (state as LoginState.Error).message, Toast.LENGTH_LONG).show()
            }
            is LoginState.PasswordResetSent -> {
                Toast.makeText(context, "Email de recuperación enviado a ${(state as LoginState.PasswordResetSent).email}", Toast.LENGTH_LONG).show()
                viewModel.login(email, password)
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AuthBgDeep)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AuthLogo()

        Spacer(modifier = Modifier.height(16.dp))

        AuthTitle()

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = password,
            onValueChange = { password = it },
            label = "Contraseña",
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthClickableText(
            text = "¿Has olvidado la contraseña?",
            onClick = {
                if (email.isNotEmpty()) {
                    viewModel.resetPassword(email)
                } else {
                    Toast.makeText(context, "Ingresa tu email primero", Toast.LENGTH_SHORT).show()
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthButton(
            text = "Iniciar sesión",
            isLoading = state is LoginState.Loading,
            onClick = {
                when {
                    email.isEmpty() || password.isEmpty() -> {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        viewModel.login(email, password)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthClickableText(
            text = "¿No tienes cuenta? Regístrate",
            onClick = onNavigateToRegister
        )
    }
}
