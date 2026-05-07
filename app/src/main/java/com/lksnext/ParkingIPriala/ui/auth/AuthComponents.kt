package com.lksnext.ParkingIPriala.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lksnext.ParkingIPriala.R

val AuthBgDeep = Color(0xFF080D1A)
val AuthBgCard = Color(0xFF0F1726)
val AuthBorderColor = Color(0xFF1A2235)
val AuthTextPrimary = Color(0xFFE4ECF7)
val AuthTextMuted = Color(0xFF4A6080)
val AuthAccentBlue = Color(0xFF2563EB)

@Composable
fun AuthLogo() {
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
}

@Composable
fun AuthTitle(text: String = "ParkingIPriala") {
    Text(
        text = text,
        fontSize = 32.sp,
        fontWeight = FontWeight.ExtraBold,
        color = AuthTextPrimary
    )
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = AuthTextMuted) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = visualTransformation,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = AuthTextPrimary,
            unfocusedTextColor = AuthTextPrimary,
            focusedBorderColor = AuthAccentBlue,
            unfocusedBorderColor = AuthBorderColor,
            cursorColor = AuthAccentBlue,
            focusedContainerColor = AuthBgCard,
            unfocusedContainerColor = AuthBgCard,
            focusedLabelColor = AuthAccentBlue,
            unfocusedLabelColor = AuthTextMuted
        )
    )
}

@Composable
fun AuthButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth().height(50.dp),
        shape = RoundedCornerShape(12.dp),
        enabled = !isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = AuthAccentBlue,
            disabledContainerColor = AuthAccentBlue.copy(alpha = 0.5f)
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        } else {
            Text(text, color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun AuthClickableText(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        modifier = Modifier.clickable(onClick = onClick),
        color = AuthAccentBlue,
        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
    )
}
