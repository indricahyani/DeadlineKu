package com.example.deadlineku.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deadlineku.model.Task
import com.example.deadlineku.repository.TaskRepository
import androidx.navigation.NavController
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import com.example.deadlineku.model.Category
import com.example.deadlineku.repository.CategoryRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavController
) {

    val repository = TaskRepository()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var deadlineDate by remember { mutableStateOf("") }
    var deadlineTime by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Tambah Tugas")

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Judul Tugas") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Deskripsi") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = category,
            onValueChange = { category = it },
            label = { Text("Kategori") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = deadlineDate,
            onValueChange = { deadlineDate = it },
            label = { Text("Tanggal Deadline") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = deadlineTime,
            onValueChange = { deadlineTime = it },
            label = { Text("Waktu Deadline") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val task = Task(
                    title = title,
                    description = description,
                    category = category,
                    deadlineDate = deadlineDate,
                    deadlineTime = deadlineTime,
                    isCompleted = false
                )

                repository.addTask(task)

                navController.popBackStack()

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Tugas")
        }
    }
}