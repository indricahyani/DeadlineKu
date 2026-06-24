package com.example.deadlineku.repository

import com.example.deadlineku.model.Task
import com.google.firebase.firestore.FirebaseFirestore

class TaskRepository {

    private val db = FirebaseFirestore.getInstance()

    fun addTask(task: Task) {
        db.collection("Tasks")
            .add(task)
    }

    fun getTasks(
        onSuccess: (List<Task>) -> Unit
    ) {
        db.collection("Tasks")
            .get()
            .addOnSuccessListener { result ->

                val taskList = mutableListOf<Task>()

                for (document in result) {
                    val task = document.toObject(Task::class.java)
                    task.id = document.id
                    taskList.add(task)
                }

                onSuccess(taskList)
            }
    }

    fun deleteTask(taskId: String) {
        db.collection("Tasks")
            .document(taskId)
            .delete()
    }

    fun updateTask(task: Task) {
        db.collection("Tasks")
            .document(task.id)
            .set(task)
    }

    fun getTaskById(
        taskId: String,
        onSuccess: (Task?) -> Unit
    ) {
        db.collection("Tasks")
            .document(taskId)
            .get()
            .addOnSuccessListener { document ->

                if (document.exists()) {
                    val task = document.toObject(Task::class.java)
                    task?.id = document.id
                    onSuccess(task)
                } else {
                    onSuccess(null)
                }
            }
    }
}