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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.TimePicker
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.rememberTimePickerState
import java.util.Calendar
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import com.example.deadlineku.navigation.Screen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    navController: NavController
) {
    val context = LocalContext.current

    val repository = TaskRepository(context)

    val categoryRepository = CategoryRepository(context)

    var categoryList by remember {
        mutableStateOf(listOf<Category>())
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        categoryList = categoryRepository.getCategories()
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var showCategoryDialog by remember {
        mutableStateOf(false)
    }

    var deadlineDate by remember { mutableStateOf("") }
    var showDatePicker by remember {
        mutableStateOf(false)
    }

    val datePickerState = rememberDatePickerState()
    var deadlineTime by remember { mutableStateOf("") }
    var showTimePicker by remember {
        mutableStateOf(false)
    }

    val calendar = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    var showValidationDialog by remember {
        mutableStateOf(false)
    }

    var validationMessage by remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Tambah Tugas",
            style = MaterialTheme.typography.headlineMedium
        )

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

                if (categoryList.isEmpty()) {
                    showCategoryDialog = true
                } else {
                    expanded = !expanded
                }

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
            modifier = Modifier
                .fillMaxWidth(),
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
            trailingIcon = {
                TextButton(
                    onClick = {
                        showTimePicker = true
                    }
                ) {
                    Text("🕒")
                }
            },
            modifier = Modifier.fillMaxWidth()
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

                val task = Task(
                    title = title,
                    description = description,
                    category = category,
                    deadlineDate = deadlineDate,
                    deadlineTime = deadlineTime,
                    completed = false
                )

                val success = repository.addTask(task)

                if (success) {
                    navController.popBackStack()
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Simpan Tugas")
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

    if (showCategoryDialog) {

        AlertDialog(

            onDismissRequest = {
                showCategoryDialog = false
            },

            title = {
                Text("Belum Ada Kategori")
            },

            text = {

                Text(
                    "Sebelum membuat tugas, kamu perlu menambahkan minimal satu kategori terlebih dahulu melalui menu Kelola Kategori."
                )

            },

            confirmButton = {

                TextButton(
                    onClick = {

                        showCategoryDialog = false

                        navController.navigate(Screen.CategoryFromAddTask.route)
                    }
                ) {
                    Text("Kelola Kategori")
                }

            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showCategoryDialog = false
                    }
                ) {
                    Text("Nanti")
                }

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