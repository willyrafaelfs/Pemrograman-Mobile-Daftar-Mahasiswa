package com.example.daftarmahasiswa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.daftarmahasiswa.navigation.AppNavigation
import com.example.daftarmahasiswa.ui.theme.DaftarMahasiswaTheme

/**
 * MainActivity - Entry point aplikasi.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Task 5: Dark mode toggle - simpan preferensi tema menggunakan rememberSaveable
            var isDarkMode by rememberSaveable { mutableStateOf(false) }

            DaftarMahasiswaTheme(darkTheme = isDarkMode) {
                // AppNavigation mengatur semua halaman & perpindahan
                AppNavigation(
                    isDarkMode = isDarkMode,
                    onThemeChange = { isDarkMode = it }
                )
            }
        }
    }
}
