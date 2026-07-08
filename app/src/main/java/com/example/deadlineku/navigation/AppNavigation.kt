package com.example.deadlineku.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.deadlineku.screen.SplashScreen
import com.example.deadlineku.screen.AddTaskScreen
import com.example.deadlineku.screen.CalendarScreen
import com.example.deadlineku.screen.HomeScreen
import com.example.deadlineku.screen.SettingsScreen
import com.example.deadlineku.screen.TaskDetailScreen
import com.example.deadlineku.screen.EditTaskScreen
import androidx.compose.material3.Scaffold
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.deadlineku.navigation.BottomNavBar
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import com.example.deadlineku.screen.CategoryScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()
    val currentRoute =
        navController.currentBackStackEntryAsState()
            .value
            ?.destination
            ?.route ?: ""

    Scaffold(

        bottomBar = {

            if (
                currentRoute == Screen.Home.route ||
                currentRoute == Screen.Calendar.route ||
                currentRoute == Screen.Settings.route
            ) {

                BottomNavBar(

                    selectedItem = currentRoute,

                    onItemSelected = { route ->

                        navController.navigate(route)

                    }
                )

            }

        }

    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Splash.route) {
                SplashScreen(navController)
            }

            composable(Screen.Home.route) {
                HomeScreen(navController)
            }

            composable(Screen.Calendar.route) {
                CalendarScreen(navController)
            }

            composable(Screen.Settings.route) {
                SettingsScreen(navController)
            }

            composable(Screen.AddTask.route) {
                AddTaskScreen(navController)
            }

            composable(
                route = Screen.TaskDetail.route
            ) { backStackEntry ->

                val taskId =
                    backStackEntry.arguments
                        ?.getString("taskId")
                        ?.toIntOrNull() ?: 0

                TaskDetailScreen(
                    taskId = taskId,
                    navController = navController
                )
            }

            composable(
                route = Screen.EditTask.route
            ) { backStackEntry ->

                val taskId =
                    backStackEntry.arguments
                        ?.getString("taskId")
                        ?.toIntOrNull() ?: 0

                EditTaskScreen(
                    taskId = taskId,
                    navController = navController
                )
            }

            composable(Screen.Category.route) {
                CategoryScreen(
                    navController = navController,
                    openedFromAddTask = false
                )
            }

            composable(Screen.CategoryFromAddTask.route) {
                CategoryScreen(
                    navController = navController,
                    openedFromAddTask = true
                )
            }
        }
    }
}