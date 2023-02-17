package com.oguzhancetin.pomodoro.presentation.screen.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.use_case.task.AddTaskItemUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.DeleteTaskItemUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.GetTasksUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.UpdateTaskItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TaskUIState(
    val taskItems: List<TaskItem> = listOf(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)


@HiltViewModel
class TaskViewModel @Inject constructor(
    getTasksUseCase: GetTasksUseCase,
    private val addTaskItemUseCase: AddTaskItemUseCase,
    private val updateTaskItemUseCase: UpdateTaskItemUseCase,
    private val deleteTaskItemUseCase: DeleteTaskItemUseCase
) :
    ViewModel() {

    private val _taskItems = getTasksUseCase.invoke()


    private val _isLoading = MutableStateFlow(false)

    val taskUIState: StateFlow<TaskUIState> =
        combine(_taskItems, _isLoading) { taskItems, _ ->
            when (taskItems) {
                is Resource.Loading<*> -> TaskUIState().copy(
                    isLoading = true
                )
                is Resource.Error<*> -> TaskUIState().copy(
                    errorMessage = taskItems.message
                )
                is Resource.Success -> {
                    taskItems.data?.let {
                        TaskUIState().copy(
                            taskItems = it,
                            isLoading = false
                        )
                    } ?: TaskUIState().copy()
                }
            }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = TaskUIState(isLoading = true)
            )

    fun addTask(taskItem: TaskItem) {
        viewModelScope.launch(Dispatchers.IO) {
            addTaskItemUseCase.invoke(taskItem = taskItem)
        }

    }

    fun updateTask(taskItem: TaskItem) {
        viewModelScope.launch(Dispatchers.IO) {
            updateTaskItemUseCase.invoke(taskItem = taskItem)
        }
    }

}

