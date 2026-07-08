package com.example.deadlineku.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.deadlineku.navigation.Screen

@Composable
fun SplashScreen(
    navController: NavController
) {

    var visible by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        visible = true

        delay(2000)

        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Splash.route) {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F8FC)),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + scaleIn()
            ) {

                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .shadow(
                            12.dp,
                            shape = MaterialTheme.shapes.large
                        )
                        .background(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.large
                        ),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            AnimatedVisibility(
                visible = visible,
                enter = fadeIn()
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "DeadlineKu",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 34.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Manajemen tugas cerdas\nuntuk produktivitas Anda.",
                        color = Color.Gray,
                        fontSize = 17.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    Row {

                        repeat(3) { index ->

                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(8.dp)
                                    .background(
                                        if (index == 1)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            Color(0xFFD5E2F8),
                                        shape = MaterialTheme.shapes.small
                                    )
                            )

                        }

                    }

                }

            }

        }

    }

}