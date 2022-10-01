package com.oguzhancetin.pomodoro.util

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.workDataOf
import kotlin.reflect.KProperty

object WorkRequestBuilders {
    private val workRequestBuilder =
        OneTimeWorkRequestBuilder<TimerWorker>()

    fun timeRequest(time: Times) =
        workRequestBuilder
            .setInputData(workDataOf("Time" to time.time,"Left" to time.left))
            .build()
}

fun Long.getMinute(): Long {
    return this/60000
}