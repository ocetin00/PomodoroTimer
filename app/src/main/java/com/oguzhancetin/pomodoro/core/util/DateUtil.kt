package com.oguzhancetin.pomodoro.core.util

import java.util.GregorianCalendar


/**
 * Remove milisecond, hour, second, minute info from calender
 */
fun GregorianCalendar.removeDetails():GregorianCalendar {
    this.apply {
        set(GregorianCalendar.SECOND, 0)
        set(GregorianCalendar.MILLISECOND, 0)
        set(GregorianCalendar.MINUTE, 0)
        set(GregorianCalendar.HOUR, 0)
        set(GregorianCalendar.HOUR_OF_DAY, 0)

    }
    return this
}