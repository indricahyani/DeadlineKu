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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun SettingsScreen(
    navController: NavController
) {

    val context = LocalContext.current

    val repository = TaskRepository(context)

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    var showAboutDialog by remember {
        mutableStateOf(false)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF6F8FC)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                }

                Text(
                    text = "Pengaturan",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

            }

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        navController.navigate(Screen.Category.route)
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {

                ListItem(
                    headlineContent = {
                        Text("Kelola Kategori")
                    },
                    supportingContent = {
                        Text("Tambah dan hapus kategori")
                    },
                    trailingContent = {

                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.Gray
                        )

                    },
                    leadingContent = {

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    Color(0xFFE8F0FF),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                Icons.Default.List,
                                null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                        }

                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showDeleteDialog = true
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {

                ListItem(
                    headlineContent = {
                        Text("Hapus Semua Tugas")
                    },
                    supportingContent = {
                        Text("Menghapus seluruh data tugas")
                    },
                    trailingContent = {

                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.Gray
                        )

                    },
                    leadingContent = {

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    Color(0xFFE8F0FF),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                Icons.Default.List,
                                null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                        }

                    }
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showAboutDialog = true
                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(5.dp)
            ) {

                ListItem(
                    headlineContent = {
                        Text("Tentang DeadlineKu")
                    },
                    supportingContent = {
                        Text("Versi 1.0")
                    },
                    trailingContent = {

                        Icon(
                            Icons.Default.KeyboardArrowRight,
                            contentDescription = null,
                            tint = Color.Gray
                        )

                    },
                    leadingContent = {

                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    Color(0xFFE8F0FF),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                Icons.Default.List,
                                null,
                                tint = MaterialTheme.colorScheme.primary
                            )

                        }

                    }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "DeadlineKu",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = "Versi 1.0",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Made with ❤️ using Kotlin",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
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

                                val success = repository.deleteAllTasks()

                                if (success) {
                                    showDeleteDialog = false
                                }
                            }
                        ) {
                            Text(
                                text = "Hapus",
                                color = MaterialTheme.colorScheme.error
                            )
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
                            Text("• SQLite")
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
}