package com.example.deadlineku.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.ContentValues
import com.example.deadlineku.model.Task
import com.example.deadlineku.model.Category

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    companion object {

        private const val DATABASE_NAME = "DeadlineKu.db"

        private const val DATABASE_VERSION = 1

        const val TABLE_TASK = "Tasks"

        const val TABLE_CATEGORY = "Categories"

        const val COLUMN_ID = "id"
        const val COLUMN_TITLE = "title"
        const val COLUMN_DESCRIPTION = "description"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_DEADLINE_DATE = "deadlineDate"
        const val COLUMN_DEADLINE_TIME = "deadlineTime"
        const val COLUMN_IS_COMPLETED = "isCompleted"
        const val COLUMN_CATEGORY_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            """
            CREATE TABLE $TABLE_TASK(
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TITLE TEXT NOT NULL,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_CATEGORY TEXT,
                $COLUMN_DEADLINE_DATE TEXT,
                $COLUMN_DEADLINE_TIME TEXT,
                $COLUMN_IS_COMPLETED INTEGER
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_CATEGORY(
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY_NAME TEXT NOT NULL
            )
            """.trimIndent()
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS Tasks")
        db.execSQL("DROP TABLE IF EXISTS Categories")

        onCreate(db)

    }

    fun insertTask(task: Task): Boolean {

        val db = this.writableDatabase

        val values = ContentValues()

        values.put(COLUMN_TITLE, task.title)
        values.put(COLUMN_DESCRIPTION, task.description)
        values.put(COLUMN_CATEGORY, task.category)
        values.put(COLUMN_DEADLINE_DATE, task.deadlineDate)
        values.put(COLUMN_DEADLINE_TIME, task.deadlineTime)

        values.put(
            COLUMN_IS_COMPLETED,
            if (task.completed) 1 else 0
        )

        val result = db.insert(
            TABLE_TASK,
            null,
            values
        )

        db.close()

        return result != -1L
    }

    fun getAllTasks(): List<Task> {

        val taskList = mutableListOf<Task>()

        val db = this.readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_TASK",
            null
        )

        if (cursor.moveToFirst()) {

            do {

                val task = Task()

                task.id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_ID)
                )

                task.title = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_TITLE)
                )

                task.description = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)
                )

                task.category = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)
                )

                task.deadlineDate = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_DEADLINE_DATE)
                )

                task.deadlineTime = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_DEADLINE_TIME)
                )

                task.completed =
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)
                    ) == 1

                taskList.add(task)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return taskList
    }

    fun getTaskById(taskId: Int): Task? {

        val db = readableDatabase

        val cursor = db.query(
            TABLE_TASK,
            null,
            "$COLUMN_ID=?",
            arrayOf(taskId.toString()),
            null,
            null,
            null
        )

        var task: Task? = null

        if (cursor.moveToFirst()) {

            task = Task()

            task.id = cursor.getInt(
                cursor.getColumnIndexOrThrow(COLUMN_ID)
            )

            task.title = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_TITLE)
            )

            task.description = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)
            )

            task.category = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)
            )

            task.deadlineDate = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_DEADLINE_DATE)
            )

            task.deadlineTime = cursor.getString(
                cursor.getColumnIndexOrThrow(COLUMN_DEADLINE_TIME)
            )

            task.completed =
                cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_IS_COMPLETED)
                ) == 1
        }

        cursor.close()
        db.close()

        return task
    }

    fun deleteTask(taskId: Int): Boolean {

        val db = writableDatabase

        val result = db.delete(
            TABLE_TASK,
            "$COLUMN_ID=?",
            arrayOf(taskId.toString())
        )

        db.close()

        return result > 0
    }

    fun deleteAllTasks(): Boolean {

        val db = writableDatabase

        val result = db.delete(
            TABLE_TASK,
            null,
            null
        )

        db.close()

        return result >= 0
    }

    fun updateTask(task: Task): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put(COLUMN_TITLE, task.title)
        values.put(COLUMN_DESCRIPTION, task.description)
        values.put(COLUMN_CATEGORY, task.category)
        values.put(COLUMN_DEADLINE_DATE, task.deadlineDate)
        values.put(COLUMN_DEADLINE_TIME, task.deadlineTime)
        values.put(COLUMN_IS_COMPLETED, if (task.completed) 1 else 0)

        val result = db.update(
            TABLE_TASK,
            values,
            "$COLUMN_ID=?",
            arrayOf(task.id.toString())
        )

        db.close()

        return result > 0
    }

    fun updateTaskStatus(
        taskId: Int,
        completed: Boolean
    ): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put(
            COLUMN_IS_COMPLETED,
            if (completed) 1 else 0
        )

        val result = db.update(
            TABLE_TASK,
            values,
            "$COLUMN_ID=?",
            arrayOf(taskId.toString())
        )

        db.close()

        return result > 0
    }

    fun insertCategory(category: Category): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put(COLUMN_CATEGORY_NAME, category.name)

        val result = db.insert(
            TABLE_CATEGORY,
            null,
            values
        )

        db.close()

        return result != -1L
    }

    fun getAllCategories(): List<Category> {

        val categoryList = mutableListOf<Category>()

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM $TABLE_CATEGORY",
            null
        )

        if (cursor.moveToFirst()) {

            do {

                val category = Category()

                category.id = cursor.getInt(
                    cursor.getColumnIndexOrThrow(COLUMN_ID)
                )

                category.name = cursor.getString(
                    cursor.getColumnIndexOrThrow(COLUMN_CATEGORY_NAME)
                )

                categoryList.add(category)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return categoryList
    }

    fun deleteCategory(categoryId: Int): Boolean {

        val db = writableDatabase

        val result = db.delete(
            TABLE_CATEGORY,
            "$COLUMN_ID=?",
            arrayOf(categoryId.toString())
        )

        db.close()

        return result > 0
    }
}