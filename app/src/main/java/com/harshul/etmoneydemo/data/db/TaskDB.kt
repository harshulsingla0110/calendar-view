package com.harshul.etmoneydemo.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.harshul.etmoneydemo.data.models.Task

@Database(entities = [Task::class], version = 1)
@androidx.room.TypeConverters(TypeConverters::class)
abstract class TaskDB : RoomDatabase() {

    abstract fun getTaskDAO(): TaskDAO
}