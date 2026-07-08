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
import com.example.deadlineku.model.Category
import com.example.deadlineku.repository.TaskRepository
import androidx.compose.ui.platform.LocalContext
import com.example.deadlineku.repository.CategoryRepository
import androidx.compose.material3.*
import java.text.SimpleDateFormat
import java.util.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: Int,
    navController: NavController
) {

    val context = LocalContext.current

    val repository = TaskRepository(context)
    val categoryRepository = CategoryRepository(context)

    var task by remember {
        mutableStateOf<Task?>(null)
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var deadlineDate by remember { mutableStateOf("") }
    var deadlineTime by remember { mutableStateOf("") }

    var showValidationDialog by remember {
        mutableStateOf(false)
    }

    var validationMessage by remember {
        mutableStateOf("")
    }

    var categoryList by remember {
        mutableStateOf(listOf<Category>())
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = null
    )

    var showTimePicker by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        categoryList = categoryRepository.getCategories()

        task = repository.getTaskById(taskId)

        task?.let {

            title = it.title
            description = it.description
            category = it.category
            deadlineDate = it.deadlineDate
            try {

                val formatter = SimpleDateFormat(
                    "dd/MM/yyyy",
                    Locale.getDefault()
                )

                formatter.timeZone = TimeZone.getTimeZone("UTC")

                val date = formatter.parse(it.deadlineDate)

                datePickerState.selectedDateMillis = date?.time

            } catch (_: Exception) {
            }
            deadlineTime = it.deadlineTime

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

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {

            OutlinedTextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                label = {
                    Text("Kategori")
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    expanded = false
                }
            ) {

                categoryList.forEach { item ->

                    DropdownMenuItem(
                        text = {
                            Text(item.name)
                        },
                        onClick = {

                            category = item.name
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = deadlineDate,
            onValueChange = {},
            readOnly = true,
            label = {
                Text("Tanggal Deadline")
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {

                TextButton(
                    onClick = {
                        showDatePicker = true
                    }
                ) {
                    Text("📅")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = deadlineTime,
            onValueChange = {},
            readOnly = true,
            label = {
                Text("Waktu Deadline")
            },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {

                TextButton(
                    onClick = {
                        showTimePicker = true
                    }
                ) {
                    Text("🕒")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {

                val errors = mutableListOf<String>()

                if (title.isBlank()) {
                    errors.add("• Judul tugas")
                }

                if (category.isBlank()) {
                    errors.add("• Kategori")
                }

                if (deadlineDate.isBlank()) {
                    errors.add("• Tanggal deadline")
                }

                if (deadlineTime.isBlank()) {
                    errors.add("• Waktu deadline")
                }

                if (errors.isNotEmpty()) {

                    validationMessage =
                        "Mohon lengkapi data berikut:\n\n" +
                                errors.joinToString("\n")

                    showValidationDialog = true

                    return@Button
                }

                val updatedTask = Task(
                    id = taskId,
                    title = title,
                    description = description,
                    category = category,
                    deadlineDate = deadlineDate,
                    deadlineTime = deadlineTime,
                    completed = task?.completed ?: false
                )

                val success = repository.updateTask(updatedTask)

                if (success) {
                    navController.popBackStack()
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Perbarui Tugas")
        }
    }

    if (showDatePicker) {

        DatePickerDialog(

            onDismissRequest = {
                showDatePicker = false
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        datePickerState.selectedDateMillis?.let {

                            val formatter = SimpleDateFormat(
                                "dd/MM/yyyy",
                                Locale.getDefault()
                            )

                            deadlineDate = formatter.format(Date(it))
                        }

                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Batal")
                }
            }

        ) {

            DatePicker(
                state = datePickerState
            )
        }
    }

    if (showTimePicker) {

        val parts = deadlineTime.split(":")

        val hour = parts.getOrNull(0)?.toIntOrNull() ?: 0
        val minute = parts.getOrNull(1)?.toIntOrNull() ?: 0

        val timePickerState = rememberTimePickerState(
            initialHour = hour,
            initialMinute = minute,
            is24Hour = true
        )

        AlertDialog(

            onDismissRequest = {
                showTimePicker = false
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        deadlineTime =
                            "%02d:%02d".format(
                                timePickerState.hour,
                                timePickerState.minute
                            )

                        showTimePicker = false
                    }
                ) {
                    Text("OK")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showTimePicker = false
                    }
                ) {
                    Text("Batal")
                }
            },

            text = {
                TimePicker(
                    state = timePickerState
                )
            }
        )
    }

    if (showValidationDialog) {

        AlertDialog(

            onDismissRequest = {
                showValidationDialog = false
            },

            title = {
                Text("Data Belum Lengkap")
            },

            text = {
                Text(validationMessage)
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        showValidationDialog = false
                    }
                ) {
                    Text("Mengerti")
                }

            }

        )

    }
}