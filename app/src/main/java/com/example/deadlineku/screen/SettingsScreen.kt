package com.example.deadlineku.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.deadlineku.navigation.Screen
import androidx.compose.runtime.*
import com.example.deadlineku.repository.TaskRepository
import androidx.compose.material3.*

@Composable
fun SettingsScreen(
    navController: NavController
) {

    val repository = TaskRepository()

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var showAboutDialog by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Pengaturan",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(Screen.Category.route)
                }
        ) {

            ListItem(
                headlineContent = {
                    Text("Kelola Kategori")
                },
                supportingContent = {
                    Text("Tambah dan hapus kategori")
                },
                leadingContent = {
                    Icon(
                        Icons.Default.List,
                        null
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showDeleteDialog = true
                }
        ) {

            ListItem(
                headlineContent = {
                    Text("Hapus Semua Tugas")
                },
                supportingContent = {
                    Text("Menghapus seluruh data tugas")
                },
                leadingContent = {
                    Icon(
                        Icons.Default.Delete,
                        null
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    showAboutDialog = true
                }
        )   {

            ListItem(
                headlineContent = {
                    Text("Tentang DeadlineKu")
                },
                supportingContent = {
                    Text("Versi 1.0")
                },
                leadingContent = {
                    Icon(
                        Icons.Default.Info,
                        null
                    )
                }
            )
        }
        if (showDeleteDialog) {

            AlertDialog(

                onDismissRequest = {
                    showDeleteDialog = false
                },

                title = {
                    Text("Hapus Semua Tugas")
                },

                text = {
                    Text(
                        "Semua tugas akan dihapus secara permanen. Apakah yakin ingin melanjutkan?"
                    )
                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            repository.deleteAllTasks()

                            showDeleteDialog = false
                        }
                    ) {
                        Text("Hapus")
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {
                            showDeleteDialog = false
                        }
                    ) {
                        Text("Batal")
                    }
                }
            )
        }

        if (showAboutDialog) {

            AlertDialog(

                onDismissRequest = {
                    showAboutDialog = false
                },

                title = {
                    Text("Tentang DeadlineKu")
                },

                text = {

                    Column {

                        Text("Versi 1.0")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            "DeadlineKu adalah aplikasi manajemen tugas yang membantu pengguna mencatat, mengelola, dan memantau deadline agar pekerjaan lebih terorganisir."
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("Dikembangkan menggunakan:")

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("• Kotlin")
                        Text("• Jetpack Compose")
                        Text("• Firebase Firestore")
                        Text("• Material 3")

                        Spacer(modifier = Modifier.height(12.dp))

                        Text("© 2026 DeadlineKu")
                    }
                },

                confirmButton = {

                    TextButton(
                        onClick = {
                            showAboutDialog = false
                        }
                    ) {
                        Text("Tutup")
                    }
                }
            )
        }
    }
}