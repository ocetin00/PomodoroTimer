package com.oguzhancetin.pomodoro.common.util.Time


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.*
import com.oguzhancetin.pomodoro.R
import com.oguzhancetin.pomodoro.presentation.ui.MainActivity
import kotlinx.coroutines.delay


class TimerWorker(
    private val appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private var timeNotificationBuilder: NotificationCompat.Builder? = null
    private var finishNotificationBuilder: NotificationCompat.Builder? = null

    private var left = 0L


    companion object {
        const val CHANNEL_ID = "PomodoroTimerChannel"
        const val CHANNEL_NAME = "Pomodoro"
        const val FINISH_NOTIFICATION_ID = 12345
        const val TIMER_NOTIFICATION_ID = 123456
        const val interval = 1000L
        const val oneMinute = 60000L
    }


    override suspend fun doWork(): Result {
        val startTime = inputData.getLong("Time", 0)
        val leftTime = inputData.getLong("Left", 0)
        left = leftTime
        try {

            setForeground(createForegroundInfo("Pomodoro Running"))
            //clear finished notification
            notificationManager.cancel(TIMER_NOTIFICATION_ID)

            repeat((leftTime / interval).toInt()) {
                delay(interval)
                left -= interval
                Log.e("kalan", "${leftTime / oneMinute} : ${(left % oneMinute) / interval}")
                setProgress(workDataOf("Left" to left.toFloat() / startTime.toFloat()))
                timeNotificationBuilder?.let {
                    it.setContentTitle(getLeftTime(left));
                    notificationManager.notify(TIMER_NOTIFICATION_ID, it.build());
                }

            }

            finishNotificationBuilder?.let {
                notificationManager.notify(FINISH_NOTIFICATION_ID, it.build());
            }
            return Result.success()
        } catch (
            e: Exception
        ) {
            return Result.failure()
        }
    }

    /**
     * Long to Time
     */
    private fun getLeftTime(left: Long): String {
        var minute = (left / oneMinute).toString()
        var second = ((left % oneMinute) / interval).toString()
        if (minute.length == 1) minute = "0${minute}"
        if (second.length == 1) second = "0${second}"
        return "$minute : $second"
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }

    private val notificationManager =
        appContext.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    private fun createForegroundInfo(progress: String): ForegroundInfo {
        val id = CHANNEL_ID
        val title = "Pomodoro Running"
        val cancel = "cancel"
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())


        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }


        //tab intent
        val mainActivityIntent = Intent(applicationContext, MainActivity::class.java).also {
            it.action = Intent.ACTION_MAIN;
            it.addCategory(Intent.CATEGORY_LAUNCHER);
        }


        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                mainActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        timeNotificationBuilder = NotificationCompat.Builder(applicationContext, id)
            .setSilent(true)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setAutoCancel(false)
            .setSmallIcon(android.R.drawable.arrow_down_float)
            .setOngoing(true)
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            .setContentIntent(pendingIntent)


        val soundUri =
            Uri.parse("android.resource://" + applicationContext.packageName + "/" + R.raw.bellring)
        finishNotificationBuilder = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle("Finished")
            .setTicker(title)
            .setContentText(progress)
            .setAutoCancel(true)
            //.setSound(soundUri)
            .setSmallIcon(android.R.drawable.arrow_down_float)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            .setContentIntent(pendingIntent)

        val isSilent = inputData.getBoolean("IsSilent", false)
        finishNotificationBuilder?.setSilent(isSilent)


        return ForegroundInfo(TIMER_NOTIFICATION_ID, timeNotificationBuilder!!.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name = CHANNEL_ID
        val descriptionText = CHANNEL_NAME
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = descriptionText
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this

        val soundUri =
            Uri.parse("android.resource://" + applicationContext.packageName + "/" + R.raw.bellring)
        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION_RINGTONE)
            .build()


            mChannel.setSound(soundUri, audioAttributes)


        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }


}