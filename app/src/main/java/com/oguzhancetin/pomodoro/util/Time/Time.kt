package com.oguzhancetin.pomodoro.util

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey

sealed class Times(var time: kotlin.Long, var left: kotlin.Long) {
    object Long : Times(900000, 900000) {
        override fun refresh() {
            time = 900000
            left = 900000
        }

        override fun getPrefKey(): Preferences.Key<kotlin.Long> =
            longPreferencesKey("long_time")
    }

    object Short : Times(300000, 300000) {
        override fun refresh() {
            time = 300000
            left = 300000
        }
        override fun getPrefKey(): Preferences.Key<kotlin.Long> =
            longPreferencesKey("short_time")

    }

    object Pomodoro : Times(1500000, 1500000) {
        override fun refresh() {
            time = 1500000
            left = 1500000
        }
        override fun getPrefKey(): Preferences.Key<kotlin.Long> =
            longPreferencesKey("pomodoro_time")

    }

    abstract fun refresh()

    fun getCurrentPercentage(): Float {
        return this.left.toFloat().div(this.time)
    }


     fun getText(progress:Float): String {
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
