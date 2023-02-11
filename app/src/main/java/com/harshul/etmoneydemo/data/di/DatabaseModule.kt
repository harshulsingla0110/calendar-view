package com.harshul.etmoneydemo.data.di

import android.content.Context
import androidx.room.Room
import com.harshul.etmoneydemo.data.db.TaskDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideTaskDB(@ApplicationContext context: Context): TaskDB {
        return Room.databaseBuilder(context, TaskDB::class.java, "TaskDB").build()
    }

}