package com.example.deadlineku.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")

    object AddTask : Screen("add_task")

    object TaskDetail : Screen("task_detail/{taskId}")

    object Calendar : Screen("calendar")

    object Settings : Screen("settings")

    object Category : Screen("category")

    object CategoryFromAddTask : Screen("category_from_add_task")

    object EditTask : Screen("edit_task/{taskId}")
}