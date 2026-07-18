package com.example.daftarmahasiswa.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daftarmahasiswa.data.Student
import com.example.daftarmahasiswa.ui.theme.DaftarMahasiswaTheme

/**
 * ============================================================
 * ADD STUDENT SCREEN: Form Tambah Mahasiswa Baru
 * ============================================================
 *
 * KONSEP YANG DITERAPKAN:
 *
 * 1. rememberSaveable
 *    Semua input form menggunakan rememberSaveable, BUKAN remember.
 *    Mengapa? Karena jika user merotasi layar saat mengisi form,
 *    data yang sudah diketik TIDAK HILANG.
 *
 *    Bayangkan: Anda sudah mengisi 5 field, lalu rotate layar.
 *    - remember → semua input HILANG (frustasi!)
 *    - rememberSaveable → semua input TETAP ADA (nyaman!)
 *
 * 2. STATE HOISTING (untuk Screen-level)
 *    - State input form disimpan di DALAM screen ini (karena
 *      state form hanya relevan untuk screen ini sendiri)
 *    - Tapi HASIL akhir (Student baru) dikirim ke PARENT
 *      melalui callback onSave
 *
 *    Jadi ada 2 level hoisting:
 *    a. Form state → lokal di AddStudentScreen
 *    b. Navigasi & data → di-hoist ke AppNavigation
 *
 * 3. FORM VALIDATION
 *    Validasi sederhana sebelum menyimpan.
 *    Tombol simpan hanya aktif jika field wajib terisi.
 *
 * @param onSave Callback saat user menyimpan (mengirim Student baru)
 * @param onBack Callback untuk kembali
 * @param nextId ID untuk mahasiswa baru
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddStudentScreen(
    onSave: (Student) -> Unit = {},
    onBack: () -> Unit = {},
    nextId: Int = 1
) {
    // ════════════════════════════════════════════════
    // STATE FORM - menggunakan rememberSaveable
    // ════════════════════════════════════════════════
    // rememberSaveable = state bertahan meskipun:
    // - Layar dirotasi (configuration change)
    // - Proses dibunuh oleh sistem (process death)
    //
    // Ini WAJIB untuk form input agar data user tidak hilang!

    var nama by rememberSaveable { mutableStateOf("") }
    var nim by rememberSaveable { mutableStateOf("") }
    var jurusan by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }

    // State untuk menampilkan pesan error
    var showError by rememberSaveable { mutableStateOf(false) }

    // Validasi: cek apakah field wajib sudah terisi
    val isFormValid = nama.isNotBlank() && nim.isNotBlank() && jurusan.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Mahasiswa", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Header ──
            Text(
                text = "📝 Data Mahasiswa Baru",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Isi data berikut untuk menambahkan mahasiswa baru. Field dengan * wajib diisi.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ════════════════════════════════════════
            // INPUT FIELDS
            // ════════════════════════════════════════
            // Setiap OutlinedTextField menerapkan state hoisting:
            // - value = state saat ini
            // - onValueChange = callback yang mengubah state
            //
            // TextField TIDAK menyimpan teksnya sendiri.
            // State disimpan di variabel (nama, nim, dll)
            // dan DIBERIKAN ke TextField melalui parameter value.

            FormTextField(
                value = nama,                       // State → TextField
                onValueChange = { nama = it },      // TextField → State
                label = "Nama Lengkap *",
                icon = Icons.Default.Person,
                isError = showError && nama.isBlank(),
                errorMessage = "Nama wajib diisi"
            )

            FormTextField(
                value = nim,
                onValueChange = { nim = it },
                label = "NIM (Nomor Induk Mahasiswa) *",
                icon = Icons.Default.Badge,
                keyboardType = KeyboardType.Number,
                isError = showError && nim.isBlank(),
                errorMessage = "NIM wajib diisi"
            )

            FormTextField(
                value = jurusan,
                onValueChange = { jurusan = it },
                label = "Jurusan / Program Studi *",
                icon = Icons.Default.School,
                isError = showError && jurusan.isBlank(),
                errorMessage = "Jurusan wajib diisi"
            )

            FormTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email (opsional)",
                icon = Icons.Default.Email,
                keyboardType = KeyboardType.Email
            )

            FormTextField(
                value = phone,
                onValueChange = { phone = it },
                label = "No. Telepon (opsional)",
                icon = Icons.Default.Phone,
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(8.dp))

            // ── Tombol Simpan ──
            Button(
                onClick = {
                    if (isFormValid) {
                        // Buat objek Student baru dan kirim ke parent
                        val newStudent = Student(
                            id = nextId,
                            name = nama.trim(),
                            nim = nim.trim(),
                            jurusan = jurusan.trim(),
                            email = email.trim(),
                            phone = phone.trim()
                        )
                        onSave(newStudent)   // Callback ke parent!
                    } else {
                        showError = true     // Tampilkan pesan error
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Simpan", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

            // ── Tombol Batal ──
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Batal")
            }
        }
    }
}

/**
 * ============================================================
 * FORM TEXT FIELD: Komponen input yang reusable
 * ============================================================
 *
 * State Hoisting diterapkan di sini juga!
 * FormTextField TIDAK menyimpan state sendiri.
 * Parent memberikan value dan menerima onValueChange.
 *
 * @param value Teks saat ini (dari parent state)
 * @param onValueChange Callback saat teks berubah
 * @param label Label field
 * @param icon Icon di kiri field
 * @param keyboardType Tipe keyboard (text, number, email, phone)
 * @param isError Apakah field dalam keadaan error
 * @param errorMessage Pesan error yang ditampilkan
 */
@Composable
fun FormTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String = ""
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            leadingIcon = {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = if (isError)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        // Tampilkan error message jika ada error
        if (isError && errorMessage.isNotEmpty()) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

// ── PREVIEW ──
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddStudentScreenPreview() {
    DaftarMahasiswaTheme {
        AddStudentScreen()
    }
}
