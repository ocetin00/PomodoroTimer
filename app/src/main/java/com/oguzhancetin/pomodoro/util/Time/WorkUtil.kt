package com.oguzhancetin.pomodoro.util.Time

import android.app.Application
import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


object WorkUtil {

    var timerIsRunning = MutableStateFlow(false)
    var runningTimeType: MutableStateFlow<Times> = MutableStateFlow(Times.Pomodoro())

    var cachedTime: Long? = null

    //var currentTime = MediatorLiveData(selectedTimeType)

    var progress = MediatorLiveData(1f)


    private var request: OneTimeWorkRequest? = null
    private val workRequestBuilder = OneTimeWorkRequestBuilder<TimerWorker>()


    fun stopTimer(context: Application) {
        cachedTime =
            (runningTimeType.value.time.times(runningTimeType.value.time.toFloat())).toLong()
        request?.id?.let {
            WorkManager.getInstance(context).cancelAllWork()
        }
        timerIsRunning.value = false
    }

    fun restart(context: Application) {
        stopTimer(context)
        runningTimeType.value.left = runningTimeType.value.time
        startTime(context = context, cachedTime)
    }

    /**
     * Start timer according to selected timer
     */
    fun startTime(context: Application, left: Long? = null) {
        timerIsRunning.value = true
        stopTimer(context)
        request = workRequestBuilder.setInputData(
            workDataOf(
                "Time" to runningTimeType.value.time,
                "Left" to (left ?: runningTimeType.value.time)
            )
        ).build()
        timerIsRunning.value = true
        request?.let { request ->

            WorkManager.getInstance(context).enqueue(request)


            val translatedProgress: LiveData<Float> = Transformations.switchMap(
                WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.id)
            ) { workInfo ->
                when (workInfo.state) {
                    WorkInfo.State.ENQUEUED -> {
                        timerIsRunning.value = true
                        return@switchMap MutableLiveData(1f)
                    }
                    WorkInfo.State.RUNNING -> {
                        val progress = workInfo?.progress?.getFloat(
                            "Left", 1f
                        ) ?: 1f
                        return@switchMap MutableLiveData(progress)
                    }
                    WorkInfo.State.SUCCEEDED -> {
                        val progress = workInfo?.progress?.getFloat(
                            "Left", 1f
                        ) ?: 1f
                        timerIsRunning.value = false
                        return@switchMap MutableLiveData(progress)
                    }
                    else -> {
                        return@switchMap MutableLiveData(1f)
                    }
                }
            }

            progress.addSource(translatedProgress) { translatedProgress ->
                Log.e("Time", translatedProgress.toString())
                progress.value = translatedProgress

            }

            /*val translatedProgress: LiveData<Times> = Transformations.switchMap(
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
                currentTime.value = updatedTime*/


        }
    }


    fun changeCurrentTime(time: Times, context: Application) {
        stopTimer(context)
        runningTimeType.value = time
    }


    fun Long.getMinute(): Long {
        return this / 60000
    }
}