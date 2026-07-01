package com.example.deadlineku.repository

import com.example.deadlineku.model.Category
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class CategoryRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getCategories(
        onSuccess: (List<Category>) -> Unit
    ) {

        Log.d("CATEGORY", "getCategories dipanggil")

        db.collection("Categories")
            .get()
            .addOnSuccessListener { result ->

                val categoryList = mutableListOf<Category>()

                for (document in result) {

                    val category = Category(
                        id = document.id,
                        name = document.getString("name") ?: ""
                    )

                    categoryList.add(category)
                }

                onSuccess(categoryList)
            }
    }

    fun addCategory(category: Category) {

        db.collection("Categories")
            .add(category)
    }

    fun deleteCategory(categoryId: String) {

        db.collection("Categories")
            .document(categoryId)
            .delete()
    }
}