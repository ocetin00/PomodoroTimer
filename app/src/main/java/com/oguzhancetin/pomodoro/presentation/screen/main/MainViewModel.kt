package com.oguzhancetin.pomodoro.presentation.screen.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.data.local.entity.TaskItemEntity
import com.oguzhancetin.pomodoro.core.Time.WorkUtil
import com.oguzhancetin.pomodoro.core.preference.IS_SILENT_NOTIFICATION
import com.oguzhancetin.pomodoro.util.Times
import com.oguzhancetin.pomodoro.core.preference.dataStore
import com.oguzhancetin.pomodoro.core.util.removeDetails
import com.oguzhancetin.pomodoro.data.repository.MainRepository
import com.oguzhancetin.pomodoro.domain.model.Pomodoro
import com.oguzhancetin.pomodoro.domain.use_case.pomodoro.AddPomodoroUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
    val isLoading: Boolean = false,
    val userMessage: String? = null,
    val timePreferencesState: PreferencesState = PreferencesState(),
    val isPomodoroFinish: Boolean = false

)

data class FavoriteTaskUiState(
    val favouriteTasks: List<TaskItemEntity> = listOf()
)

data class PreferencesState(
    val short: Long? = null,
    val long: Long? = null,
    val pomodoro: Long? = null,
)


@OptIn(ExperimentalCoroutinesApi::class)
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
    val favoriteTaskUiState: StateFlow<FavoriteTaskUiState> =
        combine(_favoriteTaskItems, transform = {
            FavoriteTaskUiState(it.first())

        })
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = FavoriteTaskUiState()
            )


    private var _timerIsRunning = WorkUtil.timerIsRunning
    private var _runningTimeType = WorkUtil.runningTimeType
    private var _progress = WorkUtil.progress.asFlow()


    /**
     * Get times for time types,
     * For example Pomodoro: 25dk,Short: 10 dk ...
     */
    private var _longTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            val time = preferences[Times.Long().getPrefKey()] ?: Times.Long().time
            //updateCurrentTime(Times.Long(time))
            return@map time
        }
    private var _shortTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            val time = preferences[Times.Short().getPrefKey()] ?: Times.Short().time
            //updateCurrentTime(Times.Short(time))
            return@map time
        }
    private var _pomodoroTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            val time = preferences[Times.Pomodoro().getPrefKey()] ?: Times.Pomodoro().time
            if (_timerIsRunning.first().not()) // if timer is not running update current time because of not blocking timer
                updateCurrentTime(Times.Pomodoro(time))
            return@map time
        }

    private var _isSilentNotification: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[IS_SILENT_NOTIFICATION] ?: false
        }


    private val _timePreferencesState =
        combine(_longTime, _pomodoroTime, _shortTime) { longTime, pomodoroTime, shortTime ->
            PreferencesState().copy(short = shortTime, long = longTime, pomodoro = pomodoroTime)
        }

    val mainUiState =
        combine(
            _timePreferencesState,
            _progress,
            _timerIsRunning,
            _runningTimeType,
            transform = { timePreferencesState, progress, timerIsRunning, runningTimeType ->
                Log.d("timerPref", "$timePreferencesState.toString()")
                MainUiState().copy(
                    timePreferencesState = timePreferencesState,
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

