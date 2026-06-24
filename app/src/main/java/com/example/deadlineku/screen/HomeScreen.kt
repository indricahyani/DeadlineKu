package com.example.deadlineku.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deadlineku.model.Task
import com.example.deadlineku.repository.TaskRepository
import androidx.compose.material3.Button
import androidx.navigation.NavController
import com.example.deadlineku.navigation.Screen
import androidx.compose.foundation.clickable

@Composable
fun HomeScreen(navController: NavController) {

    val repository = TaskRepository()

    var taskList by remember {
        mutableStateOf(listOf<Task>())
    }

    LaunchedEffect(Unit) {
        repository.getTasks {
            taskList = it
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Daftar Tugas",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                navController.navigate(Screen.AddTask.route)
            }
        ) {
            Text("Tambah Tugas")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {

            items(taskList) { task ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                        .clickable {
                            navController.navigate("task_detail/${task.id}")
                        }
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(task.description)

                        Spacer(modifier = Modifier.height(4.dp))

                        Text("Kategori: ${task.category}")

                        Text("Deadline: ${task.deadlineDate}")
                        Text(
                            text = if (task.isCompleted)
                                "Status: Selesai"
                            else
                                "Status: Belum Selesai"
                        )
                    }
                }
            }
        }
    }
}