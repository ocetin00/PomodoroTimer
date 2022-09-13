package com.oguzhancetin.pomodoro.util

import androidx.lifecycle.MutableLiveData

sealed class Times(val time:Long,var left:Long) {
    class LongTime: Times(900000,900000)
    class Short: Times(300000,300000)
    class PomodoroTime: Times(1500000,1500000)

    fun getCurrentPercentage(): Float {
        return this.left.toFloat().div(this.time)
    }

    override fun toString(): String {
        val timeMillis = (getCurrentPercentage() *  time).toLong()
        var minute = (timeMillis / 60000).toString()
        var second = ((timeMillis % 60000) / 1000).toString()
        if (minute.length == 1) minute = "0${minute}"
        if (second.length == 1) second = "0${second}"
        return "$minute : $second"
    }
}

/**
 * hold data percentage version => if 100 minute tatal left 25  data will be 0.25
 */
val LeftTime = MutableLiveData<Float>(1f)