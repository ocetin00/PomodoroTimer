package com.oguzhancetin.pomodoro.screen.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import com.oguzhancetin.pomodoro.util.Time.WorkUtil
import com.oguzhancetin.pomodoro.util.Times
import com.oguzhancetin.pomodoro.util.preference.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val runningTimeType: Times = Times.Pomodoro(),
    val timeProgress: Float = 0f,
    val timerIsRunning: Boolean = false,
    val leftTime: String = "00:00",
    val favouriteTasks: List<TaskItem> = listOf(),
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val timePreferencesState: PreferencesState = PreferencesState()
)

data class PreferencesState(
    val short: Long? = null,
    val long: Long? = null,
    val pomodoro: Long? = null,
)


@HiltViewModel
class MainViewModel @Inject constructor(
    private val context: Application,
    private val mainRepository: MainRepository
) : AndroidViewModel(context) {

    private val _favoriteTaskItems = mainRepository.getFavoriteTaskItems()

    /**
     * Get times for time types,
     * For example Pomodoro: 25dk,Short: 10 dk ...
     */
    private var _longTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Long().getPrefKey()] ?: Times.Long().time
        }
    private var _shortTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Short().getPrefKey()] ?: Times.Short().time
        }
    private var _pomodoroTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Pomodoro().getPrefKey()] ?: Times.Pomodoro().time
        }

    var timerIsRunning = WorkUtil.timerIsRunning
    var runningTimeType = WorkUtil.runningTimeType
    var progress = WorkUtil.progress.asFlow()

    private val _timePreferencesState =
        combine(_longTime, _pomodoroTime, _shortTime) { longTime, pomodoroTime, shortTime ->
            PreferencesState().copy(short = shortTime, long = longTime, pomodoro = pomodoroTime)
        }

    val mainUiState: StateFlow<MainUiState> =
        combine(
            _timePreferencesState,
            _favoriteTaskItems,
            progress,
            timerIsRunning,
            runningTimeType
        ) { timePreferencesState, favoriteTaskItems, progress, timerIsRunning, runningTimeType ->
            MainUiState().copy(
                timePreferencesState = timePreferencesState,
                favouriteTasks = favoriteTaskItems,
                timeProgress = progress,
                timerIsRunning = timerIsRunning,
                runningTimeType = runningTimeType,
                isLoading = false
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainUiState(isLoading = true)
            )


    fun pauseOrPlayTimer() {
        if (timerIsRunning.value) {
            WorkUtil.apply {
                stopTimer(context)
            }
        } else {
            WorkUtil.startTime(context)

        }
    }


    fun restart() =
        WorkUtil.restart(context)

    fun updateCurrentTime(time: Times) =
        WorkUtil.c(time, context)

    fun updateTask(taskItem: TaskItem) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.updateTask(taskItem = taskItem)
        }
    }

    fun updateTaskItem(taskItem: TaskItem) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.updateTask(taskItem)
        }
    }

}