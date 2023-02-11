package com.harshul.etmoneydemo.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harshul.etmoneydemo.data.models.Task
import com.harshul.etmoneydemo.data.repos.TaskRepository
import com.kizitonwose.calendar.core.CalendarDay
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val repository: TaskRepository) : ViewModel() {

    val month: LiveData<Int>
        get() = _month
    private val _month = MutableLiveData<Int>()

    fun setMonth(month: Int) {
        _month.postValue(month)
    }

    val day: LiveData<CalendarDay?>
        get() = _day
    private val _day = MutableLiveData<CalendarDay?>()

    fun setDay(day: CalendarDay?) = _day.postValue(day)

    fun insertTask(task: Task) = viewModelScope.launch {
        repository.insertTask(task)
    }

    fun getTask(date: Date) = repository.getTask(date)

    fun getTasks(month: Int) = repository.getTasks(month)

    fun getAllTasks() = repository.getAllTasks()

    fun updateTask(task: Task) = viewModelScope.launch {
        repository.updateTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch {
        repository.deleteTask(task)
    }

}