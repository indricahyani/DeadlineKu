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

@Composable
fun SettingsScreen(
    navController: NavController
) {

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
            modifier = Modifier.fillMaxWidth()
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
            modifier = Modifier.fillMaxWidth()
        ) {

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
    }
}