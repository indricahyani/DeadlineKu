package com.example.deadlineku

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.deadlineku.ui.theme.DeadlineKuTheme
import com.google.firebase.firestore.FirebaseFirestore
import com.example.deadlineku.model.Task
import com.example.deadlineku.repository.TaskRepository
import com.example.deadlineku.screen.HomeScreen
import com.example.deadlineku.navigation.AppNavigation

class MainActivity : ComponentActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TEST KONEKSI FIREBASE
        db.collection("Tasks")
            .get()
            .addOnSuccessListener { result ->
                Log.d("FIREBASE", "Jumlah Data: ${result.size()}")
            }
            .addOnFailureListener { exception ->
                Log.e("FIREBASE", "Error: ${exception.message}")
            }

        enableEdgeToEdge()

        setContent {
            DeadlineKuTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DeadlineKuTheme {
        Greeting("Android")
    }
}