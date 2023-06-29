package com.oguzhancetin.pomodoro.presentation.screen.task

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.core.model.Category
import com.oguzhancetin.pomodoro.core.model.TaskItem
import com.oguzhancetin.pomodoro.domain.use_case.category.GetAllCategoryUseCase
import com.oguzhancetin.pomodoro.domain.use_case.category.GetCategoryByIdUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.AddTaskItemUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.DeleteTaskItemUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.GetTaskByIdUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.UpdateTaskItemUseCase
import com.oguzhancetin.pomodoro.presentation.common.util.clearSufAndPrefix
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.util.UUID
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


sealed class TaskDetailUIState {
    object Loading : TaskDetailUIState()
    data class Success(
        val selectedCategory: Category? = null,
        val text: String = "",
        val isSaveButtonActive: Boolean = false,
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
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val deleteTaskItemUseCase: DeleteTaskItemUseCase
) : ViewModel() {

    private val taskId = savedStateHandle.getStateFlow<String?>("taskId", null)


    private val selectedCategoryId: String? = savedStateHandle["selectedCategoryId"]

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private var category: Category? = null

    private val _text = MutableStateFlow("")

    private val _task: MutableStateFlow<TaskItem?> = MutableStateFlow(null)


    init {
        viewModelScope.launch(Dispatchers.IO) {
            //get Task
            val taskId = taskId.value?.clearSufAndPrefix()
            if (!taskId.isNullOrEmpty()) {
                val taskItem =
                    getTaskByIdUseCase.invoke(UUID.fromString(taskId))
                _text.emit(taskItem.description ?: "")
                _task.emit(taskItem)
            }
            //get Category
            selectedCategoryId?.let {
                category =
                    getCategoryByIdUseCase.invoke(UUID.fromString(selectedCategoryId.clearSufAndPrefix()))
            }


        }
    }


    val uiState: StateFlow<TaskDetailUIState> =
        combine(_text, _isLoading) { text, _ ->
            TaskDetailUIState.Success(
                selectedCategory = category,
                text = text,
                isSaveButtonActive = text.isNotEmpty(),
            )

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TaskDetailUIState.Loading
        )

    fun saveTask() {
        viewModelScope.launch {
            if (taskId.value?.clearSufAndPrefix().isNullOrEmpty()) {
                addTaskItemUseCase.invoke(
                    TaskItem(
                        id = UUID.randomUUID(),
                        categoryId = UUID.fromString(
                            selectedCategoryId?.removePrefix("{")?.removeSuffix("}")
                        ),
                        description = _text.value.trim()
                    )
                )
            } else {
                updateTaskItemUseCase.invoke(
                    TaskItem(
                        id = UUID.fromString(taskId.value?.clearSufAndPrefix()),
                        categoryId = UUID.fromString(
                            selectedCategoryId?.clearSufAndPrefix()
                        ),
                        description = _text.value.trim()
                    )
                )
            }
        }
    }

    fun updateTask(taskItem: TaskItem) {
        viewModelScope.launch {
            addTaskItemUseCase.invoke(taskItem)
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            _task.value?.let {
                deleteTaskItemUseCase.invoke(it)
            }
        }
    }


    fun onTextChange(it: String) {
        _text.value = it
    }

}