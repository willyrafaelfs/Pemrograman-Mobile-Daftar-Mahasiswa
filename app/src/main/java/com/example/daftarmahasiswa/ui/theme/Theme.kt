package com.example.daftarmahasiswa.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Warna kustom - skema warm amber/teal
val light_primary = Color(0xFF1565C0)
val light_primaryContainer = Color(0xFFD1E4FF)
val light_secondary = Color(0xFF00897B)
val light_secondaryContainer = Color(0xFFB2DFDB)
val light_surface = Color(0xFFFFFFFF)
val light_surfaceVariant = Color(0xFFF0F4F8)
val light_background = Color(0xFFFAFAFA)
val light_error = Color(0xFFD32F2F)

val dark_primary = Color(0xFF90CAF9)
val dark_primaryContainer = Color(0xFF1565C0)
val dark_secondary = Color(0xFF80CBC4)
val dark_surface = Color(0xFF1E1E1E)
val dark_surfaceVariant = Color(0xFF2C2C2C)
val dark_background = Color(0xFF121212)

private val LightColors = lightColorScheme(
    primary = light_primary,
    primaryContainer = light_primaryContainer,
    secondary = light_secondary,
    secondaryContainer = light_secondaryContainer,
    surface = light_surface,
    surfaceVariant = light_surfaceVariant,
    background = light_background,
    error = light_error,
)

private val DarkColors = darkColorScheme(
    primary = dark_primary,
    primaryContainer = dark_primaryContainer,
    secondary = dark_secondary,
    surface = dark_surface,
    surfaceVariant = dark_surfaceVariant,
    background = dark_background,
)

@Composable
fun DaftarMahasiswaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
