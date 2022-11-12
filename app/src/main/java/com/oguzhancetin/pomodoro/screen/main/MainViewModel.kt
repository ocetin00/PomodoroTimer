package com.oguzhancetin.pomodoro.screen.main

import android.app.Application
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.oguzhancetin.pomodoro.data.model.Task.TaskItem
import com.oguzhancetin.pomodoro.util.Times
import com.oguzhancetin.pomodoro.util.WorkRequestBuilders
import com.oguzhancetin.pomodoro.util.preference.dataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val context: Application,private val mainRepository: MainRepository) : AndroidViewModel(context) {
    val favoriteTaskItems = mainRepository.getFavoriteTaskItems()

    /**
     * Get times for time types
     */
    var longTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Long.getPrefKey()] ?: Times.Long.time
        }
    var shortTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Short.getPrefKey()] ?: Times.Short.time
        }
    var pomodoroTime: Flow<Long> = context.dataStore.data
        .map { preferences ->
            preferences[Times.Pomodoro.getPrefKey()] ?: Times.Pomodoro.time
        }

    private val workManager = WorkManager.getInstance(context)
    var timerIsRunning by mutableStateOf(false)
    var workInfo: LiveData<WorkInfo?>? = null
    val currentTime: MutableState<Times> = mutableStateOf(Times.Pomodoro)


    val left: MutableState<Float> = mutableStateOf(0f)

    fun pauseOrPlayTimer() {
        if (timerIsRunning) {
            saveLeftTime()
            workManager.cancelAllWork()
            timerIsRunning = false
        } else {
            startNewTime(currentTime.value)
        }
    }

    fun restart() {
        when (currentTime.value) {
            is Times.Long -> {
                startNewTime(Times.Long)
            }
            is Times.Short -> {
                startNewTime(Times.Short)
            }
            is Times.Pomodoro -> {
                startNewTime(Times.Pomodoro)
            }
        }
    }

    fun updateCurrentTime(times: Times) {
        currentTime.value = times
        currentTime.value.left = times.left
        workManager.cancelAllWork()
        timerIsRunning = false
    }

    private fun startNewTime(times: Times) {
        workManager.cancelAllWork()
        val request = WorkRequestBuilders.timeRequest(times)
        workManager.enqueue(request)
        workInfo = WorkManager.getInstance(context)
            .getWorkInfoByIdLiveData(request.id)
         Transformations.map( WorkManager.getInstance(context)
            .getWorkInfoByIdLiveData(request.id)){
              left.value = it?.progress?.getFloat(
                  "Left",
                  currentTime.value.getCurrentPercentage()
              ) ?: currentTime.value.getCurrentPercentage()
        }
        currentTime.value = times
        timerIsRunning = true
    }

    private fun saveLeftTime() {
        val percentageTime = workInfo?.value?.progress?.getFloat("Left", 0f)
        percentageTime?.let { leftPercentage ->
            currentTime.value.left = (leftPercentage * currentTime.value.time).toLong()
        }
    }

    fun updateCurrentLeft(left: Float) {
        currentTime.value.left = (left * currentTime.value.time).toLong()
    }
    fun updateTask(taskItem: TaskItem){
        viewModelScope.launch (Dispatchers.IO){
            mainRepository.updateTask(taskItem = taskItem)
        }
    }

    fun updateTaskItem(taskItem: TaskItem){
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.updateTask(taskItem)
        }
    }


}