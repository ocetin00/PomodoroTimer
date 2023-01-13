package com.oguzhancetin.pomodoro.screen.task

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskUIState(
    val taskItems: List<TaskItem> = listOf(),
    val isLoading: Boolean = true
)


@HiltViewModel
class TaskViewModel @Inject constructor(private val taskItemRepository: TaskItemRepository) :
    ViewModel() {

    private val _taskItems = taskItemRepository.getTaskItems()

    private val _isLoading = MutableStateFlow(false)

     val taskUIState: StateFlow<TaskUIState> = combine(_taskItems,_isLoading){taskItems,isLoading ->
        TaskUIState().copy(taskItems = taskItems , isLoading = isLoading)
    }
         .stateIn(
             scope = viewModelScope,
             started = SharingStarted.WhileSubscribed(5_000),
             initialValue = TaskUIState(isLoading = true)
         )

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

