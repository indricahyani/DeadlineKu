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
import androidx.compose.material3.Checkbox
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import com.example.deadlineku.model.Category
import com.example.deadlineku.repository.CategoryRepository
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current

    val repository = TaskRepository(context)

    var taskList by remember {
        mutableStateOf(listOf<Task>())
    }

    var searchText by remember {
        mutableStateOf("")
    }

    val categoryRepository = CategoryRepository(context)

    var categoryList by remember {
        mutableStateOf(listOf<Category>())
    }

    var selectedStatus by remember {
        mutableStateOf("Semua")
    }

    var selectedCategory by remember {
        mutableStateOf("Semua")
    }

    LaunchedEffect(Unit) {
        taskList = repository.getTasks()
        categoryList = categoryRepository.getCategories()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        )   {

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

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                listOf(
                    "Semua",
                    "Belum Selesai",
                    "Selesai"
                ).forEach { status ->

                    FilterChip(

                        selected = selectedStatus == status,

                        onClick = {
                            selectedStatus = status
                        },

                        label = {
                            Text(status)
                        }
                    )
                }
            }

            Row(
                modifier = Modifier.horizontalScroll(
                    rememberScrollState()
                ),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                FilterChip(

                    selected = selectedCategory == "Semua",

                    onClick = {
                        selectedCategory = "Semua"
                    },

                    label = {
                        Text("Semua")
                    }
                )

                categoryList.forEach { category ->

                    FilterChip(

                        selected = selectedCategory == category.name,

                        onClick = {
                            selectedCategory = category.name
                        },

                        label = {
                            Text(category.name)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            val filteredTasks = taskList.filter { task ->

                val searchMatch =
                    task.title.contains(searchText, true) ||
                            task.description.contains(searchText, true)

                val statusMatch =
                    when (selectedStatus) {

                        "Belum Selesai" ->
                            !task.completed

                        "Selesai" ->
                            task.completed

                        else ->
                            true
                    }

                val categoryMatch =
                    selectedCategory == "Semua" ||
                            task.category == selectedCategory

                searchMatch &&
                        statusMatch &&
                        categoryMatch
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

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(task.description)

                                    Spacer(modifier = Modifier.height(8.dp))

                                    AssistChip(
                                        onClick = { },
                                        label = {
                                            Text(task.category)
                                        }
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text("📅 ${task.deadlineDate}")
                                    Text("🕒 ${task.deadlineTime}")

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Text(
                                        text = if (task.completed)
                                            "✅ Selesai"
                                        else
                                            "⏳ Belum Selesai"
                                    )
                                }

                                Checkbox(
                                    checked = task.completed,
                                    onCheckedChange = { checked ->

                                        repository.updateTaskStatus(
                                            task.id,
                                            checked
                                        )

                                        taskList = repository.getTasks()
                                    }
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

