package com.example.deadlineku.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import java.time.YearMonth
import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.foundation.border
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.IconButton
import androidx.compose.animation.animateContentSize
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import com.example.deadlineku.navigation.Screen

@Composable
fun CalendarScreen(
    navController: NavController
) {

    var currentMonth by remember {
        mutableStateOf(
            YearMonth.now()
        )
    }

    val monthName =
        currentMonth.month.getDisplayName(
            TextStyle.FULL,
            Locale("id", "ID")
        ).replaceFirstChar {
            it.uppercase()
        }

    val days = getDaysInMonth(currentMonth)

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FC))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F8FC))
        ) {

            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "DeadlineKu",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateContentSize(),
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp
                        )
                    ) {

                        Column(
                            modifier = Modifier.padding(20.dp)
                        ) {

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Text(
                                    text = "$monthName ${currentMonth.year}",
                                    modifier = Modifier.weight(1f),
                                    style = MaterialTheme.typography.headlineSmall,
                                    fontWeight = FontWeight.Bold
                                )

                                IconButton(

                                    onClick = {

                                        currentMonth =
                                            currentMonth.minusMonths(1)

                                        val day = minOf(
                                            selectedDate.dayOfMonth,
                                            currentMonth.lengthOfMonth()
                                        )

                                        selectedDate = currentMonth.atDay(day)

                                    }

                                ) {

                                    Icon(
                                        Icons.Default.KeyboardArrowLeft,
                                        null
                                    )

                                }

                                IconButton(

                                    onClick = {

                                        currentMonth =
                                            currentMonth.plusMonths(1)

                                        val day = minOf(
                                            selectedDate.dayOfMonth,
                                            currentMonth.lengthOfMonth()
                                        )

                                        selectedDate = currentMonth.atDay(day)

                                    }

                                ) {

                                    Icon(
                                        Icons.Default.KeyboardArrowRight,
                                        null
                                    )

                                }

                            }

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {

                                listOf(
                                    "Sen",
                                    "Sel",
                                    "Rab",
                                    "Kam",
                                    "Jum",
                                    "Sab",
                                    "Min"
                                ).forEach {

                                    Text(
                                        text = it,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight(1f)
                                    )

                                }

                            }

                            Spacer(
                                modifier = Modifier.height(20.dp)
                            )

                            Column {

                                days.chunked(7).forEach { week ->

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 10.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        week.forEach { day ->

                                            val isSelected =
                                                day == selectedDate

                                            val isToday = day == LocalDate.now()

                                            val hasTask =
                                                day != null &&
                                                        taskList.any {
                                                            it.deadlineDate ==
                                                                    day.format(formatter)
                                                        }

                                            Box(
                                                modifier = Modifier
                                                    .size(44.dp)
                                                    .clip(RoundedCornerShape(12.dp))
                                                    .background(
                                                        if (isSelected)
                                                            MaterialTheme.colorScheme.primary
                                                        else
                                                            Color.Transparent
                                                    )
                                                    .clickable {

                                                        if (day != null)
                                                            selectedDate = day

                                                    },
                                                contentAlignment = Alignment.Center
                                            ) {

                                                if (day != null) {

                                                    Column(
                                                        horizontalAlignment = Alignment.CenterHorizontally
                                                    ) {

                                                        Text(
                                                            text = day.dayOfMonth.toString(),
                                                            color = when {

                                                                isSelected ->
                                                                    Color.White

                                                                day.month != currentMonth.month ->
                                                                    Color.LightGray

                                                                else ->
                                                                    MaterialTheme.colorScheme.onBackground

                                                            }
                                                        )

                                                        Spacer(
                                                            modifier = Modifier.height(2.dp)
                                                        )

                                                        if (hasTask) {

                                                            Box(
                                                                modifier = Modifier
                                                                    .size(5.dp)
                                                                    .clip(RoundedCornerShape(12.dp))
                                                                    .background(
                                                                        if (isSelected)
                                                                            MaterialTheme.colorScheme.primary
                                                                        else
                                                                            MaterialTheme.colorScheme.primary
                                                                    )
                                                            )

                                                        } else {

                                                            Spacer(
                                                                modifier = Modifier.size(5.dp)
                                                            )

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = "Deadline ${
                                if (selectedDate == LocalDate.now()) "Hari Ini" else selectedDate.format(
                                    formatter
                                )
                            }",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )

                        Text(
                            text = "${selectedTasks.size} Tugas",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (selectedTasks.isEmpty()) {

                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 40.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "📅",
                                style = MaterialTheme.typography.displaySmall
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Tidak ada deadline",
                                fontWeight = FontWeight.SemiBold
                            )

                            Text(
                                text = "Nikmati harimu 😊",
                                color = Color.Gray
                            )
                        }
                    }

                } else {

                    items(selectedTasks) { task ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .clickable {
                                    navController.navigate("task_detail/${task.id}")
                                },
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 5.dp
                            )
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Checkbox(
                                    checked = task.completed,
                                    onCheckedChange = { checked ->

                                        repository.updateTaskStatus(task.id, checked)
                                        taskList = repository.getTasks()

                                    },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary
                                    )
                                )

                                Spacer(modifier = Modifier.width(12.dp))

                                Column(
                                    modifier = Modifier.weight(1f)
                                ) {

                                    Text(
                                        text = task.title,
                                        fontWeight = FontWeight.Bold,
                                        textDecoration =
                                            if (task.completed)
                                                TextDecoration.LineThrough
                                            else
                                                TextDecoration.None
                                    )

                                    Spacer(modifier = Modifier.height(3.dp))

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
                                            task.deadlineTime,
                                            style = MaterialTheme.typography.bodySmall
                                        )

                                        Spacer(modifier = Modifier.width(8.dp))

                                        Card(
                                            colors = CardDefaults.cardColors(
                                                containerColor = Color(0xFFE8F0FF)
                                            ),
                                            shape = RoundedCornerShape(8.dp)
                                        ) {

                                            Text(
                                                text = task.category,
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp,
                                                    vertical = 3.dp
                                                ),
                                                color = MaterialTheme.colorScheme.primary,
                                                style = MaterialTheme.typography.bodySmall
                                            )

                                        }

                                    }

                                }

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

fun getDaysInMonth(month: YearMonth): List<LocalDate?> {

    val firstDay = month.atDay(1)

    val daysInMonth = month.lengthOfMonth()

    val startOffset = when (firstDay.dayOfWeek) {
        DayOfWeek.MONDAY -> 0
        DayOfWeek.TUESDAY -> 1
        DayOfWeek.WEDNESDAY -> 2
        DayOfWeek.THURSDAY -> 3
        DayOfWeek.FRIDAY -> 4
        DayOfWeek.SATURDAY -> 5
        DayOfWeek.SUNDAY -> 6
    }

    val days = mutableListOf<LocalDate?>()

    repeat(startOffset) {
        days.add(null)
    }

    for (i in 1..daysInMonth) {
        days.add(month.atDay(i))
    }

    while (days.size % 7 != 0) {
        days.add(null)
    }

    return days
}