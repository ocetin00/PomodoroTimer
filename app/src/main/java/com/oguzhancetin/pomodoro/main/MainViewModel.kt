package com.oguzhancetin.pomodoro.main

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.oguzhancetin.pomodoro.util.Times
import com.oguzhancetin.pomodoro.util.WorkRequestBuilders
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(val context: Application) : AndroidViewModel(context) {
    private val workManager = WorkManager.getInstance(context)
    var timerIsRunning by mutableStateOf(false)
    var workInfo: LiveData<WorkInfo?>? = null
    var currentTime: Times by mutableStateOf(Times.PomodoroTime())

    fun pauseOrPlayTimer() {
        if (timerIsRunning) {
            saveLeftTime()
            workManager.cancelAllWork()
            timerIsRunning = false
        } else {
            startNewTime(currentTime)
        }

    }
    fun restart() {
        when (currentTime) {
            is Times.LongTime -> {
                startNewTime(Times.LongTime())
            }
            is Times.Short -> {
                startNewTime(Times.Short())
            }
            is Times.PomodoroTime -> {
                startNewTime(Times.PomodoroTime())
            }
        }

    }
    fun updateCurrentTime(times: Times){
        currentTime = times
        workManager.cancelAllWork()
        timerIsRunning = false
    }
    fun startNewTime(times: Times) {
        workManager.cancelAllWork()
        val request = WorkRequestBuilders.timeRequest(times)
        workManager.enqueue(request)
        workInfo = WorkManager.getInstance(context)
            .getWorkInfoByIdLiveData(request.id)
        currentTime = times
        timerIsRunning = true
        // workManager.enqueueUniqueWork("TimerWork",ExistingWorkPolicy.REPLACE,request)
    }

    private fun saveLeftTime() {
        val percentageTime = workInfo?.value?.progress?.getFloat("Left", 0f)
        percentageTime?.let { leftPercentage ->
            currentTime.left = (leftPercentage * currentTime.time).toLong()
        }
    }

    fun updateCurrent(left:Float){
        currentTime.left = (left * currentTime.time).toLong()
    }


}