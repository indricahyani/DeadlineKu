package com.example.deadlineku.model

data class Task(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var category: String = "",
    var deadlineDate: String = "",
    var deadlineTime: String = "",
    var completed: Boolean = false
)