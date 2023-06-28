package com.oguzhancetin.pomodoro.core.util

import java.util.*

/**
 * Remove milisecond, hour, second, minute info from calender
 */
fun Calendar.removeDetails():Calendar {
    this.apply {
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.HOUR, 0)
        set(Calendar.HOUR_OF_DAY, 0)
    }
    return this

}