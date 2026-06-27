package com.lksnext.ParkingIPriala.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(onLogout: () -> Unit) {
    // Estado para controlar qué sección está seleccionada
    // 0: Home, 1: Reservas, 2: Historial, 3: Perfil, 4: Nueva Reserva (+)
    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            CustomBottomNavigation(
                selectedIndex = selectedIndex,
                onItemSelected = { selectedIndex = it }
            )
        },
        containerColor = BgGray
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            when (selectedIndex) {
                0 -> MainHomeContent()
                1 -> PlaceholderScreen("Mis Reservas")
                2 -> PlaceholderScreen("Historial")
                3 -> ProfileScreen(onLogout = onLogout)
                4 -> PlaceholderScreen("Nueva Reserva")
            }
        }
    }
}

@Composable
fun MainHomeContent() {
    val user = FirebaseAuth.getInstance().currentUser
    val userName = user?.displayName ?: "Ivan Priala"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        // --- HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Bienvenido,", fontSize = 16.sp, color = Color.Gray)
                Text(text = userName, fontSize = 24.sp, fontWeight = FontWeight.Black, color = TextBlack)
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) { /* Acción de notificaciones */ },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsNone,
                    contentDescription = "Notificaciones",
                    tint = TextBlack,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        ParkingStatusCard()

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Actividad reciente",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextBlack
        )

        Spacer(modifier = Modifier.height(16.dp))

        repeat(3) {
            ActivityItem()
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun PlaceholderScreen(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextBlack)
    }
}
