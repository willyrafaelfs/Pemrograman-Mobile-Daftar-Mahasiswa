package com.example.daftarmahasiswa.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.daftarmahasiswa.ui.theme.DaftarMahasiswaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditPhotoScreen(
    onBack: () -> Unit = {}
) {
    // State mode edit untuk data kontak
    var isEditing by rememberSaveable { mutableStateOf(false) }

    // Data profil kontak (Hardcoded untuk contoh, bisa di-hoist jika perlu)
    var email by rememberSaveable { mutableStateOf("ahmad.fauzi@student.ac.id") }
    var telepon by rememberSaveable { mutableStateOf("+62 812-3456-7890") }
    var alamat by rememberSaveable { mutableStateOf("Malang, Jawa Timur") }

    // State URI foto profil
    var photoUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    // Launcher untuk membuka galeri gambar
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri?.let { photoUri = it }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profil", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali")
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Spacer dihapus agar konten lebih naik ke atas
            // Bagian foto profil yang dapat diklik untuk mengganti gambar
            ProfilePhotoSection(
                photoUri = photoUri,
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Ahmad Fauzi Rahman",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "NIM: 20210001",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "📞 Informasi Kontak",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    EditRow(
                        icon = Icons.Default.Email,
                        label = "Email",
                        value = email,
                        onValueChange = { email = it },
                        enabled = isEditing
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    EditRow(
                        icon = Icons.Default.Phone,
                        label = "Telepon",
                        value = telepon,
                        onValueChange = { telepon = it },
                        enabled = isEditing
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    EditRow(
                        icon = Icons.Default.LocationOn,
                        label = "Alamat",
                        value = alamat,
                        onValueChange = { alamat = it },
                        enabled = isEditing
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { isEditing = !isEditing },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isEditing) "Simpan Perubahan" else "Edit Profil",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun ProfilePhotoSection(
    photoUri: Uri?,
    onClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                )
                .border(4.dp, MaterialTheme.colorScheme.primary, CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            if (photoUri != null) {
                AsyncImage(
                    model = photoUri,
                    contentDescription = "Foto Profil",
                    modifier = Modifier.fillMaxSize().clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Foto Profil",
                    modifier = Modifier.size(70.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .border(2.dp, Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Ubah Foto",
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
fun EditRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
            if (enabled) {
                TextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    )
                )
            } else {
                Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileEditPhotoScreenPreview() {
    DaftarMahasiswaTheme {
        ProfileEditPhotoScreen()
    }
}
