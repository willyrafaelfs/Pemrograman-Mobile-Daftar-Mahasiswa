package com.example.daftarmahasiswa.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.daftarmahasiswa.data.Student
import com.example.daftarmahasiswa.ui.theme.DaftarMahasiswaTheme

/**
 * ============================================================
 * HOME SCREEN: Daftar Mahasiswa
 * ============================================================
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    students: List<Student>,              // Data dari parent
    onStudentClick: (Int) -> Unit = {},   // Callback klik item (kirim ID)
    onAddClick: () -> Unit = {}           // Callback klik tombol tambah
) {
    // Task 4: State untuk search
    var searchQuery by rememberSaveable { mutableStateOf("") }
    
    // Filter students berdasarkan query
    val filteredStudents = remember(searchQuery, students) {
        students.filter {
            it.name.contains(searchQuery, ignoreCase = true) ||
            it.nim.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        // ── TOP APP BAR ──
        topBar = {
            Column(
                modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                TopAppBar(
                    title = {
                        Text(
                            text = "📋 Daftar Mahasiswa",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
                
                // Task 4: TextField di HomeScreen untuk mencari mahasiswa
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari mahasiswa...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Hapus Pencarian")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    )
                )
            }
        },

        // ── FLOATING ACTION BUTTON ──
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tambah Mahasiswa"
                )
            }
        }
    ) { innerPadding ->

        if (filteredStudents.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = if (searchQuery.isEmpty()) Icons.Default.PersonOff else Icons.Default.SearchOff,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (searchQuery.isEmpty()) "Belum ada mahasiswa" else "Mahasiswa tidak ditemukan",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Menampilkan ${filteredStudents.size} mahasiswa",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                items(
                    items = filteredStudents,
                    key = { it.id }
                ) { student ->
                    StudentCard(
                        student = student,
                        onClick = { onStudentClick(student.id) }
                    )
                }
            }
        }
    }
}

/**
 * ============================================================
 * STUDENT CARD: Satu item mahasiswa di daftar
 * ============================================================
 *
 * Komponen REUSABLE — menerima data via parameter.
 * Tidak tahu tentang Navigation, state management, dll.
 * Hanya tugas: tampilkan data student & kirim event onClick.
 *
 * @param student Data mahasiswa yang ditampilkan
 * @param onClick Callback saat card di-klik
 */
@Composable
fun StudentCard(
    student: Student,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),   // Seluruh card bisa di-klik
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ── Avatar (huruf pertama nama) ──
            Surface(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = student.name.first().uppercase(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // ── Info mahasiswa ──
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis   // "..." jika terlalu panjang
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = student.nim,
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = student.jurusan,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.outline
                )
            }

            // ── Arrow icon ──
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Lihat Detail",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ============================================================
// PREVIEW
// ============================================================
// Karena State Hoisting, kita bisa preview TANPA Navigation!
// Cukup berikan data dummy sebagai parameter.

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    DaftarMahasiswaTheme {
        HomeScreen(
            students = listOf(
                Student(1, "Ahmad Fauzi", "20210001", "Teknik Informatika"),
                Student(2, "Siti Aisyah", "20210002", "Sistem Informasi"),
                Student(3, "Budi Santoso", "20210003", "Teknik Informatika"),
            )
        )
    }
}

@Preview(showBackground = true, name = "Empty State")
@Composable
fun HomeScreenEmptyPreview() {
    DaftarMahasiswaTheme {
        HomeScreen(students = emptyList())
    }
}
