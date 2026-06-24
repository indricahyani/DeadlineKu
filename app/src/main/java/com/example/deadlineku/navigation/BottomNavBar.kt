package com.example.deadlineku.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavBar(
    selectedItem: String,
    onItemSelected: (String) -> Unit
) {

    NavigationBar {

        NavigationBarItem(
            selected = selectedItem == "home",
            onClick = {
                onItemSelected("home")
            },
            icon = {
                Icon(Icons.AutoMirrored.Filled.List, null)
            },
            label = {
                Text("Tugas")
            }
        )

        NavigationBarItem(
            selected = selectedItem == "calendar",
            onClick = {
                onItemSelected("calendar")
            },
            icon = {
                Icon(Icons.Default.DateRange, null)
            },
            label = {
                Text("Kalender")
            }
        )

        NavigationBarItem(
            selected = selectedItem == "settings",
            onClick = {
                onItemSelected("settings")
            },
            icon = {
                Icon(Icons.Default.Settings, null)
            },
            label = {
                Text("Pengaturan")
            }
        )
    }
}