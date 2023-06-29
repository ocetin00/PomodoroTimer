package com.oguzhancetin.pomodoro.core.time

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey

sealed class Time(open var time: kotlin.Long) {
    private var left: kotlin.Long? = null

    data class Long(override var time: kotlin.Long = 900000) : Time(time) {
        override fun getPrefKey(): Preferences.Key<kotlin.Long> =
            longPreferencesKey("long_time")
    }

    data class Short(override var time: kotlin.Long = 300000) : Time(time) {
        override fun getPrefKey(): Preferences.Key<kotlin.Long> =
            longPreferencesKey("short_time")
    }

    data class Pomodoro(override var time: kotlin.Long = 1500000) : Time(time) {
        override fun getPrefKey(): Preferences.Key<kotlin.Long> =
            longPreferencesKey("pomodoro_time")
    }

    fun getCurrentPercentage(): Float {
        return this.left?.toFloat()?.div(this.time) ?: 1f
    }


    fun getText(progress: Float): String {
        val timeMillis = (progress * time).toLong()
        var minute = (timeMillis / 60000).toString()
        var second = ((timeMillis % 60000) / 1000).toString()
        if (minute.length == 1) minute = "0${minute}"
        if (second.length == 1) second = "0${second}"
        return "$minute : $second"
    }

    override fun toString(): String {
        val timeMillis = (getCurrentPercentage() * time).toLong()
        var minute = (timeMillis / 60000).toString()
        var second = ((timeMillis % 60000) / 1000).toString()
        if (minute.length == 1) minute = "0${minute}"
        if (second.length == 1) second = "0${second}"
        return "$minute : $second"
    }

    /**
     * store preferences key
     */
    abstract fun getPrefKey(): Preferences.Key<kotlin.Long>
}
