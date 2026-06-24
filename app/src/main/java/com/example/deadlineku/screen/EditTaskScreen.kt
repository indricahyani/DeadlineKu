package com.example.deadlineku.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.deadlineku.model.Task
import com.example.deadlineku.repository.TaskRepository

@Composable
fun EditTaskScreen(
    taskId: String,
    navController: NavController
) {

    val repository = TaskRepository()

    var task by remember {
        mutableStateOf<Task?>(null)
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var deadlineDate by remember { mutableStateOf("") }
    var deadlineTime by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {

        repository.getTaskById(taskId) {

            task = it

            if (it != null) {
                title = it.title
                description = it.description
                category = it.category
                deadlineDate = it.deadlineDate
                deadlineTime = it.deadlineTime
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("Edit Tugas")

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

                val updatedTask = Task(
                    id = taskId,
                    title = title,
                    description = description,
                    category = category,
                    deadlineDate = deadlineDate,
                    deadlineTime = deadlineTime,
                    isCompleted = task?.isCompleted ?: false
                )

                repository.updateTask(updatedTask)

                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Perbarui Tugas")
        }
    }
}