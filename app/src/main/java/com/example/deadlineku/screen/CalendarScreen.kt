package com.example.deadlineku.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.boguszpawlowski.composecalendar.Calendar
import io.github.boguszpawlowski.composecalendar.rememberCalendarState
import com.example.deadlineku.model.Task
import com.example.deadlineku.repository.TaskRepository
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.navigation.NavController
import androidx.compose.foundation.clickable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun CalendarScreen(
    navController: NavController
) {

    val calendarState = rememberCalendarState()

    val context = LocalContext.current

    val repository = TaskRepository(context)

    var taskList by remember {
        mutableStateOf(listOf<Task>())
    }

    val localDateSaver = Saver<LocalDate, String>(
        save = { it.toString() },
        restore = { LocalDate.parse(it) }
    )

    var selectedDate by rememberSaveable(
        stateSaver = localDateSaver
    ) {
        mutableStateOf(LocalDate.now())
    }

    LaunchedEffect(Unit) {
        taskList = repository.getTasks()
    }

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val selectedTasks = taskList.filter {
        it.deadlineDate == selectedDate.format(formatter)
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Kalender Deadline",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Calendar(
            calendarState = calendarState,
            dayContent = { day ->

                val isSelected = day.date == selectedDate

                val hasTask = taskList.any { task ->
                    task.deadlineDate == day.date.format(formatter)
                }

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .size(40.dp)
                        .background(
                            if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                Color.Transparent,
                            CircleShape
                        )
                        .clickable {
                            selectedDate = day.date
                        },
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {

                        Text(
                            text = day.date.dayOfMonth.toString(),
                            color =
                                if (isSelected)
                                    Color.White
                                else
                                    MaterialTheme.colorScheme.onBackground
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        if (hasTask) {

                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected)
                                            Color.White
                                        else
                                            MaterialTheme.colorScheme.primary
                                    )
                            )

                        } else {

                            Spacer(modifier = Modifier.size(5.dp))
                        }
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Tugas ${selectedDate.format(formatter)}",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (selectedTasks.isEmpty()) {

            Text("Tidak ada tugas.")

        } else {

            LazyColumn {

                items(selectedTasks) { task ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                navController.navigate("task_detail/${task.id}")
                            }
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            Text(
                                task.title,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(task.category)

                            Text("${task.deadlineDate} • ${task.deadlineTime}")

                            Text(
                                if (task.completed)
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
}