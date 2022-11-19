package com.oguzhancetin.pomodoro.util.Time

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.oguzhancetin.pomodoro.util.Times
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


object WorkUtil {

    var selectedTimeType= mutableStateOf<Times>(Times.Pomodoro)

    var leftTime =  MediatorLiveData<Float?>(null)

    private var request: OneTimeWorkRequest? = null

    private val workRequestBuilder =
        OneTimeWorkRequestBuilder<TimerWorker>()

    fun timeRequest(time: Times) {
        request = workRequestBuilder
            .setInputData(workDataOf("Time" to time.time, "Left" to time.left))
            .build()
    }

    fun stopTimer(context: Application) = WorkManager.getInstance(context).cancelAllWork()

    fun startTime(context: Application) {
        stopTimer(context)
        request?.let { request ->

            WorkManager.getInstance(context).enqueue(request)

            leftTime.addSource(Transformations.switchMap(
                WorkManager.getInstance(context)
                    .getWorkInfoByIdLiveData(request.id)
            ) { workInfo ->
                val progress = workInfo?.progress?.getFloat(
                    "Left",
                   1f
                ) ?: 1f

                MutableLiveData(progress)
            }){
                leftTime.value = it
            }


        }
    }
}

fun Long.getMinute(): Long {
    return this / 60000
}