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
import androidx.navigation.NavController
import com.example.deadlineku.navigation.Screen
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.OutlinedTextField


@Composable
fun HomeScreen(navController: NavController) {

    val repository = TaskRepository()

    var taskList by remember {
        mutableStateOf(listOf<Task>())
    }

    var searchText by remember {
        mutableStateOf("")
    }

    LaunchedEffect(Unit) {
        repository.getTasks {
            taskList = it
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

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

            OutlinedTextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                },
                label = {
                    Text("Cari tugas")
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Spacer(modifier = Modifier.height(16.dp))

            val filteredTasks = taskList.filter {

                it.title.contains(
                    searchText,
                    ignoreCase = true
                ) ||

                        it.description.contains(
                            searchText,
                            ignoreCase = true
                        )
            }

            if (filteredTasks.isEmpty()) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 64.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "📝",
                        style = MaterialTheme.typography.displayLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text =
                            if (searchText.isBlank())
                                "Belum ada tugas"
                            else
                                "Tugas tidak ditemukan"
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text =
                            if (searchText.isBlank())
                                "Tekan tombol + untuk menambahkan tugas"
                            else
                                "Coba kata kunci lain"
                    )
                }

            } else {

                LazyColumn {

                    items(filteredTasks) { task ->

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

                                AssistChip(
                                    onClick = { },
                                    label = {
                                        Text(task.category)
                                    }
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "📅 ${task.deadlineDate}",
                                    style = MaterialTheme.typography.bodyMedium
                                )

                                Text(
                                    text = "🕒 ${task.deadlineTime}"
                                )

                                Text(
                                    text =
                                        if (task.isCompleted)
                                            "✅ Selesai"
                                        else
                                            "⏳ Belum Selesai"
                                )
                            }
                        }
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                navController.navigate(Screen.AddTask.route)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Tambah Tugas"
            )
        }
    }
}