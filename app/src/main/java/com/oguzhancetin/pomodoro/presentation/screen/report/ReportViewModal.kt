package com.oguzhancetin.pomodoro.presentation.screen.report

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.common.Resource
import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity
import com.oguzhancetin.pomodoro.common.util.removeDetails
import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import com.oguzhancetin.pomodoro.domain.model.TaskItem
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.AddPomodoroUseCase
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.GetCurrentWeekPomodoroListUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.GetCurrentWeekDoneTaskItems
import com.oguzhancetin.pomodoro.presentation.screen.task.TaskUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


data class ReportUIState(
    val pomodoroList: List<Pomodoro> = listOf(),
    val taskList: List<TaskItem> = listOf(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

@HiltViewModel
class ReportViewModal @Inject constructor(
    private val getPomodoroListUseCase: GetCurrentWeekPomodoroListUseCase,
    private val getCurrentWeekDoneTaskItems: GetCurrentWeekDoneTaskItems,
) : ViewModel() {

    private var currentWeekMillis: Long = 0L

    init {

        //start date of current week
        val calendar: Calendar = Calendar.getInstance()
        Log.d("date_current", calendar.timeInMillis.toString())
        calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        Log.d("date_current", calendar.timeInMillis.toString())
        calendar.removeDetails()

    }


    private val _pomodoroList = getPomodoroListUseCase.invoke(currentWeekMillis)
    private val _taskList = getCurrentWeekDoneTaskItems.invoke(currentWeekMillis)


    private val _isLoading = MutableStateFlow(false)

    val reportUIState: StateFlow<ReportUIState> =
        combine(_pomodoroList, _taskList, _isLoading) { pomodoroList, taskList, _ ->
            Log.d("pomodoroList", pomodoroList.toString())
            val state = when (pomodoroList) {
                is Resource.Loading<*> -> ReportUIState().copy(
                    isLoading = true
                )

                is Resource.Error<*> -> ReportUIState().copy(
                    errorMessage = pomodoroList.message
                )

                is Resource.Success -> {
                    Log.d("pomodoroList", pomodoroList.data.toString())
                    pomodoroList.data?.let {
                        ReportUIState().copy(
                            pomodoroList = it,
                            isLoading = false,
                        )
                    } ?: ReportUIState().copy()
                }
            }
            when (taskList) {
                is Resource.Loading<*> -> state.copy(
                    isLoading = true
                )

                is Resource.Error<*> -> state.copy(
                    errorMessage = taskList.message
                )

                is Resource.Success -> {
                    Log.d("pomodoroList", taskList.data.toString())
                    taskList.data?.let {
                        state.copy(
                            taskList = it,
                            isLoading = false,
                        )
                    } ?: ReportUIState().copy()
                }
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ReportUIState(isLoading = true)
        )


}
