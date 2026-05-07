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
import com.lksnext.ParkingIPriala.viewmodel.RegisterState
import com.lksnext.ParkingIPriala.viewmodel.RegisterViewModel

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state) {
        when (state) {
            is RegisterState.Success -> {
                Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()
                onRegisterSuccess()
            }
            is RegisterState.Error -> {
                Toast.makeText(context, (state as RegisterState.Error).message, Toast.LENGTH_LONG).show()
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

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Crear Cuenta",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = AuthTextPrimary.copy(alpha = 0.8f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        AuthTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = "Nombre completo"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = usuario,
            onValueChange = { usuario = it },
            label = "Nombre de usuario"
        )

        Spacer(modifier = Modifier.height(12.dp))

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

        AuthTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = "Confirmar contraseña",
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        AuthButton(
            text = "Registrarse",
            isLoading = state is RegisterState.Loading,
            onClick = {
                when {
                    nombre.isEmpty() || usuario.isEmpty() || email.isEmpty() || password.isEmpty() -> {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                    password != confirmPassword -> {
                        Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                    }
                    password.length < 6 -> {
                        Toast.makeText(context, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        viewModel.register(nombre, usuario, email, password)
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthClickableText(
            text = "¿Ya tienes cuenta? Inicia sesión",
            onClick = onNavigateToLogin
        )
    }
}
