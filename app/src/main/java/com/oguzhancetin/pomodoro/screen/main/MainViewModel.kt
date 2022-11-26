package com.oguzhancetin.pomodoro.screen.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import com.oguzhancetin.pomodoro.util.Time.WorkUtil
import com.oguzhancetin.pomodoro.util.Times
import com.oguzhancetin.pomodoro.util.preference.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val context: Application,
    private val mainRepository: MainRepository
) : AndroidViewModel(context) {
    val favoriteTaskItems = mainRepository.getFavoriteTaskItems()

    /**
     * Get times for time types
     */
    var longTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Long().getPrefKey()] ?: Times.Long().time
        }
    var shortTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Short().getPrefKey()] ?: Times.Short().time
        }
    var pomodoroTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Pomodoro().getPrefKey()] ?: Times.Pomodoro().time
        }

    var timerIsRunning = WorkUtil.timerIsRunning

    var leftTime = WorkUtil.currentTime


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