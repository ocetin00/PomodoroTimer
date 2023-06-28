package com.oguzhancetin.pomodoro.core.Time

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.work.Constraints
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.oguzhancetin.pomodoro.util.Times
import kotlinx.coroutines.flow.MutableStateFlow


object WorkUtil {

    var timerIsRunning = MutableStateFlow(false)
    var runningTimeType: MutableStateFlow<Times> = MutableStateFlow(Times.Pomodoro())

    /**
     * Time is cached when timer is stop, if timer type change or restart same time type
     * cached will be cleared
     */
    var cachedTime: Long? = runningTimeType.value.time

    /**
     * Get percentage version of cached time
     * Ex 5_000 / 15_000 => 0.3f
     */
    private val cachedTimePercentage
        get() = cachedTime?.toFloat()?.div(runningTimeType.value.time.toFloat())


    /**
     * Hold progress between (0.0f - 1.0f)
     */
    var progress = MediatorLiveData(1f)

    val constraints = Constraints.Builder()
        .setRequiresDeviceIdle(false)
        .build()


    private var request: OneTimeWorkRequest? = null
    private val workRequestBuilder = OneTimeWorkRequestBuilder<TimerWorker>().also {
        it.setConstraints(constraints)
    }


    lateinit var onFinishPomodoro: () -> Unit


    fun stopTimer(context: Application) {
        cachedTime = (progress.value?.times(runningTimeType.value.time))?.toLong()
        request?.id?.let {
            WorkManager.getInstance(context).cancelAllWork()
        }
        timerIsRunning.value = false
    }

    fun restart(context: Application) {
        cachedTime = runningTimeType.value.time
        request?.id?.let {
            WorkManager.getInstance(context).cancelAllWork()
        }
        startTime(context = context)
    }

    /**
     * Start timer according to selected timer
     */
    fun startTime(context: Application, isSilent: Boolean = false) {
        timerIsRunning.value = true
        request = workRequestBuilder.setInputData(
            workDataOf(
                "Time" to runningTimeType.value.time,
                "Left" to (cachedTime ?: runningTimeType.value.time),
                "IsSilent" to isSilent
            )
        )
            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
            .build()
        request?.let { request ->

            WorkManager.getInstance(context).enqueue(request)

            val translatedProgress: LiveData<Float?> =
                WorkManager.getInstance(context).getWorkInfoByIdLiveData(request.id)
                    .switchMap { workInfo ->
                        when (workInfo.state) {
                            WorkInfo.State.ENQUEUED -> {
                                timerIsRunning.value = true
                                return@switchMap MutableLiveData(cachedTimePercentage)
                            }

                            WorkInfo.State.RUNNING -> {

                                val progress = workInfo?.progress?.getFloat(
                                    "Left",
                                    cachedTimePercentage ?: 1f

                                )

                                return@switchMap MutableLiveData(progress)
                            }

                            WorkInfo.State.SUCCEEDED -> {
                                val progress = workInfo?.progress?.getFloat(
                                    "Left", 1f
                                ) ?: -1f
                                timerIsRunning.value = false
                                showFinishNotification()
                                if (this::onFinishPomodoro.isInitialized && runningTimeType.value is Times.Pomodoro) {
                                    onFinishPomodoro()
                                }
                                return@switchMap MutableLiveData(progress)
                            }

                            else -> {
                                return@switchMap MutableLiveData(
                                    cachedTimePercentage
                                )
                            }
                        }
                    }

            progress.addSource(translatedProgress) {
                Log.e("Time", it.toString())
                progress.value = it
            }
        }
    }

    private fun showFinishNotification() {

    }

    fun changeCurrentTime(time: Times, context: Application? = null) {
        context?.let { stopTimer(it) }
        runningTimeType.value = time
        cachedTime = runningTimeType.value.time
    }

    fun Long.getMinute(): Long {
        return this / 60000
    }


}