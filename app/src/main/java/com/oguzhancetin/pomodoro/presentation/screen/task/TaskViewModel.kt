package com.oguzhancetin.pomodoro.presentation.screen.task

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.data.local.entity.relation.CategoryWithTask
import com.oguzhancetin.pomodoro.data.mapper.toMapTaskItem
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.repository.CategoryRepository
import com.oguzhancetin.pomodoro.domain.repository.TaskItemRepository
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
import kotlinx.coroutines.async
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
    private val categoryRepository: CategoryRepository,
    private val taskItemRepository: TaskItemRepository,
    private val saveStateHandle: SavedStateHandle
) :
    ViewModel() {


    private val _categories = getAllCategoryWithTasksUseCase.invoke()


    private val _isLoading = MutableStateFlow(false)

    private val emptyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000")
    private val _selectedTaskCategory = MutableStateFlow<UUID?>(emptyUUID)


    init {

        viewModelScope.launch {
            _categories.collect {
                if (_selectedTaskCategory.value == emptyUUID) {
                    val firstId = it.firstOrNull()?.category?.id
                    _selectedTaskCategory.emit(firstId)
                }

            }
        }

    }


    val taskUIState: StateFlow<UIState> =
        combine(_isLoading, _categories, _selectedTaskCategory)
        { _, categories, selectedTaskCategory ->
            UIState.Success(
                selectedTaskCategory,
                categories,
                categories.filter { it.category.id == selectedTaskCategory }
                    .firstOrNull()?.taskList?.map { it.toMapTaskItem() }
                    ?: listOf()
            )
        }.stateIn(
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
        viewModelScope.launch {
            _selectedTaskCategory.emit(category)
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

    fun clearList() {
        viewModelScope.launch(Dispatchers.IO) {
            _selectedTaskCategory.first()?.let {
                taskItemRepository.deleteByCategoryId(it)
                //trigger categories items
                _selectedTaskCategory.emit(it)
            }
        }
    }

    fun refreshTask() {
        viewModelScope.launch {
            _selectedTaskCategory.first()?.let {
                _selectedTaskCategory.emit(it)
            }
        }

    }


}

