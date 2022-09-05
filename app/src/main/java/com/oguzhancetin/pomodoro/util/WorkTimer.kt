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
import androidx.core.content.ContextCompat.getSystemService
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class WorkTimer(appContext: Context, workerParams: WorkerParameters):
    CoroutineWorker(appContext, workerParams) {

    companion object {
        const val Progress = "Progress"
        private const val delayDuration = 1L
        private var left = 25*1000
        val workRequest: WorkRequest =
            OneTimeWorkRequestBuilder<WorkTimer>()
                .build()
    }


    override suspend fun doWork(): Result {
        setForeground(createForegroundInfo("progress"))
        withContext(Dispatchers.Default) {
            repeat(60*25){
                delay(1000)
                left -= 1000
                Log.e("left",left.toString())
                val firstUpdate = workDataOf(Progress to  left )
                setProgressAsync(firstUpdate)
            }

        }
        return Result.success()
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

        return ForegroundInfo(123,notification)
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
        val notificationManager = applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }
}