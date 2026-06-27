package com.lksnext.ParkingIPriala.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
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

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFEFEFEF))) {
        HeaderBackground()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text(
                    "REGISTER",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF060606),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(40.dp))

                AuthTextField(nombre, { nombre = it }, "Nombre completo")
                Spacer(modifier = Modifier.height(12.dp))
                AuthTextField(email, { email = it }, "Email")
                Spacer(modifier = Modifier.height(12.dp))
                AuthPasswordField(password, { password = it }, "Contraseña")
                Spacer(modifier = Modifier.height(12.dp))
                AuthPasswordField(confirmPassword, { confirmPassword = it }, "Confirmar contraseña")

                if (confirmPassword.isNotEmpty() && password != confirmPassword) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Las contraseñas no coinciden",
                        color = Color(0xFFD32F2F),
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                AuthButton("REGISTRARSE", state is RegisterState.Loading) {
                    when {
                        nombre.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
                            Toast.makeText(context, "Completa los campos", Toast.LENGTH_SHORT).show()
                        }
                        password != confirmPassword -> {
                            Toast.makeText(context, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            viewModel.register(nombre, "", email, password)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("¿Ya tienes cuenta? ", color = Color(0xFF060606))
                    Text(
                        "Inicia sesión",
                        color = Color(0xFFD7EE46),
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) { onNavigateToLogin() }
                    )
                }
            }
        }
    }
}