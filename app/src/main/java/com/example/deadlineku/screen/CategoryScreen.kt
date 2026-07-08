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
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.foundation.shape.RoundedCornerShape

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

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF6F8FC)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {

                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                }

                Text(
                    text = "Kelola Kategori",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = categoryName,
                onValueChange = {
                    categoryName = it
                },
                singleLine = true,
                label = {
                    Text("Nama Kategori")
                },
                placeholder = {
                    Text("Contoh: Kuliah")
                },
                leadingIcon = {

                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                },
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0xFFD9DEE8),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {

                Text(
                    "Tambah Kategori",
                    fontWeight = FontWeight.SemiBold
                )

            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Kategori Tersimpan (${categoryList.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn {

                items(categoryList) { category ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
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
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {

                                    Icon(
                                        imageVector = Icons.Default.Folder,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )

                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = category.name,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Row {

                                IconButton(
                                    onClick = {

                                        categoryToEdit = category
                                        editCategoryName = category.name

                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                }

                                IconButton(
                                    onClick = {
                                        categoryToDelete = category
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null,
                                        tint = Color(0xFFE53935)
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
                            Text(
                                text = "Hapus",
                                color = MaterialTheme.colorScheme.error
                            )
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