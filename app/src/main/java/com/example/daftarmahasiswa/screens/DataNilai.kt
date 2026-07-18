package com.example.daftarmahasiswa.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daftarmahasiswa.data.Student
import com.example.daftarmahasiswa.ui.theme.DaftarMahasiswaTheme

/**
 * DataNilaiScreen - Halaman untuk menampilkan nilai mahasiswa.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataNilaiScreen(
    student: Student?,
    onBack: () -> Unit = {}
) {
    if (student == null) {
        onBack()
        return
    }

    // Data mata kuliah (contoh statis, bisa dikembangkan agar dinamis)
    val daftarNilai = listOf(
        MataKuliah("TIF401", "Pemrograman Mobile", 85, "A"),
        MataKuliah("TIF402", "Basis Data Lanjut", 78, "B+"),
        MataKuliah("TIF403", "Jaringan Komputer", 83, "A-"),
        MataKuliah("TIF404", "Kecerdasan Buatan", 88, "A"),
        MataKuliah("TIF405", "Sistem Operasi", 74, "B"),
        MataKuliah("TIF406", "Statistika", 81, "A-")
    )

    val ipSementara = hitungIPSementara(daftarNilai)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Data Nilai: ${student.name}", fontWeight = FontWeight.SemiBold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    InfoItem(label = "NIM", value = student.nim)
                    InfoItem(label = "Nama", value = student.name)
                    InfoItem(label = "Semester", value = student.semester.toString())
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            TabelHeader()

            Spacer(modifier = Modifier.height(8.dp))

            Card(
                modifier = Modifier.fillMaxWidth().weight(1f),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(daftarNilai) { mk ->
                        TabelBaris(mataKuliah = mk)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "IP Sementara",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = String.format("%.2f", ipSementara),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

data class MataKuliah(
    val kode: String,
    val nama: String,
    val nilaiAkhir: Int,
    val nilaiHuruf: String
)

@Composable
fun InfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TabelHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Kode",
            modifier = Modifier.weight(1.5f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Mata Kuliah",
            modifier = Modifier.weight(3f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Start
        )
        Text(
            text = "Nilai",
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Huruf",
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun TabelBaris(mataKuliah: MataKuliah) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = mataKuliah.kode,
            modifier = Modifier.weight(1.5f),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Text(
            text = mataKuliah.nama,
            modifier = Modifier.weight(3f),
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start
        )
        Text(
            text = mataKuliah.nilaiAkhir.toString(),
            modifier = Modifier.weight(1f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(4.dp))
                .background(warnaNilaiHuruf(mataKuliah.nilaiHuruf))
                .padding(vertical = 4.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = mataKuliah.nilaiHuruf,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun warnaNilaiHuruf(nilai: String): Color {
    return when (nilai) {
        "A" -> Color(0xFF4CAF50)
        "A-" -> Color(0xFF8BC34A)
        "B+" -> Color(0xFFCDDC39)
        "B" -> Color(0xFFFFC107)
        "B-" -> Color(0xFFFF9800)
        "C+" -> Color(0xFFFF5722)
        "C" -> Color(0xFF795548)
        else -> MaterialTheme.colorScheme.error
    }
}

fun hitungIPSementara(daftar: List<MataKuliah>): Double {
    if (daftar.isEmpty()) return 0.0
    val totalBobot = daftar.sumOf { mk ->
        when {
            mk.nilaiAkhir >= 85 -> 4.0
            mk.nilaiAkhir >= 80 -> 3.7
            mk.nilaiAkhir >= 75 -> 3.3
            mk.nilaiAkhir >= 70 -> 3.0
            mk.nilaiAkhir >= 65 -> 2.7
            mk.nilaiAkhir >= 60 -> 2.3
            mk.nilaiAkhir >= 55 -> 2.0
            mk.nilaiAkhir >= 40 -> 1.0
            else -> 0.0
        }
    }
    return totalBobot / daftar.size
}

@Preview(showBackground = true)
@Composable
fun DataNilaiPreview() {
    DaftarMahasiswaTheme {
        DataNilaiScreen(
            student = Student(1, "Ahmad Fauzi", "220411100001", "Teknik Informatika")
        )
    }
}
