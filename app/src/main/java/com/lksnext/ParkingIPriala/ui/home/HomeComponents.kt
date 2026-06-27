package com.lksnext.ParkingIPriala.ui.home

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

// Colores del tema centralizados
val BgGray = Color(0xFFF2F2F2)
val AccentLime = Color(0xFFD7EE46)
val TextBlack = Color(0xFF060606)

@Composable
fun CustomBottomNavigation(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val navShape = remember {
        object : Shape {
            override fun createOutline(
                size: Size,
                layoutDirection: LayoutDirection,
                density: Density
            ): Outline {
                val barHeightPx = with(density) { 72.dp.toPx() }
                val bulbSizePx = with(density) { 88.dp.toPx() }
                val cornerRadiusPx = with(density) { 22.dp.toPx() }

                val barTop = (size.height - barHeightPx) / 2f
                val barBottom = barTop + barHeightPx

                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(left = 0f, top = barTop, right = size.width, bottom = barBottom),
                            cornerRadius = CornerRadius(cornerRadiusPx)
                        )
                    )
                    addOval(
                        Rect(
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = bulbSizePx / 2f
                        )
                    )
                }
                return Outline.Generic(path)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding() // Respeta la barra de Android
            .padding(bottom = 32.dp) // Sube la barra para que no solape
            .height(110.dp)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Superficie única para toda la pieza integrada
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = navShape,
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
                val constraints = this
                val tabWidth = constraints.maxWidth / 5
                val indicatorSize = 48.dp

                // Ajuste de slots: 0, 1, 3, 4 son iconos. 2 es el centro
                val targetSlot = when(selectedIndex) {
                    0 -> 0
                    1 -> 1
                    2 -> 3
                    3 -> 4
                    else -> 0
                }
                
                val indicatorOffset by animateDpAsState(
                    targetValue = (tabWidth * targetSlot.toFloat()) + (tabWidth - indicatorSize) / 2,
                    animationSpec = spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow),
                    label = "slide"
                )

                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .align(Alignment.CenterStart)
                        .size(indicatorSize)
                        .background(AccentLime, CircleShape)
                )

                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BottomNavItem(Modifier.weight(1f), Icons.Default.Home) { onItemSelected(0) }
                    BottomNavItem(Modifier.weight(1f), Icons.Default.LocalParking) { onItemSelected(1) }
                    Spacer(modifier = Modifier.weight(1f)) // Hueco para el +
                    BottomNavItem(Modifier.weight(1f), Icons.Default.History) { onItemSelected(2) }
                    BottomNavItem(Modifier.weight(1f), Icons.Default.PersonOutline) { onItemSelected(3) }
                }
            }
        }

        // Botón verde "+" perfectamente centrado en el bulbo
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(AccentLime)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { /* Acción añadir */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, null, tint = TextBlack, modifier = Modifier.size(34.dp))
        }
    }
}

@Composable
fun BottomNavItem(modifier: Modifier, icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, null, tint = TextBlack, modifier = Modifier.size(26.dp))
    }
}

@Composable
fun ParkingStatusCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Estado actual", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(45.dp).background(AccentLime, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("P1", fontWeight = FontWeight.Black, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Planta 1 - Plaza 142", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("Ocupada hace 2h 15m", fontSize = 13.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun ActivityItem() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).background(BgGray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.History, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Parking Centro", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text("Ayer, 18:30", fontSize = 12.sp, color = Color.Gray)
        }
        Text("4.50€", fontWeight = FontWeight.Black, fontSize = 16.sp)
    }
}
