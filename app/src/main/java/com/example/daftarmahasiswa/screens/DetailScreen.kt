package com.example.daftarmahasiswa.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daftarmahasiswa.data.Student
import com.example.daftarmahasiswa.ui.theme.DaftarMahasiswaTheme

/**
 * ============================================================
 * DETAIL SCREEN: Informasi Lengkap Mahasiswa
 * ============================================================
 *
 * KONSEP STATE HOISTING yang diterapkan:
 *
 * Screen ini TIDAK tahu tentang NavController!
 * Screen hanya menerima:
 * 1. student: Student? → data yang ditampilkan
 * 2. onBack: () -> Unit → callback untuk kembali
 *
 * Parent (AppNavigation) yang menentukan:
 * - Student mana yang dikirim (berdasarkan route parameter)
 * - Apa yang terjadi saat onBack dipanggil (popBackStack)
 *
 * Keuntungan:
 * ✅ Bisa di-preview tanpa Navigation
 * ✅ Bisa di-test tanpa Navigation
 * ✅ Bisa digunakan ulang di context berbeda
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    student: Student?,                // Data mahasiswa (nullable jika tidak ditemukan)
    onBack: () -> Unit = {},           // Callback untuk kembali
    onEdit: (Int) -> Unit = {},        // Task 1: Callback untuk edit
    onDelete: (Int) -> Unit = {},       // Task 2: Callback untuk hapus
    onViewGrades: (Int) -> Unit = {}    // Task: Callback untuk lihat nilai
) {
    // State untuk mengontrol dialog konfirmasi hapus
    var showDeleteDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Mahasiswa", fontWeight = FontWeight.Bold) },
                // Tombol back di TopAppBar
                navigationIcon = {
                    IconButton(onClick = onBack) {   // Callback ke parent!
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                actions = {
                    // Task 1: Tombol Edit di TopAppBar
                    if (student != null) {
                        IconButton(onClick = { onEdit(student.id) }) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit")
                        }
                        // Task 2: Tombol Delete di TopAppBar (Sekarang membuka dialog)
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = "Hapus")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->

        // ── DIALOG KONFIRMASI HAPUS ──
        if (showDeleteDialog && student != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text(text = "Hapus Data") },
                text = { Text(text = "Apakah Anda yakin ingin menghapus data ${student.name}?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                            onDelete(student.id)
                        }
                    ) {
                        Text(text = "Hapus", color = MaterialTheme.colorScheme.error)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text(text = "Batal")
                    }
                }
            )
        }

        // Jika student null (tidak ditemukan), tampilkan pesan error
        if (student == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.ErrorOutline,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Mahasiswa tidak ditemukan",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
            return@Scaffold
        }

        // ── Konten utama ──
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Avatar besar ──
            Surface(
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = student.name.first().uppercase(),
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Nama & NIM ──
            Text(
                text = student.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "NIM: ${student.nim}",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ── Card: Info Akademik ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "🎓 Info Akademik",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    DetailInfoRow(
                        icon = Icons.Default.School,
                        label = "Jurusan",
                        value = student.jurusan
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailInfoRow(
                        icon = Icons.Default.CalendarMonth,
                        label = "Semester",
                        value = "Semester ${student.semester}"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // ── Card: Kontak ──
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "📞 Kontak",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    DetailInfoRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = student.email.ifEmpty { "-" }
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailInfoRow(
                        icon = Icons.Default.Phone,
                        label = "Telepon",
                        value = student.phone.ifEmpty { "-" }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Tombol Lihat Nilai ──
            Button(
                onClick = { if (student != null) onViewGrades(student.id) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(Icons.Default.Assessment, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Lihat Data Nilai")
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ── Tombol Kembali ──
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Kembali ke Daftar")
            }
        }
    }
}

/**
 * Satu baris informasi detail (icon + label + value).
 * Komponen reusable yang dipakai berkali-kali di DetailScreen.
 */
@Composable
fun DetailInfoRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surface,
                RoundedCornerShape(8.dp)
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

// ── PREVIEW ──
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DetailScreenPreview() {
    DaftarMahasiswaTheme {
        DetailScreen(
            student = Student(
                id = 1,
                name = "Ahmad Fauzi Rahman",
                nim = "20210001",
                jurusan = "Teknik Informatika",
                email = "ahmad@student.ac.id",
                phone = "+62 812-3456-7890",
                semester = 6
            )
        )
    }
}

@Preview(showBackground = true, name = "Not Found")
@Composable
fun DetailScreenNotFoundPreview() {
    DaftarMahasiswaTheme {
        DetailScreen(student = null)
    }
}
