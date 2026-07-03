package com.example.deadlineku.repository

import android.content.Context
import com.example.deadlineku.database.DatabaseHelper
import com.example.deadlineku.model.Task

class TaskRepository(context: Context) {

    private val db = DatabaseHelper(context)

    fun addTask(task: Task): Boolean {
        return db.insertTask(task)
    }

    fun getTasks(): List<Task> {
        return db.getAllTasks()
    }

    fun getTaskById(taskId: Int): Task? {
        return db.getTaskById(taskId)
    }

    fun updateTask(task: Task): Boolean {
        return db.updateTask(task)
    }

    fun updateTaskStatus(
        taskId: Int,
        completed: Boolean
    ): Boolean {
        return db.updateTaskStatus(taskId, completed)
    }

    fun deleteTask(taskId: Int): Boolean {
        return db.deleteTask(taskId)
    }

    fun deleteAllTasks(): Boolean {
        return db.deleteAllTasks()
    }
}