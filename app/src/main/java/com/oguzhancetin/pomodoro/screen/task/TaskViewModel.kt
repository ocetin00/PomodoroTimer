package com.oguzhancetin.pomodoro.screen.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(private val taskItemRepository: TaskItemRepository) :
    ViewModel() {

    val taskItems = taskItemRepository.getTaskItems()

    fun addTask(taskItem: TaskItem) {
        viewModelScope.launch (Dispatchers.IO){
            taskItemRepository.insertTaskItem(taskItem = taskItem)
        }

    }

    fun updateTask(taskItem: TaskItem){
        viewModelScope.launch (Dispatchers.IO){
            taskItemRepository.updateTask(taskItem = taskItem)
        }
    }

}

