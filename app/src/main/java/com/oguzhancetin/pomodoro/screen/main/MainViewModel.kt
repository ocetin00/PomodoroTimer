package com.oguzhancetin.pomodoro.screen.main

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.map
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

    init {
        Log.e("viewModel", "vm init")
    }

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

    var timerIsRunning = mutableStateOf(false)


    //var workInfo = MediatorLiveData<WorkInfo?>(null)

    var selectedTimeType = WorkUtil.selectedTimeType

    fun pauseOrPlayTimer() {
        if (timerIsRunning.value) {
            WorkUtil.apply {
                stopTimer(context)
            }
            timerIsRunning.value = false
        } else {
            WorkUtil.timeRequest(WorkUtil.selectedTimeType.value)
            WorkUtil.startTime(context)
            timerIsRunning.value = true
        }
    }

    /*
        fun restart() {
            when (currentTime.value) {
                is Times.Long -> {
                    startNewTime(Times.Long.also { it.refresh() })

                }
                is Times.Short -> {
                    startNewTime(Times.Short.also { it.refresh() })
                }
                is Times.Pomodoro -> {
                    startNewTime(Times.Pomodoro.also { it.refresh() })
                }
            }
        }*/
    fun updateCurrentTime(times: Times) {
        WorkUtil.selectedTimeType.value = times
    }

/*
    private fun startNewTime(times: Times) {
        workManager.cancelAllWork()
        val request = WorkRequestBuilders.timeRequest(times)
        workManager.enqueue(request)
        workInfo.addSource(
            WorkManager.getInstance(context)
                .getWorkInfoByIdLiveData(request.id)
        ) {
            val workerState = it.state

            if(workerState == WorkInfo.State.SUCCEEDED){

            }
            workInfo.value = it
        }
        timerIsRunning.value = true
    }
*/


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