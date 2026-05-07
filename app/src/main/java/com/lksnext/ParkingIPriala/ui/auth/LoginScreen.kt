package com.lksnext.ParkingIPriala.ui.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.lksnext.ParkingIPriala.R
import com.lksnext.ParkingIPriala.viewmodel.LoginState
import com.lksnext.ParkingIPriala.viewmodel.LoginViewModel

private val BgDeep      = Color(0xFF080D1A)
private val BgCard      = Color(0xFF0F1726)
private val BorderColor = Color(0xFF1A2235)
private val TextPrimary = Color(0xFFE4ECF7)
private val TextMuted   = Color(0xFF4A6080)
private val AccentBlue  = Color(0xFF2563EB)

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
            .background(BgDeep)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(listOf(Color(0xFF1D4ED8), Color(0xFF7C3AED)))
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.parking_logo),
                contentDescription = "Logo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ParkingIPriala",
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email", color = TextMuted) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = AccentBlue,
                unfocusedBorderColor = BorderColor,
                cursorColor = AccentBlue,
                focusedContainerColor = BgCard,
                unfocusedContainerColor = BgCard,
                focusedLabelColor = AccentBlue,
                unfocusedLabelColor = TextMuted
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña", color = TextMuted) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                focusedBorderColor = AccentBlue,
                unfocusedBorderColor = BorderColor,
                cursorColor = AccentBlue,
                focusedContainerColor = BgCard,
                unfocusedContainerColor = BgCard,
                focusedLabelColor = AccentBlue,
                unfocusedLabelColor = TextMuted
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "¿Has olvidado la contraseña?",
            modifier = Modifier.clickable {
                if (email.isNotEmpty()) {
                    viewModel.resetPassword(email)
                } else {
                    Toast.makeText(context, "Ingresa tu email primero", Toast.LENGTH_SHORT).show()
                }
            },
            color = AccentBlue,
            textDecoration = TextDecoration.Underline
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                when {
                    email.isEmpty() || password.isEmpty() -> {
                        Toast.makeText(context, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        viewModel.login(email, password)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(12.dp),
            enabled = state !is LoginState.Loading,
            colors = ButtonDefaults.buttonColors(
                containerColor = AccentBlue,
                disabledContainerColor = AccentBlue.copy(alpha = 0.5f)
            )
        ) {
            if (state is LoginState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    strokeWidth = 2.dp,
                    color = Color.White
                )
            } else {
                Text("Iniciar sesión", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "¿No tienes cuenta? Regístrate",
            modifier = Modifier.clickable { onNavigateToRegister() },
            color = AccentBlue,
            textDecoration = TextDecoration.Underline
        )
    }
}
