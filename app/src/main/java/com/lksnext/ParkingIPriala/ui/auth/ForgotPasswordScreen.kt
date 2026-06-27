package com.lksnext.ParkingIPriala.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lksnext.ParkingIPriala.viewmodel.LoginState
import com.lksnext.ParkingIPriala.viewmodel.LoginViewModel

@Composable
fun ForgotPasswordScreen(
    onNavigateBack: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }

    LaunchedEffect(state) {
        when (state) {
            is LoginState.PasswordResetEmailSent -> {
                Toast.makeText(
                    context, 
                    "Se ha enviado un correo de recuperación a $email. Revisa tu bandeja de entrada (y spam).", 
                    Toast.LENGTH_LONG
                ).show()
                onNavigateBack() // Redirigir al login
                viewModel.resetState()
            }
            is LoginState.Error -> {
                Toast.makeText(context, (state as LoginState.Error).message, Toast.LENGTH_LONG).show()
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFFEFEFEF))) {
        HeaderBackground()

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(top = 250.dp) // Subimos la tarjeta para que solape más con el fondo verde
                .align(Alignment.TopCenter),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(32.dp)) {
                Text(
                    "RESTABLECER",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF060606),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Introduce tu email para recibir el enlace de recuperación de contraseña.",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                AuthTextField(email, { email = it }, "Email")
                
                Spacer(modifier = Modifier.height(24.dp))
                
                AuthButton("ENVIAR CORREO", state is LoginState.Loading) {
                    if (email.isNotEmpty()) {
                        viewModel.sendResetPasswordEmail(email)
                    } else {
                        Toast.makeText(context, "Introduce tu email", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }
    }
}
