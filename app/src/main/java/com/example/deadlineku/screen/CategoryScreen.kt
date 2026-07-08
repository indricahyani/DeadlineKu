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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Edit
import androidx.navigation.NavController

@Composable
fun CategoryScreen(
    navController: NavController,
    openedFromAddTask: Boolean = false
) {

    val context = LocalContext.current

    val repository = CategoryRepository(context)

    var categoryList by remember {
        mutableStateOf(listOf<Category>())
    }

    var categoryName by remember {
        mutableStateOf("")
    }

    var categoryToDelete by remember {
        mutableStateOf<Category?>(null)
    }

    var categoryToEdit by remember {
        mutableStateOf<Category?>(null)
    }

    var editCategoryName by remember {
        mutableStateOf("")
    }

    var showCategoryUsedDialog by remember {
        mutableStateOf(false)
    }

    var usedCategoryName by remember {
        mutableStateOf("")
    }

    var showDuplicateDialog by remember {
        mutableStateOf(false)
    }


    fun loadCategories() {
        categoryList = repository.getCategories()
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

                    if (
                        repository.isCategoryExists(
                            formatCategoryName(categoryName)
                        )
                    ) {

                        showDuplicateDialog = true

                    } else {

                        val success = repository.addCategory(
                            Category(
                                name = formatCategoryName(categoryName)
                            )
                        )

                        if (success) {

                            categoryName = ""

                            loadCategories()

                            if (openedFromAddTask) {
                                navController.popBackStack()
                            }
                        }
                    }
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

                        Row {

                            IconButton(
                                onClick = {

                                    categoryToEdit = category
                                    editCategoryName = category.name

                                }
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit"
                                )
                            }

                            IconButton(
                                onClick = {
                                    categoryToDelete = category
                                }
                            ) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Hapus"
                                )
                            }

                        }
                    }
                }
            }
        }

        categoryToDelete?.let { selectedCategory ->

            AlertDialog(
                onDismissRequest = {
                    categoryToDelete = null
                },

                title = {
                    Text("Hapus Kategori")
                },

                text = {
                    Text("Apakah yakin ingin menghapus kategori \"${selectedCategory.name}\"?")
                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            if (repository.isCategoryUsed(selectedCategory.name)) {

                                usedCategoryName = selectedCategory.name
                                showCategoryUsedDialog = true

                            } else {

                                repository.deleteCategory(selectedCategory.id)

                                loadCategories()

                            }

                            categoryToDelete = null
                        }
                    ) {
                        Text("Hapus")
                    }
                },

                dismissButton = {

                    TextButton(
                        onClick = {
                            categoryToDelete = null
                        }
                    ) {
                        Text("Batal")
                    }
                }
            )
        }

        categoryToEdit?.let { selectedCategory ->

            AlertDialog(

                onDismissRequest = {
                    categoryToEdit = null
                },

                title = {
                    Text("Edit Kategori")
                },

                text = {

                    OutlinedTextField(
                        value = editCategoryName,
                        onValueChange = {
                            editCategoryName = it
                        },
                        label = {
                            Text("Nama Kategori")
                        },
                        singleLine = true
                    )

                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            if (editCategoryName.isNotBlank()) {

                                if (
                                    formatCategoryName(editCategoryName) ==
                                    selectedCategory.name
                                ) {

                                    categoryToEdit = null
                                    return@TextButton

                                }

                                if (
                                    repository.isCategoryExists(
                                        formatCategoryName(editCategoryName)
                                    )
                                ) {

                                    showDuplicateDialog = true
                                    return@TextButton

                                }

                                val oldName = selectedCategory.name

                                repository.updateCategory(
                                    Category(
                                        id = selectedCategory.id,
                                        name = formatCategoryName(editCategoryName)
                                    )
                                )

                                repository.updateTaskCategory(
                                    oldName,
                                    formatCategoryName(editCategoryName)
                                )

                                loadCategories()

                                categoryToEdit = null
                            }
                        }
                    ) {
                        Text("Simpan")
                    }

                },

                dismissButton = {

                    TextButton(
                        onClick = {
                            categoryToEdit = null
                        }
                    ) {
                        Text("Batal")
                    }

                }

            )

        }

        if (showCategoryUsedDialog) {

            AlertDialog(

                onDismissRequest = {
                    showCategoryUsedDialog = false
                },

                title = {
                    Text("Kategori Tidak Dapat Dihapus")
                },

                text = {

                    Text(
                        "Kategori \"$usedCategoryName\" masih digunakan oleh satu atau lebih tugas.\n\n" +
                                "Untuk menjaga konsistensi data, kategori ini tidak dapat dihapus sebelum semua tugas yang menggunakannya dipindahkan ke kategori lain."
                    )

                },

                confirmButton = {

                    TextButton(
                        onClick = {

                            showCategoryUsedDialog = false
                            usedCategoryName = ""

                        }
                    ) {
                        Text("Mengerti")
                    }

                }

            )

        }

        if (showDuplicateDialog) {

            AlertDialog(

                onDismissRequest = {
                    showDuplicateDialog = false
                },

                title = {
                    Text("Kategori Sudah Ada")
                },

                text = {

                    Text(
                        "Kategori dengan nama tersebut sudah tersedia.\n\nSilakan gunakan nama lain agar tidak terjadi duplikasi."
                    )

                },

                confirmButton = {

                    TextButton(
                        onClick = {
                            showDuplicateDialog = false
                        }
                    ) {
                        Text("Mengerti")
                    }

                }

            )

        }
    }
}

fun formatCategoryName(name: String): String {
    return name
        .trim()
        .lowercase()
        .split(" ")
        .joinToString(" ") { word ->
            word.replaceFirstChar {
                it.uppercase()
            }
        }
}