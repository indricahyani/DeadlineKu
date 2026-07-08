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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@Composable
fun TaskDetailScreen(
    taskId: Int,
    navController: NavController
) {

    val context = LocalContext.current

    val repository = TaskRepository(context)

    var task by remember {
        mutableStateOf<Task?>(null)
    }

    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        task = repository.getTaskById(taskId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF6F8FC)
    ) {

        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 16.dp)
                .fillMaxSize()
        ) {

            Row(
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
                    text = "Detail Tugas",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 10.dp)
            ) {
                task?.let {

                    Surface(
                        color = Color(0xFFE8F0FF),
                        shape = RoundedCornerShape(50.dp)
                    ) {

                        Text(
                            text = it.category,
                            modifier = Modifier.padding(
                                horizontal = 14.dp,
                                vertical = 6.dp
                            ),
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = it.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            // TANGGAL
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Icon(
                                        Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = Color.White
                                    )

                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {

                                    Text(
                                        "Tanggal Deadline",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.Gray
                                    )

                                    Text(
                                        it.deadlineDate,
                                        fontWeight = FontWeight.Bold
                                    )

                                }

                            }

                            // WAKTU
                            Row(
                                modifier = Modifier.weight(1f),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(46.dp)
                                        .background(
                                            Color(0xFFE9EEF8),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Icon(
                                        Icons.Default.AccessTime,
                                        contentDescription = null,
                                        tint = Color.Gray
                                    )

                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {

                                    Text(
                                        "Waktu Deadline",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color.Gray
                                    )

                                    Text(
                                        it.deadlineTime,
                                        fontWeight = FontWeight.Bold
                                    )

                                }

                            }

                        }

                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Deskripsi",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        border = BorderStroke(
                            1.dp,
                            Color(0xFFE3E8EF)
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {

                        Text(
                            text =
                                if (it.description.isBlank())
                                    "Tidak ada deskripsi."
                                else
                                    it.description,
                            modifier = Modifier.padding(18.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color =
                                if (it.description.isBlank())
                                    Color.Gray
                                else
                                    MaterialTheme.colorScheme.onSurface
                        )

                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (!it.completed) {

                        Button(
                            onClick = {

                                val updatedTask = it.copy(
                                    completed = true
                                )

                                val success = repository.updateTask(updatedTask)

                                if (success) {
                                    navController.popBackStack()
                                }

                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(18.dp)
                        ) {

                            Icon(
                                Icons.Default.CheckCircleOutline,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                "Tandai Selesai",
                                fontWeight = FontWeight.Bold
                            )

                        }

                    } else {

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFE8F5E9)
                            )
                        ) {

                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {

                                Icon(
                                    Icons.Default.CheckCircleOutline,
                                    null,
                                    tint = Color(0xFF2E7D32)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    "Tugas Sudah Selesai",
                                    color = Color(0xFF2E7D32),
                                    fontWeight = FontWeight.Bold
                                )

                            }

                        }

                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        OutlinedButton(

                            onClick = {

                                navController.navigate("edit_task/${it.id}")

                            },

                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp),

                            shape = RoundedCornerShape(18.dp)

                        ) {

                            Icon(
                                Icons.Default.Edit,
                                null
                            )

                            Spacer(Modifier.width(6.dp))

                            Text("Edit")

                        }

                        OutlinedButton(

                            onClick = {

                                showDeleteDialog = true

                            },

                            modifier = Modifier
                                .weight(1f)
                                .height(54.dp),

                            shape = RoundedCornerShape(18.dp),

                            colors = androidx.compose.material3.ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.Red
                            )

                        ) {

                            Icon(
                                Icons.Default.DeleteOutline,
                                null
                            )

                            Spacer(Modifier.width(6.dp))

                            Text("Hapus")

                        }

                    }

                    if (showDeleteDialog) {

                        AlertDialog(

                            onDismissRequest = {
                                showDeleteDialog = false
                            },

                            title = {
                                Text("Hapus Tugas")
                            },

                            text = {
                                Text("Apakah yakin ingin menghapus tugas \"${it.title}\"?")
                            },

                            confirmButton = {

                                TextButton(
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = Color.Red
                                    ),
                                    onClick = {

                                        val success = repository.deleteTask(it.id)

                                        if (success) {
                                            showDeleteDialog = false
                                            navController.popBackStack()
                                        }
                                    }
                                ) {
                                    Text("Hapus")
                                }
                            },

                            dismissButton = {

                                TextButton(
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.primary
                                    ),
                                    onClick = {
                                        showDeleteDialog = false
                                    }
                                ) {
                                    Text("Batal")
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}