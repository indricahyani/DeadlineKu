package com.example.deadlineku.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.CalendarToday
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState


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

    var showCategorySheet by remember {
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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF6F8FC)
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                }

                Text(
                    text = "Edit Tugas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    label = {
                        Text("Judul Tugas")
                    },
                    placeholder = {
                        Text("Contoh: Laporan PBKDF2")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFD9DEE8),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = {
                        description = it
                    },
                    label = {
                        Text("Deskripsi")
                    },
                    placeholder = {
                        Text("Tambahkan catatan...")
                    },
                    minLines = 4,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color(0xFFD9DEE8),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                                showCategorySheet = true
                        }
                ) {

                    OutlinedTextField(
                        value = category,
                        onValueChange = {},
                        readOnly = true,
                        enabled = false,
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text("Kategori")
                        },
                        placeholder = {
                            Text("Pilih kategori")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Folder,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledContainerColor = Color.White,
                            disabledBorderColor = Color(0xFFD9DEE8),
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.primary,
                            disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
                            disabledLabelColor = Color.Gray
                        )
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDatePicker = true
                        }
                ) {

                    OutlinedTextField(
                        value = deadlineDate,
                        onValueChange = {},
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        label = {
                            Text("Tanggal Deadline")
                        },
                        placeholder = {
                            Text("Pilih tanggal")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.CalendarToday,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledContainerColor = Color.White,
                            disabledBorderColor = Color(0xFFD9DEE8),
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.primary,
                            disabledLabelColor = Color.Gray
                        )
                    )

                }

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showTimePicker = true
                        }
                ) {

                    OutlinedTextField(
                        value = deadlineTime,
                        onValueChange = {},
                        enabled = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        label = {
                            Text("Waktu Deadline")
                        },
                        placeholder = {
                            Text("Pilih waktu")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.AccessTime,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        shape = RoundedCornerShape(18.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledContainerColor = Color.White,
                            disabledBorderColor = Color(0xFFD9DEE8),
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledLeadingIconColor = MaterialTheme.colorScheme.primary,
                            disabledLabelColor = Color.Gray
                        )
                    )

                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {

                        val errors = mutableListOf<String>()

                        if (title.isBlank()) errors.add("• Judul tugas")
                        if (category.isBlank()) errors.add("• Kategori")
                        if (deadlineDate.isBlank()) errors.add("• Tanggal deadline")
                        if (deadlineTime.isBlank()) errors.add("• Waktu deadline")

                        if (errors.isNotEmpty()) {

                            validationMessage =
                                "Mohon lengkapi data berikut:\n\n" +
                                        errors.joinToString("\n")

                            showValidationDialog = true
                            return@Button
                        }

                        // WAJIB ADA
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Simpan Perubahan")
                }
            }
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

    if (showCategorySheet) {

        ModalBottomSheet(
            onDismissRequest = {
                showCategorySheet = false
            }
        ) {

            Text(
                text = "Pilih Kategori",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(20.dp)
            )

            categoryList.forEach { item ->

                OutlinedCard(
                    onClick = {
                        category = item.name
                        showCategorySheet = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(
                        1.dp,
                        Color(0xFFD9DEE8)
                    )
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.LocalOffer,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.bodyLarge
                        )

                    }

                }

            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}