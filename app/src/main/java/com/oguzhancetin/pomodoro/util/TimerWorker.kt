package com.oguzhancetin.pomodoro.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import kotlin.time.Duration


class TimerWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    companion object{
        private var timer: CountDownTimer? = null
    }

    private var left = 0L

    override suspend fun doWork(): Result {
        val startTime = inputData.getLong("Time", 0)
        val leftTime =  inputData.getLong("Left", 0)
        left = leftTime
        setForeground(createForegroundInfo("Pomodoro Running"))
        withContext(Dispatchers.IO) {
            repeat((leftTime / 1000).toInt()) {
                delay(1000)
                left -= 1000
                Log.e("kalan", "${leftTime/60000} : ${(left%60000)/1000}")
                setProgress(workDataOf("Left" to left.toFloat()/startTime.toFloat()))
            }
        }

        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager


    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val id = "chanel1"
        val title = "title1"
        val cancel = "cancel"
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(android.R.drawable.arrow_down_float)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        return ForegroundInfo(123, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name = "chanel1"
        val descriptionText = "chanel1"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel("chanel1", name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    private fun createTimer(time: Long) {
        timer?.cancel()
        timer = null
        timer = Timer(time).start()

    }
    inner class Timer(private val time:Long) :CountDownTimer(time, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            Log.e("kalan", "${millisUntilFinished/60000} : ${(millisUntilFinished%60000)/1000}")
            LeftTime.value = millisUntilFinished.toFloat()/time.toFloat()
        }

        override fun onFinish() {
            Log.e("kaln", "bitti")
            LeftTime.value = 0f
        }
    }

}