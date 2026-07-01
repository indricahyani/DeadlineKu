package com.example.deadlineku.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import com.example.deadlineku.model.Task
import com.example.deadlineku.repository.TaskRepository
import androidx.compose.material3.Button
import androidx.navigation.NavController


@Composable
fun TaskDetailScreen(
    taskId: String,
    navController: NavController
) {

    val repository = TaskRepository()

    var task by remember {
        mutableStateOf<Task?>(null)
    }

    LaunchedEffect(Unit) {
        repository.getTaskById(taskId) {
            task = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Detail Tugas",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        task?.let {

            Text(
                text = it.title,
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Deskripsi",
                style = MaterialTheme.typography.titleMedium
            )

            Text(it.description)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Kategori: ${it.category}"
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Deadline: ${it.deadlineDate} ${it.deadlineTime}"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = if (it.completed)
                    "Status: Selesai"
                else
                    "Status: Belum Selesai"
            )

            if (!it.completed) {

                Button(
                    onClick = {

                        val updatedTask = Task(
                            id = it.id,
                            title = it.title,
                            description = it.description,
                            category = it.category,
                            deadlineDate = it.deadlineDate,
                            deadlineTime = it.deadlineTime,
                            completed = true
                        )

                        repository.updateTask(updatedTask)

                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Tandai Selesai")
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    navController.navigate("edit_task/${it.id}")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Edit Tugas")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {

                    repository.deleteTask(it.id)

                    navController.popBackStack()

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Hapus Tugas")
            }
        }
    }
}