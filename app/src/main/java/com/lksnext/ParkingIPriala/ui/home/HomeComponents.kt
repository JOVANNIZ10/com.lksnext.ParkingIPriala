package com.lksnext.ParkingIPriala.ui.home

import androidx.compose.animation.core.*
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

// Colores del tema centralizados (Ajustados a la paleta solicitada)
val BgGray = Color(0xFFEFF0EF)
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
            .navigationBarsPadding()
            .padding(bottom = 32.dp)
            .height(110.dp)
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
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

                var previousIndex by remember { mutableIntStateOf(selectedIndex) }
                val isComingFromPlus = (previousIndex == 4 && selectedIndex != 4)

                val indicatorAlpha by animateFloatAsState(
                    targetValue = if (selectedIndex == 4) 0f else 1f,
                    label = "visibility"
                )

                val targetSlot = remember(selectedIndex) {
                    when(selectedIndex) {
                        0 -> 0f
                        1 -> 1f
                        2 -> 3f
                        3 -> 4f
                        else -> null 
                    }
                }

                var lastValidSlot by remember { mutableFloatStateOf(0f) }
                if (targetSlot != null) { 
                    lastValidSlot = targetSlot 
                }
                
                val indicatorOffset by animateDpAsState(
                    targetValue = (tabWidth * lastValidSlot) + (tabWidth - indicatorSize) / 2,
                    animationSpec = if (isComingFromPlus) snap() else spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow),
                    label = "slide"
                )

                LaunchedEffect(selectedIndex) {
                    previousIndex = selectedIndex
                }

                Box(
                    modifier = Modifier
                        .offset(x = indicatorOffset)
                        .graphicsLayer {
                            alpha = indicatorAlpha // Aplicamos solo la transparencia
                        }
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
                    Spacer(modifier = Modifier.weight(1f)) // Hueco central para el botón +
                    BottomNavItem(Modifier.weight(1f), Icons.Default.History) { onItemSelected(2) }
                    BottomNavItem(Modifier.weight(1f), Icons.Default.PersonOutline) { onItemSelected(3) }
                }
            }
        }

        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(AccentLime)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onItemSelected(4) },
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
fun QuickReserveCard(onReserveClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = TextBlack),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Disponibilidad ahora",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "12 Plazas libres",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(AccentLime.copy(alpha = 0.2f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FlashOn,
                        contentDescription = null,
                        tint = AccentLime,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onReserveClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentLime)
            ) {
                Text(
                    text = "RESERVAR AHORA",
                    color = TextBlack,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}

@Composable
fun ParkingStatusCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Tu Plaza Actual", fontSize = 14.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(48.dp).background(AccentLime, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("P1", fontWeight = FontWeight.Black, fontSize = 18.sp, color = TextBlack)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Planta 1 - Plaza 142", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextBlack)
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
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(44.dp).background(BgGray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.History, null, tint = Color.Gray, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text("Parking Centro", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextBlack)
            Text("Ayer, 18:30", fontSize = 12.sp, color = Color.Gray)
        }
        Text("4.50€", fontWeight = FontWeight.Black, fontSize = 16.sp, color = TextBlack)
    }
}
