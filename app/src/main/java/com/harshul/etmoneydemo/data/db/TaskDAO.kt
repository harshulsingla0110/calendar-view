package com.harshul.etmoneydemo.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.harshul.etmoneydemo.data.models.Task
import java.util.*

@Dao
interface TaskDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Query("SELECT * FROM task_table WHERE task_table.date == :date")
    fun getTask(date: Date): LiveData<List<Task>>

    @Query("SELECT * FROM task_table WHERE month == :month ORDER BY date ASC")
    fun getTasks(month: Int): LiveData<List<Task>>

    @Query("SELECT * FROM task_table ORDER BY date ASC")
    fun getAllTasks(): LiveData<List<Task>>

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}