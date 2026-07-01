package com.example.deadlineku.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.deadlineku.model.Category
import com.example.deadlineku.repository.CategoryRepository

@Composable
fun CategoryScreen() {

    val repository = CategoryRepository()

    var categoryList by remember {
        mutableStateOf(listOf<Category>())
    }

    var categoryName by remember {
        mutableStateOf("")
    }

    fun loadCategories() {
        repository.getCategories {
            categoryList = it
        }
    }

    LaunchedEffect(Unit) {
        loadCategories()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Kelola Kategori",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = categoryName,
            onValueChange = {
                categoryName = it
            },
            label = {
                Text("Nama Kategori")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

                if (categoryName.isNotBlank()) {

                    repository.addCategory(
                        Category(
                            name = categoryName
                        )
                    )

                    categoryName = ""

                    loadCategories()
                }

            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Tambah Kategori")
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {

            items(categoryList) { category ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                        Text(category.name)

                        IconButton(
                            onClick = {

                                repository.deleteCategory(category.id)

                                loadCategories()
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = null
                            )
                        }
                    }
                }
            }
        }
    }
}