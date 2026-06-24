package com.example.deadlineku.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.boguszpawlowski.composecalendar.Calendar
import io.github.boguszpawlowski.composecalendar.rememberCalendarState

@Composable
fun CalendarScreen() {

    val calendarState = rememberCalendarState()

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