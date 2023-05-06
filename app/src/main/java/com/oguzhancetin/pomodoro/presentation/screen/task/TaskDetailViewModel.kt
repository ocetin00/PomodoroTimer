package com.oguzhancetin.pomodoro.presentation.screen.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.domain.model.Category
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.use_case.category.GetAllCategoryUseCase
import com.oguzhancetin.pomodoro.domain.use_case.category.GetCategoryByIdUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.AddTaskItemUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.GetTaskByIdUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.UpdateTaskItemUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.UUID
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


sealed class TaskDetailUIState {
    object Loading : TaskDetailUIState()
    data class Success(
        val selectedCategoryId: UUID? = null,
        val categoryList: List<Category> = listOf(),
        val text: String = "",
        val isSaveButtonActive: Boolean = false,
        val taskItem: TaskItem? = null

    ) : TaskDetailUIState()

    data class Error(val message: String) : TaskDetailUIState()
}


@HiltViewModel
class TaskDetailViewModel
@Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val addTaskItemUseCase: AddTaskItemUseCase,
    private val updateTaskItemUseCase: UpdateTaskItemUseCase,
    private val getCategoryByIdUseCase: GetCategoryByIdUseCase,
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase
) : ViewModel() {

    private val taskId: String? = savedStateHandle["taskId"]
    private val selectedCategoryId: String? = savedStateHandle["selectedCategoryId"]

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    private val _allCategories = getAllCategoryUseCase.invoke()
    private val _text = MutableStateFlow("")


    private val _task =
        taskId?.let {
            getTaskByIdUseCase.invoke(
                UUID.fromString(
                    taskId.removePrefix("{").removeSuffix("}")
                )
            )
        } ?: MutableStateFlow(
            Resource.Success(null)
        )



    var category: Category? = null


    val uiState: StateFlow<TaskDetailUIState> =
        combine(_allCategories, _isLoading, _text, _task) { categories, isLoading, text, task ->
            when {
                categories is Resource.Success<*> || task is Resource.Success -> {
                    TaskDetailUIState.Success(
                        selectedCategoryId = UUID.fromString(
                            selectedCategoryId?.removePrefix("{")?.removeSuffix("}")
                        ),
                        categoryList = categories.data ?: listOf(),
                        text = text,
                        isSaveButtonActive = text.isNotEmpty(),
                        taskItem = task.data
                    )
                }

                categories is Resource.Loading<*> -> {
                    TaskDetailUIState.Loading
                }

                categories is Resource.Error -> {
                    TaskDetailUIState.Error("")
                }

                else -> {
                    TaskDetailUIState.Loading
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskDetailUIState.Loading
        )

    fun saveTask() {
        viewModelScope.launch {
            addTaskItemUseCase.invoke(
                TaskItem(
                    id = UUID.randomUUID(),
                    categoryId = UUID.fromString(
                        selectedCategoryId?.removePrefix("{")?.removeSuffix("}")
                    ),
                    description = _text.value
                )
            )
        }
    }

    fun updateTask(taskItem: TaskItem) {
        viewModelScope.launch {
            addTaskItemUseCase.invoke(taskItem)
        }
    }

    fun onTextChange(it: String) {
        _text.value = it
    }

}