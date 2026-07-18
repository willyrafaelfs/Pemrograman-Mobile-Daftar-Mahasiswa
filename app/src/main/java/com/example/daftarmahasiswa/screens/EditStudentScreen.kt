package com.example.daftarmahasiswa.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daftarmahasiswa.data.Student
import com.example.daftarmahasiswa.ui.theme.DaftarMahasiswaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditStudentScreen(
    student: Student?,
    onSave: (Student) -> Unit = {},
    onBack: () -> Unit = {}
) {
    // Jika data student tidak ditemukan, tampilkan pesan error atau kembali
    if (student == null) {
        LaunchedEffect(Unit) {
            onBack()
        }
        return
    }

    // Menginisialisasi state dengan data mahasiswa yang ada
    var nama by rememberSaveable { mutableStateOf(student.name) }
    var nim by rememberSaveable { mutableStateOf(student.nim) }
    var jurusan by rememberSaveable { mutableStateOf(student.jurusan) }
    var email by rememberSaveable { mutableStateOf(student.email) }
    var phone by rememberSaveable { mutableStateOf(student.phone) }

    var showError by rememberSaveable { mutableStateOf(false) }

    val isFormValid = nama.isNotBlank() && nim.isNotBlank() && jurusan.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Mahasiswa", fontWeight = FontWeight.Bold) },
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
            Text(
                text = "✏️ Ubah Data Mahasiswa",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Perbarui informasi mahasiswa di bawah ini.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            FormTextField(
                value = nama,
                onValueChange = { nama = it },
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

            Button(
                onClick = {
                    if (isFormValid) {
                        val updatedStudent = student.copy(
                            name = nama.trim(),
                            nim = nim.trim(),
                            jurusan = jurusan.trim(),
                            email = email.trim(),
                            phone = phone.trim()
                        )
                        onSave(updatedStudent)
                    } else {
                        showError = true
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
                Icon(Icons.Default.Update, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Update Data", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }

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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun EditStudentScreenPreview() {
    DaftarMahasiswaTheme {
        EditStudentScreen(
            student = Student(1, "Ahmad Fauzi", "20210001", "Teknik Informatika")
        )
    }
}
