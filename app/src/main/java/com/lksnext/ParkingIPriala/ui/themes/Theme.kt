package com.lksnext.ParkingIPriala.ui.themes

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.lksnext.ParkingIPriala.Typography

private val BgDeep = Color(0xFF080D1A)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF2563EB),
    secondary = PurpleGrey80,
    tertiary = Pink80,
    background = BgDeep,
    surface = BgDeep,
    onPrimary = Color.White,
    onBackground = Color(0xFFE4ECF7),
    onSurface = Color(0xFFE4ECF7)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2563EB),
    secondary = PurpleGrey40,
    tertiary = Pink40,
    background = BgDeep,
    surface = BgDeep,
    onPrimary = Color.White,
    onBackground = Color(0xFFE4ECF7),
    onSurface = Color(0xFFE4ECF7)
)

@Composable
fun ParkingIPrialaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}