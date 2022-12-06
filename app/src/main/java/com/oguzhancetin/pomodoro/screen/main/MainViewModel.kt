package com.oguzhancetin.pomodoro.screen.main

import android.app.Application
import android.util.Log
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

    private var _timerIsRunning = WorkUtil.timerIsRunning
    private var _runningTimeType = WorkUtil.runningTimeType
    private var _progress = WorkUtil.progress.asFlow()

    private val _timePreferencesState =
        combine(_longTime, _pomodoroTime, _shortTime) { longTime, pomodoroTime, shortTime ->
            PreferencesState().copy(short = shortTime, long = longTime, pomodoro = pomodoroTime)
        }

    val mainUiState: StateFlow<MainUiState> =
        combine(
            _timePreferencesState,
            _favoriteTaskItems,
            _progress,
            _timerIsRunning,
            _runningTimeType
        ) { timePreferencesState, favoriteTaskItems, progress, timerIsRunning, runningTimeType ->
            Log.d("progress", progress.toString())
            MainUiState().copy(
                timePreferencesState = timePreferencesState,
                favouriteTasks = favoriteTaskItems,
                timeProgress = progress,
                timerIsRunning = timerIsRunning,
                leftTime = runningTimeType.getText(progress),
                runningTimeType = runningTimeType,
                isLoading = false,
                userMessage = null,
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainUiState(isLoading = true)
            )


    fun pauseOrPlayTimer() {
        if (_timerIsRunning.value) {
            WorkUtil.apply {
                stopTimer(context)
            }
        } else {
            WorkUtil.stopTimer(context)
            WorkUtil.startTime(context)

        }
    }


    fun restart() =
        WorkUtil.restart(context)

    fun updateCurrentTime(time: Times) =
        WorkUtil.changeCurrentTime(time, context)

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

