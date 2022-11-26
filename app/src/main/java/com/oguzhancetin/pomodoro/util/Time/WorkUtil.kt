package com.oguzhancetin.pomodoro.util.Time

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.oguzhancetin.pomodoro.util.Times


object WorkUtil {

    var timerIsRunning = mutableStateOf(false)
    private var selectedTimeType:Times = Times.Pomodoro()

    var currentTime = MediatorLiveData(selectedTimeType)


    private var request: OneTimeWorkRequest? = null
    private val workRequestBuilder =
        OneTimeWorkRequestBuilder<TimerWorker>()


    fun stopTimer(context: Application) {
        request?.id?.let {
            WorkManager.getInstance(context).cancelAllWork()
        }
        timerIsRunning.value = false
    }
    fun restart(context:Application){
        stopTimer(context)
        selectedTimeType.left = selectedTimeType.time
        startTime(context = context)
    }

    /**
     * Start timer according to selected timer
     */
    fun startTime(context: Application) {
        stopTimer(context)
        request = workRequestBuilder
            .setInputData(workDataOf("Time" to selectedTimeType.time, "Left" to selectedTimeType.left))
            .build()
        timerIsRunning.value = true
        request?.let { request ->

            WorkManager.getInstance(context).enqueue(request)

            val translatedProgress: LiveData<Times> = Transformations.switchMap(
                WorkManager.getInstance(context)
                    .getWorkInfoByIdLiveData(request.id)
            ) { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.RUNNING -> {
                        val progress = workInfo?.progress?.getFloat(
                            "Left",
                            selectedTimeType.getCurrentPercentage()
                        ) ?: selectedTimeType.getCurrentPercentage()
                        return@switchMap MutableLiveData(selectedTimeType.also { it.setLeft(progress) })
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        val progress = workInfo?.progress?.getFloat(
                            "Left",
                            selectedTimeType.getCurrentPercentage()
                        ) ?: selectedTimeType.getCurrentPercentage()
                        timerIsRunning.value = false
                        return@switchMap MutableLiveData(selectedTimeType.also { it.setLeft(progress) })
                    }
                    else -> {
                        return@switchMap MutableLiveData(selectedTimeType)
                    }
                }
            }

            currentTime.addSource(translatedProgress) { updatedTime ->
                Log.e("Time", updatedTime.toString())
                currentTime.value = updatedTime

            }


        }
    }

    fun changeCurrentTime(time: Times,context: Application) {
        stopTimer(context)
        selectedTimeType = time
        currentTime.value = time
    }
}

fun Long.getMinute(): Long {
    return this / 60000
}