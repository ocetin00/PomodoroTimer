package com.oguzhancetin.pomodoro.presentation.screen.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity
import com.oguzhancetin.pomodoro.common.util.Time.WorkUtil
import com.oguzhancetin.pomodoro.common.util.combine
import com.oguzhancetin.pomodoro.common.util.preference.IS_SILENT_NOTIFICATION
import com.oguzhancetin.pomodoro.util.Times
import com.oguzhancetin.pomodoro.common.util.preference.dataStore
import com.oguzhancetin.pomodoro.common.util.removeDetails
import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.AddPomodoroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class MainUiState(
    val runningTimeType: Times = Times.Pomodoro(),
    val timeProgress: Float = 0f,
    val timerIsRunning: Boolean = false,
    val leftTime: String = "00:00",
    val favouriteTasks: List<TaskItemEntity> = listOf(),
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val timePreferencesState: PreferencesState = PreferencesState(),
    val isPomodoroFinish: Boolean = false

)

data class PreferencesState(
    val short: Long? = null,
    val long: Long? = null,
    val pomodoro: Long? = null,
)


@HiltViewModel
class MainViewModel @Inject constructor(
    private val context: Application,
    private val mainRepository: MainRepository,
    private val addPomodoroUseCase: AddPomodoroUseCase
) : AndroidViewModel(context) {

    private val calender = Calendar.getInstance()

    init {
        WorkUtil.onFinishPomodoro = {
            viewModelScope.launch {
                addPomodoroUseCase.invoke(
                    Pomodoro(
                        id = UUID.randomUUID(),
                        doneDate = calender.removeDetails().timeInMillis
                    )
                )
            }
        }
    }

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

    private var _isSilentNotification: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_SILENT_NOTIFICATION] ?: false
        }

    private var _timerIsRunning = WorkUtil.timerIsRunning
    private var _runningTimeType = WorkUtil.runningTimeType
    private var _progress = WorkUtil.progress.asFlow()


    private val _timePreferencesState =
        combine(_longTime, _pomodoroTime, _shortTime) { longTime, pomodoroTime, shortTime ->
            PreferencesState().copy(short = shortTime, long = longTime, pomodoro = pomodoroTime)
        }

    val mainUiState =
        combine(
            _timePreferencesState,
            _favoriteTaskItems,
            _progress,
            _timerIsRunning,
            _runningTimeType,
            transform = { timePreferencesState, favoriteTaskItems, progress, timerIsRunning, runningTimeType ->
                MainUiState().copy(
                    timePreferencesState = timePreferencesState,
                    favouriteTasks = favoriteTaskItems,
                    timeProgress = progress,
                    timerIsRunning = timerIsRunning,
                    leftTime = runningTimeType.getText(progress),
                    runningTimeType = runningTimeType,
                    isLoading = false,
                    userMessage = null,
                    isPomodoroFinish = false
                )
            }
        )
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = MainUiState(isLoading = true)
            )


    fun pauseOrPlayTimer() {
        viewModelScope.launch {
            Log.d("isSilent", _isSilentNotification.first().toString())
            if (_timerIsRunning.value) {
                WorkUtil.apply {
                    stopTimer(context)
                }
            } else {
                WorkUtil.stopTimer(context)
                WorkUtil.startTime(context, isSilent = _isSilentNotification.first())

            }
        }


    }


    fun restart() =
        WorkUtil.restart(context)

    fun updateCurrentTime(time: Times) =
        WorkUtil.changeCurrentTime(time, context)

    fun updateTask(taskItem: TaskItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.updateTask(taskItem = taskItem)
        }
    }

    fun onDoneTask(taskItem: TaskItemEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.updateTask(
                taskItem.apply {
                    doneDate = calender.removeDetails().timeInMillis
                }
            )
        }
    }

}

