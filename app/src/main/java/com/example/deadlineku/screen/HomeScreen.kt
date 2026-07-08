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
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.compose.material3.CheckboxDefaults

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
                text = "DeadlineKu",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
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
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    listOf(
                        "Semua",
                        "Belum Selesai",
                        "Selesai"
                    ).forEach { status ->

                        FilterChip(
                            modifier = Modifier.height(34.dp),
                            selected = selectedStatus == status,
                            onClick = {
                                selectedStatus = status
                            },
                            shape = RoundedCornerShape(50.dp),
                            label = {
                                Text(status)
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {

                    FilterChip(
                        modifier = Modifier.height(34.dp),
                        selected = selectedCategory == "Semua",
                        onClick = {
                            selectedCategory = "Semua"
                        },
                        shape = RoundedCornerShape(50.dp),
                        label = {
                            Text("Semua")
                        }
                    )

                    categoryList.forEach { category ->

                        FilterChip(
                            modifier = Modifier.height(34.dp),
                            selected = selectedCategory == category.name,
                            onClick = {
                                selectedCategory = category.name
                            },
                            shape = RoundedCornerShape(50.dp),
                            label = {
                                Text(category.name)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

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

            val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

            val sortedTasks = filteredTasks.sortedWith(
                compareBy<Task>(
                    {
                        LocalDate.parse(it.deadlineDate, dateFormatter)
                    },
                    {
                        LocalTime.parse(it.deadlineTime, timeFormatter)
                    }
                )
            )

            if (sortedTasks.isEmpty()) {

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

                    items(sortedTasks) { task ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                                .clickable {
                                    navController.navigate("task_detail/${task.id}")
                                },

                            shape = RoundedCornerShape(20.dp),

                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 4.dp
                            ),

                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
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

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Surface(
                                            shape = RoundedCornerShape(8.dp),
                                            color = Color(0xFFE8F0FF)
                                        ) {

                                            Text(
                                                text = task.category,
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp,
                                                    vertical = 4.dp
                                                ),
                                                color = Color(0xFF4F7CFF),
                                                style = MaterialTheme.typography.labelSmall
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Text(
                                            text = task.title,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }

                                    if (task.description.isNotBlank()) {

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Text(
                                            text = task.description,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )

                                        Spacer(modifier = Modifier.height(2.dp))
                                    } else {
                                        Spacer(modifier = Modifier.height(1.dp))
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.AccessTime,
                                            contentDescription = null,
                                            modifier = Modifier.size(16.dp)
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text = getDeadlineText(
                                                task.deadlineDate,
                                                task.deadlineTime
                                            ),
                                            color =
                                                if (
                                                    isOverdue(
                                                        task.deadlineDate,
                                                        task.completed
                                                    )
                                                )
                                                    MaterialTheme.colorScheme.error
                                                else
                                                    MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }

                                Checkbox(
                                    checked = task.completed,
                                    onCheckedChange = { checked ->

                                        repository.updateTaskStatus(
                                            task.id,
                                            checked
                                        )

                                        taskList = repository.getTasks()
                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary
                                    )
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
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary,

            contentColor = Color.White,

            shape = RoundedCornerShape(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Tambah Tugas"
            )
        }
    }
}

fun getDeadlineStatus(
    deadlineDate: String,
    completed: Boolean
): String {

    if (completed) {
        return "✅ Selesai"
    }

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val deadline = LocalDate.parse(deadlineDate, formatter)

    val today = LocalDate.now()

    val days = ChronoUnit.DAYS.between(today, deadline)

    return when {

        days < 0 ->
            "🔴 Terlambat"

        days == 0L ->
            "🟠 Hari Ini"

        days == 1L ->
            "🟡 Besok"

        else ->
            "📅 ${days} Hari Lagi"
    }
}

fun formatDeadlineDate(date: String): String {

    val inputFormatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val outputFormatter =
        DateTimeFormatter.ofPattern(
            "EEEE, d MMMM yyyy",
            Locale("id", "ID")
        )

    return LocalDate
        .parse(date, inputFormatter)
        .format(outputFormatter)
}

fun getDeadlineText(
    deadlineDate: String,
    deadlineTime: String
): String {

    val formatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val deadline =
        LocalDate.parse(deadlineDate, formatter)

    val today = LocalDate.now()

    return when {

        deadline.isEqual(today) ->
            "Hari Ini • $deadlineTime"

        deadline.isEqual(today.plusDays(1)) ->
            "Besok • $deadlineTime"

        else ->
            "${formatDeadlineDate(deadlineDate)} • $deadlineTime"
    }
}

fun isOverdue(
    deadlineDate: String,
    completed: Boolean
): Boolean {

    if (completed) return false

    val formatter =
        DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val deadline =
        LocalDate.parse(deadlineDate, formatter)

    return deadline.isBefore(LocalDate.now())
}