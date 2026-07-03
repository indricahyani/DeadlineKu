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

@Composable
fun CalendarScreen() {

    val calendarState = rememberCalendarState()

    val context = LocalContext.current

    val repository = TaskRepository(context)

    var taskList by remember {
        mutableStateOf(listOf<Task>())
    }

    LaunchedEffect(Unit) {
        taskList = repository.getTasks()
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
            calendarState = calendarState
        )
    }
}