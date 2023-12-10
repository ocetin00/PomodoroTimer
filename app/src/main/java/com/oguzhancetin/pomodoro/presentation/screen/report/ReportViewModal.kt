package com.oguzhancetin.pomodoro.presentation.screen.report

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.core.preference.IS_DARK_MODE_KEY
import com.oguzhancetin.pomodoro.core.preference.dataStore
import com.oguzhancetin.pomodoro.core.util.removeDetails
import com.oguzhancetin.pomodoro.core.model.Pomodoro
import com.oguzhancetin.pomodoro.core.model.TaskItem
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.GetCurrentWeekPomodoroListUseCase
import com.oguzhancetin.pomodoro.domain.use_case.task.GetCurrentWeekDoneTaskItems
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject


data class ReportUIState(
    val pomodoroList: List<Pomodoro> = listOf(),
    val taskList: List<TaskItem> = listOf(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isDarkTheme: Boolean? = false
)


class ReportViewModal @Inject constructor(
    getPomodoroListUseCase: GetCurrentWeekPomodoroListUseCase,
    getCurrentWeekDoneTaskItems: GetCurrentWeekDoneTaskItems,
    val context: Context
) : ViewModel() {

    private var currentWeekMillis: Long = 0L

    init {

        //start date of current week
        val calendar = GregorianCalendar(TimeZone.getDefault())
        Log.d("date_current", calendar.timeInMillis.toString())
        calendar.set(GregorianCalendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
        Log.d("date_current", calendar.timeInMillis.toString())
        calendar.removeDetails()

    }


    private val _pomodoroList = getPomodoroListUseCase.invoke(currentWeekMillis)
    private val _taskList = getCurrentWeekDoneTaskItems.invoke(currentWeekMillis)

    private var _isDarkTheme: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_DARK_MODE_KEY] ?: false
        }


    private val _isLoading = MutableStateFlow(false)

    val reportUIState: StateFlow<ReportUIState> =
        combine(
            _pomodoroList,
            _taskList,
            _isLoading,
            _isDarkTheme
        ) { pomodoroList, taskList, _, isDarkTheme ->
            ReportUIState().copy(
                pomodoroList = pomodoroList,
                isLoading = false,
                isDarkTheme = isDarkTheme,
                taskList = taskList,

                )

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ReportUIState(isLoading = true)
        )


}
