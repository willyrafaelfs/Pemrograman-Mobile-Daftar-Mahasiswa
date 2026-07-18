package com.example.daftarmahasiswa.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.daftarmahasiswa.data.Student

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    students: List<Student>,
    onStudentClick: (Int) -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    
    val filteredStudents = remember(searchQuery, students) {
        if (searchQuery.isBlank()) {
            emptyList()
        } else {
            students.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                it.nim.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cari Mahasiswa", fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ketik nama atau NIM...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (searchQuery.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredStudents, key = { it.id }) { student ->
                        StudentCard(
                            student = student,
                            onClick = { onStudentClick(student.id) }
                        )
                    }
                }
            } else {
                Text(
                    text = "Mulai mengetik untuk mencari mahasiswa.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
