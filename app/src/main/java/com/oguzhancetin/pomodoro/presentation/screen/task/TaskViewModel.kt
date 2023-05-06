package com.oguzhancetin.pomodoro.presentation.screen.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.data.local.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.repository.CategoryRepository
import com.oguzhancetin.pomodoro.domain.use_case.category.AddCategoryUseCase
import com.oguzhancetin.pomodoro.domain.use_case.category.GetAllCategoryUseCase
import com.oguzhancetin.pomodoro.domain.use_case.category.GetAllCategoryWithTasksUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.AddTaskItemUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.DeleteTaskItemUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.GetTasksByCategoryIdUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.GetTasksByCategoryNameUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.GetTasksUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.UpdateTaskItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

sealed class UIState {
    object Loading : UIState()
    data class Success(
        val selectedTaskCategory: UUID? = null,
        val categories: List<CategoryWithTask>? = listOf(),
        val taskItems: List<TaskItem>? = listOf(),

    ) : UIState()

    data class Error(val message: String) : UIState()
}



@HiltViewModel
class TaskViewModel @Inject constructor(
    getTasksUseCase: GetTasksUseCase,
    private val addTaskItemUseCase: AddTaskItemUseCase,
    private val updateTaskItemUseCase: UpdateTaskItemUseCase,
    private val deleteTaskItemUseCase: DeleteTaskItemUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getAllCategoryWithTasksUseCase: GetAllCategoryWithTasksUseCase,
    private val getTasksByCategoryIdUseCase: GetTasksByCategoryIdUseCase,
    private val categoryRepository: CategoryRepository
) :
    ViewModel() {


    private val _categories = getAllCategoryWithTasksUseCase.invoke()


    private val _isLoading = MutableStateFlow(false)

    private val _selectedTaskCategory = MutableStateFlow<UUID?>(null)
    private val _taskItems = _selectedTaskCategory.map {
        it?.let {
            getTasksByCategoryIdUseCase.invoke(it)
        }

    }


    val taskUIState: StateFlow<UIState> =
        combine(_taskItems, _isLoading, _categories, _selectedTaskCategory)
        { taskItems, _, categories, selectedTaskCategory ->
            when {
                taskItems is Resource.Loading<*> || categories is Resource.Loading<*> -> UIState.Loading
                taskItems is Resource.Error<*> -> UIState.Error(taskItems.message + "")
                categories is Resource.Error<*> -> UIState.Error((categories.message + ""))
                else -> UIState.Success(
                    selectedTaskCategory,
                    categories.data,
                    taskItems?.values?.firstOrNull()

                )
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

    fun onChangeSelectedCategory(category: UUID) {
        _selectedTaskCategory.update { category }
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

    fun upsertCategory(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.upsertCategory(category)
        }
    }

    fun deleteCategory(categoryId: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            categoryRepository.deleteCategory(categoryId)
        }
    }


}

