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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Surface
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.LocalOffer


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

    var showCategorySheet by remember {
        mutableStateOf(false)
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
                    .padding(
                        horizontal = 12.dp,
                        vertical = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {

                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                }

                Text(
                    text = "Tambah Tugas",
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
                            if (categoryList.isEmpty()) {
                                showCategoryDialog = true
                            } else {
                                showCategorySheet = true
                            }
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text("Simpan Tugas")
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