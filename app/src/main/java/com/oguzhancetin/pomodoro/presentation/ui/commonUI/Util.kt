package com.oguzhancetin.pomodoro.presentation.ui.commonUI

import com.oguzhancetin.pomodoro.presentation.ui.Screen


/**
 * pomodoro -> Pomodoro
 */
fun String.UpperFirstChar(): String{
    return "${this[0].uppercase() + this.substring(1,this.length)}"
}