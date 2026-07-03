package com.example.deadlineku.repository

import android.content.Context
import com.example.deadlineku.database.DatabaseHelper
import com.example.deadlineku.model.Category

class CategoryRepository(context: Context) {

    private val db = DatabaseHelper(context)

    fun getCategories(): List<Category> {
        return db.getAllCategories()
    }

    fun addCategory(category: Category): Boolean {
        return db.insertCategory(category)
    }

    fun deleteCategory(categoryId: Int): Boolean {
        return db.deleteCategory(categoryId)
    }
}