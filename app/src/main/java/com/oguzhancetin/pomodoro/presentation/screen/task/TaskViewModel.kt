package com.oguzhancetin.pomodoro.presentation.screen.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.data.local.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.use_case.category.AddCategoryUseCase
import com.oguzhancetin.pomodoro.domain.use_case.category.GetAllCategoryUseCase
import com.oguzhancetin.pomodoro.domain.use_case.category.GetAllCategoryWithTasksUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.AddTaskItemUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.DeleteTaskItemUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.GetTasksUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.UpdateTaskItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UIState {
    object Loading : UIState()
    data class Success(val data: TaskWithCategories) : UIState()
    data class Error(val message: String) : UIState()
}

data class TaskWithCategories(
    val taskItems: List<TaskItem>? = listOf(),
    val categories: List<CategoryWithTask>? = listOf()
)


@HiltViewModel
class TaskViewModel @Inject constructor(
    getTasksUseCase: GetTasksUseCase,
    private val addTaskItemUseCase: AddTaskItemUseCase,
    private val updateTaskItemUseCase: UpdateTaskItemUseCase,
    private val deleteTaskItemUseCase: DeleteTaskItemUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getAllCategoryWithTasksUseCase: GetAllCategoryWithTasksUseCase,
) :
    ViewModel() {

    private val _taskItems = getTasksUseCase.invoke()
    private val _categories = getAllCategoryWithTasksUseCase.invoke()


    private val _isLoading = MutableStateFlow(false)



    val taskUIState: StateFlow<UIState> =
        combine(_taskItems, _isLoading, _categories)
        { taskItems, _, categories ->
            when {
                taskItems is Resource.Loading<*> || categories is Resource.Loading<*> -> UIState.Loading
                taskItems is Resource.Error<*> -> UIState.Error(taskItems.message + "")
                categories is Resource.Error<*> -> UIState.Error((categories.message + ""))
                else -> UIState.Success(TaskWithCategories(taskItems.data, categories.data))
            }

        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = UIState.Loading
            )

    fun addCategory(category: Category) {
        viewModelScope.launch {
            addCategoryUseCase.invoke(category)
        }
    }

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

