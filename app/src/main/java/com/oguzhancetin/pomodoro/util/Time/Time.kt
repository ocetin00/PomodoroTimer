package com.oguzhancetin.pomodoro.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey

sealed class Times(open var time: kotlin.Long) {
    //var time by mutableStateOf(_time)
     var left:kotlin.Long? by mutableStateOf(null)
    data class Long(override var time: kotlin.Long = 900000) : Times(time) {
        override fun refresh() {
            time = 900000
        }

        override fun getPrefKey(): Preferences.Key<kotlin.Long> =
            longPreferencesKey("long_time")
    }

    data class Short(override var time: kotlin.Long = 300000) : Times(time) {
        override fun refresh() {
            time = 300000
        }
        override fun getPrefKey(): Preferences.Key<kotlin.Long> =
            longPreferencesKey("short_time")

    }

    data class Pomodoro(override var time: kotlin.Long = 1500000) : Times(time) {
        override fun refresh() {
            time = 1500000
        }

        override fun toString(): String {
            return super.toString()
        }

        override fun getPrefKey(): Preferences.Key<kotlin.Long> =
            longPreferencesKey("pomodoro_time")

    }

    abstract fun refresh()

    fun setLeft (left:Float){
        //this.left = ((left)*this.time).toLong()
        this.left = ((this.left)?.times(this.time))
    }

    fun getCurrentPercentage(): Float {
        return this.left?.toFloat()?.div(this.time) ?: 1f
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
