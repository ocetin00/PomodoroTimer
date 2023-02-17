package com.oguzhancetin.pomodoro.common.util

import java.util.*

/**
 * Remove milisecond, hour, second, minute info from calender
 */
fun Calendar.removeDetails() {
    this.apply {
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.HOUR, 0)
        set(Calendar.HOUR_OF_DAY, 0)
    }

}