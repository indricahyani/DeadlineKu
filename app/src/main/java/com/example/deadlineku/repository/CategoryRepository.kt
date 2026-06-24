package com.example.deadlineku.repository

import com.example.deadlineku.model.Category
import com.google.firebase.firestore.FirebaseFirestore

class CategoryRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getCategories(
        onSuccess: (List<Category>) -> Unit
    ) {

        db.collection("Categories")
            .get()
            .addOnSuccessListener { result ->

                val categoryList = mutableListOf<Category>()

                for (document in result) {

                    val category =
                        document.toObject(Category::class.java)

                    category.id = document.id

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