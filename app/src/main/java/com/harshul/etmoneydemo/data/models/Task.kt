package com.harshul.etmoneydemo.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "task_table")
data class Task(
    var task: String,
    val date: Date,
    val month: Int,
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0
)