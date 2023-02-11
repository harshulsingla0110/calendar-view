package com.harshul.etmoneydemo.data.repos

import com.harshul.etmoneydemo.data.db.TaskDB
import com.harshul.etmoneydemo.data.models.Task
import java.util.*
import javax.inject.Inject

class TaskRepository @Inject constructor(private val taskDB: TaskDB) {

    fun getTask(date: Date) = taskDB.getTaskDAO().getTask(date)

    fun getTasks(month: Int) = taskDB.getTaskDAO().getTasks(month)

    fun getAllTasks() = taskDB.getTaskDAO().getAllTasks()

    suspend fun insertTask(task: Task) {
        taskDB.getTaskDAO().insertTask(task = task)
    }

    suspend fun updateTask(task: Task) = taskDB.getTaskDAO().updateTask(task)

    suspend fun deleteTask(task: Task) = taskDB.getTaskDAO().deleteTask(task)

}